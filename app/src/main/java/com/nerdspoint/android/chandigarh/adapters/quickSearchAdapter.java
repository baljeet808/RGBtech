package com.nerdspoint.android.chandigarh.adapters;

import android.content.Context;
import android.database.Cursor;
import android.widget.ArrayAdapter;

import com.nerdspoint.android.chandigarh.offlineDB.DBHandler;

import java.util.ArrayList;

/**
 * Created by android on 3/22/2017.
 */

public class quickSearchAdapter {

    Context context;
    static quickSearchAdapter adapter;
    ArrayList<String> items;
    ArrayAdapter arrayAdapter;
    DBHandler handler;


    public quickSearchAdapter(Context context)
    {
        this.context=context;


    }

    public static synchronized quickSearchAdapter getCustomInstance (Context context)
    {
        if(adapter==null)
        {
            adapter= new quickSearchAdapter(context);
        }
        return adapter;
    }

    public ArrayAdapter getArrayAdapter (String tableNAme)
    {
        handler = new DBHandler(context);
        items= new ArrayList<String>();

      if(tableNAme.equals("Product")) {
            addProducts();
        }
        else
        {
            addShops();
        }

        arrayAdapter = new ArrayAdapter(context,android.R.layout.simple_list_item_1,items);

        return  arrayAdapter;
    }

    public ArrayList<String> getArrayList()
    {
        return items;
    }


    public void addProducts()
    {
        Cursor cursor= handler.getAll("Product");


        if (cursor.moveToFirst()){
            while(!cursor.isAfterLast()){

                String product=""+cursor.getString(cursor.getColumnIndex("ProductName"));
                items.add(product);
                cursor.moveToNext();
            }
        }
        cursor.close();
    }


    public void addCategories()
    {
        Cursor cursor= handler.getAll("Category");


        if (cursor.moveToFirst()){
            while(!cursor.isAfterLast()){

                String Category=""+cursor.getString(cursor.getColumnIndex("CategoryName"));
                items.add(Category);
                cursor.moveToNext();
            }
        }
        cursor.close();
    }

    public void addShops()
    {
        Cursor cursor= handler.getAll("ShopMasterTable");


        if (cursor.moveToFirst()){
            while(!cursor.isAfterLast()){

                String Shop=""+cursor.getString(cursor.getColumnIndex("ShopName"));
                items.add(Shop);
                cursor.moveToNext();
            }
        }
        cursor.close();
    }

}
