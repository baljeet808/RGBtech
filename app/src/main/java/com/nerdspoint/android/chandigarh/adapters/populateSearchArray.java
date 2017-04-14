package com.nerdspoint.android.chandigarh.adapters;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.Toast;

import com.nerdspoint.android.chandigarh.R;
import com.nerdspoint.android.chandigarh.activities.MainPage;
import com.nerdspoint.android.chandigarh.fragments.QuickSearchResults;

import java.util.ArrayList;

/**
 * Created by android on 3/23/2017.
 */

public class populateSearchArray
{
    AutoCompleteTextView searchBar;



    Context context;
    ArrayList<String> items;

    static populateSearchArray searchArray;



    public  populateSearchArray(Context context, AutoCompleteTextView searchBar)
    {
        this.context = context;
        this.searchBar= (AutoCompleteTextView) searchBar.findViewById(R.id.et_quickSearch);
    }

    public static synchronized populateSearchArray getCustomInstance(Context context, AutoCompleteTextView searchBar)
    {
        if(searchArray==null){
            return searchArray = new populateSearchArray(context,searchBar);
        }


        return searchArray;
    }


    public void populate(String tableName)
    {
        searchBar.setAdapter(quickSearchAdapter.getCustomInstance(context).getArrayAdapter(tableName));
        searchBar.setThreshold(1);
        
    }


}
