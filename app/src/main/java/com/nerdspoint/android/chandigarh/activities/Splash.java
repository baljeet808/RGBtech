package com.nerdspoint.android.chandigarh.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.nerdspoint.android.chandigarh.R;
import com.nerdspoint.android.chandigarh.fragments.profileUpdation;
import com.nerdspoint.android.chandigarh.sharedPrefs.ActiveUserDetail;
import com.nerdspoint.android.chandigarh.sharedPrefs.CategoriesDetail;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Splash extends AppCompatActivity {


    //create objects here

    TextView textView;
    public Typeface typeface;
    private static String fetchCategories_url="https://baljeet808singh.000webhostapp.com/chandigarh/category_fetch.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        textView = (TextView) findViewById(R.id.textView6);


         typeface=  Typeface.createFromAsset(getAssets(),"waltograph42.ttf");
        textView.setTypeface(typeface);

        fetchCategories();
    }

    public void fetchCategories()
    {

        StringRequest request = new StringRequest(Request.Method.POST, fetchCategories_url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Response", response+"Done");
               try
                {
                    CategoriesDetail.getCustomInstance(getApplicationContext()).clearCategories();
                    JSONArray jsonArray = new JSONArray(response);
                    for(int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        CategoriesDetail.getCustomInstance(getApplicationContext()).setCID(i,jsonObject.getString("CategoryID"));
                        CategoriesDetail.getCustomInstance(getApplicationContext()).setCategory(i,jsonObject.getString("CategoryName"));
                    }
                    CategoriesDetail.getCustomInstance(getApplicationContext()).setCategoryCount(jsonArray.length());
                    Intent i = new Intent(Splash.this,LoginActivity.class);
                    startActivity(i);
                    finish();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ERROR","error => "+error.toString());

                  }
        }
        )
        {
            @Override
            protected Map getParams() throws AuthFailureError {
                Map map = new HashMap<>() ;

                return map;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);

    }
}
