package com.nerdspoint.android.chandigarh.fragments;


import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.nerdspoint.android.chandigarh.R;
import com.nerdspoint.android.chandigarh.activities.MainPage;
import com.nerdspoint.android.chandigarh.adapters.AddProductListAdapter;
import com.nerdspoint.android.chandigarh.adapters.productListAdapter;
import com.nerdspoint.android.chandigarh.offlineDB.DBHandler;
import com.nerdspoint.android.chandigarh.sharedPrefs.ActiveUserDetail;
import com.nerdspoint.android.chandigarh.sharedPrefs.ProductDetails;
import com.nerdspoint.android.chandigarh.sharedPrefs.ShopDetails;
import com.twitter.sdk.android.core.models.TwitterCollection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProductAdd extends Fragment {
    String ID;
    Button back;
    Spinner spinner;
    ArrayList list;
    String cid;
    int count;
    ProductDetails productDetails;
    List<ProductDetails> list2;
    ArrayAdapter adapter;
    AddProductListAdapter listAdapter;
    ListView  listView;
    public ProductAdd() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view= inflater.inflate(R.layout.fragment_product_add, container, false);


        Bundle bundle=new Bundle();

        list2 = new ArrayList<>();

    listView =(ListView)view.findViewById(R.id.listView_Products);

        ID= getArguments().getString("shopid");
        Toast.makeText(getActivity(), ""+ID.toString(), Toast.LENGTH_SHORT).show();
        list = new ArrayList<>();
        back=(Button)view.findViewById(R.id.back_button);

        list = new ArrayList();

        Cursor cursor=new DBHandler(getActivity()).getAll("Category");


        if(cursor.moveToFirst())
        {
            while(!cursor.isAfterLast())
            {
                list.add(cursor.getString(cursor.getColumnIndex("CategoryName")));
                cursor.moveToNext();

            }
        }
        adapter= new ArrayAdapter(getActivity().getApplicationContext(),R.layout.spinner,list);
        spinner = (Spinner) view.findViewById(R.id.products_category);

        spinner.setAdapter(adapter);

      spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
          @Override
          public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
             String str = parent.getItemAtPosition(position).toString();
              Cursor cursor1 = new DBHandler(getActivity()).getCategoryID(str);
              if(cursor1.moveToFirst())
              {
                  Toast.makeText(getActivity(), ""+cursor1.getString(cursor1.getColumnIndex("CategoryID")), Toast.LENGTH_SHORT).show();
              }
              Cursor cursor = new DBHandler(getActivity()).getProductByCategoryName(str);
              if(cursor.moveToFirst())
              {
                  list2.clear();
                  while(!cursor.isAfterLast())
                  {
                      productDetails = new ProductDetails();
                      productDetails.ProductName = cursor.getString(cursor.getColumnIndex("ProductName"));
                      productDetails.ProductID=cursor.getString(cursor.getColumnIndex("ProductID"));
                      list2.add(productDetails);
                      cursor.moveToNext();
                  }
                  listAdapter=new AddProductListAdapter(getActivity(),list2);
                  listView.setAdapter(listAdapter);

              }

          }

          @Override
          public void onNothingSelected(AdapterView<?> parent) {

          }
      });


        back.setText("Back");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainPage)getActivity()).Shopmanger();
            }
        });



        return view;
    }






}
