package com.thenikaran.guide.Adapters;

import android.content.Context;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import com.thenikaran.guide.MVP.Place;
import com.thenikaran.guide.R;


/**
 * Created by ubantu on 3/3/17.
 */
public class AllPlaceViewHolder extends RecyclerView.ViewHolder {

    ImageView image;
    TextView placeName, distance;
    CardView cardView;

    public AllPlaceViewHolder(final Context context, View itemView, List<Place> newsListResponse) {
        super(itemView);
        image = (ImageView) itemView.findViewById(R.id.placeImage);
        placeName = (TextView) itemView.findViewById(R.id.placeName);
        distance = (TextView) itemView.findViewById(R.id.distance);
        cardView = (CardView) itemView.findViewById(R.id.cardView);
    }
}
