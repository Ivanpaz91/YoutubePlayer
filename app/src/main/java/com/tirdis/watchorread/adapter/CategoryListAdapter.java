package com.tirdis.watchorread.adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;

import com.kyleduo.switchbutton.SwitchButton;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tirdis.watchorread.ApplicationController;
import com.tirdis.watchorread.CategoryActivity;
import com.tirdis.watchorread.R;
import com.tirdis.watchorread.model.Category;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 5/27/2015.
 */
public class CategoryListAdapter extends BaseAdapter {

    private Context mContext;
    List<Category> categoryList;
    String mFallbackUrl = null;
    boolean should_run = false;

    private static LayoutInflater mInflater = null;

    public int getDefaultCategory() {
        return defaultCategory;
    }

    public void setDefaultCategory(int defaultCategory) {
        this.defaultCategory = defaultCategory;
    }

    private int defaultCategory = 0;
    private List<Boolean> isInterests;

    public List<Boolean> getIsInterests() {
        return isInterests;
    }

    public CategoryListAdapter(Context context) {
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);
        try {
            categoryList = ((ApplicationController)mContext.getApplicationContext()).getCategories();

        } catch (Exception e) {
            categoryList = null;
            e.printStackTrace();
        }

        Drawable drawable = mContext.getResources().getDrawable(R.drawable.home);

        //set interest status.
        isInterests = ((ApplicationController) mContext.getApplicationContext()).getIsInterests();
        defaultCategory  = ((ApplicationController) mContext.getApplicationContext()).getDefaultCategory();
    }



    @Override
    public int getCount() {
        return categoryList.size();
    }

    @Override
    public Object getItem(int position) {
        return categoryList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
         ViewHolder viewHolder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.view_category_list, parent, false);
            viewHolder = new ViewHolder();

            viewHolder.mImage = (ImageView) convertView.findViewById(R.id.imageView_icon_category);
            viewHolder.mName = (TextView)convertView.findViewById(R.id.textView_name_category);
            viewHolder.mSwitch = (SwitchButton)convertView.findViewById(R.id.switch_play_category);
            viewHolder.mRadioBtn = (RadioButton)convertView.findViewById(R.id.radioButton_default_category);


            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final Category category = (Category) getItem(position);
        viewHolder.mName.setText(category.getCdscp());
        setCategoryImage(category, viewHolder);

        setSwitchBtn(position, viewHolder.mSwitch);
        setDefaultRadioBtn(position,viewHolder.mRadioBtn);


//        viewHolder.mName.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(viewHolder.mSwitch.isChecked()){
//                    ((CategoryActivity)mContext).goToCategory(category.getId());
//                }

//            }
//        });

        return convertView;
    }

    private void setSwitchBtn(final int position, SwitchButton mSwitch) {


//         if(isInterests.get(position)){
//             mSwitch.setChecked(true);
//         }else{
//             mSwitch.setChecked(false);
//         }
        if(defaultCategory == position) {
            mSwitch.setEnabled(false);
        }else{
            mSwitch.setEnabled(true);
        }
        mSwitch.setChecked(isInterests.get(position));

     //   mSwitch.setTag(position);
//        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                //     int position1 = (int)buttonView.getTag();
//                if (defaultCategory == position) {
//                    buttonView.setChecked(true);
//
//                } else {
//                    isInterests.set(position, isChecked);
//
//                }
////                isInterests.set(position,isChecked);
//// notifyDataSetChanged();
//            }
//
//
//        });

        mSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SwitchButton button = (SwitchButton)v;
                if(defaultCategory == position){
//                    button.setChecked(true);
             //
//                   button.toggle();
                }else{
                    button.setEnabled(true);
                   if(button.isChecked()) {
                       button.setChecked(false);
                       isInterests.set(position,false);
                   }else{
                       button.setChecked(true);
                       isInterests.set(position,true);
                   }

                }

            }
        });

    }

    private void setDefaultRadioBtn(final int position,RadioButton radioButton) {

       if(position == defaultCategory){
           radioButton.setChecked(true);
       }else{
           radioButton.setChecked(false);
       }
        radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isInterests.get(position)){
                    ((RadioButton)v).setChecked(false);
                }else {
                    defaultCategory = position;

                }
                notifyDataSetChanged();
            }
        });
//        radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView1, boolean isChecked) {
//                if(isChecked){
//                    if(!isInterests.get(position)){
//                        buttonView1.setChecked(false);
//                    }else{
//                        defaultCategory = position;
//                        notifyDataSetChanged();
//                    }
//
//                }
//            }
//        });

    }

    private void setCategoryImage(Category category,ViewHolder viewHolder) {
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage("http://watchorread.com/categories/" + category.getCimage(), viewHolder.mImage, ApplicationController.getInstance().getUniversialImageOpition());
    }

     class ViewHolder {
        ImageView mImage;
        TextView mName;
        RadioButton mRadioBtn;
        SwitchButton mSwitch;
    }

    }
