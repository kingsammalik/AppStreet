package com.samapps.appstreet;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;
import java.util.List;

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
        final ImageView imageView;

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

            saveImage(position);

            //Glide.with(mContext).load(photo.get(position).getPhotoPath()).centerCrop().into(imageView);
        }
        else {
            Picasso.get().load(searchList.get(position).getPath()).into(imageView);
            //Glide.with(mContext).load(searchList.get(position).getPath()).into(imageView);
        }

        return convertView;
    }

    private void saveImage(final int position) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    Bitmap bitmap = Picasso.get().load(photo.get(position).getPhotoPath()).get();
                    Log.e("tag","size "+bitmap.getHeight()+" * "+bitmap.getWidth());
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();
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


    private static Bitmap resize(Bitmap image, int maxWidth, int maxHeight) {
        if (maxHeight > 0 && maxWidth > 0) {
            int width = image.getWidth();
            int height = image.getHeight();
            float ratioBitmap = (float) width / (float) height;
            float ratioMax = (float) maxWidth / (float) maxHeight;

            int finalWidth = maxWidth;
            int finalHeight = maxHeight;
            if (ratioMax > ratioBitmap) {
                finalWidth = (int) ((float)maxHeight * ratioBitmap);
            } else {
                finalHeight = (int) ((float)maxWidth / ratioBitmap);
            }
            image = Bitmap.createScaledBitmap(image, finalWidth, finalHeight, true);
            return image;
        } else {
            return image;
        }
    }

}
