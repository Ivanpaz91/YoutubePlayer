package com.tirdis.watchorread;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridView;

import com.tirdis.watchorread.adapter.GridMenuAdapter;
import com.tirdis.watchorread.model.Category;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class MenuActivity extends Activity {
    @InjectView(R.id.gridView_menu)
    GridView mGridView;

    GridMenuAdapter mGridMenuAdapter;
    boolean mIsNoArticleView;
    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_menu);

        this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ButterKnife.inject(this);

        Intent intent = getIntent();
        if(intent.hasExtra("NoArticle")){
            if(intent.getExtras().getBoolean("NoArticle")){
                mIsNoArticleView = true;
            }
        }
        setCategoryList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_menu, menu);
        return true;
    }
    private void setCategoryList() {
        List<Category> categoryActivityList = ((ApplicationController)getApplicationContext()).getCategories();
        List<Category> filteredCategoryActivityList = new ArrayList<>();
        List<Boolean> isInterests = ((ApplicationController)getApplicationContext()).getIsInterests();
        int defaultCategory = ((ApplicationController)getApplicationContext()).getDefaultCategory();
        try {
            for (int i = defaultCategory; i < categoryActivityList.size(); i++) {
//                if (isInterests.get(i)) {
                    filteredCategoryActivityList.add(categoryActivityList.get(i));
//                }
            }
            ;
        }catch (Exception e){

        }
        try {
            for (int i = 0; i < defaultCategory; i++) {
//                if (isInterests.get(i)) {
                    filteredCategoryActivityList.add(categoryActivityList.get(i));
//                }
            }
            ;
        } catch (Exception e){
        }

        mGridMenuAdapter = new GridMenuAdapter(this,R.layout.view_row_grid,filteredCategoryActivityList);
        mGridView.setAdapter(mGridMenuAdapter);
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
    public void onSettingBtnClick(View view){
        startActivity(new Intent(this, CategoryActivity.class));
    }
    public void onBackBtnClick(View view){
        this.finish();

        if(mIsNoArticleView){
            Intent intent = new Intent(this, ArticleViewActivity.class);
            startActivity(intent);

        }
    }
    public void goToCategory(String id) {
        ((ApplicationController)getApplicationContext()).setIsCategoryClick(true);
        Intent intent = new Intent(this, ArticleViewActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("GOTOINDEX", id);
        startActivity(intent);
    }
}
