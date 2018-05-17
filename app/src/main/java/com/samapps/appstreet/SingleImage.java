package com.samapps.appstreet;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;

import java.io.File;

public class SingleImage extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private static int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_image);
        getWindow().setSharedElementEnterTransition(TransitionInflater.from(this).inflateTransition(R.transition.shared_element_transation));
        //mainImage.setTransitionName("thumbnailTransition");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        position = getIntent().getIntExtra("position",0);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter();

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(position);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //Log.e("tag","act "+position);

            }

            @Override
            public void onPageSelected(int position) {
                SingleImage.position=position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });



    }

    @Override
    public void onBackPressed() {
        supportFinishAfterTransition();
        super.onBackPressed();
    }

    public class SectionsPagerAdapter extends PagerAdapter {
        LayoutInflater layoutInflater;
        public SectionsPagerAdapter() {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }



        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            View itemView = layoutInflater.inflate(R.layout.fragment_single_image, container, false);

            ImageView imageView = (ImageView) itemView.findViewById(R.id.imageview);
            imageView.setTransitionName(String.valueOf(position));
            if (BaseModel.isIsOnline()){
                Picasso.get().load(BaseModel.getPhotos().get(position).getPhotoPath()).noFade().into(imageView);
            }
            else {
                Picasso.get().load(new File(BaseModel.getSearch().getPathList().get(position).getPath())).noFade().into(imageView);
            }
            //Picasso.get().load(BaseModel.getPhotos().get(position).getPhotoPath()).noFade().networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE) .into(imageView);
            container.addView(itemView);



            return itemView;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            if (BaseModel.isIsOnline()){
                return BaseModel.getPhotos().size();
            }
            else {
                return BaseModel.getSearch().getPathList().size();
            }
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == ((LinearLayout) object);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((LinearLayout) object);
        }
    }
}
