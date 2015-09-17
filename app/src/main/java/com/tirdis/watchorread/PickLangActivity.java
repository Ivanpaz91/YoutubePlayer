package com.tirdis.watchorread;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.DataSetObserver;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.tirdis.watchorread.event.ApiErrorEvent;
import com.tirdis.watchorread.event.CategoryLoadEvent;
import com.tirdis.watchorread.event.ContentLoadEvent;
import com.tirdis.watchorread.event.ContentLoadedEvent;
import com.tirdis.watchorread.event.LanguageLoadEvent;
import com.tirdis.watchorread.event.LanguageLoadedEvent;
import com.tirdis.watchorread.model.Content;
import com.tirdis.watchorread.model.Language;
import com.tirdis.watchorread.view.ArticleFragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.greenrobot.event.EventBus;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;


public class PickLangActivity extends Activity implements ArticleFragment.OnFragmentInteractionListener {
    Context mContext ;

    List<Language> languages;

    List<String> languageList = new ArrayList<>();
    List<String> languageIdList = new ArrayList<>();
    ArrayAdapter<String>  mSpinnerAdapter;

    int mSelectLanguageId = 0;

    ApplicationController mController;

    String langId;
    int ghostCount=0;
    AlertDialog mAlertDialog;

    @InjectView(R.id.spinner_countries)
    Button mCountrySpinner;
    @InjectView(R.id.progressBar)
    GifImageView mProgressBar;

    int networkConnectionCount= 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.activity_language);
        ButterKnife.inject(this);

        mContext = this;
        mController = (ApplicationController)getApplicationContext();

}

    @Override
    public void onResume() {
        mCountrySpinner.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.VISIBLE);

        super.onResume();

        EventBus.getDefault().register(this);
        EventBus.getDefault().post(new LanguageLoadEvent());


    }
    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public void onEvent(LanguageLoadedEvent event) {
       //SpinnerAdapter.clear();
        mCountrySpinner.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.INVISIBLE);
        languageList.clear();
        for(Language language:event.mLanguages){
            languageList.add(language.getLdscp());
            languageIdList.add(language.getId());
        }



        setSpinnerList();

//        mCountrySpinner.setAdapter(mSpinnerAdapter);
//        mCountrySpinner.setSelection(mSpinnerAdapter.getCount());

        int selectId = Integer.parseInt(ApplicationController.getInstance().getSelectedLang());
//        if (selectId == -1) {
//            mCountrySpinner.setSelection(mSpinnerAdapter.getCount());
//        } else {
//            //mCountrySpinner.setSelection(selectId - 1);
//        }

      //  mSpinnerAdapter.notifyDataSetChanged();


    }
    public void onEvent(ContentLoadedEvent event) {

        ApplicationController controller = (ApplicationController)getApplicationContext();
        controller.setContents(event.mContentList);
        startActivity(new Intent(mContext, ArticleViewActivity.class));
        overridePendingTransition(R.anim.slideindown, R.anim.slideoutdown);
        mProgressBar.setVisibility(View.INVISIBLE);
        finish();

    }
    public void onEvent(ApiErrorEvent event) {
        if(event.mError.equals("Content")){
             networkConnectionCount += 1;
            if(networkConnectionCount < 3) {
                EventBus.getDefault().post(new ContentLoadEvent(langId, ApplicationController.getInstance().getCurrentDate()));
            }else{
                Toast.makeText(this,"Network Connection Error",Toast.LENGTH_SHORT).show();
                networkConnectionCount = 0;
                mProgressBar.setVisibility(View.INVISIBLE);
                mCountrySpinner.setVisibility(View.VISIBLE);
            }
        }
        if(event.mError.equals("category")){
            networkConnectionCount += 1;
            if(networkConnectionCount < 3) {

                EventBus.getDefault().post(new CategoryLoadEvent());
            }else{
                Toast.makeText(this,"Network Connection Error",Toast.LENGTH_SHORT).show();
                networkConnectionCount = 0;
                mProgressBar.setVisibility(View.INVISIBLE);
                EventBus.getDefault().post(new CategoryLoadEvent());
              //  mCountrySpinner.setVisibility(View.VISIBLE);
            }
        }


    }

    private void setSpinnerList() {
        int selectedLang = Integer.parseInt(ApplicationController.getInstance().getSelectedLang());
        if(selectedLang == -1){
            mCountrySpinner.setHint("CHOOSE LANGUAGE");
        }else{
            mCountrySpinner.setText(languageList.get(selectedLang));
        }


        mSpinnerAdapter = new ArrayAdapter<String>(this,R.layout.view_spinner_lang_item,languageList);
        mAlertDialog = new AlertDialog.Builder(this).setSingleChoiceItems(mSpinnerAdapter, mSelectLanguageId,
                new  DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        mProgressBar.setVisibility(View.VISIBLE);
                        mCountrySpinner.setVisibility(View.INVISIBLE);

                        mCountrySpinner.setText(languageList.get(which).toString());
                        mSelectLanguageId = which;
                        mAlertDialog.dismiss();

                        String langId = languageIdList.get(mSelectLanguageId);
                        ApplicationController.getInstance().setSelectedLang(String.valueOf(mSelectLanguageId));
                        ApplicationController.getInstance().setSelectedLangID(langId);

                        EventBus.getDefault().post(new ContentLoadEvent(langId,ApplicationController.getInstance().getCurrentDate()));

//                    Log.e("Retrofit","Content");

                    }}).create();

        mCountrySpinner.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Display display = getWindowManager().getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);
                int width = size.x;
                int height = size.y;
                mAlertDialog.getWindow().getAttributes().verticalMargin = 0.2F;
                mAlertDialog.getListView().setSelection(mSelectLanguageId);
                mAlertDialog.show();
                Resources r = getResources();
                float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,250,r.getDisplayMetrics());
                mAlertDialog.getWindow().setLayout(Math.round(px), height / 2);
                mAlertDialog.setCanceledOnTouchOutside(true);


            }
        });





        //     mSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);



//        mCountrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                //change color of selected item
//
//
//                Boolean isStatus = false;
//                if(mController.getSelectedCountry().equals("-1")){
//                    mSelectLanguageId = position;
//                    langId  = String.valueOf(mSelectLanguageId);
//
//                    if(position!= mSpinnerAdapter.getCount()){
//                     //   isStatus = true;
//                    }
//                }else{
//                    if(ghostCount == 1) {
//                        mSelectLanguageId = position + 1;
//                        langId = String.valueOf(mSelectLanguageId);
////                    if(!langId.equals(mController.getSelectedCountry())){
//                        isStatus = true;
//                        ghostCount = 0;
//                    }
////
//                    ghostCount++;
//                }
//                if(isStatus){
//                    mController.setSelectedCountry(langId);
//                    ApplicationController.getInstance().setSelectedCountry(String.valueOf(langId));
//                    EventBus.getDefault().post(new ContentLoadEvent(langId,ApplicationController.getInstance().getCurrentDate()));
//                  //  EventBus.getDefault().post(new ContentLoadEvent("1","2015-05-21"));
//                    Log.e("Retrofit","Content");
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}



