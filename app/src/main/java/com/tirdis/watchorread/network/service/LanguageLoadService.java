package com.tirdis.watchorread.network.service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.tirdis.watchorread.event.ApiErrorEvent;
import com.tirdis.watchorread.event.LanguageLoadEvent;
import com.tirdis.watchorread.event.LanguageLoadedEvent;
import com.tirdis.watchorread.model.Category;
import com.tirdis.watchorread.model.Language;
import com.tirdis.watchorread.network.ApiService;
import com.tirdis.watchorread.network.RestCallback;
import com.tirdis.watchorread.network.RestError;

import java.lang.reflect.Type;
import java.util.List;


import de.greenrobot.event.EventBus;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by admin on 5/22/2015.
 */
public class LanguageLoadService {
    private ApiService mApi;
    private EventBus mBus;

    public LanguageLoadService(ApiService api, EventBus bus) {
        mApi = api;
        mBus = bus;
    }


    public void onEvent(LanguageLoadEvent event) {
        mApi.getLanguage(new RestCallback<JsonObject>() {
            @Override
            public void success(JsonObject jsonObject, Response response) {
                List<Language> languageList = null;
                if (jsonObject.has("status") && jsonObject.get("status").toString().compareTo("true")==0){
                    JsonArray categoryJsonArray = jsonObject.getAsJsonArray("languages");
                    Gson gson = new Gson();

                    Type listType = new TypeToken<List<Language>>() {}.getType();
                    languageList = gson.fromJson(categoryJsonArray.toString(),listType);

                    mBus.post(new LanguageLoadedEvent(languageList));
                }

            }

            @Override
            public void failure(RestError restError) {
                mBus.post(new ApiErrorEvent("Language"));
            }

        });

    }
}
