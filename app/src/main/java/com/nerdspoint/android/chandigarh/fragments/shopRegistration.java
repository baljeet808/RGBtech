package com.nerdspoint.android.chandigarh.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;

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
import com.nerdspoint.android.chandigarh.offlineDB.ipAddress;
import com.nerdspoint.android.chandigarh.sharedPrefs.ActiveUserDetail;
import com.nerdspoint.android.chandigarh.sharedPrefs.CategoriesDetail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class shopRegistration extends Fragment {

    private static String shopRegistering_url="/shop.php";

    ArrayList list;
    ArrayAdapter adapter;
    Spinner spinner;
    EditText tv_shopname,tv_shopAddress,tv_pincode,tv_SCO;


    public shopRegistration() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_shop_registration, container, false);

        shopRegistering_url= ipAddress.getCustomInstance(getActivity()).getIp()+shopRegistering_url;



        list = new ArrayList();

        int count = CategoriesDetail.getCustomInstance(getActivity()).getCategoryCount();
        for(int i=0;i<count;i++)
        {
            list.add(CategoriesDetail.getCustomInstance(getActivity()).getCategory(i));
        }
        adapter= new ArrayAdapter(getActivity().getApplicationContext(),R.layout.support_simple_spinner_dropdown_item,list);

        spinner = (Spinner) view.findViewById(R.id.category);
        spinner.setAdapter(adapter);


        return view;
    }

    public void updateValues(View view)
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
                Log.d("Response", response+"Done");


                if(response.equals("") )
                {
                    alert.cancel();
                    Snackbar.make(getActivity().getCurrentFocus(),"record saved",Snackbar.LENGTH_SHORT).setAction("Action",null).show();
                    check();
                    Intent h = new Intent(getActivity(),MainPage.class);
                    startActivity(h);
                    getActivity().finish();

                }
                else
                {
                    Snackbar.make(getActivity().getCurrentFocus(),response.toString(),Snackbar.LENGTH_SHORT).setAction("Action",null).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ERROR","error => "+error.toString());
                Snackbar.make(getActivity().getCurrentFocus(),error.getMessage(),Snackbar.LENGTH_SHORT).setAction("Action",null).show();
            }
        }
        )
        {
            @Override
            protected Map getParams() throws AuthFailureError {


                Map map = new HashMap<>() ;

                return map;


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
