package com.samapps.appstreet;


import java.util.ArrayList;
import java.util.List;

public class UrlModel {
    private List<Search> searchList = new ArrayList<>();

    public UrlModel() {
    }

    public UrlModel(List<Search> searchList) {
        this.searchList = searchList;
    }

    public List<Search> getSearchList() {
        return searchList;
    }
}
