package com.nerdspoint.android.chandigarh;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Splash extends AppCompatActivity {


    //create objects here

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

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
