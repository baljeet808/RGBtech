package com.nerdspoint.android.chandigarh.fragments;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.nerdspoint.android.chandigarh.R;
import com.nerdspoint.android.chandigarh.activities.MainPage;
import com.nerdspoint.android.chandigarh.offlineDB.DBHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class productMenu extends Fragment implements AdapterView.OnItemClickListener{

    List<String> list;
    ArrayAdapter arrayAdapter;
    DBHandler handler;
    Cursor cursor;
    ListView listView;

    public productMenu() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_product_menu, container, false);


        list = new ArrayList<String>();
        handler= new DBHandler(getActivity());
        listView= (ListView) view.findViewById(R.id.product_lists);

        Bundle bundle = getArguments();

        cursor= handler.getProductByCategoryName(bundle.getString("category"));
        if(cursor.moveToFirst())
        {
            while (!cursor.isAfterLast())
            {
                list.add(cursor.getString(cursor.getColumnIndex("ProductName")));
                //   Toast.makeText(getActivity(), ""+cursor.getString(cursor.getColumnIndex("CategoryName")), Toast.LENGTH_SHORT).show();

                cursor.moveToNext();
            }

            arrayAdapter= new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1,list);
        }else {
            list.add("NO ITEM TO DISPLAY");
            arrayAdapter= new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1,list);
        }
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(this);
        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String ProductName = parent.getItemAtPosition(position).toString();

        Toast.makeText(getActivity(), ""+ProductName, Toast.LENGTH_SHORT).show();
        view.setBackgroundResource(R.drawable.backgraoundwithborder);
        ((MainPage)getActivity()).setCustomPRoductstoList(ProductName);

    }



}
