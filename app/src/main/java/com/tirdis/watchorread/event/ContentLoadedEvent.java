package com.tirdis.watchorread.event;

import com.tirdis.watchorread.model.Content;

import java.util.List;

/**
 * Created by admin on 5/24/2015.
 */
public class ContentLoadedEvent {
    public List<Content> mContentList;
    public ContentLoadedEvent(List<Content> contentList) {
        mContentList = contentList;
    }
}
