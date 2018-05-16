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
    List<Photo> photo;
    private Callback callback;

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public interface Callback{
        void storeImage(String path,String id);
    }

    // Constructor
    public GridAdapter(List<Photo> photo, Context c) {
        mContext = c;
        this.photo=photo;
    }

    public int getCount() {
        return photo.size();
    }

    public Object getItem(int position) {
        return photo.get(position).getPhotoPath();
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        //System.out.println(position);
        ImageView imageView;
        Photo photoobj = photo.get(position);
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
        Picasso.get().load(photoobj.getPhotoPath()) .resize(250, 250)
                .centerCrop().into(imageView);
        //callback.storeImage(photoobj.getPhotoPath(),photoobj.getId());
        //imageView.setImageResource(mThumbIds[position]);
        return imageView;
    }
}
