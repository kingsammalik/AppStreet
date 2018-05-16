package com.samapps.appstreet;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AbsListView.OnScrollListener {

    String FlickrQuery_url = "https://api.flickr.com/services/rest/?method=flickr.photos.search";
    String FlickrQuery_per_page = "&per_page=12";
    String FlickrQuery_nojsoncallback = "&nojsoncallback=1";
    String FlickrQuery_page = "&page=";
    String FlickrQuery_format = "&format=json";
    String FlickrQuery_tag = "&tags=";
    String FlickrQuery_key = "&api_key=d8ac26169e4f789e882e0112e7cfce04";

    GridView gridView;
    EditText editText;
    int page=1;
    String TAG=MainActivity.class.getName();
    ImageResponse imageResponse;
    GridAdapter gridAdapter;
    boolean isLoading=false;
    List<Photo> photo;
    String tag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        photo = new ArrayList<>();
        gridView = findViewById(R.id.gridview);
        gridView.setNumColumns(2);
        editText = findViewById(R.id.edit);

        (findViewById(R.id.search)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tag=editText.getText().toString();
                callFlickr();

            }
        });
        gridView.setOnScrollListener(this);
        gridAdapter=new GridAdapter(photo,MainActivity.this);
        gridView.setAdapter(gridAdapter);
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
                Log.d(TAG, response);
                isLoading=false;
                imageResponse = new Gson().fromJson(response,ImageResponse.class);
                storeImage();
                photo.addAll(imageResponse.getPhotos().getPhoto());
                page++;
                gridAdapter.notifyDataSetChanged();
                //Log.e(TAG, imageResponse.getPhotos().getPhoto().get(0).getTitle());
                pDialog.hide();


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                pDialog.hide();
            }
        });



// Adding request to request queue
        Volley.newRequestQueue(this).add(strReq);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (totalItemCount > 0) {
            int lastVisibleItem = firstVisibleItem + visibleItemCount;
            Log.e(TAG,"first item "+firstVisibleItem+" visible count "+visibleItemCount+" total "+totalItemCount);
            if (!isLoading &&  (lastVisibleItem == totalItemCount)) {
                isLoading = true;
                //callFlickr();
                //load more items--
                Log.e(TAG,"loading items "+page);

                //isLoading=false;
            }
        }
    }

    public File getCacheFolder(Context context) {
        File cacheDir = null;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            cacheDir = new File(Environment.getExternalStorageDirectory(), "cachefolder");
            if(!cacheDir.isDirectory()) {
                cacheDir.mkdirs();
            }
        }

        if(!cacheDir.isDirectory()) {
            cacheDir = context.getCacheDir(); //get system cache folder
        }

        return cacheDir;
    }




    public void storeImage() {

    final List<path> pathList=new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < photo.size(); i++) {
                    URL wallpaperURL = null;
                    try {
                        wallpaperURL = new URL(photo.get(i).getPhotoPath());
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    try {
                        URLConnection connection = wallpaperURL.openConnection();
                        InputStream inputStream = new BufferedInputStream(wallpaperURL.openStream(), 10240);
                        File cacheDir = getCacheFolder(MainActivity.this);
                        File cacheFile = new File(cacheDir, photo.get(i).getId()+".jpg");
                        pathList.add(new path(cacheFile.getAbsolutePath()));
                        Log.e(TAG,"path "+cacheFile.getAbsolutePath());
                        FileOutputStream outputStream = new FileOutputStream(cacheFile);

                        byte buffer[] = new byte[1024];
                        int dataSize;
                        int loadedSize = 0;
                        while ((dataSize = inputStream.read(buffer)) != -1) {
                            loadedSize += dataSize;
                            outputStream.write(buffer, 0, dataSize);
                        }

                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }



                }
                List<Search> searchList = new ArrayList<>();
                searchList.add(new Search(tag,pathList));
                offLine offLineobj = new offLine(searchList);
                //offLineobj.getSearchList().add(new Search(tag,pathList));
                System.out.println("size "+offLineobj.getSearchList().size());
                Gson gson = new Gson();
                String json = gson.toJson(offLineobj);
                System.out.println(json);
            }
        }).start();
    }
}
