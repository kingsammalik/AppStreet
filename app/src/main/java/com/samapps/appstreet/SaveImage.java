package com.samapps.appstreet;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.util.Log;

import com.google.gson.Gson;
import com.samapps.appstreet.newdev.Base;
import com.squareup.picasso.Callback;

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

import static android.content.Context.MODE_PRIVATE;

public class SaveImage implements Callback {

    private Context context;
    private String keyword;
    private String url;
    private String id;
    private boolean isOnline;
    private String TAG=MainActivity.class.getName();
    private static final String MY_PREFS_NAME = "APPSTREET";

    public SaveImage(Context context, String keyword, String url, String id, boolean isOnline) {
        this.context = context;
        this.keyword = keyword;
        this.url = url;
        this.id = id;
        this.isOnline = isOnline;
    }

    @Override
    public void onSuccess() {

        /*new Thread(new Runnable() {
            @Override
            public void run() {

                URL wallpaperURL = null;
                try {
                    wallpaperURL = new URL(url);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                try {
                    URLConnection connection = wallpaperURL.openConnection();
                    InputStream inputStream = new BufferedInputStream(wallpaperURL.openStream(), 10240);
                    File cacheDir = getCacheFolder(context);
                    File cacheFile = new File(cacheDir, keyword+"_"+id+".jpg");
                    //pathList.add(new path(cacheFile.getAbsolutePath()));
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
        }).start();*/




    }

    @Override
    public void onError(Exception e) {

    }

    private File getCacheFolder(Context context) {
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

}
