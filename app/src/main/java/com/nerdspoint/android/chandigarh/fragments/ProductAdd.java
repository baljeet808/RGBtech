package com.nerdspoint.android.chandigarh.fragments;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.nerdspoint.android.chandigarh.R;
import com.nerdspoint.android.chandigarh.activities.MainPage;
import com.nerdspoint.android.chandigarh.offlineDB.DBHandler;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProductAdd extends Fragment {
    String ID;
    Button back;
    Spinner spinner;
    ArrayList list;
    ArrayAdapter adapter;

    public ProductAdd() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view= inflater.inflate(R.layout.fragment_product_add, container, false);

        Bundle bundle=new Bundle();

        ID= getArguments().getString("shopid");
        Toast.makeText(getActivity(), ""+ID.toString(), Toast.LENGTH_SHORT).show();

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
