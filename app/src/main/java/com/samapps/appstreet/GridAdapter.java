package com.samapps.appstreet;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class GridAdapter extends BaseAdapter {

    private Context mContext;
    private List<Photo> photo;
    private List<path> searchList;
    private boolean isOnline;

    // Constructor
    public GridAdapter(List<Photo> photo, boolean isOnline, Context c) {
        mContext = c;
        this.photo=photo;
        this.isOnline=isOnline;
    }

    public GridAdapter(Context mContext, List<path> searchList, boolean isOnline) {
        this.mContext = mContext;
        this.searchList = searchList;
        this.isOnline = isOnline;
    }

    public int getCount() {
        if (isOnline)
        return photo.size();
        else return searchList.size();
    }

    public Object getItem(int position) {
        if (isOnline)
        return photo.get(position).getPhotoPath();
        else return searchList.get(position).getPath();
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        //System.out.println(position);
        final ImageView imageView;

        if (convertView == null) {
            imageView = new ImageView(mContext);
            imageView.setPadding(8, 8, 8, 8);

        }
        else
        {
            imageView = (ImageView) convertView;
        }
        imageView.setTransitionName("T"+String.valueOf(position));

        if (isOnline){
            Picasso.get().load(photo.get(position).getPhotoPath()).resize(250,250).centerCrop().into(imageView);
           //Picasso itself stores the image in the LRU cache and loads it from the cache when same url is passed to it.
        }
        else {
            Picasso.get().load(searchList.get(position).getPath()).resize(250,250).centerCrop().into(imageView);

        }

        return imageView;
    }



}
