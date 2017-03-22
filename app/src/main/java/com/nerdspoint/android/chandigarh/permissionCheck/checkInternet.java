package com.nerdspoint.android.chandigarh.permissionCheck;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.sip.SipSession;
import android.os.Bundle;

import com.nerdspoint.android.chandigarh.sharedPrefs.ActiveUserDetail;

/**
 * Created by android on 3/16/2017.
 */

public class checkInternet {

    Context context;
    static checkInternet classObject;
    NetworkInfo.State state;

    public checkInternet(Context context)
    {
        this.context=context;
    }

    public static synchronized checkInternet getCustomInstance(Context context)
    {
        if(classObject==null) {
            classObject = new checkInternet(context);
        }
        return classObject;
    }


    public void setState(NetworkInfo.State state)
    {
        this.state=state;
    }
    public NetworkInfo.State getState()
    {
        return state;
    }

}
