package com.tirdis.watchorread;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.provider.ContactsContract;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.johnpersano.supertoasts.SuperToast;
import com.github.johnpersano.supertoasts.util.Style;
import com.tirdis.watchorread.adapter.ArticlePagerAdapter;
import com.tirdis.watchorread.event.ContentLoadEvent;
import com.tirdis.watchorread.event.ContentLoadedEvent;
import com.tirdis.watchorread.event.LanguageLoadEvent;
import com.tirdis.watchorread.model.Category;
import com.tirdis.watchorread.model.Content;
import com.tirdis.watchorread.util.DateHelper;
import com.tirdis.watchorread.view.ArticleFragment;
import com.tirdis.watchorread.view.CustomViewPager;

import org.parceler.javaxinject.Inject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.greenrobot.event.EventBus;
import icepick.Icepick;
import icepick.Icicle;


public class ArticleViewActivity extends FragmentActivity {
    private String mSelectedLanguage;
    private String mDate;
    private String[] mCategoryId;
    private List<Content> mContentList;
    private SuperToast superToast;

    private ArticlePagerAdapter mArticlePagerAdapter;
    public Boolean isFirstTime = false;
    Handler mHandler;

    int mMessageTime = 0;

    @Icicle
    int mSelectedIndex;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);
    }

    Boolean mPageEnd = false;
    @InjectView(R.id.view_pager)
    CustomViewPager mViewPager;

    @InjectView(R.id.textView_header_title)
    TextView mHeaderTitle;

    @InjectView(R.id.textView_header_date)
    TextView mHeaderDate;


    @InjectView(R.id.btn_calendar)
    ImageView mCalendarView;

    private Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mContext = this;
        //set the language
        ApplicationController controller = (ApplicationController)getApplicationContext();
        mSelectedLanguage = controller.getSelectedLang();

        setContentView(R.layout.activity_article_view);
        ButterKnife.inject(this);


        //set the calendar date to today
        initDate();
        //todo: set the category

        //get article from server
        mCalendarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, PickDateActivity.class));
                stopShowInfoMessageRepeat();
                overridePendingTransition(R.anim.slideinup, R.anim.slideoutup);
                //stop video
                ArrayList<ArticleFragment> fragments = mArticlePagerAdapter.getRegisteredFragmentCurrent(mSelectedIndex);
                if (fragments != null) {
                    for (ArticleFragment fragment : fragments) {
                        fragment.onPauseVideo();
                    }
                }
                //   showCalendar();
            }
        });
        if(!ApplicationController.getInstance().isScrollDetect()) {
            setShowInfoMessageRepeat();

        }
//        mViewPager.setOffscreenPageLimit(1);

    }

    @Override
    public void onResume() {
        super.onResume();

        EventBus.getDefault().register(this);
        mArticlePagerAdapter = new ArticlePagerAdapter(getSupportFragmentManager(),this);

        mViewPager.setAdapter(mArticlePagerAdapter);
        isFirstTime = true;

       // mArticlePagerAdapter.notifyDataSetChanged()
        try {
            mViewPager.setCurrentItem(mSelectedIndex);
        } catch (Exception e) {
            e.printStackTrace();
        }
        showArticleByCategoryId();
        setHeaderTitle();
        setHeaderDate();
      //  EventBus.getDefault().post(new ContentLoadEvent(mSelectedLanguage,mDate));
    }

    private void showArticleByCategoryId() {
        Boolean flag = false;
        if( getIntent().hasExtra("GOTOINDEX")){

            String categoryId = getIntent().getStringExtra("GOTOINDEX");

        List<Content> contents = mArticlePagerAdapter.getFilteredContents();
        for(int i=0;i<contents.size();i++){
            Content content = contents.get(i);
            if(content.getCatg_id().equals(categoryId)){
                if((content.getId().equals("1"))||(content.getId().equals("0"))){
                    mViewPager.setCurrentItem(i);
                    List<Category> categories = ((ApplicationController)getApplicationContext()).getCategories();
                    mHeaderTitle.setText(categories.get(Integer.valueOf(content.getCatg_id())-1).getCdscp().toUpperCase());
                    flag = true;
                    break;
                }
                flag = true;
            }
        }
        if(flag == false){
            mArticlePagerAdapter.setFilteredContents(categoryId);
            List<Category> categories = ((ApplicationController)getApplicationContext()).getCategories();
            mHeaderTitle.setText(categories.get(Integer.valueOf(categoryId) - 1).getCdscp().toUpperCase());

        }
        }else{
            ((ApplicationController)getApplicationContext()).setIsCategoryClick(false);
        }

    }

    private void setHeaderDate() {
        initDate();
        Date date = DateHelper.parseUtcDate(mDate);
        if(DateHelper.getTheDayDate().equals(DateHelper.formatUtcDate(date))){
            mHeaderDate.setText("TODAY");
        } else{
            mHeaderDate.setText(DateHelper.formatDate(date));

        }
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
        ApplicationController.getInstance().setScrollDetect(true);
        stopShowInfoMessageRepeat();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_article_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);

    }


    public void setHeaderTitle(){

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


                if (mPageEnd && position == mSelectedIndex) {
                    Log.d(getClass().getName(), "Okay");
                    mPageEnd = false;//To avoid multiple calls.
                    if(mMessageTime % 3 == 0){
                        showEndOfDayMessage();
                    }
                    mMessageTime += 1;
                } else {
                    mPageEnd = false;
                }


            }

            @Override
            public void onPageSelected(int position) {
                mSelectedIndex = position;

                List<Category> categories = ((ApplicationController) getApplicationContext()).getCategories();
                mHeaderTitle.setText(categories.get(Integer.valueOf(mArticlePagerAdapter.getFilteredContents().get(position).getCatg_id()) - 1).getCdscp().toUpperCase());
                ApplicationController.getInstance().setScrollDetect(true);
                stopShowInfoMessageRepeat();

//                try{
//                    ArticleFragment page = (ArticleFragment)getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.view_pager + ":" + mViewPager.getCurrentItem());
////                if(mViewPager.getCurrentItem() ==0 && page!= null){
//                    page.onPauseVideo();
//
////                }
//                }catch (Exception e){}
                //   mArticlePagerAdapter.notifyDataSetChanged();

                ArrayList<ArticleFragment> fragments = mArticlePagerAdapter.getRegisteredFragment(position);
                if(fragments!=null){
                    for(ArticleFragment fragment:fragments){
                        fragment.onPauseVideo();
                    }
                }
                try{
                    ArticleFragment currentFragment = mArticlePagerAdapter.getRegisteredFragmentCurrent(position).get(0);
                    if(currentFragment!=null){
                        currentFragment.onStartVideo();
                        currentFragment.refresh();
                    }

                }catch (Exception e){
                    Log.e("Internal error","Fragment not created");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

                if (mSelectedIndex == mArticlePagerAdapter.getFilteredContents().size() - 1) {
                    mPageEnd = true;
                }
            }
        });

    }



    private void initDate(){
      //  Calendar c = Calendar.getInstance();
       // mDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        mDate = ((ApplicationController)getApplicationContext()).getCurrentDate();
    }
    public void onEvent(ContentLoadedEvent event) {
//        if(event.mContentList!=null) {

        mContentList = event.mContentList;
        ApplicationController controller = (ApplicationController) getApplicationContext();
        controller.setContents(mContentList);

        mArticlePagerAdapter = new ArticlePagerAdapter(getSupportFragmentManager(),this);
        mViewPager.setAdapter(mArticlePagerAdapter);
        mViewPager.setOffscreenPageLimit(1);
//        }else{
      //      Toast.makeText(this,"Empty Content",Toast.LENGTH_SHORT);
//        }

    }
    public void onHomeBtnClick(View view){
        startActivity(new Intent(this, PickLangActivity.class));
        overridePendingTransition(R.anim.slideinup, R.anim.slideoutup);
        stopShowInfoMessageRepeat();

        ArrayList<ArticleFragment> fragments = mArticlePagerAdapter.getRegisteredFragmentCurrent(mSelectedIndex);
        if(fragments!=null){
            for(ArticleFragment fragment:fragments){
                fragment.onPauseVideo();
            }
        }
        this.finish();

    }
    public void onCategoryBtnClick(View view){
        stopShowInfoMessageRepeat();
        startActivity(new Intent(this, MenuActivity.class));

        ArrayList<ArticleFragment> fragments = mArticlePagerAdapter.getRegisteredFragmentCurrent(mSelectedIndex);
        if(fragments!=null){
            for(ArticleFragment fragment:fragments){
                fragment.onPauseVideo();
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    public void SetHeaderTitleFromStart(String s){
        mHeaderTitle.setText(s);
    }
    public void showSwipeInfoMessage(){
//        superToast = new SuperToast(this);
//        superToast.setDuration(SuperToast.Duration.LONG);
//        superToast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL,0,30);
//        View view = superToast.getView();
//        view.setBackgroundResource(R.color.black);
//        superToast.setText(getString(R.string.info_swipe_string));
//        superToast.setTextColor(Color.WHITE);
        Toast superToast = Toast.makeText(this, R.string.info_swipe_string, Toast.LENGTH_LONG);

        superToast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 10);
//        View view = superToast.getView();
//        view.setBackgroundResource(R.color.black);



       // superToast.setIcon(R.drawable.swipe, SuperToast.IconPosition.LEFT);
        superToast.show();
    }
    public void showEndOfDayMessage(){
        Toast lastToast = Toast.makeText(this,"Last Video Of the Day",Toast.LENGTH_LONG);
        lastToast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 10);
        lastToast.show();
//        SuperToast.create(this, "Last Video Of the Day", SuperToast.Duration.LONG,
//                Style.getStyle(Style.BLACK, SuperToast.Animations.FLYIN)).show();


    }
    public void setShowInfoMessageRepeat(){

        mHandler = new Handler();
        mHandler.postDelayed(mRunnable, 4000);
    }
    public void stopShowInfoMessageRepeat(){
        if(mHandler!=null) {
            mHandler.removeCallbacks(mRunnable);
        }
    }
    private Runnable mRunnable = new Runnable() {

        @Override
        public void run() {
            Log.e("Handlers", "Calls");
            /** Do something **/
            mHandler.postDelayed(mRunnable, 4000);
            showSwipeInfoMessage();


        }
    };
    public void resetVideo(){
        mViewPager.setAdapter(mArticlePagerAdapter);
    }
    public void swipeOn(){
        mViewPager.setSwipable(true);
    }
    public void swipeOff(){
        mViewPager.setSwipable(false);
    }
}
