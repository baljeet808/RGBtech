package com.nerdspoint.android.chandigarh;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

/**
 * Created by android on 3/17/2017.
 */

public class MyApplication extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);

    }

}

