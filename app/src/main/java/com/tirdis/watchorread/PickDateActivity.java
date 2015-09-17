package com.tirdis.watchorread;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.pnikosis.materialishprogress.ProgressWheel;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;
import com.squareup.timessquare.CalendarPickerView;
import com.tirdis.watchorread.event.ApiErrorEvent;
import com.tirdis.watchorread.event.ContentLoadEvent;
import com.tirdis.watchorread.event.ContentLoadedEvent;
import com.tirdis.watchorread.event.LanguageLoadEvent;
import com.tirdis.watchorread.util.DateHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.greenrobot.event.EventBus;
import pl.droidsonroids.gif.GifImageView;


public class PickDateActivity extends FragmentActivity {

    private boolean undo = false;
    private CaldroidFragment caldroidFragment;
    private CaldroidFragment dialogCaldroidFragment;

//    @InjectView(R.id.view_calendar)
//    CalendarPickerView mCalendarView;

//    @InjectView(R.id.progress_wheel)
//    ProgressWheel mProgressBar;
    Date mSelectDate = null;
    @InjectView(R.id.progressBar1)
    GifImageView mProgressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_pick_date);
        ButterKnife.inject(this);

        this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //code add
        final SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");

        // Setup caldroid fragment
        // **** If you want normal CaldroidFragment, use below line ****
        caldroidFragment = new CaldroidFragment();

        // //////////////////////////////////////////////////////////////////////
        // **** This is to show customized fragment. If you want customized
        // version, uncomment below line ****
//		 caldroidFragment = new CaldroidSampleCustomFragment();

        // Setup arguments

        // If Activity is created after rotation
        if (savedInstanceState != null) {
            caldroidFragment.restoreStatesFromKey(savedInstanceState,
                    "CALDROID_SAVED_STATE");
        }
        // If activity is created from fresh
        else {
            Bundle args = new Bundle();
            Calendar cal = Calendar.getInstance();
            args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
            args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
            args.putBoolean(CaldroidFragment.ENABLE_SWIPE, true);
            args.putBoolean(CaldroidFragment.SIX_WEEKS_IN_CALENDAR, true);


            args.putInt(CaldroidFragment.THEME_RESOURCE, com.caldroid.R.style.CaldroidDefault);

            caldroidFragment.setArguments(args);
        }

        setCustomResourceForDates();

        // Attach to the activity
        FragmentTransaction t = this.getSupportFragmentManager().beginTransaction();
        t.replace(R.id.calendar1, caldroidFragment);
        t.commit();

        // Setup listener
        final CaldroidListener listener = new CaldroidListener() {

            @Override
            public void onSelectDate(Date date, View view) {
                Toast.makeText(getApplicationContext(), formatter.format(date),
                        Toast.LENGTH_SHORT).show();

                if(mSelectDate != null) {
                    caldroidFragment.clearBackgroundResourceForDate(mSelectDate);
                    caldroidFragment.setTextColorForDate(R.color.black, mSelectDate);
                }
                caldroidFragment.setBackgroundResourceForDate(R.color.red,
                        date);

                caldroidFragment.setTextColorForDate(R.color.white, date);
                caldroidFragment.refreshView();
                mSelectDate = date;

            }

            @Override
            public void onChangeMonth(int month, int year) {
//                String text = "month: " + month + " year: " + year;
//                Toast.makeText(getApplicationContext(), text,
//                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClickDate(Date date, View view) {
//                Toast.makeText(getApplicationContext(),
//                        "Long click " + formatter.format(date),
//                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCaldroidViewCreated() {
                if (caldroidFragment.getLeftArrowButton() != null) {
//                    Toast.makeText(getApplicationContext(),
//                            "Caldroid view is created", Toast.LENGTH_SHORT)
//                            .show();
                }
            }

        };

        // Setup Caldroid
        caldroidFragment.setCaldroidListener(listener);


    }
    public void onDoneBtnClick(View view){
        if(mSelectDate!=null){
            String selectedDate = DateHelper.formatUtcDate(mSelectDate);
            EventBus.getDefault().post(new ContentLoadEvent(ApplicationController.getInstance().getSelectedLangID(), selectedDate));

            ApplicationController.getInstance().setCurrentDate(selectedDate);

            mProgressBar.setVisibility(View.VISIBLE);
        }else{
            Toast.makeText(this,"Select Date!",Toast.LENGTH_SHORT).show();
        }


    }

    public void onBackBtnClick(View view){
        finish();
        overridePendingTransition(R.anim.slideindown, R.anim.slideoutdown);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pick_date, menu);
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
    public void onEvent(ContentLoadedEvent event) {

        ApplicationController controller = (ApplicationController)getApplicationContext();
        controller.setContents(event.mContentList);
        mProgressBar.setVisibility(View.GONE);
        Intent intent = new Intent(this,ArticleViewActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.slideindown, R.anim.slideoutdown);
        finish();


    }
    public void onEvent(ApiErrorEvent event) {
        if(event.mError.equals("Content")){

            //todo fix 2 time
            EventBus.getDefault().post(new ContentLoadEvent(ApplicationController.getInstance().getSelectedLang(),ApplicationController.getInstance().getCurrentDate()));
        }

    }
    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }
    @Override
    public void onResume() {
        super.onResume();

        EventBus.getDefault().register(this);


    }

    private void setCustomResourceForDates() {
        Calendar cal = Calendar.getInstance();


        String mDate = ((ApplicationController)getApplicationContext()).getCurrentDate();
        Date date = DateHelper.parseUtcDate(mDate);
        mSelectDate = date;

        if (caldroidFragment != null) {
            caldroidFragment.setBackgroundResourceForDate(R.color.red,
                    mSelectDate);
//            caldroidFragment.setBackgroundResourceForDate(R.color.green,
//                    greenDate);
            caldroidFragment.setTextColorForDate(R.color.white, date);
//            caldroidFragment.setTextColorForDate(R.color.white, greenDate);
        }
    }
}
