package com.nerdspoint.android.chandigarh.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
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
import com.nerdspoint.android.chandigarh.offlineDB.DBHandler;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        textView = (TextView) findViewById(R.id.textView6);





        typeface = Typeface.createFromAsset(getAssets(), "waltograph42.ttf");
        textView.setTypeface(typeface);


            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(ActiveUserDetail.getCustomInstance(getApplicationContext()).getIsActive()) {
                       Intent j = new Intent(Splash.this,MainPage.class);
                        startActivity(j);
                        finish();
                    }
                    else
                    {
                        Intent j = new Intent(Splash.this, LoginActivity.class);
                        startActivity(j);
                        finish();
                    }
                }
            }, 3000);



    }


}
