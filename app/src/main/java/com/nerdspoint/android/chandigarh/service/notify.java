package com.nerdspoint.android.chandigarh.service;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.nerdspoint.android.chandigarh.offlineDB.ipAddress;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by android on 4/3/2017.
 */

public class notify {


    Context  context;
    private String url= "/index.php";

    public notify(Context context)
    {
        this.context= context;
        url= ipAddress.getCustomInstance(context).getIp()+url;
    }

    public void sendNotification(final String message, final String title, final String fid)
    {
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(context, "message Sent", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "message not sent "+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                map.put("title",title);
                map.put("message",message);
                map.put("regId",fid);

                return map;
            }
        };
    }

}
