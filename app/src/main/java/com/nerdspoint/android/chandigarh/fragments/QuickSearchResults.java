package com.nerdspoint.android.chandigarh.fragments;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.nerdspoint.android.chandigarh.R;
import com.nerdspoint.android.chandigarh.activities.MainPage;
import com.nerdspoint.android.chandigarh.adapters.listCustomAdapter;
import com.nerdspoint.android.chandigarh.offlineDB.DBHandler;
import com.nerdspoint.android.chandigarh.sharedPrefs.CategoryDetails;
import com.nerdspoint.android.chandigarh.sharedPrefs.ProductDetails;
import com.nerdspoint.android.chandigarh.sharedPrefs.ShopDetails;
import com.nerdspoint.android.chandigarh.adapters.RecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class QuickSearchResults extends Fragment implements View.OnClickListener {

    RecyclerView recyclerView;
    RecyclerAdapter shopAdapter;
    List<ShopDetails> list;

    List<CategoryDetails> list1;
    List<ProductDetails> list2;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;


    Context context;
    listCustomAdapter customAdapter;
    TextView query,resultCounts,searchType;
    LinearLayout categoryLinearLayout;
    String queryValue="nerdspoint",searchtype;
    DBHandler handler;
    AutoCompleteTextView searchBar;
    RelativeLayout main_fragment_holder,frag_menu_hold;
    TabHost host;
    ArrayAdapter productsList;
    Cursor cursor;

    Button actionButton;


    public QuickSearchResults() {

        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
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

        frag_menu_hold= (RelativeLayout) view.findViewById(R.id.frag_menu_holder);

        actionButton = (Button) view.findViewById(R.id.floating_button);
        actionButton.setOnClickListener(this);

        categoryLinearLayout= (LinearLayout) view.findViewById(R.id.layout_for_categories);



        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        host = (TabHost) ((MainPage)getActivity()).findViewById(R.id.tabHost);

        searchBar=(AutoCompleteTextView)((MainPage)getActivity()).findViewById(R.id.et_quickSearch);
        searchType=(TextView) ((MainPage)getActivity()).findViewById(R.id.tv_search_type);

        categoryLinearLayout.setVisibility(View.INVISIBLE);


        searchBar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String value= (String) parent.getItemAtPosition(position);

                searchtype=searchType.getText().toString();

                main_fragment_holder.setVisibility(View.VISIBLE);
                host.setVisibility(View.VISIBLE);


                host.setCurrentTabByTag("Search Result");


                queryValue=value;


                if(searchtype.equals("Shops"))
                {
                    recyclerView.setVisibility(View.VISIBLE);
                    categoryLinearLayout.setVisibility(View.INVISIBLE);
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
                    shopAdapter = new RecyclerAdapter(getActivity(),getActivity(),list,null,"shop");
                    recyclerView.setAdapter(shopAdapter);
                    shopAdapter.notifyDataSetChanged();

                    recyclerView.setVisibility(View.VISIBLE);


                }else
                {
                    recyclerView.setVisibility(View.VISIBLE);
                    categoryLinearLayout.setVisibility(View.INVISIBLE);
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
                    shopAdapter = new RecyclerAdapter(getActivity(),getActivity(),null,list2,"product");
                    recyclerView.setAdapter(shopAdapter);
                    shopAdapter.notifyDataSetChanged();
                    recyclerView.setVisibility(View.VISIBLE);


                }




                query.setText("showing results for "+queryValue);
                resultCounts.setText("results - "+cursor.getCount());



                cursor.close();
            }
        });


        Toast.makeText(getActivity(), ""+queryValue, Toast.LENGTH_LONG).show();

        return view;
    }


    @Override
    public void onClick(View v) {
        recyclerView.setVisibility(View.INVISIBLE);
        categoryLinearLayout.setVisibility(View.VISIBLE);
        ((MainPage)getActivity()).setCategoriesToFragmentHolder(v);
        Toast.makeText(getActivity(), "onclick running", Toast.LENGTH_SHORT).show();
    }
}
