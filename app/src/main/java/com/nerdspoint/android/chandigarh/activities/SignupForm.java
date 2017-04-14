package com.nerdspoint.android.chandigarh.activities;

import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.digits.sdk.android.AuthCallback;
import com.digits.sdk.android.AuthConfig;
import com.digits.sdk.android.Digits;
import com.digits.sdk.android.DigitsAuthButton;
import com.digits.sdk.android.DigitsException;
import com.digits.sdk.android.DigitsSession;
import com.nerdspoint.android.chandigarh.R;
import com.nerdspoint.android.chandigarh.fragments.profileUpdation;
import com.nerdspoint.android.chandigarh.sharedPrefs.ActiveUserDetail;

import java.util.HashMap;
import java.util.Map;

public class SignupForm extends AppCompatActivity {

    EditText mobile,Email,Password,ConfirmPassword,userName;
    CheckBox visitor,shopowner;
    Button Back,OTP;
    RelativeLayout holder;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    FragmentTransaction fragmentTransaction;
    View view;

    FragmentManager fragmentManager;

    Boolean isLocationSet=false;

    private String signup_url ="https://baljeet808singh.000webhostapp.com/chandigarh/signup.php";          // enter the url here for signup purpose


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_signup_form);
        sharedPreferences = getSharedPreferences("userDetail",MODE_PRIVATE);     // SharedPreferences Name >> usrDetail
        editor= sharedPreferences.edit();                                       // SharedPreferences contain >>  email , password , location, sex , age, interests,name , type  of user
        editor.apply();
        OTP=(Button)findViewById(R.id.otp);


        holder = (RelativeLayout) findViewById(R.id.holder);
        mobile = (EditText) findViewById(R.id.mobileNo);
        Email = (EditText) findViewById(R.id.email);
        Back=(Button)findViewById(R.id.btnBack);

        Password=(EditText) findViewById(R.id.password);
        ConfirmPassword=(EditText) findViewById(R.id.confirmpassword);

        OTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthConfig.Builder builder = new AuthConfig.Builder();

                builder.withPhoneNumber("+91 "+mobile.getText());


                builder.withAuthCallBack(new AuthCallback() {
                    @Override
                    public void success(DigitsSession session, String Number) {
                        Digits.getActiveSession();
                        Digits.getInstance().logout();

                        Toast.makeText(getApplicationContext(), "Authentication successful for "
                                + Number, Toast.LENGTH_LONG).show();

                        // Do something
                    }

                    @Override
                    public void failure(DigitsException error) {
                        // Do something
                        Log.d("Digits", "Sign in with Digits failure", error);
                    }
                });

                AuthConfig authConfig = builder.build();

                Digits.authenticate(authConfig);
            }
        });




    }

    public void getLocation(View view) {

        isLocationSet=true;
    }

    public void skip(View v)
    {
        Intent i = new Intent(SignupForm.this,MainPage.class);
        startActivity(i);
        finish();
    }

    public void signup(final View v)
    {
                if(mobile.getText().length()==10)
                {
                        String email= Email.getText().toString();
                        if(email.contains("@gmail.com") || email.contains("@live.com") || email.contains("@hotmail.com") || email.contains("@yahoo.in"))
                        {
                            if(Password.getText().length()>4 )
                            {
                                if(Password.getText().toString().equals(ConfirmPassword.getText().toString()))
                                {
                                    final AlertDialog.Builder alert =new AlertDialog.Builder(v.getContext());
                                    alert.setTitle("Enter Your Name >");
                                    final EditText editText = new EditText(getApplicationContext());
                                    editText.setHint("Note : username will be used for login");
                                    editText.setHintTextColor(Color.RED);
                                    editText.setTextColor(Color.BLACK);
                                    editText.setTextSize(14.0f);
                                    alert.setView(editText);

                                    alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int whichButton) {

                                            if(editText.getText().length()>1) {

                                                SaveUser(v,mobile.getText().toString(), Email.getText().toString(), Password.getText().toString(),editText.getText().toString());
                                            }
                                            else
                                            {
                                     //           Snackbar.make(v.findFocus(),"give a UserName for future login reference ",Snackbar.LENGTH_SHORT).setAction("Action",null).show();
                                            }
                                        }
                                    });
                                    alert.show();
                                }
                                else{
                             //       Snackbar.make(v.findFocus(),"Password does not match",Snackbar.LENGTH_SHORT).setAction("Action",null).show();
                                    }
                            }else
                            {
                          //      Snackbar.make(v.findFocus(),"Password should be atleast of 5 letters",Snackbar.LENGTH_SHORT).setAction("Action",null).show();
                            }
                        }else
                        {
                       //     Snackbar.make(v.findFocus(),"invalid email",Snackbar.LENGTH_SHORT).setAction("Action",null).show();
                        }

                }else
                {
                  //  Snackbar.make(v.findFocus(),"please correct mobile number",Snackbar.LENGTH_SHORT).setAction("Action",null).show();
                }

    }

    private void SaveUser(final View v,final String Mobile, final String email, final String password, final String userName)
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        final AlertDialog alert = builder.create();
        alert.setTitle("Registering");
        final ProgressBar progressBar = new ProgressBar(getApplicationContext());
        alert.setView(progressBar);
        alert.show();

        StringRequest request = new StringRequest(Request.Method.POST, signup_url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Response", response+"Done");
                if(response.equals("Already"))
                {

                    alert.dismiss();
                    Snackbar.make(getCurrentFocus(),"User already exists with this detail",Snackbar.LENGTH_SHORT).setAction("Action",null).show();
                }

                if(response.equals("User Successfully Registered"))
                {
                    alert.dismiss();
                 //   Snackbar.make(v.findFocus(),"Moving to next fragment",Snackbar.LENGTH_SHORT).setAction("Action",null).show();

                    ActiveUserDetail.getCustomInstance(getApplicationContext()).setLoginType("Simple");
                    ActiveUserDetail.getCustomInstance(getApplicationContext()).setEmailAddress(email);
                    ActiveUserDetail.getCustomInstance(getApplicationContext()).setUserName(userName);
                    ActiveUserDetail.getCustomInstance(getApplicationContext()).setPhoneNumber(Mobile);
                    ActiveUserDetail.getCustomInstance(getApplicationContext()).setPassword(password);
                    ActiveUserDetail.getCustomInstance(getApplicationContext()).setIsActive(true);

                   profileUpdation profileUpdation = new profileUpdation();
                    fragmentManager =getSupportFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.add(R.id.fragment_container,profileUpdation);
                    fragmentTransaction.commit();
                    holder.setVisibility(View.GONE);

                 // Snackbar.make(v.findFocus(),"Moving to next Step",Snackbar.LENGTH_SHORT).setAction("Action",null).show();

                }
                else
                {
                    Log.d("response",response);
                    alert.dismiss();
                  //  Snackbar.make(v.findFocus(),response.toString(),Snackbar.LENGTH_SHORT).setAction("Action",null).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ERROR","error => "+error.toString());
                alert.dismiss();
            //    Snackbar.make(v.findFocus(),error.getMessage(),Snackbar.LENGTH_SHORT).setAction("Action",null).show();
            }
        }
        )
        {
            @Override
            protected Map getParams() throws AuthFailureError {
                Map map = new HashMap<>() ;

                map.put("UserName",userName);
                map.put("Email",email);
                map.put("Password",password);
                map.put("PhoneNumber",Mobile);

                return map;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);


    }
    public void back(View view)
    {
         Intent intent=new Intent(this,LoginActivity.class);
        startActivity(intent);
        finish();
    }
}

