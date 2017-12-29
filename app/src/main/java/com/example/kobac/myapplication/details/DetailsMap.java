package com.example.kobac.myapplication.details;

import java.util.ArrayList;

/**
 * Created by kobac on 22.8.17..
 */

public class DetailsMap {

    final private ArrayList<String> mMap;

    public DetailsMap(final ArrayList<String> map) {
        this.mMap = map;
    }

    public String getURLForPosition(final int position) {
        return mMap.get(position);
    }

    public int getCount() {
        return mMap.size();
    }
}
