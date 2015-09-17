package com.tirdis.watchorread.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.tirdis.watchorread.ApplicationController;
import com.tirdis.watchorread.CategoryActivity;
import com.tirdis.watchorread.MenuActivity;
import com.tirdis.watchorread.R;
import com.tirdis.watchorread.model.Category;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sb on 6/12/15.
 */

public class GridMenuAdapter extends ArrayAdapter<Category> {
    Context context;
    int layoutResourceId;
    List<Category> data = new ArrayList<Category>();

    public GridMenuAdapter(Context context, int layoutResourceId,
                                 List<Category> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        RecordHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new RecordHolder();
            holder.txtTitle = (TextView) row.findViewById(R.id.item_text);
            holder.imageItem = (ImageView) row.findViewById(R.id.item_image);
            row.setTag(holder);
        } else {
            holder = (RecordHolder) row.getTag();
        }

        final Category item = data.get(position);
        holder.txtTitle.setText(item.getCdscp());

        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage("http://watchorread.com/categories/" + item.getCimage(), holder.imageItem, ApplicationController.getInstance().getUniversialImageOpition());
        holder.imageItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MenuActivity) context).goToCategory(item.getId());
            }
        });

        return row;

    }

    static class RecordHolder {
        TextView txtTitle;
        ImageView imageItem;

    }
}
