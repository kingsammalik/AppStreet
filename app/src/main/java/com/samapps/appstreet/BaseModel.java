package com.samapps.appstreet;

import java.util.List;

public class BaseModel {

    private static List<Photo> photos;

    private static Search search;

    private static boolean isOnline;

    public static List<Photo> getPhotos() {
        return photos;
    }

    public static void setPhotos(List<Photo> photos) {
        BaseModel.photos = photos;
    }

    public static Search getSearch() {
        return search;
    }

    public static void setSearch(Search search) {
        BaseModel.search = search;
    }

    public static boolean isIsOnline() {
        return isOnline;
    }

    public static void setIsOnline(boolean isOnline) {
        BaseModel.isOnline = isOnline;
    }
}
