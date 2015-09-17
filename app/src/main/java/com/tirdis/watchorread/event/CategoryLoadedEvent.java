package com.tirdis.watchorread.event;

import com.tirdis.watchorread.model.Category;

import java.util.List;

/**
 * Created by admin on 5/24/2015.
 */
public class CategoryLoadedEvent {
   List<Category> mCategories;
    public CategoryLoadedEvent(List<Category> categoryList) {
        mCategories = categoryList;

    }
}
