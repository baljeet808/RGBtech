package com.nerdspoint.android.chandigarh.permissionCheck;

import android.content.Context;

/**
 * Created by android on 3/22/2017.
 */

public class ServerOrLocalCheck {


Context context;

    boolean OnServer= false;            //     make it true if you want to run this project with online functionality

    static ServerOrLocalCheck check;

    public ServerOrLocalCheck(Context context)
    {
        this.context=context;
    }
    public static synchronized ServerOrLocalCheck getCustomInstance(Context context){
        if(check == null)
        {
            check= new ServerOrLocalCheck(context);
        }



        return check;
    }

    public boolean isOnServer()
    {
        return  OnServer;
    }




}
