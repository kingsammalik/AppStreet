package com.samapps.appstreet.newdev;

import com.samapps.appstreet.path;

import java.util.List;

public class Base {
    private static UrlModel urlModel;

    private static List<path> pathList;

    public static UrlModel getUrlModel() {
        return urlModel;
    }

    public static void setUrlModel(UrlModel urlModel) {
        Base.urlModel = urlModel;
    }

    public static List<path> getPathList() {
        return pathList;
    }

    public static void setPathList(List<path> pathList) {
        Base.pathList = pathList;
    }
}
