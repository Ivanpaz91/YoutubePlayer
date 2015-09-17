package com.tirdis.watchorread;

import android.app.Application;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.tirdis.watchorread.event.CategoryLoadEvent;
import com.tirdis.watchorread.event.ContentLoadEvent;
import com.tirdis.watchorread.model.Category;
import com.tirdis.watchorread.model.Content;
import com.tirdis.watchorread.network.ApiService;
import com.tirdis.watchorread.network.service.CategoryLoadService;
import com.tirdis.watchorread.network.service.ContentLoadService;
import com.tirdis.watchorread.network.service.LanguageLoadService;
import com.tirdis.watchorread.util.DateHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by admin on 5/22/2015.
 */
public class ApplicationController extends Application{
    private static final String BASE_URL = "http://watchorread.com/index.php/";
    private static ApplicationController instance;

    public boolean isIsCategoryClick() {
        return mIsCategoryClick;
    }

    public void setIsCategoryClick(boolean mIsCategoryClick) {
        this.mIsCategoryClick = mIsCategoryClick;
    }

    private boolean mIsCategoryClick = false;

    private static final RestAdapter mRestAdapter = new RestAdapter.Builder().setEndpoint(BASE_URL).build();
    private static final ApiService mApiService = mRestAdapter.create(ApiService.class);
    Callback callback = new Callback() {
        @Override
        public void success(Object o, Response response) {

        }

        @Override
        public void failure(RetrofitError retrofitError) {

        }
    };

    private LanguageLoadService mLanguageLoadService;
    private CategoryLoadService mCategoryLoadService;

    public List<Boolean> getIsInterests() {
        return isInterests;
    }

    private ContentLoadService  mContentLoadService;



    private String mSelectedLang = "-1";


    private List<Content> mContents;
    private List<Category> mCategories;

    private int defaultCategory = 0;
    private List<Boolean> isInterests = new ArrayList<>();

    public int getLangId() {
        return mLangId;
    }

    public void setLangId(int mLangId) {
        this.mLangId = mLangId;
    }

    private int mLangId;
    private EventBus mBus = EventBus.getDefault();

    public String getCurrentDate() {
        return mCurrentDate;
    }

    public void setCurrentDate(String mCurrentDate) {
        this.mCurrentDate = mCurrentDate;
    }

    private String mCurrentDate;

    public boolean isScrollDetect() {
        return isScrollDetect;
    }

    public void setScrollDetect(boolean isScrollDetect) {
        this.isScrollDetect = isScrollDetect;
    }
    Drawable mCustomeDrawable;
    private boolean isScrollDetect = false;
    public ApplicationController() {
    }
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        mCustomeDrawable = getApplicationContext().getResources().getDrawable(R.drawable.home);

        // UNIVERSAL IMAGE LOADER SETUP
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisc(true).cacheInMemory(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .displayer(new FadeInBitmapDisplayer(300)).build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .memoryCache(new WeakMemoryCache())
                .discCacheSize(100 * 1024 * 1024).build();

        ImageLoader.getInstance().init(config);
        // END - UNIVERSAL IMAGE LOADER SETUP

        mLanguageLoadService = new LanguageLoadService(mApiService,mBus);
        mCategoryLoadService = new CategoryLoadService(mApiService,mBus);
        mContentLoadService =  new ContentLoadService(mApiService,mBus);

        mBus.register(mLanguageLoadService);
        mBus.register(mCategoryLoadService);
        mBus.register(mContentLoadService);
     //   EventBus.getDefault().post(new ContentLoadEvent("1","2015-05-21"));
        EventBus.getDefault().post(new CategoryLoadEvent());
     //   mBus.register(this);
        //set interestes status from saved

        mCurrentDate = DateHelper.getTheDayDate();

    }

    public static synchronized ApplicationController getInstance() {

        return instance;
    }
    public static RestAdapter getRestAdapter(){

        return mRestAdapter;
    }
    public  String getSelectedLang() {
        return mSelectedLang;
    }

    public void setSelectedLang(String mSelectedLang) {
        this.mSelectedLang = mSelectedLang;
    }
    public List<Content> getContents() {
        return mContents;
    }

    public void setContents(List<Content> mContents) {
        this.mContents = mContents;
    }
    public List<Category> getCategories() {
        return mCategories;
    }

    public void setIsInterests(List<Boolean> isInterests) {
        this.isInterests = isInterests;
    }
    public void setDefaultCategory(int defaultCategory) {
        this.defaultCategory = defaultCategory;
    }
    public int getDefaultCategory() {
        return this.defaultCategory ;
    }

    public void setCategories(List<Category> mCategories) {

        this.mCategories = mCategories;
        //get interest settings
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if(settings.contains("INTERESTS")) {
            String interests = settings.getString("INTERESTS", "1111111");

            if(interests.length() > mCategories.size()){
                for(int i=0;i<mCategories.size();i++){
                    isInterests.add(Boolean.parseBoolean(interests.substring(i,i+1)));
                }
            }else{
                for(int i=0;i<interests.length();i++){
                    if(interests.substring(i,i+1).equals("1")){
                        isInterests.add(true);
                    }else{
                        isInterests.add(false);
                    }
                  
                }
                for(int j=0;j < mCategories.size()-interests.length();j++){
                    isInterests.add(true);
                }

            }

        }else{
            isInterests=new ArrayList<Boolean>(Arrays.asList(new Boolean[mCategories.size()]));
            Collections.fill(isInterests, new Boolean(true));
        }

        if(settings.contains("DEFAULT_CATEGORY")) {
            defaultCategory = settings.getInt("DEFAULT_CATEGORY",0);
        }else{
            defaultCategory = 0;
        }

    }
    public DisplayImageOptions getUniversialImageOpition(){
        return  new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisc(true).resetViewBeforeLoading(true)
                .showImageForEmptyUri(mCustomeDrawable)
                .showImageOnFail(mCustomeDrawable)
                .showImageOnLoading(mCustomeDrawable).build();
    }
    private String mLangID;
    public void setSelectedLangID(String lang){
        mLangID = lang;
    }
    public String getSelectedLangID(){
        return  mLangID;
    }
}
