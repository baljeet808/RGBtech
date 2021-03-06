package com.nerdspoint.android.chandigarh.fragments;


import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.gson.Gson;
import com.nerdspoint.android.chandigarh.R;
import com.nerdspoint.android.chandigarh.adapters.NotificationAdapter;
import com.nerdspoint.android.chandigarh.app.Config;
import com.nerdspoint.android.chandigarh.offlineDB.DBHandler;
import com.nerdspoint.android.chandigarh.sharedPrefs.ActiveUserDetail;
import com.nerdspoint.android.chandigarh.sharedPrefs.NotificatioDetail;
import com.nerdspoint.android.chandigarh.util.NotificationUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Notification extends Fragment {

    ListView notificationList, SentOrders;
    NotificationAdapter adapter,adapter1;
    List<NotificatioDetail> list,list2;
    NotificatioDetail detail;
    SwipeRefreshLayout swipeRefreshLayout, refreshLayout;
    boolean flag=true;

    private static final String TAG = Notification.class.getSimpleName();
    private BroadcastReceiver mRegistrationBroadcastReceiver;


    public Notification() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_notification, container, false);

        list = new ArrayList<>();
        list2 = new ArrayList<>();

        swipeRefreshLayout= (SwipeRefreshLayout) view.findViewById(R.id.activity_main_swipe_refresh_layout);
        refreshLayout= (SwipeRefreshLayout) view.findViewById(R.id.activity_main_swipe_refresh_layout1);



        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);

                    displayFirebaseRegId();

                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    Toast.makeText(getActivity(), "something happening ", Toast.LENGTH_SHORT).show();

                    //  FirebaseMessagingService

                    if (intent.getStringExtra("status").equals("accepted") || intent.getStringExtra("status").equals("rejected")) {
                        new DBHandler(getActivity()).updateNotificationStatus(intent.getStringExtra("status"), intent.getStringExtra("messageId"));

                    } else {
                        int id=0;
                        String message = intent.getStringExtra("message");
                        String title = intent.getStringExtra("title");
                        String UID = intent.getStringExtra("UID");
                        String Name = intent.getStringExtra("Name");
                        String uFid = intent.getStringExtra("uFid");
                        String status = intent.getStringExtra("status");
                        String timestamp = intent.getStringExtra("timeStamp");
                        String length = intent.getStringExtra("length");
                        String messageId = intent.getStringExtra("messageId");
                        List ids = new ArrayList();
                        int size = Integer.parseInt(length);
                        for (int i = 0; i < size; i++) {
                            ids.add(intent.getStringExtra("cpid" + i));
                        }
                        Gson gson = new Gson();

                        String inputString = gson.toJson(ids);

                        System.out.println("inputString= " + inputString);


                        try {
                            ContentValues row = new ContentValues();
                           id = new DBHandler(context).getTheLastReceiverMessageId()+1;
                            row.put("title", title);
                            row.put("Name", Name);
                            row.put("messageId",id);
                            row.put("message", message);
                            row.put("UID", UID);
                            row.put("status", status);
                            row.put("myDate", timestamp);
                            row.put("CPIDS", inputString);
                            row.put("senderFID", uFid);
                            row.put("foundId", messageId);
                            new DBHandler(getContext()).add("Receiver", row);
                        } catch (Exception e) {
                            Toast.makeText(context, "error in saving the message - " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }


                        detail = new NotificatioDetail();
                        detail.Message = message;
                        detail.foundMessageId = messageId;
                        detail.timestamp = timestamp;
                        detail.senderFID = uFid;
                        detail.notificationID=""+id;
                        detail.status = status;
                        detail.type = "recieved";
                        detail.visitorName = Name;
                        detail.title = title;
                        detail.UID = UID;
                        detail.cpids = inputString;
                        list.add(detail);
                        adapter.notifyDataSetChanged();
                        //  txtRegId.setText( "Push notification: " + message +" title "+title+"  payload ");

                        // Toast.makeText(getActivity(), "Push notification: " + message +" title   payload ",Toast.LENGTH_LONG).show();
                        //  Snackbar.make(getView(), "Push notification: " + message +" title "+title+"  payload "+arr.toString(),Snackbar.LENGTH_LONG).setAction("Action",null).show();

                    }
                }
            }
        };


        displayFirebaseRegId();

                    notificationList = (ListView) view.findViewById(R.id.notificationlist);
                    SentOrders = (ListView) view.findViewById(R.id.sent_list);
                    Cursor cursor = new DBHandler(getActivity()).getSenderNotifications();
        if(cursor.moveToFirst())
        {
            while(!cursor.isAfterLast())
            {
                detail = new NotificatioDetail();
                detail.Message= cursor.getString(cursor.getColumnIndex("message"));
                detail.receiverFID = cursor.getString(cursor.getColumnIndex("receiverFID"));
                detail.visitorName=cursor.getString(cursor.getColumnIndex("Name"));
                detail.type= "sent";
                detail.status=cursor.getString(cursor.getColumnIndex("status"));
                detail.timestamp= cursor.getString(cursor.getColumnIndex("myDate"));
                detail.title=cursor.getString(cursor.getColumnIndex("title"));
                detail.UID=cursor.getString(cursor.getColumnIndex("UID"));
                detail.notificationID= cursor.getString(cursor.getColumnIndex("messageId"));
                detail.cpids = cursor.getString(cursor.getColumnIndex("CPIDS"));
                list2.add(detail);
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
                detail.senderFID= cursor2.getString(cursor2.getColumnIndex("senderFID"));
                detail.visitorName=cursor2.getString(cursor2.getColumnIndex("Name"));
                detail.type= "received";
                detail.status = cursor2.getString(cursor2.getColumnIndex("status"));
                detail.foundMessageId = cursor2.getString(cursor2.getColumnIndex("foundId"));
                detail.timestamp= cursor2.getString(cursor2.getColumnIndex("myDate"));
                detail.title=cursor2.getString(cursor2.getColumnIndex("title"));
                detail.UID=cursor2.getString(cursor2.getColumnIndex("UID"));
                detail.notificationID= cursor2.getString(cursor2.getColumnIndex("messageId"));
                detail.cpids = cursor2.getString(cursor2.getColumnIndex("CPIDS"));
                list.add(detail);
                cursor2.moveToNext();
            }
        }

        adapter = new NotificationAdapter(getActivity(),list);
        adapter1 = new NotificationAdapter(getActivity(),list2);
        notificationList.setAdapter(adapter);
        SentOrders.setAdapter(adapter1);



        swipeRefreshLayout.setColorSchemeResources(R.color.color_2, R.color.color_16, R.color.color_12);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //handling swipe refresh
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        list2.clear();
                        Cursor cursor = new DBHandler(getActivity()).getSenderNotifications();
                        Toast.makeText(getActivity(), "sent list refreshed values fetched "+cursor.getCount(), Toast.LENGTH_LONG).show();

                        if(cursor.moveToFirst())
                        {
                            while(!cursor.isAfterLast())
                            {
                                detail = new NotificatioDetail();
                                detail.Message= cursor.getString(cursor.getColumnIndex("message"));
                                detail.receiverFID = cursor.getString(cursor.getColumnIndex("receiverFID"));
                                detail.visitorName=cursor.getString(cursor.getColumnIndex("Name"));
                                detail.type= "sent";
                                detail.status=cursor.getString(cursor.getColumnIndex("status"));
                                detail.timestamp= cursor.getString(cursor.getColumnIndex("myDate"));
                                detail.title=cursor.getString(cursor.getColumnIndex("title"));
                                detail.UID=cursor.getString(cursor.getColumnIndex("UID"));
                                detail.notificationID= cursor.getString(cursor.getColumnIndex("messageId"));
                                detail.cpids = cursor.getString(cursor.getColumnIndex("CPIDS"));
                                list2.add(detail);
                                cursor.moveToNext();
                            }
                        }
                        adapter1.notifyDataSetChanged();
                    }
                }, 2000);
            }
        });




        refreshLayout.setColorSchemeResources(R.color.color_2, R.color.color_16, R.color.color_12);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //handling swipe refresh
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                        list.clear();
                        Cursor cursor2 = new DBHandler(getActivity()).getReceiverNotifications();
                        Toast.makeText(getActivity(), "sent list refreshed values fetched "+cursor2.getCount(), Toast.LENGTH_LONG).show();
                        if(cursor2.moveToFirst())
                        {
                            while(!cursor2.isAfterLast())
                            {
                                detail = new NotificatioDetail();
                                detail.Message= cursor2.getString(cursor2.getColumnIndex("message"));
                                detail.senderFID= cursor2.getString(cursor2.getColumnIndex("senderFID"));
                                detail.visitorName=cursor2.getString(cursor2.getColumnIndex("Name"));
                                detail.type= "received";
                                detail.status = cursor2.getString(cursor2.getColumnIndex("status"));
                                detail.foundMessageId = cursor2.getString(cursor2.getColumnIndex("foundId"));
                                detail.timestamp= cursor2.getString(cursor2.getColumnIndex("myDate"));
                                detail.title=cursor2.getString(cursor2.getColumnIndex("title"));
                                detail.UID=cursor2.getString(cursor2.getColumnIndex("UID"));
                                detail.notificationID= cursor2.getString(cursor2.getColumnIndex("messageId"));
                                detail.cpids = cursor2.getString(cursor2.getColumnIndex("CPIDS"));
                                list.add(detail);
                                cursor2.moveToNext();
                            }
                        }
                        adapter.notifyDataSetChanged();

                    }
                }, 2000);
            }
        });


        return view;
    }


    private void displayFirebaseRegId() {
        SharedPreferences pref = getActivity().getSharedPreferences(Config.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);
        //txtRegId.setText(regId);
        ActiveUserDetail.getCustomInstance(getActivity()).setIsFirebaseSet(true);
        ActiveUserDetail.getCustomInstance(getActivity()).setFirbaseRegId(regId);
        new DBHandler(getActivity()).updateFirebaseId(regId);
        Log.e(TAG, "Firebase reg id: " + regId);

    }


    @Override
    public void onResume() {
        super.onResume();


        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getActivity());
    }

    @Override
    public void onPause() {

        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mRegistrationBroadcastReceiver);

        super.onPause();



    }
}
