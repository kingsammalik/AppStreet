package com.samapps.appstreet;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

public class GridAdapter extends BaseAdapter {

    private Context mContext;
    List<Photo> photo;
    private Callback callback;
    private Search search;
    private boolean isOnline;

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public interface Callback{
        void storeImage(String path,String id);
    }

    // Constructor
    public GridAdapter(List<Photo> photo, Search search, boolean isOnline, Context c) {
        mContext = c;
        this.photo=photo;
        this.search=search;
        this.isOnline=isOnline;
    }

    public int getCount() {
        if (isOnline)
        return photo.size();
        else return search.getPathList().size();
    }

    public Object getItem(int position) {
        if (isOnline)
        return photo.get(position).getPhotoPath();
        else return search.getPathList().get(position).getPath();
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        //System.out.println(position);
        ImageView imageView;

        if (convertView == null) {
            imageView = new ImageView(mContext);
            //imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
           // imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        }
        else
        {
            imageView = (ImageView) convertView;
        }
        imageView.setTransitionName(String.valueOf(position));
        if (isOnline){
            Picasso.get().load(photo.get(position).getPhotoPath()) .resize(250, 250)
                    .centerCrop().into(imageView);
        }
        else {
            Picasso.get().load(new File(search.getPathList().get(position).getPath())) .resize(250, 250)
                    .centerCrop().into(imageView);
        }

        return imageView;
    }
}
