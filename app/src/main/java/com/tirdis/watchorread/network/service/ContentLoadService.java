package com.tirdis.watchorread.network.service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.tirdis.watchorread.event.ApiErrorEvent;
import com.tirdis.watchorread.event.CategoryLoadedEvent;
import com.tirdis.watchorread.event.ContentLoadEvent;
import com.tirdis.watchorread.event.ContentLoadedEvent;
import com.tirdis.watchorread.event.EmptyContentEvent;
import com.tirdis.watchorread.model.Content;
import com.tirdis.watchorread.network.ApiService;
import com.tirdis.watchorread.network.RestCallback;
import com.tirdis.watchorread.network.RestError;

import java.lang.reflect.Type;
import java.util.List;

import de.greenrobot.event.EventBus;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by admin on 5/24/2015.
 */
public class ContentLoadService {
    private ApiService mApi;
    private EventBus mBus;

    public ContentLoadService(ApiService api, EventBus bus) {
        mApi = api;
        mBus = bus;
    }


    public void onEvent(ContentLoadEvent event) {
        mApi.getContent(event.mLang_id, event.mDate, new RestCallback<JsonObject>() {
            @Override
            public void success(JsonObject jsonObject, Response response) {
                List<Content> contentList = null;
                if (jsonObject.has("status") && jsonObject.get("status").toString().compareTo("true") == 0) {
                    JsonArray categoryJsonArray = jsonObject.getAsJsonArray("conts");
                    Gson gson = new Gson();

                    Type listType = new TypeToken<List<Content>>() {
                    }.getType();
                    contentList = gson.fromJson(categoryJsonArray.toString(), listType);

                    mBus.post(new ContentLoadedEvent(contentList));
                } else {
                   // mBus.post(new ContentLoadedEvent(null));
                    mBus.post(new ContentLoadedEvent(null));
                }

            }


            @Override
            public void failure(RestError restError) {
              //  super.failure(restError);

           //     mBus.post(new EmptyContentEvent());
                mBus.post(new ApiErrorEvent("Content"));
            }

//            @Override
//            public void failure(RetrofitError error) {
//                super.failure(error);
//            }
        });

    }
}
