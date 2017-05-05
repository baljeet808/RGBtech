package com.nerdspoint.android.chandigarh.service;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.nerdspoint.android.chandigarh.offlineDB.DBHandler;
import com.nerdspoint.android.chandigarh.offlineDB.ipAddress;
import com.nerdspoint.android.chandigarh.sharedPrefs.ActiveUserDetail;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.wasabeef.blurry.Blurry;

/**
 * Created by android on 4/3/2017.
 */

public class notify {


    Context  context;
    private String url= "/index.php";
    int lastMessageId=0;


    public notify(Context context)
    {
        this.context= context;
        url= ipAddress.getCustomInstance(context).getIp()+url;
    }

    public void sendNotification(final View view, final String type , final String Shopid, final String message, final String ShopName, final String title, final String fid, final List<String> cpIds)
    {


        final AlertDialog dialog;
        AlertDialog.Builder alert;
        alert = new AlertDialog.Builder(context);
        if(Shopid!=null) {
            lastMessageId = new DBHandler(context).getTheLastSenderMessageId() + 1;
            alert.setTitle("Sendiing Message to "+ShopName);
        }
        else {
            if(type.equals("reject")) {
                alert.setTitle("Rejecting Customer demand\n Tap anywhere to cancel");
            }
            else
            {
                alert.setTitle("Accepting Customer Demand\n Tap anywhere to cancel");
            }
        }


        final ProgressBar progressBar = new ProgressBar(context);
        alert.setView(progressBar);

        alert.setCancelable(true);
        alert.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                dialog.dismiss();
                Blurry.delete((ViewGroup) view);
                Toast.makeText(context, "message sending canceled", Toast.LENGTH_SHORT).show();
            }
        });
        Blurry.with(context).radius(25).sampling(2).onto((ViewGroup) view );
        dialog = alert.create();
        dialog.show();


        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(context, "message Sent " + response, Toast.LENGTH_LONG).show();
                dialog.dismiss();
                Blurry.delete((ViewGroup) view);
                if (Shopid != null) {
                    try {
                        Gson gson = new Gson();

                        String inputString = gson.toJson(cpIds);


                        ContentValues row = new ContentValues();
                        row.put("title", title);
                        row.put("Name", ShopName);
                        row.put("message", message);
                        row.put("status", "waiting");
                        row.put("messageId", lastMessageId);
                        row.put("UID", Shopid);
                        row.put("CPIDS", inputString);
                        row.put("receiverFID", fid);
                        new DBHandler(context).add("Sender", row);
                    } catch (Exception e) {
                        Toast.makeText(context, "error in saving the message - " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "message not sent "+error.getMessage(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                Blurry.delete((ViewGroup) view);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                if(Shopid!=null) {
                    map.put("title", title);
                    map.put("type", type);
                    map.put("messageId", "" + lastMessageId);
                    map.put("message", message);
                    map.put("regId", fid);
                    map.put("uFid", ActiveUserDetail.getCustomInstance(context).getFirebaseRegId());
                    map.put("idsLength", "" + cpIds.size());
                    for (int i = 0; i < cpIds.size(); i++) {
                        map.put("cpid" + i, cpIds.get(i));
                    }
                    map.put("Name", ActiveUserDetail.getCustomInstance(context).getFirstName() + " " + ActiveUserDetail.getCustomInstance(context).getLastName());
                    map.put("UID", ActiveUserDetail.getCustomInstance(context).getUID());
                    map.put("type", "sending");
                }
                else
                {
                    map.put("type",type);
                    map.put("messageId",message);
                    map.put("name",ShopName);
                    map.put("regId",fid);
                }
                return map;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);

    }

}
