package com.nerdspoint.android.chandigarh.fragments;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.nerdspoint.android.chandigarh.R;
import com.nerdspoint.android.chandigarh.adapters.listCustomAdapter;
import com.nerdspoint.android.chandigarh.offlineDB.DBHandler;
import com.nerdspoint.android.chandigarh.sharedPrefs.CategoryDetails;
import com.nerdspoint.android.chandigarh.sharedPrefs.ProductDetails;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class customProductList extends Fragment {

    List<ProductDetails> list;
    listCustomAdapter customAdapter;
    DBHandler handler;
    Cursor cursor;
    ListView listView;
    String queryValue;

    public customProductList() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_custom_product_list, container, false);
        list=new ArrayList<ProductDetails>();
        Bundle bundle= getArguments();
        queryValue= bundle.getString("product");
        handler = new DBHandler(getActivity());
        listView= (ListView) view.findViewById(R.id.custom_product_list);

        cursor=handler.getProduct(queryValue);
        list.clear();
        if(cursor.moveToFirst())
        {
            while (!cursor.isAfterLast())
            {
                ProductDetails  productDetails = new ProductDetails();
                productDetails.ProductName=cursor.getString(cursor.getColumnIndex("ProductName"));
                productDetails.ProductID=cursor.getString(cursor.getColumnIndex("ProductID"));
                productDetails.CustomPID= cursor.getString(cursor.getColumnIndex("CustomPID"));
                productDetails.ShopID= cursor.getString(cursor.getColumnIndex("ShopID"));
                productDetails.price= cursor.getString(cursor.getColumnIndex("Price"));
                list.add(productDetails);
                cursor.moveToNext();
            }
        }
        customAdapter= new listCustomAdapter(getActivity(),getActivity(),list);
        listView.setAdapter(customAdapter);
        return  view;
    }

}
