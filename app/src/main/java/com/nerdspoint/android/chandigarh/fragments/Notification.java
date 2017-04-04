package com.nerdspoint.android.chandigarh.fragments;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import com.nerdspoint.android.chandigarh.R;
import com.nerdspoint.android.chandigarh.adapters.NotificationAdapter;
import com.nerdspoint.android.chandigarh.offlineDB.DBHandler;
import com.nerdspoint.android.chandigarh.sharedPrefs.NotificatioDetail;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Notification extends Fragment {

    ListView notificationList;
    NotificationAdapter adapter;
    List<NotificatioDetail> list;
    NotificatioDetail detail;
    boolean flag=true;


    public Notification() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_notification, container, false);

        list = new ArrayList<>();



        notificationList = (ListView) view.findViewById(R.id.notificationlist);
        Cursor cursor = new DBHandler(getActivity()).getSenderNotifications();

        if(cursor.moveToFirst())
        {
            while(!cursor.isAfterLast())
            {
                detail = new NotificatioDetail();
                detail.Message= cursor.getString(cursor.getColumnIndex("message"));
                Cursor cursor1 = new  DBHandler(getActivity()).getShopByID(cursor.getString(cursor.getColumnIndex("ShopID")));
                if(cursor1.moveToFirst())
                {
                    detail.ShopName = cursor1.getString(cursor1.getColumnIndex("ShopName"));
                }
                detail.type= "sent";
                detail.notificationID= cursor.getString(cursor.getColumnIndex("messageId"));
                list.add(detail);
                cursor.moveToNext();
            }
        }

        Cursor cursor2 = new DBHandler(getActivity()).getReceiverNotifications();

        if(cursor2.moveToFirst())
        {
            while(!cursor2.isAfterLast())
            {
                detail = new NotificatioDetail();
                detail.Message= cursor2.getString(cursor2.getColumnIndex("message"));
                Cursor cursor1 = new  DBHandler(getActivity()).getShopByID(cursor2.getString(cursor2.getColumnIndex("ShopID")));
                if(cursor1.moveToFirst())
                {
                    detail.ShopName = cursor1.getString(cursor1.getColumnIndex("ShopName"));
                }
                detail.type= "received";
                detail.notificationID= cursor2.getString(cursor2.getColumnIndex("messageId"));
                list.add(detail);
                cursor2.moveToNext();
            }
        }

        adapter = new NotificationAdapter(getActivity(),list);
        notificationList.setAdapter(adapter);


        return view;
    }



}
