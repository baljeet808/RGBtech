package com.nerdspoint.android.chandigarh.fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.digits.sdk.android.models.PhoneNumber;
import com.nerdspoint.android.chandigarh.R;
import com.nerdspoint.android.chandigarh.activities.FogotPassword;
import com.nerdspoint.android.chandigarh.activities.LoginActivity;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class ForgetPass extends Fragment {

 public Button Cancel,Change;
    EditText Password,ReTypePass;
    String NewPassword;
    String PhoneNumber;
    ProgressDialog progress;

    private  final  static String URL_Password="https://baljeet808singh.000webhostapp.com/chandigarh/password_change.php";
    public ForgetPass() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        final View view=inflater.inflate(R.layout.fragment_forget_pass, container, false);


        PhoneNumber=getArguments().getString("PhoneNumber");
        Cancel=(Button)view.findViewById(R.id.cancel);
        Change=(Button)view.findViewById(R.id.change);
        Password=(EditText)view.findViewById(R.id.NewPass);
        ReTypePass=(EditText)view.findViewById(R.id.ReTypePass);



         Cancel.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent intent=new Intent(getActivity(), LoginActivity.class);
                 startActivity(intent);


             }
         });

        Change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Password.getText().toString().length()<=0 && ReTypePass.getText().toString().length()<=0)
                {
                    Toast.makeText(getActivity(), "Please Fill the data", Toast.LENGTH_SHORT).show();
                }
                else if(Password.getText().toString().equals(ReTypePass.getText().toString()))
                {
                    NewPassword=Password.getText().toString();
                   String PhoneNumber1=PhoneNumber.substring(3,PhoneNumber.length());
                    progress = ProgressDialog.show(getActivity(),"Changing...","Please wait.",false, false);


                    PassChange(NewPassword,PhoneNumber1);
                }
                else
                {
                    Toast.makeText(getActivity(), "password is not matched", Toast.LENGTH_SHORT).show();
                }
            }
        });



        return view;



    }





    public void PassChange(final String Password, final String PhoneNumber)
    {
        StringRequest request = new StringRequest(Request.Method.POST, URL_Password, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progress.dismiss();
                if (response.equals("success"))
                {

                    Toast.makeText(getActivity(), "password changed", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);


                }
                else
                {
                    Toast.makeText(getActivity(), PhoneNumber, Toast.LENGTH_SHORT).show();
                    Toast.makeText(getActivity(), "password can not be changed", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progress.dismiss();
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("Password",Password);
                params.put("PhoneNumber",PhoneNumber);
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(request);

    }

}
