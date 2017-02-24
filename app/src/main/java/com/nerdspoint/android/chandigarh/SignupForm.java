package com.nerdspoint.android.chandigarh;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.util.HashMap;
import java.util.Map;

import static android.provider.UserDictionary.Words.APP_ID;

public class SignupForm extends AppCompatActivity {

    EditText firstname,lastname,mobile,Email,Password,ConfirmPassword;
    CheckBox visitor,shopowner;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    String longitude,lattitude;
    Boolean isLocationSet=false;

    private String signup_url ="";          // enter the url here for signup purpose


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_form);

        sharedPreferences = getSharedPreferences("userDetail",MODE_PRIVATE);     // SharedPreferences Name >> usrDetail
        editor= sharedPreferences.edit();                                       // SharedPreferences contain >>  email , password , location, sex , age, interests,name , type  of user
        editor.apply();

        firstname = (EditText) findViewById(R.id.editText2);
        lastname = (EditText) findViewById(R.id.editText3);
        mobile = (EditText) findViewById(R.id.editText4);
        Email = (EditText) findViewById(R.id.editText5);
        visitor = (CheckBox) findViewById(R.id.checkBox);
        shopowner = (CheckBox) findViewById(R.id.checkBox2);

        Password=(EditText) findViewById(R.id.editText7);
        ConfirmPassword=(EditText) findViewById(R.id.editText8);


        visitor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shopowner.setChecked(false);
            }
        });

        shopowner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                visitor.setChecked(false);
            }
        });


    }

    public void getLocation(View view) {
        longitude="122.22.33";
        lattitude="082.39.30";
        isLocationSet=true;
    }

    public void skip(View v)
    {
        Intent i = new Intent(SignupForm.this,MainPage.class);
        startActivity(i);
        finish();
    }

    public void signup(View v)
    {
        if(firstname.getText().length()>1)
        {

            if(lastname.getText().length()>1)
            {

                if(mobile.getText().length()==10)
                {
                    if(visitor.isChecked() || shopowner.isChecked())
                    {
                        String email= Email.getText().toString();
                        if(email.contains("@gmail.com") || email.contains("@live.com") || email.contains("@hotmail.com") || email.contains("@yahoo.in"))
                        {
                            if(Password.getText().length()>4 )
                            {
                                if(Password.getText().toString().equals(ConfirmPassword.getText().toString()))
                                {

                                       SaveUser(firstname.getText().toString(),lastname.getText().toString(),mobile.getText().toString(),visitor.isChecked(),shopowner.isChecked(),Email.getText().toString(),Password.getText().toString());
                                }
                                else{
                                    Snackbar.make(getCurrentFocus(),"Password does not match",Snackbar.LENGTH_SHORT).setAction("Action",null).show();
                                    }
                            }else
                            {
                                Snackbar.make(getCurrentFocus(),"Password should be atleast of 5 letters",Snackbar.LENGTH_SHORT).setAction("Action",null).show();
                            }
                        }
                        else
                        {
                            Snackbar.make(getCurrentFocus(),"Invalid email",Snackbar.LENGTH_SHORT).setAction("Action",null).show();
                        }
                    }else
                    {
                        Snackbar.make(getCurrentFocus(),"please select User type",Snackbar.LENGTH_SHORT).setAction("Action",null).show();
                    }

                }else
                {
                    Snackbar.make(getCurrentFocus(),"please correct mobile number",Snackbar.LENGTH_SHORT).setAction("Action",null).show();
                }

            }else
            {
                Snackbar.make(getCurrentFocus(),"please enter lastname",Snackbar.LENGTH_SHORT).setAction("Action",null).show();
            }
        }else
        {
            Snackbar.make(getCurrentFocus(),"please enter firstname",Snackbar.LENGTH_SHORT).setAction("Action",null).show();
        }
    }

    private void SaveUser(final String firstName, final String lastName, final String Mobile, final boolean Visitor, final boolean shopOwner, final String email, final String password)
    {

        StringRequest request = new StringRequest(Request.Method.POST, signup_url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Response", response);
                if(response.equals("success"))
                {

                    Snackbar.make(getCurrentFocus(),"Signup SUCCESSFUL",Snackbar.LENGTH_SHORT).setAction("Action",null).show();
                    editor.putString("email",email);
                    editor.putString("password",password);
                    editor.putString("firstName",firstName);
                    editor.putString("lastName",lastName);
                    editor.putString("Mobile",Mobile);
                    editor.putBoolean("Visitor",Visitor);
                    editor.putBoolean("shopOwner",shopOwner);
                    editor.apply();
                    Intent i = new Intent(SignupForm.this,MainPage.class);
                    startActivity(i);
                    finish();
                    Snackbar.make(getCurrentFocus(),"Moving to MainPage Activity",Snackbar.LENGTH_SHORT).setAction("Action",null).show();
                }
                else
                {
                    Snackbar.make(getCurrentFocus(),"SIGNUP FAILED",Snackbar.LENGTH_SHORT).setAction("Action",null).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ERROR","error => "+error.toString());
                Snackbar.make(getCurrentFocus(),error.getMessage(),Snackbar.LENGTH_SHORT).setAction("Action",null).show();
            }
        }
        )
        {
            @Override
            protected Map<String,String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>() ;
                map.put("email",email);
                map.put("password",password);
                map.put("firstName",firstName);
                map.put("lastName",lastName);
                map.put("Mobile",Mobile);
                map.put("Visitor",String.valueOf(Visitor));
                map.put("shopOwner",String.valueOf(shopOwner));
                return map;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);


    }
}

