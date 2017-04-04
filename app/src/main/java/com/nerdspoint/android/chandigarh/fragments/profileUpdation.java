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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;

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

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class profileUpdation extends Fragment {

    EditText fname,lname;
    CheckBox visitor,shopOwner;
    String userType;
    Button skip,update;
    private static String signup1_url="/update_user.php";


    public profileUpdation() {
        // Required empty public constructor
    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_profile_updation, container, false);

        signup1_url= ipAddress.getCustomInstance(getActivity()).getIp()+signup1_url;

        fname= (EditText) view.findViewById(R.id.fname);
        lname= (EditText) view.findViewById(R.id.lname);
        visitor= (CheckBox) view.findViewById(R.id.cb_visitor);
        shopOwner= (CheckBox) view.findViewById(R.id.cb_shopOwner);
        skip= (Button) view.findViewById(R.id.buttonSkip);
        update=(Button) view.findViewById(R.id.button4);


        visitor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shopOwner.setChecked(false);
                userType = "Visitor";
            }
        });
        shopOwner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                visitor.setChecked(false);
                userType="ShopOwner";
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fname.getText().length()>1 )
                {
                    if(visitor.isChecked()==true || shopOwner.isChecked()==true)
                    {
                        updateValues(v);
                    }
                    else {
                        Snackbar.make(getView(),"Please check one of the checkbox above",Snackbar.LENGTH_SHORT).setAction("Action",null).show();
                    }
                }
                else {
                    Snackbar.make(getView(),"please give first name",Snackbar.LENGTH_SHORT).setAction("Action",null).show();
                }
            }
        });

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), LoginActivity.class);
                startActivity(i);
                getActivity().finish();
            }
        });


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


        StringRequest request = new StringRequest(Request.Method.POST, signup1_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response+"Done");


                if(response.equals("fail")==false)
                {
                    alert.cancel();
                    Snackbar.make(getActivity().getCurrentFocus(),"record saved",Snackbar.LENGTH_SHORT).setAction("Action",null).show();
                    ActiveUserDetail.getCustomInstance(getActivity()).setFirstName(fname.getText().toString());
                    ActiveUserDetail.getCustomInstance(getActivity()).setLastName(lname.getText().toString());
                    ActiveUserDetail.getCustomInstance(getActivity()).setUserType(userType);
                    ActiveUserDetail.getCustomInstance(getActivity()).setUID(response);
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


                map.put("FirstName",fname.getText().toString());
                map.put("LastName",lname.getText().toString());
                map.put("UserType",userType);
                map.put("UserName",ActiveUserDetail.getCustomInstance(getActivity()).getUserName());


                return map;


            }
        };

        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        queue.add(request);

    }





}
