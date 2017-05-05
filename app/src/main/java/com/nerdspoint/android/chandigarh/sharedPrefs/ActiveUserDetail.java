package com.nerdspoint.android.chandigarh.sharedPrefs;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

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
        return sharedPreferences.getString("UID","0");
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
    public boolean getIsActive()
    {
        return sharedPreferences.getBoolean("IsActive",false);
    }
    public String getLoginType()
    {
        return sharedPreferences.getString("LoginType","Simple");
    }
    public boolean getIsFirstSync(){ return sharedPreferences.getBoolean("FirstSync",true);}
    public int getLastProductID(){ return  sharedPreferences.getInt("ProductID",0);}
    public int getTotalRecords(){return sharedPreferences.getInt("Records",0);}

    public void setIsFirebaseSet(boolean set)
    {
        editor.putBoolean("fStatus",set);
        editor.apply();
    }
    public boolean isFirebaseSet()
    {
        return  sharedPreferences.getBoolean("fStatus",false);
    }

    public void setFirbaseRegId(String Id)
    {
        editor.putString("RegId",Id);
        editor.apply();
    }
    public String getFirebaseRegId()
    {
        return  sharedPreferences.getString("RegId","0");
    }


    public void setTotalRecords(int records)
    {
        editor.putInt("Records",records);
        editor.apply();
    }

    public void setLastProductID(int ID)
    {
        editor.putInt("ProductID",ID);
        editor.apply();
        Log.d("  last product id ","  \t\t\t\t\t\t\t\t\t"+ID);
    }

    public int getLastCategoryID(){ return  sharedPreferences.getInt("CategoryID",0);}

    public void setLastCateegoryID(int ID)
    {
        editor.putInt("CategoryID",ID);
        editor.apply();
        Log.d("  last Category id ","  \t\t\t\t\t\t\t\t\t"+ID);
    }

    public int getLastShopID(){ return  sharedPreferences.getInt("ShopID",0);}

    public void setLastShopID(int ID)
    {
        editor.putInt("ShopID",ID);
        editor.apply();
        Log.d("  last shop id ","  \t\t\t\t\t\t\t\t\t"+ID);

    }

    public int getLastCustomPID(){ return  sharedPreferences.getInt("CustomPID",0);}

    public void setLastCustomPID(int ID)
    {
        editor.putInt("CustomPID",ID);
        editor.apply();
        Log.d("  last custom P id ","  \t\t\t\t\t\t\t\t\t"+ID);

    }

    public void setIsFirstSync(boolean value)
    {
        editor.putBoolean("FirstSync",value);
        editor.apply();
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
    public void setIsActive(Boolean isActive)
    {
        editor.putBoolean("IsActive",isActive);editor.apply();
    }
    public void setLoginType(String loginType)
    {
        editor.putString("LoginType",loginType);editor.apply();
    }

    public Bundle getUserFullDetailAsBundle()
    {
        Bundle bundle= new Bundle();
        bundle.putString("UID",sharedPreferences.getString("UID","1"));
        bundle.putString("UserName",sharedPreferences.getString("UserName","nerdspoint"));
        bundle.putString("LastName",sharedPreferences.getString("LastName","nerdspoint"));
        bundle.putString("FirstName",sharedPreferences.getString("FirstName","nerdspoint"));
        bundle.putString("Email",sharedPreferences.getString("Email","nerdspoint"));
        bundle.putString("PhoneNumber",sharedPreferences.getString("PhoneNumber","nerdspoint"));
        bundle.putString("Password",sharedPreferences.getString("Password","nerdspoint"));
        bundle.putString("UserType",sharedPreferences.getString("UserType","nerdspoint"));
        bundle.putString("IsActive",sharedPreferences.getString("IsActive","logOut"));
        bundle.putString("LoginType",sharedPreferences.getString("LoginType",""));
        return bundle;
    }

    public void logoutUser()
    {
        editor.clear();
        editor.apply();
    }

    public void setUserImageName(String imageName)
    {
        editor.putString("ImageName",imageName);
        editor.apply();
    }

    public String getUserImageName() {
        return sharedPreferences.getString("ImageName","null");
    }
}
