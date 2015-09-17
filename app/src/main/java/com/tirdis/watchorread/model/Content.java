package com.tirdis.watchorread.model;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

/**
 * Created by admin on 5/22/2015.
 */
@Parcel(Parcel.Serialization.BEAN)
public class Content {

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLang_id() {
        return lang_id;
    }

    public void setLang_id(String lang_id) {
        this.lang_id = lang_id;
    }

    public String getCatg_id() {
        return catg_id;
    }

    public void setCatg_id(String catg_id) {
        this.catg_id = catg_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getYtlink() {
        return ytlink;
    }

    public void setYtlink(String ytlink) {
        this.ytlink = ytlink;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
    @ParcelConstructor
    public Content(String id, String lang_id, String catg_id, String date, String ytlink, String head, String text) {

        this.id = id;
        this.lang_id = lang_id;
        this.catg_id = catg_id;
        this.date = date;
        this.ytlink = ytlink;
        this.head = head;
        this.text = text;
    }

    String  id;
    String lang_id;
    String catg_id;
    String date;
    String ytlink;
    String head;
    String text;

}
