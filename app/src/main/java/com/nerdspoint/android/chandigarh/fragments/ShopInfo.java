package com.nerdspoint.android.chandigarh.fragments;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.nerdspoint.android.chandigarh.R;
import com.nerdspoint.android.chandigarh.activities.MainPage;
import com.nerdspoint.android.chandigarh.adapters.AndroidImageAdapter;
import com.nerdspoint.android.chandigarh.adapters.GPSTracker;
import com.nerdspoint.android.chandigarh.offlineDB.DBHandler;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShopInfo extends Fragment {

  Button Back;
    GPSTracker gps;
    EditText Shopname,ShopAddres,PhoneNumber,SCO,Pincode,Sector;
    TextView latitude,longitude;
    ViewPager mViewPager ;
    String shopid;
    ImageButton  PinOnmap;

    public ShopInfo() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_shop_info, container, false);
        Back=(Button)view.findViewById(R.id.back_button);
        Shopname=(EditText)view.findViewById(R.id.shopName);
        PhoneNumber=(EditText)view.findViewById(R.id.contactNumber);
        ShopAddres=(EditText)view.findViewById(R.id.shopAddress);
        SCO=(EditText)view.findViewById(R.id.scoNumber);
        Pincode=(EditText)view.findViewById(R.id.pinCode);
       Sector=(EditText)view.findViewById(R.id.sectorNo);
        latitude=(TextView)view.findViewById(R.id.Latitude);
        longitude=(TextView)view.findViewById(R.id.Longitude);
        PinOnmap=(ImageButton)view.findViewById(R.id.imageButton);





        Bundle bundle=getArguments();

          shopid=bundle.get("shopid").toString();

        Cursor cursor = new DBHandler(getActivity()).getShopByID(shopid);
        if(cursor.moveToFirst())
        {
            while(!cursor.isAfterLast())
            {
               Shopname.setText(cursor.getString(cursor.getColumnIndex("ShopName")));

                PhoneNumber.setText(cursor.getString(cursor.getColumnIndex("ShopContactNo")));

               ShopAddres.setText(cursor.getString(cursor.getColumnIndex("ShopAddress")));
                SCO.setText(cursor.getString(cursor.getColumnIndex("SCO")));

              Pincode.setText(cursor.getString(cursor.getColumnIndex("PinCode")));
                Sector.setText(cursor.getString(cursor.getColumnIndex("Sector")));
                latitude.setText(cursor.getString(cursor.getColumnIndex("Latitude")));
               longitude.setText(cursor.getString(cursor.getColumnIndex("Longitude")));

                cursor.moveToNext();
            }
        }



        Back.setText(shopid);

          mViewPager = (ViewPager) view.findViewById(R.id.viewPageAndroid);
        AndroidImageAdapter adapterView = new AndroidImageAdapter(getActivity());
        mViewPager.setAdapter(adapterView);

        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainPage)getActivity()).AddShop();
            }
        });




        PinOnmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gps = new GPSTracker(getActivity());

                // check if GPS enabled
                if(gps.canGetLocation()){

                   Double latitude1 = gps.getLatitude();
                    Double longitude1 = gps.getLongitude();

                    latitude.setText(""+latitude1.toString());
                    longitude.setText(""+longitude1.toString());
                    Toast.makeText(getActivity(), "latitude ="+latitude1+" longitude="+longitude1, Toast.LENGTH_LONG).show();



                }else{

                    gps.showSettingsAlert();
                }

            }
        });



        return view;
    }

}
