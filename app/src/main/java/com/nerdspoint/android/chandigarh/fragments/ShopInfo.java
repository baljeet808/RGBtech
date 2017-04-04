package com.nerdspoint.android.chandigarh.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.nerdspoint.android.chandigarh.R;
import com.nerdspoint.android.chandigarh.activities.MainPage;
import com.nerdspoint.android.chandigarh.adapters.AndroidImageAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShopInfo extends Fragment {

  Button Back;
    ViewPager mViewPager ;
    String shopid;

    public ShopInfo() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_shop_info, container, false);
        Back=(Button)view.findViewById(R.id.back_button);


          Bundle bundle=getArguments();
          shopid=bundle.get("shopid").toString();

        Back.setText(shopid);

          mViewPager = (ViewPager) view.findViewById(R.id.viewPageAndroid);
        AndroidImageAdapter adapterView = new AndroidImageAdapter(getActivity());
        mViewPager.setAdapter(adapterView);

        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainPage)getActivity()).AddShop();
            }
        });


        return view;
    }

}
