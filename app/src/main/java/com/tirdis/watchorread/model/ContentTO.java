package com.tirdis.watchorread.model;

/**
 * Created by admin on 5/22/2015.
 */
public class ContentTO {
    public String getLang_id() {
        return lang_id;
    }

    public void setLang_id(String lang_id) {
        this.lang_id = lang_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ContentTO(String lang_id, String date) {

        this.lang_id = lang_id;
        this.date = date;
    }

    String lang_id;
    String date;
}
