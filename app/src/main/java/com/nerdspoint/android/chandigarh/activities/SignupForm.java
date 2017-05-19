package com.nerdspoint.android.chandigarh.activities;

import android.app.Fragment;
import android.content.Context;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
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
import com.nerdspoint.android.chandigarh.offlineDB.ipAddress;
import com.nerdspoint.android.chandigarh.sharedPrefs.ActiveUserDetail;

import java.util.HashMap;
import java.util.Map;

public class SignupForm extends AppCompatActivity {

    EditText mobile,Email,Password,ConfirmPassword,userName;


    Button Back,OTP;
    TextView login;
    String usertype;
    RadioGroup newORold;
    EditText FirstName,LastName;
    RadioButton shopkeeper, vistor;
    RelativeLayout holder;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    FragmentTransaction fragmentTransaction;
    View view;

    FragmentManager fragmentManager;

    Boolean isLocationSet=false;

    private String signup_url ="/signup.php";          // enter the url here for signup purpose


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {


        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_signup_form);

        signup_url= ipAddress.getCustomInstance(getApplicationContext()).getIp()+signup_url;

        sharedPreferences = getSharedPreferences("userDetail",MODE_PRIVATE);     // SharedPreferences Name >> usrDetail
        editor= sharedPreferences.edit();                                       // SharedPreferences contain >>  email , password , location, sex , age, interests,name , type  of user
        editor.apply();
        OTP=(Button)findViewById(R.id.otp);
        OTP.setVisibility(View.GONE);
        login=(TextView)findViewById(R.id.link_login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplication(), LoginActivity.class);
                startActivity(intent);


            }
        });

         newORold = (RadioGroup) findViewById(R.id.UserType);

        newORold.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.visitor:

                        usertype="Visitor";
                        // do operations specific to this selection
                        // Toast.makeText(AddBook.this, "new", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.shopowner:
                        // do operations specific to this selection
                        usertype="Shop Owner";
                        // Toast.makeText(AddBook.this, "old", Toast.LENGTH_SHORT).show();

                        break;

                }
            }
        });


        holder = (RelativeLayout) findViewById(R.id.holder);
        mobile = (EditText) findViewById(R.id.mobileNo);
        Email = (EditText) findViewById(R.id.email);
        Back=(Button)findViewById(R.id.btnBack);
        FirstName=(EditText)findViewById(R.id.input_name);
        LastName=(EditText)findViewById(R.id.input_Lastname);



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



                if(FirstName.getText().length()>0)
                {

                  if(LastName.getText().length()>0) {

                      if(Email.getText().length()>0) {


                       if(Password.getText().length()>0) {

                           if(ConfirmPassword.getText().length()>0) {


                               if (mobile.getText().length() == 10) {
                                   String email = Email.getText().toString();
                                   if (email.contains("@gmail.com") || email.contains("@live.com") || email.contains("@hotmail.com") || email.contains("@yahoo.in")) {
                                       if (Password.getText().length() > 4) {
                                           if (Password.getText().toString().equals(ConfirmPassword.getText().toString()))
                                           {
                                               if (newORold.getCheckedRadioButtonId() == -1)
                                               {
                                                   Toast.makeText(this, "Please select the user type Visitor OR Shop Owner", Toast.LENGTH_SHORT).show();
                                               }
                                               else {
                                                   final AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                                                   alert.setTitle("Enter Your Name >");
                                                   final EditText editText = new EditText(getApplicationContext());
                                                   editText.setHint("Note : username will be used for login");
                                                   editText.setHintTextColor(Color.RED);
                                                   editText.setTextColor(Color.BLACK);
                                                   editText.setTextSize(14.0f);
                                                   alert.setView(editText);

                                                   alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                       public void onClick(DialogInterface dialog, int whichButton) {

                                                           if (editText.getText().length() > 1) {

                                                               SaveUser(v, mobile.getText().toString(), Email.getText().toString(), Password.getText().toString(), editText.getText().toString(), FirstName.getText().toString(), LastName.getText().toString(), usertype);
                                                           } else {
                                                               //           Snackbar.make(v.findFocus(),"give a UserName for future login reference ",Snackbar.LENGTH_SHORT).setAction("Action",null).show();
                                                           }
                                                       }
                                                   });
                                                   alert.show();
                                               }

                                           }


                                           else {
                                               Toast.makeText(this, "Password is not matching", Toast.LENGTH_SHORT).show();
                                           }
                                       } else {
                                           Toast.makeText(this, "Password is greater then 4", Toast.LENGTH_SHORT).show();
                                       }
                                   } else {
                                       Toast.makeText(this, "incorrect Email", Toast.LENGTH_SHORT).show();
                                   }

                               } else {
                                   Toast.makeText(this, "Mobile Number Should be 10 digit", Toast.LENGTH_SHORT).show();
                               }
                           }
                           else {
                               Toast.makeText(this, "Please enter the Confirm password", Toast.LENGTH_SHORT).show();
                           }



                       }

                       else {
                           Toast.makeText(this, "Please enter the password", Toast.LENGTH_SHORT).show();
                       }




                      }
                      else
                      {
                          Toast.makeText(this, "Please enter the Email", Toast.LENGTH_SHORT).show();

                      }
                  }

                  else
                      {
                          Toast.makeText(this, "Please enter the Last Name", Toast.LENGTH_SHORT).show();
                  }
                }
                else
                {
                    Toast.makeText(this, "Please enter the First Name", Toast.LENGTH_SHORT).show();
                }

    }

    private void SaveUser(final View v,final String mobile, final String email, final String password, final String userName,final String FirstName,final String LastName,final String usertype)
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
                if(response.equals("Email"))
                {

                    alert.dismiss();
                    Snackbar.make(getCurrentFocus(),"Email already exists ",Snackbar.LENGTH_LONG).setAction("Action",null).show();
                }
                if(response.equals("UserName"))
                {

                    alert.dismiss();
                    Snackbar.make(getCurrentFocus(),"Username already exists  ",Snackbar.LENGTH_LONG).setAction("Action",null).show();
                }
                if(response.equals("PhoneNumber"))
                {

                    alert.dismiss();
                    Snackbar.make(getCurrentFocus(),"PhoneNumber already exists ",Snackbar.LENGTH_LONG).setAction("Action",null).show();
                }



                if(response.equals("User Successfully Registered"))
                {
                    alert.dismiss();
                 //   Snackbar.make(v.findFocus(),"Moving to next fragment",Snackbar.LENGTH_SHORT).setAction("Action",null).show();

                    ActiveUserDetail.getCustomInstance(getApplicationContext()).setLoginType("Simple");
                    ActiveUserDetail.getCustomInstance(getApplicationContext()).setEmailAddress(email);
                    ActiveUserDetail.getCustomInstance(getApplicationContext()).setFirstName(FirstName);
                    ActiveUserDetail.getCustomInstance(getApplicationContext()).setLastName(LastName);
                    ActiveUserDetail.getCustomInstance(getApplicationContext()).setUserType(usertype);

                   // ActiveUserDetail.getCustomInstance(getApplicationContext()).setEmailAddress(email);
                    ActiveUserDetail.getCustomInstance(getApplicationContext()).setUserName(userName);
                    ActiveUserDetail.getCustomInstance(getApplicationContext()).setPhoneNumber(mobile);
                    ActiveUserDetail.getCustomInstance(getApplicationContext()).setPassword(password);
                    ActiveUserDetail.getCustomInstance(getApplicationContext()).setIsActive(true);

                 Intent intent=new Intent(getApplicationContext(),MainPage.class);
                    startActivity(intent);
                    finish();

                    //fragmentManager =getSupportFragmentManager();
                    //fragmentTransaction = fragmentManager.beginTransaction();
                   //  fragmentTransaction.add(R.id.fragment_container,profileUpdation);
                    //fragmentTransaction.commit();
                    //holder.setVisibility(View.GONE);

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
                map.put("PhoneNumber",mobile);
                map.put("Email",email);
                map.put("Password",password);
                map.put("FirstName",FirstName);
                map.put("LastName",LastName);
                map.put("UserType",usertype);


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

