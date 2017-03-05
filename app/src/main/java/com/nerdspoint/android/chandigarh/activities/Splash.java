package com.nerdspoint.android.chandigarh.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.nerdspoint.android.chandigarh.R;

public class Splash extends AppCompatActivity {


    //create objects here

    TextView textView;
    public Typeface typeface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        textView = (TextView) findViewById(R.id.textView6);


         typeface=  Typeface.createFromAsset(getAssets(),"waltograph42.ttf");
        textView.setTypeface(typeface);


        // initialize objects here  or attach there xml ids

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                    Intent i = new Intent(Splash.this,LoginActivity.class);
                    startActivity(i);
                    finish();
            }
        },3000);

    }
}
