package com.nerdspoint.android.chandigarh.fragments;


import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.nerdspoint.android.chandigarh.R;
import com.nerdspoint.android.chandigarh.activities.MainPage;
import com.nerdspoint.android.chandigarh.adapters.quickSearchAdapter;
import com.nerdspoint.android.chandigarh.offlineDB.DBHandler;
import com.nerdspoint.android.chandigarh.sharedPrefs.ShopDetails;
import com.nerdspoint.android.chandigarh.adapters.ShopAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class QuickSearchResults extends Fragment {

    RecyclerView recyclerView;
    ShopAdapter shopAdapter;
    List<ShopDetails> list;
    Context context;
    TextView query,resultCounts;
    String shopName="nerdspoint";
    DBHandler handler;
    AutoCompleteTextView searchBar;
    RelativeLayout main_fragment_holder;
    TabHost host;


    public QuickSearchResults() {

        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_quick_search_results, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_shopDetails);
        list = new ArrayList<>();

        handler=new DBHandler(getActivity());

        query=(TextView) view.findViewById(R.id.tv_query);
        resultCounts = (TextView) view.findViewById(R.id.resultCount);

        main_fragment_holder=(RelativeLayout) ((MainPage)getActivity()).findViewById(R.id.Main_Fragment_Holder);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        host = (TabHost) ((MainPage)getActivity()).findViewById(R.id.tabHost);

        searchBar=(AutoCompleteTextView)((MainPage)getActivity()).findViewById(R.id.et_quickSearch);

        searchBar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String value= (String) parent.getItemAtPosition(position);

                main_fragment_holder.setVisibility(View.VISIBLE);
                host.setVisibility(View.GONE);

                shopName=value;

                Cursor cursor= handler.getShop(shopName);



                query.setText("showing results for "+shopName);
                resultCounts.setText("results - "+cursor.getCount());
                list.clear();
                if(cursor.moveToFirst())
                {

                    while (!cursor.isAfterLast())
                    {
                        ShopDetails details = new ShopDetails();
                        details.address = cursor.getString(cursor.getColumnIndex("ShopAddress"))+", Sector "+cursor.getString(cursor.getColumnIndex("Sector"))+", SCO "+cursor.getString(cursor.getColumnIndex("SCO"));
                        details.shopName = cursor.getString(cursor.getColumnIndex("ShopName"));
                        details.productName=null;
                        details.price=null;
                        details.contactNumber=null;
                        details.ShopID=cursor.getString(cursor.getColumnIndex("ShopID"));
                        list.add(details);
                        cursor.moveToNext();
                    }
                }

                shopAdapter = new ShopAdapter(getActivity(),list);
               recyclerView.setAdapter(shopAdapter);
                shopAdapter.notifyDataSetChanged();

            }
        });


        Toast.makeText(getActivity(), ""+shopName, Toast.LENGTH_LONG).show();

        return view;
    }

   public void changeRecyclerView(String shopname)
    {
        this.shopName=shopname;
        shopAdapter.notifyDataSetChanged();
    }

}
