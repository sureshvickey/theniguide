package com.thenikaran.guide.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thenikaran.guide.DetectConnection;
import com.thenikaran.guide.Fragments.PlaceDetail;
import com.thenikaran.guide.MVP.Place;
import com.thenikaran.guide.MainActivity;
import com.thenikaran.guide.R;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 * Created by ubantu on 3/3/17.
 */
public class AllPlacesAdapter extends RecyclerView.Adapter<AllPlaceViewHolder> {
    Context context;
    List<Place> newsListResponse;
    List<Place> newsListResponse1;
    float distanceInMeters;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    public AllPlacesAdapter(Context context, List<Place> newsListResponse) {
        this.context = context;
        this.newsListResponse = newsListResponse;
        this.newsListResponse1 = new ArrayList<Place>();
        this.newsListResponse1.addAll(newsListResponse);
        sharedPreferences = context.getSharedPreferences("cacheExist", 0);
        editor = sharedPreferences.edit();
        if (DetectConnection.checkInternetConnection(context)) {
            editor.putBoolean("exist", true);
            editor.commit();
        }
    }


    @Override
    public AllPlaceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.news_list_items, null);
        AllPlaceViewHolder NewsListViewHolder = new AllPlaceViewHolder(context, view, newsListResponse);
        return NewsListViewHolder;
    }

    @Override
    public void onBindViewHolder(final AllPlaceViewHolder holder, final int position) {
        holder.placeName.setText(newsListResponse.get(position).getTitle());
        Picasso.with(context).invalidate(newsListResponse.get(position).getImage().replaceAll(" ", "%20"));
        Picasso.with(context)
                .load(newsListResponse.get(position).getImage().replaceAll(" ", "%20"))
                .placeholder(R.drawable.defaultimage)
                .error(R.drawable.defaulterrorimage)
                .into(holder.image);
        Log.d("latIinAdapter", MainActivity.latitude + "");
        if (MainActivity.latitude == 0.0) {
            holder.distance.setVisibility(View.GONE);
        } else {
            try {
                Location loc1 = new Location("");
                loc1.setLatitude(MainActivity.latitude);
                loc1.setLongitude(MainActivity.longitude);

                Location loc2 = new Location("");
                loc2.setLatitude(Double.parseDouble(newsListResponse.get(position).getLatitude()));
                loc2.setLongitude(Double.parseDouble(newsListResponse.get(position).getLongitude()));

                distanceInMeters = loc1.distanceTo(loc2);
                Log.d("distance", distanceInMeters + "");
                distanceInMeters = distanceInMeters / 1000;
                holder.distance.setText(new DecimalFormat("##.#").format(distanceInMeters) + " km");

            } catch (Exception e) {
                holder.distance.setText("Not Found");

            }
        }

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("position", position + "");
                PlaceDetail.newsListResponsesData = newsListResponse;
                Intent intent = new Intent(context, PlaceDetail.class);
                intent.putExtra("pos", position);
                intent.putExtra("distance", holder.distance.getText().toString());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return newsListResponse.size();
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        newsListResponse.clear();
        if (charText.length() == 0) {
            newsListResponse.addAll(newsListResponse1);
        } else {
            for (Place wp : newsListResponse1) {
                if (wp.getTitle().toLowerCase(Locale.getDefault()).contains(charText)) {
                    newsListResponse.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}
