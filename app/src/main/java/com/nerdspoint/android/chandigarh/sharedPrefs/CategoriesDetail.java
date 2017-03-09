package com.nerdspoint.android.chandigarh.sharedPrefs;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by android on 08-03-2017.
 */

public class CategoriesDetail {


    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Context context;
    static CategoriesDetail classObject;
    int count =0;


    public CategoriesDetail(Context context)
    {
        this.context=context;
        sharedPreferences= context.getSharedPreferences("Categories",Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
        editor.apply();
    }

    public void clearCategories()
    {
        editor.clear();
        editor.apply();
    }


    public static synchronized CategoriesDetail getCustomInstance(Context context)
    {
        if(classObject==null) {
            classObject = new CategoriesDetail(context);
        }

       return classObject;
    }

    public void setCID (int i,String cid)
    {
        editor.putString("CID"+i,cid);
        editor.apply();
    }
    public void setCategory(int i,String category)
    {
        editor.putString("Category"+i,category);
        editor.apply();
    }
    public String getCategory(int i )
    {
        return sharedPreferences.getString("Category"+i,"NA");
    }
    public String getCID(int i )
    {
        return sharedPreferences.getString("CID"+i,"NA");
    }
    public void setCategoryCount(int i)
    {
        editor.putInt("Count",i);
        editor.apply();
    }
    public int getCategoryCount()
    {
        return sharedPreferences.getInt("Count",1);
    }
}
