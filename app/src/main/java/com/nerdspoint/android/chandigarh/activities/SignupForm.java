package com.nerdspoint.android.chandigarh.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.nerdspoint.android.chandigarh.R;

import java.util.HashMap;
import java.util.Map;

public class SignupForm extends AppCompatActivity {

    EditText firstname,lastname,mobile,Email,Password,ConfirmPassword,userName;
    CheckBox visitor,shopowner;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    String longitude,lattitude;
    Boolean isLocationSet=false;

    private String signup_url ="https://baljeet808singh.000webhostapp.com/chandigarh/signup.php";          // enter the url here for signup purpose


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_form);

        sharedPreferences = getSharedPreferences("userDetail",MODE_PRIVATE);     // SharedPreferences Name >> usrDetail
        editor= sharedPreferences.edit();                                       // SharedPreferences contain >>  email , password , location, sex , age, interests,name , type  of user
        editor.apply();

        firstname = (EditText) findViewById(R.id.editText2);
<<<<<<< HEAD:app/src/main/java/com/nerdspoint/android/chandigarh/SignupForm.java
        lastname = (EditText) findViewById(R.id.editText4);
        mobile = (EditText) findViewById(R.id.editText5);
        Email = (EditText) findViewById(R.id.editText6);
        visitor = (CheckBox) findViewById(R.id.checkBox);
        shopowner = (CheckBox) findViewById(R.id.checkBox2);
        userName=(EditText)  findViewById(R.id.editText11);
        Password=(EditText) findViewById(R.id.editText7);
        ConfirmPassword=(EditText) findViewById(R.id.editText8);
=======
       // lastname = (EditText) findViewById(R.id.editText3);
        mobile = (EditText) findViewById(R.id.contact);
        Email = (EditText) findViewById(R.id.email);
      //  visitor = (CheckBox) findViewById(R.id.checkBox);
       // shopowner = (CheckBox) findViewById(R.id.checkBox2);

        Password=(EditText) findViewById(R.id.password);
        ConfirmPassword=(EditText) findViewById(R.id.confirmpassword);
>>>>>>> be89b44fab92c666d411d1453c9d9b609fcb8ceb:app/src/main/java/com/nerdspoint/android/chandigarh/activities/SignupForm.java


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
                         String userType="";
                        if(visitor.isChecked())
                        {
                             userType="Visitor";
                        }
                        if(shopowner.isChecked())
                        {
                             userType="ShopOwner";
                        }
                        String email= Email.getText().toString();
                        if(email.contains("@gmail.com") || email.contains("@live.com") || email.contains("@hotmail.com") || email.contains("@yahoo.in"))
                        {
                            if(Password.getText().length()>4 )
                            {
                                if(Password.getText().toString().equals(ConfirmPassword.getText().toString()))
                                {

                                    if(userName.getText().length()>1) {
                                        Log.d("values","firstname > "+firstname.getText().toString()+" lastname > "+lastname.getText().toString()+" mobile > "+mobile.getText().toString()+" usertype > "+userType+" Email >"+Email.getText().toString()+" Password > "+Password.getText().toString()+" userName > "+userName.getText().toString()+" ");
                                        SaveUser(firstname.getText().toString(), lastname.getText().toString(), mobile.getText().toString(),userType, Email.getText().toString(), Password.getText().toString(),userName.getText().toString());
                                    }
                                    else
                                    {
                                        Snackbar.make(getCurrentFocus(),"give a UserName for future login reference ",Snackbar.LENGTH_SHORT).setAction("Action",null).show();
                                    }
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

    private void SaveUser(final String firstName, final String lastName, final String Mobile, final String UserType, final String email, final String password, final String userName)
    {

        Log.d("values","firstname > "+firstName+" lastname > "+lastName+" mobile > "+Mobile+" usertype > "+UserType+" Email >"+email+" Password > "+password+" userName > "+userName+" ");

        StringRequest request = new StringRequest(Request.Method.POST, signup_url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Response", response+"Done");
                if(response.equals("Already"))
                {

                }
                if(response.equals("User Successfully Registered"))
                {

                    Snackbar.make(getCurrentFocus(),"Signup SUCCESSFUL",Snackbar.LENGTH_SHORT).setAction("Action",null).show();
                    editor.putString("UserName",userName);
                    editor.putString("firstName",firstName);
                    editor.putString("lastName",lastName);
                    editor.putString("email",email);
                    editor.putString("password",password);
                    editor.putString("Mobile",Mobile);
                    editor.putString("UserType",UserType);

                    editor.apply();
                    Intent i = new Intent(SignupForm.this,MainPage.class);
                    startActivity(i);
                    finish();
                    Snackbar.make(getCurrentFocus(),"Moving to MainPage Activity",Snackbar.LENGTH_SHORT).setAction("Action",null).show();
                }
                else
                {
                    Snackbar.make(getCurrentFocus(),response.toString(),Snackbar.LENGTH_SHORT).setAction("Action",null).show();
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
            protected Map getParams() throws AuthFailureError {
                Map map = new HashMap<>() ;
                Log.d("values","firstname > "+firstName+" lastname > "+lastName+" mobile > "+Mobile+" usertype > "+UserType+" Email >"+email+" Password > "+password+" userName > "+userName+" ");

                map.put("UserName",userName);
                map.put("FirstName",firstName);
                map.put("LastName",lastName);
                map.put("Email",email);
                map.put("Password",password);
                map.put("PhoneNumber",Mobile);
                map.put("UserType",UserType);

                return map;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);


    }
}

