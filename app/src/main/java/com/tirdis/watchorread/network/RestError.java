package com.tirdis.watchorread.network;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by admin on 5/22/2015.
 */

@Parcel
public class RestError
{

    public RestError(){ /*Required empty bean constructor*/ }
    @SerializedName("code")
    private Integer code;

    @SerializedName("error_message")
    private String strMessage;



    public RestError(String strMessage)
    {
        this.strMessage = strMessage;
    }

    //Getters and setters
}