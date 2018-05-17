package com.samapps.appstreet;

import java.util.List;

public class Search {

    private String tag;
    private List<path> pathList;

    public Search() {
    }

    public Search(String tag, List<path> pathList) {
        this.tag = tag;
        this.pathList = pathList;
    }

    public String getTag() {
        return tag;
    }

    public List<path> getPathList() {
        return pathList;
    }
}
