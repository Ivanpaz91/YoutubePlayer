package com.tirdis.watchorread.network.service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.tirdis.watchorread.ApplicationController;
import com.tirdis.watchorread.event.ApiErrorEvent;
import com.tirdis.watchorread.event.CategoryLoadEvent;
import com.tirdis.watchorread.event.CategoryLoadedEvent;
import com.tirdis.watchorread.model.Category;
import com.tirdis.watchorread.network.ApiService;
import com.tirdis.watchorread.network.RestCallback;
import com.tirdis.watchorread.network.RestError;

import java.lang.reflect.Type;
import java.util.List;

import de.greenrobot.event.EventBus;
import retrofit.client.Response;

/**
 * Created by admin on 5/24/2015.
 */
public class CategoryLoadService {
    private ApiService mApi;
    private EventBus mBus;

    public CategoryLoadService(ApiService api, EventBus bus) {
        mApi = api;
        mBus = bus;
    }


    public void onEvent(CategoryLoadEvent event) {
        mApi.getCategory(new RestCallback<JsonObject>() {
            @Override
            public void success(JsonObject jsonObject, Response response) {
                List<Category> categoryList = null;
                if (jsonObject.has("status") && jsonObject.get("status").toString().compareTo("true")==0){
                    JsonArray categoryJsonArray = jsonObject.getAsJsonArray("categories");
                    Gson gson = new Gson();

                    Type listType = new TypeToken<List<Category>>() {}.getType();
                    categoryList = gson.fromJson(categoryJsonArray.toString(),listType);
                    ApplicationController.getInstance().setCategories(categoryList);
               //     mBus.post(new CategoryLoadedEvent(categoryList));
                }else{


                }

            }

            @Override
            public void failure(RestError restError) {
                mBus.post(new ApiErrorEvent("category"));
            }

        });

    }
}
