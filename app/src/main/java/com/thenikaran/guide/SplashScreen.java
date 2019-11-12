package com.thenikaran.guide;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.thenikaran.guide.Fragments.PlaceDetail;
import com.thenikaran.guide.MVP.AllPlacesResponse;
import com.thenikaran.guide.MVP.CategoryListResponse;
import com.thenikaran.guide.MVP.Place;
import com.thenikaran.guide.Retrofit.Api;
import com.google.android.gms.ads.MobileAds;
import com.google.gson.Gson;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SplashScreen extends Activity {

    public static List<CategoryListResponse> categoryListResponseData;
    public static List<Place> newsListResponsesData;
    public static List<Place> imagesList1;
    public static String id = "";
    @Bind(R.id.internetNotAvailable)
    LinearLayout internetNotAvailable;
    @Bind(R.id.splashImage)
    ImageView splashImage;
    SharedPreferences sharedPreference, sharedPreferencesCache;
    SharedPreferences.Editor editor;
    @Bind(R.id.errorMessage)
    TextView errorMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        ButterKnife.bind(this);
        MobileAds.initialize(this, getResources().getString(R.string.ads_app_id));
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        sharedPreferencesCache = getSharedPreferences("cacheExist", 0);

        // check data from FCM
        try {
            Intent intent = getIntent();
            id = intent.getStringExtra("id");
            Log.d("notification Data", id);
        } catch (Exception e) {
            id = "";
            Log.d("error notification data", e.toString());
        }
        sharedPreference = getSharedPreferences("localData", 0);
        editor = sharedPreference.edit();
        // Check the internet and get response from API's
        if (DetectConnection.checkInternetConnection(getApplicationContext())) {
            getCategoryList();
        } else {
            if (sharedPreference.getString("categoryList", "").equalsIgnoreCase("") ||
                    sharedPreferencesCache.getBoolean("exist", false) == false) {
                internetNotAvailable.setVisibility(View.VISIBLE);
                splashImage.setVisibility(View.GONE);
                errorMessage.setText("Internet Connection Not Available");
            } else {
                Gson gson = new Gson();
                String json = sharedPreference.getString("categoryList", "");
                String json1 = sharedPreference.getString("placelist", "");
                Log.d("savedCategoryData", sharedPreference.getString("categoryList", "Not Available"));
                List categoryData, placeData;
                CategoryListResponse[] categoryItems = gson.fromJson(json, CategoryListResponse[].class);
                categoryData = Arrays.asList(categoryItems);
                categoryListResponseData = new ArrayList(categoryData);
                Log.d("categoryListRseDataD", categoryListResponseData.size() + "");

                Place[] placeItems = gson.fromJson(json1, Place[].class);
                placeData = Arrays.asList(placeItems);
                newsListResponsesData = new ArrayList(placeData);
                Log.d("placeListRseDataD", newsListResponsesData.size() + "");
                moveNext();
            }
        }

    }

    @OnClick(R.id.tryAgain)
    public void onClick() {
        if (DetectConnection.checkInternetConnection(getApplicationContext())) {
            internetNotAvailable.setVisibility(View.GONE);
            splashImage.setVisibility(View.VISIBLE);
            getCategoryList();
        } else {
            internetNotAvailable.setVisibility(View.VISIBLE);
            splashImage.setVisibility(View.GONE);
        }
    }

    public void getCategoryList() {
        // getting category list news data
        Api.getClient().getCategoryList(new Callback<List<CategoryListResponse>>() {
            @Override
            public void success(List<CategoryListResponse> categoryListResponses, Response response) {
                categoryListResponseData = categoryListResponses;
                Gson gson = new Gson();
                String json = gson.toJson(categoryListResponseData);
                editor.putString("categoryList", json);
                editor.commit();
                getNewsList();

            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("error", error.toString());
                internetNotAvailable.setVisibility(View.VISIBLE);
                splashImage.setVisibility(View.GONE);
            }
        });
    }

    public void getNewsList() {
        // getting news list data
        Api.getClient().getAllPlaces(new Callback<AllPlacesResponse>() {
            @Override
            public void success(AllPlacesResponse newsListResponses, Response response) {

                if (newsListResponses.getPlace() == null) {
                    Log.d("data", "null");
                    internetNotAvailable.setVisibility(View.VISIBLE);
                    splashImage.setVisibility(View.GONE);
                    errorMessage.setText("No place added by admin");

                }else {
                    newsListResponsesData = newsListResponses.getPlace();
                    Gson gson = new Gson();
                    String json = gson.toJson(newsListResponsesData);
                    editor.putString("placelist", json);
                    editor.commit();
                    moveNext();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("error", error.toString());
                internetNotAvailable.setVisibility(View.VISIBLE);
                splashImage.setVisibility(View.GONE);
            }
        });
    }

    private void moveNext() {
// redirect to next page after getting data from server
        try {
            imagesList1 = new ArrayList<>();
            if (id.length() > 0) {
                for (int j = 0; j < newsListResponsesData.size(); j++) {
                    if (newsListResponsesData.get(j).getPlaceId().trim().equalsIgnoreCase(id)) {
                        imagesList1.add(newsListResponsesData.get(j));
                    }
                }

                PlaceDetail.newsListResponsesData = imagesList1;
                Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                intent.putExtra("pos", 0);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finishAffinity();
            } else {
                Intent intent = new Intent(SplashScreen.this, WelcomeActivity.class);
                startActivity(intent);
                finish();
            }
        } catch (Exception e) {
            Log.d("error notification data", e.toString());
            Intent intent = new Intent(SplashScreen.this, WelcomeActivity.class);
            startActivity(intent);
            finish();
        }

    }

}
