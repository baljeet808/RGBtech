package com.nerdspoint.android.chandigarh.sharedPrefs;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

/**
 * Created by android on 06-03-2017.
 */

public class ActiveUserDetail {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Context context;
    static ActiveUserDetail classObject;


    public ActiveUserDetail(Context context)
    {
        this.context=context;
        sharedPreferences= context.getSharedPreferences("userDetail",Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
        editor.apply();
    }

    public static synchronized ActiveUserDetail getCustomInstance(Context context)
    {
        if(classObject==null) {
            classObject = new ActiveUserDetail(context);
        }
        return classObject;
    }

    public String getUID()
    {
        return sharedPreferences.getString("UID","1");
    }
    public String getUserName()
    {
        return sharedPreferences.getString("UserName","nerdspoint");
    }
    public String getFirstName()
    {
        return sharedPreferences.getString("FirstName","nerdspoint");
    }
    public String getLastName()
    {
        return sharedPreferences.getString("LastName","nerdspoint");
    }
    public String getEmailAddress()
    {
        return sharedPreferences.getString("Email","nerdspoint");
    }
    public String getPhoneNumber() { return sharedPreferences.getString("PhoneNumber","nerdspoint");}
    public String getPassword()
    {
        return sharedPreferences.getString("Password","nerdspoint");
    }
    public String getUserType()
    {
        return sharedPreferences.getString("UserType","nerdspoint");
    }
    public String getIsActive()
    {
        return sharedPreferences.getString("IsActive","logout");
    }
    public String getLoginType()
    {
        return sharedPreferences.getString("LoginType","Simple");
    }


    public void setPhoneNumber(String phoneNumber)
    {
        editor.putString("PhoneNumber",phoneNumber);editor.apply();
    }
    public void setUserName(String userName)
    {
        editor.putString("UserName",userName);editor.apply();
    }
    public void setUID(String uid)
    {
        editor.putString("UID",uid);editor.apply();
    }
    public void setEmailAddress(String email)
    {
        editor.putString("Email",email);editor.apply();
    }
    public void setFirstName(String firstName)
    {
        editor.putString("FirstName",firstName);editor.apply();
    }
    public void setLastName(String lastName)
    {
        editor.putString("LastName",lastName);editor.apply();
    }
    public void setPassword(String password)
    {
        editor.putString("Password",password);editor.apply();
    }
    public void setUserType(String userType)
    {
        editor.putString("UserType",userType);editor.apply();
    }
    public void setIsActive(String isActive)
    {
        editor.putString("IsActive",isActive);editor.apply();
    }
    public void setLoginType(String loginType)
    {
        editor.putString("LoginType",loginType);editor.apply();
    }

    public Bundle getUserFullDetailAsBundle()
    {
        Bundle bundle= new Bundle();
        bundle.putString("UserName",sharedPreferences.getString("UserName","nerdspoint"));
        bundle.putString("LastName",sharedPreferences.getString("LastName","nerdspoint"));
        bundle.putString("FirstName",sharedPreferences.getString("FirstName","nerdspoint"));
        bundle.putString("Email",sharedPreferences.getString("Email","nerdspoint"));
        bundle.putString("PhoneNumber",sharedPreferences.getString("PhoneNumber","nerdspoint"));
        bundle.putString("Password",sharedPreferences.getString("Password","nerdspoint"));
        bundle.putString("UserName",sharedPreferences.getString("UserType","nerdspoint"));
        bundle.putString("IsActive",sharedPreferences.getString("IsActive","logOut"));
        bundle.putString("loginType",sharedPreferences.getString("LoginType","Simple"));
        return bundle;
    }

}
