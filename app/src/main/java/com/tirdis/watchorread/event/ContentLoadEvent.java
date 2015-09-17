package com.tirdis.watchorread.event;

/**
 * Created by admin on 5/24/2015.
 */
public class ContentLoadEvent {
    public String mLang_id;
    public String mDate;

    public ContentLoadEvent(String mLang_id, String mDate) {
        this.mLang_id = mLang_id;
        this.mDate = mDate;
    }
}
