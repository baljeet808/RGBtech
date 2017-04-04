package com.nerdspoint.android.chandigarh.fragments;


import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nerdspoint.android.chandigarh.R;
import com.nerdspoint.android.chandigarh.activities.MainPage;
import com.nerdspoint.android.chandigarh.adapters.ShopMangerAdapter;
import com.nerdspoint.android.chandigarh.offlineDB.DBHandler;
import com.nerdspoint.android.chandigarh.sharedPrefs.ActiveUserDetail;
import com.nerdspoint.android.chandigarh.sharedPrefs.ShopDetails;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShopManager extends Fragment {

    FloatingActionButton fab;
    RecyclerView recyclerView;
    ShopDetails shopDetails;
    List<ShopDetails> list;
    ShopMangerAdapter mangerAdapter;

    public ShopManager() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view= inflater.inflate(R.layout.fragment_shop_manager, container, false);
        try {
            list = new ArrayList<>();
          //  Toast.makeText(getActivity(), "enter in oncreate", Toast.LENGTH_SHORT).show();
            recyclerView = (RecyclerView) view.findViewById(R.id.listview);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());

            Cursor cursor = new DBHandler(getActivity()).getOwnedShops(ActiveUserDetail.getCustomInstance(getActivity()).getUID());
            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    shopDetails = new ShopDetails();
                    shopDetails.shopName = cursor.getString(cursor.getColumnIndex("ShopName"));
                    shopDetails.address = cursor.getString(cursor.getColumnIndex("ShopAddress")) + " " + cursor.getString(cursor.getColumnIndex("Sector")) + " " + cursor.getString(cursor.getColumnIndex("SCO"));
                    shopDetails.ShopID = cursor.getString(cursor.getColumnIndex("ShopID"));
                    shopDetails.contactNumber = cursor.getString(cursor.getColumnIndex("ShopContactNo"));
                    Cursor cursor1 = new DBHandler(getActivity()).getCategoryName(cursor.getString(cursor.getColumnIndex("CategoryID")));
                    if (cursor1.moveToFirst()) {
                        shopDetails.category = cursor1.getString(cursor1.getColumnIndex("CategoryName"));
                    }
                   // Toast.makeText(getActivity(), "shop added "+shopDetails.ShopID, Toast.LENGTH_SHORT).show();
                    list.add(shopDetails);
                    cursor.moveToNext();
                }
            }else {
              //  Toast.makeText(getActivity(), "cursor khali e", Toast.LENGTH_SHORT).show();

            }
            mangerAdapter = new ShopMangerAdapter(getActivity(), list);
            recyclerView.setAdapter(mangerAdapter);
            mangerAdapter.notifyDataSetChanged();
        }
        catch (Exception e)
        {
            Toast.makeText(getActivity(), "error in Shop manager > "+e.getMessage(), Toast.LENGTH_LONG).show();
        }

            fab=(FloatingActionButton)view.findViewById(R.id.floatingActionButton);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ((MainPage)getActivity()).AddShop();
                //((MainPage)getActivity()).AddShop(ActiveUserDetail.getCustomInstance(getActivity()).getUID();



            }
        });




        return view;
    }

}
