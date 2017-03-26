package com.nerdspoint.android.chandigarh.fragments;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nerdspoint.android.chandigarh.R;
import com.nerdspoint.android.chandigarh.offlineDB.DBHandler;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShopPage extends Fragment {

    TextView shopName,Category,Address,contact;
    ImageButton map;
    ImageView profilePic;
    Button ownerDetail,Products;
    String Shopid;

    public ShopPage() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_shop_page, container, false);

        Bundle bundle= getArguments();

        Shopid=bundle.getString("SHOPID");

        shopName= (TextView) view.findViewById(R.id.shop_name);
        Category= (TextView) view.findViewById(R.id.category);
        Address= (TextView) view.findViewById(R.id.address);
        contact= (TextView) view.findViewById(R.id.contact);
        profilePic= (ImageView) view.findViewById(R.id.shop_image);
        map= (ImageButton) view.findViewById(R.id.mapbutton);
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "maps will work shortly", Toast.LENGTH_SHORT).show();
            }
        });
        ownerDetail= (Button) view.findViewById(R.id.owner_detail);
        ownerDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "owner detail will work shortly", Toast.LENGTH_SHORT).show();
            }
        });
        Products= (Button) view.findViewById(R.id.products);
        Products.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "products will open shortly", Toast.LENGTH_SHORT).show();
            }
        });

        getValues();


        return view;
    }


    public void getValues()
    {
        Cursor cursor= new DBHandler(getActivity()).getShopByID(Shopid);
        if(cursor.moveToFirst())
        {
            shopName.setText(cursor.getString(cursor.getColumnIndex("ShopName")));
            Address.setText(cursor.getString(cursor.getColumnIndex("ShopAddress"))+", Sector "+cursor.getString(cursor.getColumnIndex("Sector"))+", SCO "+cursor.getString(cursor.getColumnIndex("SCO")));
            contact.setText(cursor.getString(cursor.getColumnIndex("ShopContactNo")));
            Cursor cursor3 = new DBHandler(getActivity()).getCategoryName(cursor.getString(cursor.getColumnIndex("CategoryID")));
            if(cursor3.moveToFirst())
            {
                Category.setText(cursor3.getString(cursor3.getColumnIndex("CategoryName")));
            }
        }

    }


}
