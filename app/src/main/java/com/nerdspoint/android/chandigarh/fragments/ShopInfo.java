package com.nerdspoint.android.chandigarh.fragments;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
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
import com.nerdspoint.android.chandigarh.adapters.AndroidImageAdapter;
import com.nerdspoint.android.chandigarh.adapters.GPSTracker;
import com.nerdspoint.android.chandigarh.offlineDB.DBHandler;
import com.nerdspoint.android.chandigarh.offlineDB.ipAddress;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShopInfo extends Fragment {

  Button Back,Update;
    GPSTracker gps;
    EditText Shopname,ShopAddres,PhoneNumber,SCO,Pincode,Sector;
    TextView latitude,longitude;

    private String EditShopProfile_URL="/update_Shop.php";
    ViewPager mViewPager ;
    String shopid;
    ImageButton  PinOnmap;

    Double latitude1 ,longitude1;


    public ShopInfo() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view= inflater.inflate(R.layout.fragment_shop_info, container, false);
        EditShopProfile_URL= ipAddress.getCustomInstance(getActivity()).getIp()+ EditShopProfile_URL;
        Back=(Button)view.findViewById(R.id.back_button);
        Update=(Button)view.findViewById(R.id.Update);

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
        Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update(view);

            }
        });

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

                    latitude1 = gps.getLatitude();
                      longitude1 = gps.getLongitude();

                    latitude.setText(""+latitude1);
                    longitude.setText(""+longitude1);
                    Toast.makeText(getActivity(), "latitude ="+latitude1+" longitude="+longitude1, Toast.LENGTH_LONG).show();



                }else{

                    gps.showSettingsAlert();
                }

            }
        });



        return view;
    }



    public void update(View view)
    {



        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final AlertDialog alert = builder.create();
        alert.setTitle("Updating");
        final ProgressBar progressBar = new ProgressBar(getActivity());
        alert.setView(progressBar);
        alert.show();

        StringRequest request = new StringRequest(Request.Method.POST, EditShopProfile_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Response", response+"Done");

                if(response.equals("success"))
                {

                    alert.cancel();

                    Toast.makeText(getActivity(), "update success", Toast.LENGTH_SHORT).show();


                }
                else
                {
                    Log.d("response",response);
                    alert.cancel();
                    Toast.makeText(getActivity(), "update Fail", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ERROR","error => "+error.toString());
                alert.cancel();
                //    Snackbar.make(v.findFocus(),error.getMessage(),Snackbar.LENGTH_SHORT).setAction("Action",null).show();
            }
        }
        )
        {
            @Override
            protected Map getParams() throws AuthFailureError {
                Map map = new HashMap<>() ;
                map.put("ShopID",shopid.toString());
                map.put("ShopName",Shopname.getText().toString());
                map.put("ShopAddress",ShopAddres.getText().toString());
                map.put("ShopContactNo",PhoneNumber.getText().toString());
                map.put("PinCode", Pincode.getText().toString());
                map.put("Sector",Sector.getText().toString());
                map.put("SCO", SCO.getText().toString());

                map.put("Latitude",  ""+latitude1);
                map.put("Longitude",""+longitude1);




                return map;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(request);




    }

}
