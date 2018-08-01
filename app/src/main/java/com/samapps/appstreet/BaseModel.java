package com.samapps.appstreet;

import java.util.List;

public class BaseModel {

    private static List<Photo> photos;



    public static List<Photo> getPhotos() {
        return photos;
    }

    public static void setPhotos(List<Photo> photos) {
        BaseModel.photos = photos;
    }


}
