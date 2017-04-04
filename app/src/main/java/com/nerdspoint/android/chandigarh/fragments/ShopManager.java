package com.nerdspoint.android.chandigarh.fragments;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nerdspoint.android.chandigarh.R;
import com.nerdspoint.android.chandigarh.activities.MainPage;
import com.nerdspoint.android.chandigarh.sharedPrefs.ActiveUserDetail;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShopManager extends Fragment {

    FloatingActionButton fab;
    TextView Shopname,phone,category;
    CardView ShopInfo_card;

    public ShopManager() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view= inflater.inflate(R.layout.fragment_shop_manager, container, false);

       Shopname=(TextView)view.findViewById(R.id.Shopname);
        phone=(TextView)view.findViewById(R.id.PhoneNumber);
        category=(TextView)view.findViewById(R.id.category);
     ShopInfo_card=(CardView)view.findViewById(R.id.ShopInfo_card);


        fab=(FloatingActionButton)view.findViewById(R.id.floatingActionButton);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ((MainPage)getActivity()).AddShop();
                //((MainPage)getActivity()).AddShop(ActiveUserDetail.getCustomInstance(getActivity()).getUID();



            }
        });
        ShopInfo_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainPage)getActivity()).ShopInfo();
            }
        });



        return view;
    }

}
