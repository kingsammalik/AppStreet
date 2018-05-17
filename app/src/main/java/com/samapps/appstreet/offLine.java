package com.samapps.appstreet;

import java.util.ArrayList;
import java.util.List;

public class offLine {

    private List<Search> searchList;

    public offLine() {
    }

    public offLine(List<Search> searchList) {
        this.searchList = searchList;
    }

    public List<Search> getSearchList() {
        return searchList;
    }
}
