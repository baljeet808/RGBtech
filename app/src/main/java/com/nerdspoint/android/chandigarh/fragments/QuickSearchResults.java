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
import com.nerdspoint.android.chandigarh.offlineDB.DBHandler;
import com.nerdspoint.android.chandigarh.sharedPrefs.CategoryDetalis;
import com.nerdspoint.android.chandigarh.sharedPrefs.ProductDetails;
import com.nerdspoint.android.chandigarh.sharedPrefs.ShopDetails;
import com.nerdspoint.android.chandigarh.adapters.RecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class QuickSearchResults extends Fragment {

    RecyclerView recyclerView;
    RecyclerAdapter shopAdapter;
    List<ShopDetails> list;

    List<CategoryDetalis> list1;
    List<ProductDetails> list2;
    Context context;
    TextView query,resultCounts,searchType;
    String queryValue="nerdspoint",searchtype;
    DBHandler handler;
    AutoCompleteTextView searchBar;
    RelativeLayout main_fragment_holder;
    TabHost host;
    Cursor cursor;


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
        list1 = new ArrayList<>();
        list2 = new ArrayList<>();

        handler=new DBHandler(getActivity());

        query=(TextView) view.findViewById(R.id.tv_query);
        resultCounts = (TextView) view.findViewById(R.id.resultCount);

        main_fragment_holder=(RelativeLayout) ((MainPage)getActivity()).findViewById(R.id.Main_Fragment_Holder);


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        host = (TabHost) ((MainPage)getActivity()).findViewById(R.id.tabHost);

        searchBar=(AutoCompleteTextView)((MainPage)getActivity()).findViewById(R.id.et_quickSearch);
        searchType=(TextView) ((MainPage)getActivity()).findViewById(R.id.tv_search_type);

        searchBar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String value= (String) parent.getItemAtPosition(position);

                searchtype=searchType.getText().toString();

                main_fragment_holder.setVisibility(View.VISIBLE);
                host.setVisibility(View.GONE);


                queryValue=value;


                if(searchtype.equals("Shops"))
                {
                    cursor= handler.getShop(queryValue);
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
                            details.contactNumber=cursor.getString(cursor.getColumnIndex("ShopContactNo"));
                            details.ShopID=cursor.getString(cursor.getColumnIndex("ShopID"));
                            list.add(details);
                            cursor.moveToNext();
                        }
                    }
                    shopAdapter = new RecyclerAdapter(getActivity(),list,null,"shop");
                    recyclerView.setAdapter(shopAdapter);
                    shopAdapter.notifyDataSetChanged();

                }else if(searchtype.equals("Category"))
                {
                  cursor=handler.getCategory(queryValue);
                    list1.clear();
                    if(cursor.moveToFirst())
                    {
                        while (!cursor.isAfterLast())
                        {
                            CategoryDetalis categoryDetalis = new CategoryDetalis();
                            categoryDetalis.CategoryID=cursor.getString(cursor.getColumnIndex("CategoryID"));
                            categoryDetalis.ProductID=cursor.getString(cursor.getColumnIndex("ProductID"));
                            categoryDetalis.ProductName=cursor.getString(cursor.getColumnIndex("ProductName"));
                            list1.add(categoryDetalis);
                            cursor.moveToNext();
                        }
                    }

                }else
                {
                    cursor=handler.getProduct(queryValue);
                    list2.clear();
                    if(cursor.moveToFirst())
                    {
                        while(!cursor.isAfterLast())
                        {
                            ProductDetails productDetails  = new ProductDetails();
                            productDetails.CategoryID = cursor.getString(cursor.getColumnIndex("CategoryID"));
                            productDetails.ProductID = cursor.getString(cursor.getColumnIndex("ProductID"));
                            productDetails.ProductName = cursor.getString(cursor.getColumnIndex("ProductName"));
                            productDetails.ShopID = cursor.getString(cursor.getColumnIndex("ShopID"));
                            productDetails.CustomPID = cursor.getString(cursor.getColumnIndex("CustomPID"));
                            productDetails.price= cursor.getString(cursor.getColumnIndex("Price"));
                           Cursor cursor2 = handler.getShopByID(productDetails.ShopID);
                           if(cursor2.moveToFirst())
                           {
                               while(!cursor2.isAfterLast())
                               {
                                   productDetails.Address=cursor2.getString(cursor2.getColumnIndex("ShopAddress"))+", Sector "+cursor2.getString(cursor2.getColumnIndex("Sector"))+", SCO "+cursor2.getString(cursor2.getColumnIndex("SCO"));
                                   productDetails.ShopName=cursor2.getString(cursor2.getColumnIndex("ShopName"));
                                   productDetails.ShopContactNo=cursor2.getString(cursor2.getColumnIndex("ShopContactNo"));
                                   cursor2.moveToNext();
                               }
                           }
                            list2.add(productDetails);
                            cursor.moveToNext();
                        }
                    }
                    shopAdapter = new RecyclerAdapter(getActivity(),null,list2,"product");
                    recyclerView.setAdapter(shopAdapter);
                    shopAdapter.notifyDataSetChanged();

                }




                query.setText("showing results for "+queryValue);
                resultCounts.setText("results - "+cursor.getCount());



                cursor.close();
            }
        });


        Toast.makeText(getActivity(), ""+queryValue, Toast.LENGTH_LONG).show();

        return view;
    }


}
