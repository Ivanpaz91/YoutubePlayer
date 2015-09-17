package com.tirdis.watchorread;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;

import com.tirdis.watchorread.R;
import com.tirdis.watchorread.adapter.CategoryListAdapter;
import com.tirdis.watchorread.model.Category;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class CategoryActivity extends Activity {
    private List<Category> mCategories;
    private CategoryListAdapter categoryAdapter;
    @InjectView(R.id.listView_category)
    ListView mCategoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_category);

        this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ButterKnife.inject(this);

        setCategoryList();
    }

    private void setCategoryList() {
        categoryAdapter = new CategoryListAdapter(this);
        mCategoryList.setAdapter(categoryAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_category, menu);
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
    public void onBackBtnClick(View view){
        this.finish();
    }

    public void onDoneBtnClick(View view){
        //save status.
        ((ApplicationController)getApplicationContext()).setIsInterests(categoryAdapter.getIsInterests());
        ((ApplicationController)getApplicationContext()).setDefaultCategory(categoryAdapter.getDefaultCategory());
        //save to preferences
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = settings.edit();

        String interestString="";
        for(boolean b:categoryAdapter.getIsInterests()){
            if(b){interestString += "1";}
            else{interestString += "0";}
         }
        editor.putString("INTERESTS",interestString);
        editor.putInt("DEFAULT_CATEGORY",categoryAdapter.getDefaultCategory());

        editor.commit();



        Intent intent = new Intent(this, MenuActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("NoArticle",true);
        startActivity(intent);


    }

    public void goToCategory(String id) {

        ((ApplicationController)getApplicationContext()).setIsCategoryClick(true);
        Intent intent = new Intent(this, ArticleViewActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("GOTOINDEX", id);
        startActivity(intent);
    }
    public void onHelpBtnClick(View view){
        Intent intent = new Intent(this, HelpActivity.class);


        startActivity(intent);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }
}
