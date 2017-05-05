package com.nerdspoint.android.chandigarh.fragments;


import android.Manifest;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.test.mock.MockPackageManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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
import com.nerdspoint.android.chandigarh.activities.MainPage;
import com.nerdspoint.android.chandigarh.adapters.GPSTracker;
import com.nerdspoint.android.chandigarh.offlineDB.DBHandler;
import com.nerdspoint.android.chandigarh.offlineDB.ipAddress;
import com.nerdspoint.android.chandigarh.sharedPrefs.ActiveUserDetail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class shopRegistration extends Fragment   {
    private static final int REQUEST_CODE_PERMISSION = 2;
    String mPermission = Manifest.permission.ACCESS_FINE_LOCATION;

    private static String shopRegistering_url="/shop.php";
    GPSTracker gps;
    RelativeLayout relativeLayout;
    ArrayList list;
    ArrayAdapter adapter;
    Spinner spinner;
    EditText tv_shopname,tv_shopAddress,tv_pincode,tv_SCO,tv_Sctor,tv_Shopnumber;
    Button Submit,cancel;
    ImageButton PinOnmap;

    String cid;
    Double  latitude,longitude;

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
        spinner = (Spinner) view.findViewById(R.id.categoryEdit);
        spinner.setAdapter(adapter);

     relativeLayout=(RelativeLayout)view.findViewById(R.id.relativeLayout);
        relativeLayout.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View view, MotionEvent ev)
            {
                hideKeyboard(view);
                return false;
            }
        });


        tv_shopname=(EditText)view.findViewById(R.id.shopName);
        tv_shopAddress=(EditText)view.findViewById(R.id.shopAddress);
        tv_pincode=(EditText)view.findViewById(R.id.pinCode);
        tv_SCO=(EditText)view.findViewById(R.id.scoNumber);
        tv_Sctor=(EditText)view.findViewById(R.id.sectorNo);
        tv_Shopnumber=(EditText)view.findViewById(R.id.contactNumber);
        PinOnmap=(ImageButton)view.findViewById(R.id.imageButton);
        Submit=(Button)view.findViewById(R.id.submitButton);
        cancel=(Button)view.findViewById(R.id.cancelButton);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainPage)getActivity()).Shopmanger();
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

                   latitude = gps.getLatitude();
                   longitude = gps.getLongitude();


                  Toast.makeText(getActivity(), "latitude ="+latitude+" longitude="+longitude, Toast.LENGTH_LONG).show();



              }else{

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
                params.put("Latitude",  ""+latitude );
                params.put("Longitude",""+longitude );
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

    protected void hideKeyboard(View view)
    {
        InputMethodManager in = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

}
