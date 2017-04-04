package com.nerdspoint.android.chandigarh.fragments;


import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.icu.util.ULocale;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.test.mock.MockPackageManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
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
import com.nerdspoint.android.chandigarh.activities.LoginActivity;
import com.nerdspoint.android.chandigarh.activities.MainPage;
import com.nerdspoint.android.chandigarh.adapters.GPSTracker;
import com.nerdspoint.android.chandigarh.offlineDB.DBHandler;
import com.nerdspoint.android.chandigarh.offlineDB.ipAddress;
import com.nerdspoint.android.chandigarh.sharedPrefs.ActiveUserDetail;
import com.nerdspoint.android.chandigarh.sharedPrefs.CategoriesDetail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 */
public class shopRegistration extends Fragment {
    private static final int REQUEST_CODE_PERMISSION = 2;
    String mPermission = Manifest.permission.ACCESS_FINE_LOCATION;

    private static String shopRegistering_url="/shop.php";
    GPSTracker gps;
    ArrayList list;
    ArrayAdapter adapter;
    Spinner spinner;
    EditText tv_shopname,tv_shopAddress,tv_pincode,tv_SCO,tv_Sctor,tv_Shopnumber;
    Button PinOnmap,Submit,cancel;
    TextView longitude1, latitude1;
    String cid;


    public shopRegistration() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_shop_registration, container, false);
        shopRegistering_url="/shop.php";
        shopRegistering_url= ipAddress.getCustomInstance(getActivity()).getIp()+shopRegistering_url;

        try {
            if (ActivityCompat.checkSelfPermission(getActivity(), mPermission)
                    != MockPackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(getActivity(), new String[]{mPermission},
                        REQUEST_CODE_PERMISSION);

                // If any permission above not allowed by user, this condition will
                // execute every time, else your else part will work
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        list = new ArrayList();

        int count = CategoriesDetail.getCustomInstance(getActivity()).getCategoryCount();
        for(int i=0;i<count;i++)
        {
            list.add(CategoriesDetail.getCustomInstance(getActivity()).getCategory(i));
        }
        adapter= new ArrayAdapter(getActivity().getApplicationContext(),R.layout.support_simple_spinner_dropdown_item,list);

        spinner = (Spinner) view.findViewById(R.id.category);
        spinner.setAdapter(adapter);



        tv_shopname=(EditText)view.findViewById(R.id.Shopname);
        tv_shopAddress=(EditText)view.findViewById(R.id.ShopAddress);
        tv_pincode=(EditText)view.findViewById(R.id.pincode);
        tv_SCO=(EditText)view.findViewById(R.id.SCO);
        tv_Sctor=(EditText)view.findViewById(R.id.Sector);
        tv_Shopnumber=(EditText)view.findViewById(R.id.ShopNumber);
        PinOnmap=(Button)view.findViewById(R.id.mapPin);
        Submit=(Button)view.findViewById(R.id.submit);
        cancel=(Button)view.findViewById(R.id.cancel);
        longitude1=(TextView)view.findViewById(R.id.longitude);
        latitude1=(TextView)view.findViewById(R.id.latitude);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainPage)getActivity()).AddShop();
            }
        });


        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String category= spinner.getSelectedItem().toString();

                Cursor cursor= new DBHandler(getActivity()).getCategoryID(category);
                if(cursor.moveToFirst())
                {
                    cid=cursor.getString(cursor.getColumnIndex("CategoryID"));
                }

                if(tv_shopname.getText().toString().length()==0 || tv_shopAddress.getText().toString().length()==0 || tv_pincode.getText().toString().length()==0  || tv_SCO.getText().toString().length()==0 || tv_Sctor.getText().toString().length()==0 || tv_Shopnumber.getText().toString().length()==0 || tv_pincode.getText().toString().length()==0)
                {



                    Toast.makeText(getActivity(), "please enter the all data", Toast.LENGTH_SHORT).show();

                }
                else if(tv_pincode.getText().length()<6 || tv_pincode.getText().length()>6 )
                {
                    Toast.makeText(getActivity(), "wrong pin code", Toast.LENGTH_SHORT).show();
                }
                else if(tv_Shopnumber.getText().length()<10 || tv_Shopnumber.getText().length()>10)
                {
                    Toast.makeText(getActivity(), "wrong mobile no", Toast.LENGTH_SHORT).show();
                }

                 else {
                     ShopRegistration(v);
                 }

            }
        });


      PinOnmap.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              gps = new GPSTracker(getActivity());

              // check if GPS enabled
              if(gps.canGetLocation()){

                  double latitude = gps.getLatitude();
                  double longitude = gps.getLongitude();
                  latitude1.setText(""+longitude);
                  longitude1.setText(""+latitude);



              }else{
                  // can't get location
                  // GPS or Network is not enabled
                  // Ask user to enable GPS/network in settings
                  gps.showSettingsAlert();
              }

          }
      });

        return view;
    }

    public void ShopRegistration(View view)
    {


        final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        final AlertDialog alert = builder.create();
        alert.setTitle("Please Wait");
        final ProgressBar progressBar = new ProgressBar(getActivity());
        alert.setView(progressBar);
        alert.show();





          StringRequest request = new StringRequest(Request.Method.POST, shopRegistering_url, new Response.Listener<String>() {
              @Override
              public void onResponse(String response) {
                  Log.d("Response", response + "Done");


                  if (response.equals("Success")) {
                      alert.cancel();
                      Snackbar.make(getActivity().getCurrentFocus(), "record saved", Snackbar.LENGTH_SHORT).setAction("Action", null).show();

                      Toast.makeText(getActivity(), "shop registration Success", Toast.LENGTH_SHORT).show();
                      ((MainPage)getActivity()).AddShop();
                  } else {
                      alert.cancel();
                      Snackbar.make(getActivity().getCurrentFocus(), response.toString(), Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                  }
              }
          }, new Response.ErrorListener() {
              @Override
              public void onErrorResponse(VolleyError error) {
                  alert.cancel();
                  Log.d("ERROR", "error => " + error.toString());
                  Snackbar.make(getActivity().getCurrentFocus(), error.getMessage(), Snackbar.LENGTH_SHORT).setAction("Action", null).show();
              }
          }
          ) {
              @Override
              protected Map getParams() throws AuthFailureError {


                  Map<String, String> params = new HashMap<>();
                  params.put("ShopName",tv_shopname.getText().toString());
                  params.put("ShopAddress",tv_shopAddress.getText().toString());
                  params.put("ShopContactNo",tv_Shopnumber.getText().toString());
                  params.put("Pincode",tv_pincode.getText().toString());
                  params.put("Sector",tv_Sctor.getText().toString());
                  params.put("SCO",tv_SCO.getText().toString());
                  params.put("CategoryID",cid);
                  params.put("Latitude",  latitude1.getText().toString() );
                  params.put("Longitude",longitude1.getText().toString());
                  params.put("UID",ActiveUserDetail.getCustomInstance(getActivity()).getUID());




                  return params;


              }
          };

          RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
          queue.add(request);

    }

    public void check()
    {
        Bundle bundle=ActiveUserDetail.getCustomInstance(getActivity()).getUserFullDetailAsBundle();
        Log.d("shared preferences ","UID > "+bundle.getString("UID")+" phone "+bundle.getString("PhoneNumber")+" username "+bundle.getString("UserName")+" fname "+bundle.getString("FirstName")+" lname "+bundle.getString("LastName")+" Iactive "+bundle.getString("IsActive")+" userType "+bundle.getString("UserType")+" LoginType "+bundle.getString("LoginType")+" email "+bundle.getString("Email")+" Password "+bundle.getString("Password"));
    }



}
