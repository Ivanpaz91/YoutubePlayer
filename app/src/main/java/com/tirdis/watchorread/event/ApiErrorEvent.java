package com.tirdis.watchorread.event;

import com.tirdis.watchorread.network.RestError;

import retrofit.RetrofitError;

/**
 * Created by admin on 5/22/2015.
 */
public class ApiErrorEvent {
    public String mError;
    public ApiErrorEvent(String error) {
        mError = error;
    }
}
