package com.example.kobac.myapplication;

import android.widget.Filter;

import java.util.ArrayList;

/**
 * Created by kobac on 7.9.17..
 */

public class CustomFilter extends Filter {

    IndexAdapter adapter;
    ArrayList<IndexModel> filterList;


    public CustomFilter(ArrayList<IndexModel> filterList, IndexAdapter adapter) {
        this.adapter = adapter;
        this.filterList = filterList;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results = new FilterResults();
        if (constraint != null && constraint.length() > 0) {
            constraint = constraint.toString().toUpperCase();
            ArrayList<IndexModel> filteredTitles = new ArrayList<>();
            for (int i = 0; i < filterList.size(); i++) {
                if (filterList.get(i).getIndexTitle().toUpperCase().contains(constraint)) {
                    filteredTitles.add(filterList.get(i));
                }
            }
            results.count = filteredTitles.size();
            results.values = filteredTitles;
        } else {
            results.count = filterList.size();
            results.values = filterList;
        }
        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        adapter.indexList = (ArrayList<IndexModel>) results.values;
        adapter.notifyDataSetChanged();
    }
}
