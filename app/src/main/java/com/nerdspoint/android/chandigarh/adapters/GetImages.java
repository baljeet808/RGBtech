package com.nerdspoint.android.chandigarh.adapters;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.nerdspoint.android.chandigarh.activities.MainPage;
import com.nerdspoint.android.chandigarh.sharedPrefs.ActiveUserDetail;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Hashtable;
import java.util.Map;

import retrofit2.http.GET;

/**
 * Created by android on 4/25/2017.
 */

public class GetImages {

    Context context;
    static GetImages classObject;
    static String TableName,ID;
    ImageView view,view1,view2;
    String Download_URL ="https://baljeet808singh.000webhostapp.com/chandigarh/getImage.php";
    String url="https://baljeet808singh.000webhostapp.com/chandigarh/images/";

    public GetImages(Context context, String id, String TableName, ImageView view,ImageView view1, ImageView view2)
    {
        this.context=context;
        this.view = view;
        this.view1 = view1;
        this.view2 = view2;
        this.TableName= TableName;
        ID = id;
    }


    public void fetchImages()
    {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Download_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        try
                        {
                            Toast.makeText(context, ""+s, Toast.LENGTH_LONG).show();
                            JSONArray jsonArray = new JSONArray(s);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                           String temp = url;
                            temp = temp+""+jsonObject.getString("ImageName");
                            Toast.makeText(context, ""+temp, Toast.LENGTH_SHORT).show();
                            Glide.with(context).load(temp)
                                    .thumbnail(0.5f)
                                    .crossFade()
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .into(view);

                        }catch(Exception e)
                        {
                                Log.d("error in GetImages"," "+e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                        Toast.makeText(context, volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String
                Map<String,String> params = new Hashtable<String, String>();
                params.put("tableName",TableName);
                params.put("value",ID);

                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }




}
