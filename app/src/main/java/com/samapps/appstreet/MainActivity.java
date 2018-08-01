package com.samapps.appstreet;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AbsListView.OnScrollListener {

    private static final String MY_PREFS_NAME = "APPSTREET";
    private String FlickrQuery_url = "https://api.flickr.com/services/rest/?method=flickr.photos.search";
    private String FlickrQuery_per_page = "&per_page=8";
    private String FlickrQuery_nojsoncallback = "&nojsoncallback=1";
    private String FlickrQuery_page = "&page=";
    private String FlickrQuery_format = "&format=json";
    private String FlickrQuery_tag = "&tags=";
    private String FlickrQuery_key = "&api_key=d8ac26169e4f789e882e0112e7cfce04";

    private GridView gridView;
    private EditText editText;
    private int page=1;
    private String TAG=MainActivity.class.getName();
    private ImageResponse imageResponse;
    private GridAdapter gridAdapter;
    private boolean isLoading=false;
    private List<Photo> photo;
    private String tag;
    boolean isOnline = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e("lifecycle","onCreate invoked");
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String offlinejson = prefs.getString("OFFLINE", null);
        System.out.println("offline: "+offlinejson);
        UrlModel offLineobj = new Gson().fromJson(offlinejson,UrlModel.class);
        if (offlinejson!=null)
        Base.setUrlModel(offLineobj);
        else Base.setUrlModel(new UrlModel());
        photo = new ArrayList<>();
        gridView = findViewById(R.id.gridview);
        gridView.setNumColumns(2);
        editText = findViewById(R.id.edit);

        (findViewById(R.id.search)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tag=editText.getText().toString().toLowerCase();
                if (isNetworkAvailable()){
                    BaseModel.setPhotos(photo);
                    isOnline = true;
                    gridAdapter=new GridAdapter(BaseModel.getPhotos(), true, MainActivity.this);
                    gridView.setAdapter(gridAdapter);
                    callFlickr();
                }

                else{
                    try {

                        for (Search search:Base.getUrlModel().getSearchList()){
                            if (tag.equals(search.getTag())){
                                gridAdapter=new GridAdapter(MainActivity.this,search.getPathList(),false);
                                break;
                            }
                        }
                        //gridAdapter=new GridAdapter(BaseModel.getPhotos(),search1,false ,MainActivity.this,tag);
                        gridView.setAdapter(gridAdapter);

                        handler_doc.sendEmptyMessage(0);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }

                }

            }
        });

        if(savedInstanceState != null) {
            if (isNetworkAvailable()){
                page=savedInstanceState.getInt("page");
                gridAdapter=new GridAdapter(BaseModel.getPhotos(), true, MainActivity.this);
                gridView.setAdapter(gridAdapter);
            }
            else {
                tag = savedInstanceState.getString("tag");
                for (Search search:Base.getUrlModel().getSearchList()){
                    if (tag.equals(search.getTag())){
                        gridAdapter=new GridAdapter(MainActivity.this,search.getPathList(),false);
                        break;
                    }
                }
                //gridAdapter=new GridAdapter(BaseModel.getPhotos(),search1,false ,MainActivity.this,tag);
                gridView.setAdapter(gridAdapter);

                handler_doc.sendEmptyMessage(0);
            }

        }

        gridView.setOnScrollListener(this);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                for (Search search:Base.getUrlModel().getSearchList()){
                    if (tag.equals(search.getTag())){
                        Base.setPathList(search.getPathList());
                        break;
                    }
                }
                Intent intent = new Intent(MainActivity.this, SingleImage.class);
                intent.putExtra("position",position);
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        MainActivity.this,
                        (view),
                        "T"+String.valueOf(position));

                startActivity(intent, options.toBundle());
            }
        });

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("tag",tag);
        outState.putInt("page",page);
    }



    private  Handler handler_doc = new Handler() {

        public void handleMessage(android.os.Message msg) {
            try {
                gridAdapter.notifyDataSetChanged();
            }
            catch (Exception e){
                e.printStackTrace();
            }

        }
    };

    private boolean isNetworkAvailable() {

        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);//Menu Resource, Menu
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item2:
                Toast.makeText(getApplicationContext(),"Column 2 Selected",Toast.LENGTH_LONG).show();
                gridView.setNumColumns(2);
                return true;
            case R.id.item3:
                Toast.makeText(getApplicationContext(),"Column 3 Selected",Toast.LENGTH_LONG).show();
                gridView.setNumColumns(3);
                return true;
            case R.id.item4:
                Toast.makeText(getApplicationContext(),"Column 4 Selected", Toast.LENGTH_LONG).show();
                gridView.setNumColumns(4);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    void callFlickr(){
        String url = FlickrQuery_url+FlickrQuery_per_page+FlickrQuery_nojsoncallback+FlickrQuery_page+page+FlickrQuery_format+FlickrQuery_tag+tag+FlickrQuery_key;

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    Log.d(TAG, response);
                    isLoading=false;
                    imageResponse = new Gson().fromJson(response,ImageResponse.class);
                    photo.addAll(imageResponse.getPhotos().getPhoto());
                    BaseModel.setPhotos(photo);

                    storeImageUrl(tag,imageResponse);
                    page++;
                    gridAdapter.notifyDataSetChanged();
                    pDialog.cancel();
                }
                catch (Exception e){
                    e.printStackTrace();
                }



            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                pDialog.cancel();
            }
        });

        Volley.newRequestQueue(this).add(strReq);
    }

    private void storeImageUrl(String keyword, ImageResponse url) {
        if (isOnline){
            for (Photo photo : url.getPhotos().getPhoto()){
                boolean isfound = false;
                if (!Base.getUrlModel().getSearchList().isEmpty()){
                    for (Search search:Base.getUrlModel().getSearchList()){
                        if (keyword.equals(search.getTag())){
                            search.getPathList().add(new path(photo.getPhotoPath()));
                            isfound = true;
                            break;
                        }
                    }
                }

                if (!isfound){
                    List<path> list = new ArrayList<path>();
                    list.add(new path(photo.getPhotoPath()));
                    Base.getUrlModel().getSearchList().add(new Search(keyword,list));
                }
            }
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (isOnline){
            if (totalItemCount > 0) {
                int lastVisibleItem = firstVisibleItem + visibleItemCount;
                Log.e(TAG,"first item "+firstVisibleItem+" visible count "+visibleItemCount+" total "+totalItemCount);
                if (!isLoading &&  (lastVisibleItem == totalItemCount)) {
                    isLoading = true;
                    callFlickr();
                    Log.e(TAG,"loading items "+page);

                    //isLoading=false;
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Here I am storing the tags which were used to call the api and the related results to it in the shared preference.
        Log.e("lifecycle","onDestroy invoked");
        Gson gson = new Gson();
        String json = gson.toJson(Base.getUrlModel());
        System.out.println(json);
        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putString("OFFLINE", json);
        editor.apply();
    }



}
