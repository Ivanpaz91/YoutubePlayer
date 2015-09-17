package com.tirdis.watchorread.network;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Created by admin on 5/28/2015.
 */
public class ContentLoadOkHttp {
    public static final MediaType JSON
            = MediaType.parse("application/x-www-form-urlencoded;charset=UTF-8");


    OkHttpClient client = new OkHttpClient();

     Call post(String url, String json, Callback callback) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

         Call call = client.newCall(request);
         call.enqueue(callback);


         return call;
    }

    public String toJson(String lang_id, String date){

        return  "lang_id="+ lang_id + "&date=" + date;
     }
}
