package com.nerdspoint.android.chandigarh.offlineDB;

import android.content.Context;

import com.nerdspoint.android.chandigarh.permissionCheck.ServerOrLocalCheck;

/**
 * Created by android on 3/20/2017.
 */

public class ipAddress {


    private final Context context;




    String localip ="http://192.168.83.1//chandigarhDirectory";
    String remoteIp = "https://baljeet808singh.000webhostapp.com/chandigarh";


    static ipAddress classObject;

    public ipAddress(Context context)
    {
        this.context=context;
    }

    public String getIp() {

        if(ServerOrLocalCheck.getCustomInstance(context).isOnServer())
        {
            return remoteIp;
        }else
        {
            return localip;
        }



    }


    public static synchronized ipAddress getCustomInstance(Context context)
    {
        if(classObject==null) {
            classObject = new ipAddress(context);
        }
        return classObject;
    }



}
