package com.tirdis.watchorread.adapter;


import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.Fragment;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.tirdis.watchorread.ApplicationController;
import com.tirdis.watchorread.ArticleViewActivity;
import com.tirdis.watchorread.model.Category;
import com.tirdis.watchorread.model.Content;
import com.tirdis.watchorread.view.ArticleFragment;

import java.util.ArrayList;
import java.util.List;

import static com.tirdis.watchorread.view.ArticleFragment.*;

/**
 * Created by admin on 5/24/2015.
 */
public class ArticlePagerAdapter extends FragmentPagerAdapter {


    private List<Content> filteredContents = new ArrayList<>();
    private Context mContext;

    SparseArray<Fragment> registredFragments = new SparseArray<Fragment>();
    ArrayList<Integer> pages = new ArrayList<>();

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment)super.instantiateItem(container, position);
        registredFragments.put(position, fragment);
        return fragment;
    }
    public ArrayList<ArticleFragment> getRegisteredFragment(int position){
        ArrayList<ArticleFragment> fragments = new ArrayList<>();
        for(int i = 0;i < registredFragments.size();i++){
            int key = registredFragments.keyAt(i);
            if(key!=position){
                fragments.add((ArticleFragment)registredFragments.get(key));
            }
        }
        return fragments;

    }
    public ArrayList<ArticleFragment> getRegisteredFragmentCurrent(int position){
        ArrayList<ArticleFragment> fragments = new ArrayList<>();
        for(int i = 0;i < registredFragments.size();i++){
            int key = registredFragments.keyAt(i);
            if(key == position){
                fragments.add((ArticleFragment)registredFragments.get(key));
            }
        }
        return fragments;

    }
    public ArticleFragment getSelectedFragment(int position){
        return (ArticleFragment)registredFragments.get(position);
    }



    public ArticlePagerAdapter(FragmentManager fm,Context context) {
        super(fm);

        mContext = context;
        List<Content> contents = ((ApplicationController)context.getApplicationContext()).getContents();
        List<Boolean> isInterests = ((ApplicationController)context.getApplicationContext()).getIsInterests();
        int defaultCategory = ((ApplicationController)context.getApplicationContext()).getDefaultCategory();
        List<Category> categories = ((ApplicationController)context.getApplicationContext()).getCategories();

        for(int i = defaultCategory+1;i <isInterests.size()+1;i++){
           if(isInterests.get(i-1)){
                List<Content> subContent = new ArrayList<>();
                int subIndex=1;
                boolean isEmptyCategory = true;
               if(contents!=null){
                   for(Content content:contents) {
                       if (Integer.parseInt(content.getCatg_id()) == i) {

                           //assume id for subcategory id, lang_id for total subcategory num
                           content.setId(String.valueOf(subIndex));
                           subIndex++;
                           subContent.add(content);
                           isEmptyCategory = false;
                       }
                   }
               }else{
                   isEmptyCategory = true;
               }


                if(isEmptyCategory){
                    String text = categories.get(i-1).getCdscp();
                    filteredContents.add(new Content("0","",String.valueOf(i),"","","",text));
                }else{
                    int subCount = subContent.size();
                    for(Content content:subContent){
                        content.setLang_id(String.valueOf(subCount));
                        filteredContents.add(content);
                    }
                }
           }
        }
        for(int i = 1;i <defaultCategory + 1;i++){
            if(isInterests.get(i-1)){
                List<Content> subContent = new ArrayList<>();
                int subIndex=1;
                boolean isEmptyCategory = true;
                if(contents!=null){
                    for(Content content:contents) {
                        if (Integer.parseInt(content.getCatg_id()) == i) {

                            //assume id for subcategory id, lang_id for total subcategory num
                            content.setId(String.valueOf(subIndex));
                            subIndex++;
                            subContent.add(content);
                            isEmptyCategory = false;
                        }
                    }
                }


                if(isEmptyCategory){
                    String text = categories.get(i-1).getCdscp();
                    filteredContents.add(new Content("0","",String.valueOf(i),"","","",text));
                }else{
                    int subCount = subContent.size();
                    for(Content content:subContent){
                        content.setLang_id(String.valueOf(subCount));
                        filteredContents.add(content);
                    }
                }
            }
        }
    }

    // Returns total number of pages
    @Override
    public int getCount() {
        return filteredContents.size();
    }

    public List<Content> getFilteredContents() {
        return filteredContents;
    }

    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {
        int isStart;
        if(position==0){
            isStart = 0;
        }else if(position == getCount()-1){
            isStart = 1;
        }else{
            isStart = 2;
        }

        if((isStart == 0) && (!((ApplicationController)mContext.getApplicationContext()).isIsCategoryClick())){
            List<Category> categories = ((ApplicationController)mContext.getApplicationContext()).getCategories();
            ((ArticleViewActivity)mContext).SetHeaderTitleFromStart(categories.get(Integer.valueOf(filteredContents.get(position).getCatg_id())-1).getCdscp().toUpperCase());
        }
        Fragment fragment = ArticleFragment.newInstance(filteredContents.get(position),isStart);

//        if(pages.size()>=2){
//
//            ((ArticleFragment)getItem(pages.get(pages.size()-2))).refresh();
//        }

        //getSelectedFragment(position).refresh();
        return fragment;
    }

    public void setFilteredContents(String categoryId) {


        List<Content> contents = ((ApplicationController)mContext.getApplicationContext()).getContents();
        List<Category> categories = ((ApplicationController)mContext.getApplicationContext()).getCategories();
        filteredContents.clear();


                List<Content> subContent = new ArrayList<>();
                int subIndex=1;
                boolean isEmptyCategory = true;
                if(contents!=null){
                    for(Content content:contents) {
                        if (content.getCatg_id().equals(categoryId)) {

                            //assume id for subcategory id, lang_id for total subcategory num
                            content.setId(String.valueOf(subIndex));
                            subIndex++;
                            subContent.add(content);
                            isEmptyCategory = false;
                        }
                    }
                }


                if(isEmptyCategory){
                    String text = categories.get(Integer.parseInt(categoryId)-1).getCdscp();
                    filteredContents.add(new Content("0","",categoryId,"","","",text));
                }else{
                    int subCount = subContent.size();
                    for(Content content:subContent){
                        content.setLang_id(String.valueOf(subCount));
                        filteredContents.add(content);
                    }
                }

        notifyDataSetChanged();
        this.filteredContents = filteredContents;
    }
}
