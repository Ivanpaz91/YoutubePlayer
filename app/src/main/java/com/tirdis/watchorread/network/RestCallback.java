package com.tirdis.watchorread.network;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by admin on 5/22/2015.
 */
public abstract class RestCallback<T> implements Callback<T>
{
    public abstract void failure(RestError restError);

    @Override
    public void success(T t, Response response) {
        success(t,response);
    }

    @Override
    public void failure(RetrofitError error)
    {
        RestError restError = (RestError) error.getBodyAs(RestError.class);

        if (restError != null)
            failure(restError);
        else
        {
            failure(new RestError(error.getMessage()));
        }
    }
}