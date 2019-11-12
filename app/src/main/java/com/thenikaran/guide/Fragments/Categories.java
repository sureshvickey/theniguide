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
import com.thenikaran.guide.Adapters.CategoriesAdapter;
import com.thenikaran.guide.DetectConnection;
import com.thenikaran.guide.MVP.AllPlacesResponse;
import com.thenikaran.guide.MVP.CategoryListResponse;
import com.thenikaran.guide.R;
import com.thenikaran.guide.Retrofit.Api;
import com.thenikaran.guide.SplashScreen;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class Categories extends Fragment {

    View view;
    @Bind(R.id.recyclerview)
    RecyclerView recyclerview;
    public static CategoriesAdapter categoriesAdapter;
    public static SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_latest_places, container, false);
        ButterKnife.bind(this, view);
        try {
            new setCatData().execute();
        }catch (Exception e){
            e.printStackTrace();
        }
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.simpleSwipeRefreshLayout);
        // implement setOnRefreshListener event on SwipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (DetectConnection.checkInternetConnection(getActivity())) {
                    try {
                        new setCatData().execute();
                    }catch (Exception e){
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


    private class setCatData extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... Url) {

            try {

              getCategoryList();
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

        public void getCategoryList() {
            Api.getClient().getCategoryList(new Callback<List<CategoryListResponse>>() {
                @Override
                public void success(List<CategoryListResponse> categoryListResponses, Response response) {
                    SplashScreen.categoryListResponseData.clear();
                    SplashScreen.categoryListResponseData.addAll(categoryListResponses);
//                categoriesAdapter.notifyDataSetChanged();
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
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
            recyclerview.setLayoutManager(gridLayoutManager);
            categoriesAdapter = new CategoriesAdapter(getActivity(), SplashScreen.categoryListResponseData);
            recyclerview.setAdapter(categoriesAdapter);
        }

    }
}
