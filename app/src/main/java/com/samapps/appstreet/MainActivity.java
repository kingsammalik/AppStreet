package com.samapps.appstreet;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    String FlickrQuery_url = "https://api.flickr.com/services/rest/?method=flickr.photos.search";
    String FlickrQuery_per_page = "&per_page=10";
    String FlickrQuery_nojsoncallback = "&nojsoncallback=1";
    String FlickrQuery_format = "&format=json";
    String FlickrQuery_tag = "&tags=";
    String FlickrQuery_key = "&api_key=d8ac26169e4f789e882e0112e7cfce04";

    GridView gridView;
    EditText editText;
    int page=1;
    String TAG=MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gridView = findViewById(R.id.gridview);
        editText = findViewById(R.id.edit);

        (findViewById(R.id.search)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callFlickr();

            }
        });
    }

    void callFlickr(){
        String url = FlickrQuery_url+FlickrQuery_per_page+FlickrQuery_nojsoncallback+page+FlickrQuery_format+FlickrQuery_tag+editText.getText().toString()+FlickrQuery_key;

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                ImageResponse imageResponse = new Gson().fromJson(response.toString(),ImageResponse.class);
                Log.e(TAG, imageResponse.getPhotos().getPhoto().get(0).getTitle());
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

}
