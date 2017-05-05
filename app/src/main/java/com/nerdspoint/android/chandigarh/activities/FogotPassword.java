package com.nerdspoint.android.chandigarh.activities;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
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
import com.digits.sdk.android.models.PhoneNumber;
import com.nerdspoint.android.chandigarh.R;
import com.nerdspoint.android.chandigarh.fragments.Advrts;
import com.nerdspoint.android.chandigarh.fragments.ForgetPass;

import java.util.HashMap;
import java.util.Map;

import static android.R.attr.phoneNumber;
import static android.R.attr.sessionService;

public class FogotPassword extends AppCompatActivity {
  private Button Back,verify,OTP;
    FragmentTransaction fragmentTransaction;
    Fragment fragment;
    FragmentManager fragmentManager;
     RelativeLayout main,passfrag;
    boolean isVerified;

    private  final  static String URL_PHONE="https://baljeet808singh.000webhostapp.com/chandigarh/VerifyNumber.php";
    private EditText number;
    ProgressDialog progress;
    String Number;
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

        setContentView(R.layout.activity_fogot_password);







        Back=(Button)findViewById(R.id.back);
        OTP=(Button)findViewById(R.id.OTP);
        verify=(Button)findViewById(R.id.verify);
        number=(EditText) findViewById(R.id.PhoneNumber);

        main=(RelativeLayout)findViewById(R.id.Main_page_passHolder);
        passfrag=(RelativeLayout)findViewById(R.id.Pass_holder);
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress = ProgressDialog.show(FogotPassword.this,"Verifying...","Please wait.",false, false);

                Number=number.getText().toString();

                verifyNumber(Number);

            }

        });
        OTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MOB(Number);

            }
        });








    }
    public  void back(View view)
    {
        Intent intent=new Intent(this,LoginActivity.class);
        startActivity(intent);
        finish();


    }
    public  void MOB(String Number)
    {
        final AuthConfig.Builder builder = new AuthConfig.Builder();

        builder.withPhoneNumber("+91 "+Number);


       AuthConfig.Builder digits = builder.withAuthCallBack(new AuthCallback() {
            @Override
            public void success(DigitsSession session, String Number) {

                Digits.getActiveSession();
                Digits.getInstance().logout();


                Toast.makeText(getApplicationContext(), "Authentication successful for "
                        + Number, Toast.LENGTH_LONG).show();


                        PassFrag();


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



    public void verifyNumber(final String number)
    {
        StringRequest request = new StringRequest(Request.Method.POST, URL_PHONE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progress.dismiss();
                if (response.equals("1"))
                    isVerified = true;
                else
                    isVerified = false;

                if (isVerified) {

                    OTP.setVisibility(View.VISIBLE);

                    Toast.makeText(FogotPassword.this, "Number Verified", Toast.LENGTH_SHORT).show();

                }
                else
                {
                    Toast.makeText(FogotPassword.this, "Number Doesn't Exists", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progress.dismiss();
                Toast.makeText(FogotPassword.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("PhoneNumber",number);
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);

    }

    public  void PassFrag()
    {
        ForgetPass forgetPass = new ForgetPass();
        fragmentManager =getSupportFragmentManager();
        main.setVisibility(View.INVISIBLE);
        Bundle bundle=new Bundle();
        bundle.putString("PhoneNumber",Number);
        forgetPass.setArguments(bundle);
        passfrag.setVisibility(View.VISIBLE);
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.Pass_holder,forgetPass);
        fragmentTransaction.commit();
    }


}
