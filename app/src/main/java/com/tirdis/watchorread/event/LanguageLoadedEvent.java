package com.tirdis.watchorread.event;

import com.tirdis.watchorread.model.Category;
import com.tirdis.watchorread.model.Language;

import java.util.List;

/**
 * Created by admin on 5/22/2015.
 */
public class LanguageLoadedEvent {
    public List<Language> mLanguages;
    public LanguageLoadedEvent( List<Language> languages) {
        mLanguages = languages;
    }
}
