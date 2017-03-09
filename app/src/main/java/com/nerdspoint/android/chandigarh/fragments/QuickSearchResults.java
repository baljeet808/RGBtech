package com.nerdspoint.android.chandigarh.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nerdspoint.android.chandigarh.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class QuickSearchResults extends Fragment {


    public QuickSearchResults() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_quick_search_results, container, false);
    }

}
