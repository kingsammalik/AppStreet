package com.samapps.appstreet;

import android.app.Activity;
import android.app.SharedElementCallback;
import android.content.Context;
import android.support.transition.ChangeBounds;
import android.support.transition.ChangeClipBounds;
import android.support.transition.ChangeImageTransform;
import android.support.transition.ChangeTransform;
import android.support.transition.Explode;
import android.support.transition.Fade;
import android.support.transition.Slide;
import android.support.transition.TransitionSet;
import android.support.v7.widget.RecyclerView;
import android.transition.TransitionInflater;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;
import java.util.Map;

import static java.security.AccessController.getContext;

public class GridAdapter extends BaseAdapter {

    private Context mContext;
    private List<Photo> photo;
    private List<path> searchList;
    private Search search;
    private boolean isOnline;
    private String keyword;

    // Constructor
    public GridAdapter(List<Photo> photo, Search search, boolean isOnline, Context c, String keyword) {
        mContext = c;
        this.photo=photo;
        this.search=search;
        this.isOnline=isOnline;
        this.keyword=keyword;
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
        ImageView imageView;

        if (convertView == null) {
           // imageView = new ImageView(mContext);
            //imageView.setPadding(8, 8, 8, 8);
            LayoutInflater li = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = li.inflate(R.layout.lay_imageview, null);
        }
        else
        {
           // imageView = (ImageView) convertView;
        }
        imageView = convertView.findViewById(R.id.imageview);
        imageView.setTransitionName("T"+String.valueOf(position));
        if (isOnline){
            Picasso.get().load(photo.get(position).getPhotoPath()).resize(250,250).centerCrop().into(imageView);
        }
        else {
            Picasso.get().load(searchList.get(position).getPath()).into(imageView);
        }

        return convertView;
    }
    /*private void prepareTransitions() {
        ((Activity)mContext).getWindow().setExitTransition(TransitionInflater.from(getContext())
                .inflateTransition(R.transition.shared_element_transation));

        // A similar mapping is set at the ImagePagerFragment with a setEnterSharedElementCallback.
        ((Activity)mContext).setExitSharedElementCallback(
                new SharedElementCallback() {
                    @Override
                    public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
                        // Locate the ViewHolder for the clicked position.
                       *//* RecyclerView.ViewHolder selectedViewHolder = recyclerView
                                .findViewHolderForAdapterPosition(MainActivity.currentPosition);
                        if (selectedViewHolder == null || selectedViewHolder.itemView == null) {
                            return;
                        }*//*

                        // Map the first shared element name to the child ImageView.
                        sharedElements
                                .put(names.get(0), getView().findViewById(R.id.card_image));
                    }
                });
    }*/

}
