package com.thenikaran.guide.Fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.thenikaran.guide.Adapters.AllPlacesAdapter;
import com.thenikaran.guide.DetectConnection;
import com.thenikaran.guide.MVP.AllPlacesResponse;
import com.thenikaran.guide.R;
import com.thenikaran.guide.Retrofit.Api;
import com.thenikaran.guide.SplashScreen;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class AllPlaces extends Fragment {

    View view;

    @Bind(R.id.recyclerview)
    RecyclerView recyclerview;

    public static AllPlacesAdapter allPlacesAdapter;
    public static SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_latest_places, container, false);
        ButterKnife.bind(this, view);
        try{
            new setAllData().execute();  }catch (Exception e){
            e.printStackTrace();
        }
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.simpleSwipeRefreshLayout);
        // implement setOnRefreshListener event on SwipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (DetectConnection.checkInternetConnection(getActivity())) {
                  try{ new setAllData().execute();  }catch (Exception e){
                      e.printStackTrace();
                  }
                } else {
                    Toast.makeText(getActivity(), "Internet Not Available", Toast.LENGTH_SHORT).show();
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });
        return view;
    }


    private class setAllData extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... Url) {

            try {

                getNewsList();

            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void args) {

            try {
                setData();
            }catch (Exception e){
                e.printStackTrace();
            }

        }
        public void getNewsList() {
            Api.getClient().getAllPlaces(new Callback<AllPlacesResponse>() {
                @Override
                public void success(AllPlacesResponse newsListResponses, Response response) {
                    SplashScreen.newsListResponsesData.clear();
                    SplashScreen.newsListResponsesData.addAll(newsListResponses.getPlace());
//                allPlacesAdapter.notifyDataSetChanged();
                    swipeRefreshLayout.setRefreshing(false);
                    setData();
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.e("error", error.toString());
                    swipeRefreshLayout.setRefreshing(false);
                }
            });
        }

        private void setData() {
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),2);
            recyclerview.setLayoutManager(gridLayoutManager);
            allPlacesAdapter = new AllPlacesAdapter(getActivity(), SplashScreen.newsListResponsesData);
            recyclerview.setAdapter(allPlacesAdapter);

        }
    }
}
