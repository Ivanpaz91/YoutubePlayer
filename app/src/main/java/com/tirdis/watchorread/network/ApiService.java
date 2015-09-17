package com.tirdis.watchorread.network;

import com.google.gson.JsonObject;
import com.tirdis.watchorread.model.Category;
import com.tirdis.watchorread.model.Content;
import com.tirdis.watchorread.model.Language;

import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * Created by admin on 5/22/2015.
 */
public interface ApiService {

    @GET("/api/get_all_categories")
    public void getCategory(RestCallback<JsonObject> callback);

    @GET("/api/get_all_languages")
    public void getLanguage(RestCallback<JsonObject> callback);


    @FormUrlEncoded
    @POST("/api/get_contlang_iddate")
    public void getContent(@Field("lang_id")String lang_id,@Field("date")String date, RestCallback<JsonObject> callback);

}


