package com.nerdspoint.android.chandigarh.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nerdspoint.android.chandigarh.R;
import com.nerdspoint.android.chandigarh.sharedPrefs.ShopDetails;
import com.nerdspoint.android.chandigarh.adapters.ShopAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class QuickSearchResults extends Fragment {

    RecyclerView recyclerView;
    ShopAdapter shopAdapter;
    List<ShopDetails> list;
    public QuickSearchResults() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_quick_search_results, container, false);
        recyclerView = (RecyclerView)view.findViewById(R.id.rv_shopDetails);
        list = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            ShopDetails details = new ShopDetails();
            details.contactNumber = "2345678"+i;
            details.address = "ADRESS NO. "+i;
            details.price = "Rs. "+i;
            details.shopName = "The chandigarh Shop No. "+(i+1);
            details.productName = "Royal Stag No. "+i;
            list.add(details);
        }
        shopAdapter = new ShopAdapter(getActivity(),list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(shopAdapter);
        shopAdapter.notifyDataSetChanged();
        return view;
    }

}
