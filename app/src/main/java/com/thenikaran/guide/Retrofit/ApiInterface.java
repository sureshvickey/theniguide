package com.thenikaran.guide.Retrofit;


import java.util.List;

import com.thenikaran.guide.MVP.AllPlacesResponse;
import com.thenikaran.guide.MVP.CategoryListResponse;
import com.thenikaran.guide.MVP.RegistrationResponse;

import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;

public interface ApiInterface {

    // API's endpoints
    @GET("/app_dashboard/JSON/allplaces.php")
    public void getAllPlaces(Callback<AllPlacesResponse> callback);

    @GET("/app_dashboard/JSON/placenew.php")
    public void getCategoryList(Callback<List<CategoryListResponse>> callback);

    @FormUrlEncoded
    @POST("/app_dashboard/JSON/pushadd.php")
    public void sendAccessToken(@Field("accesstoken") String accesstoken, Callback<RegistrationResponse> callback);

}
