package com.nerdspoint.android.chandigarh;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.FacebookSdk;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private EditText et_username,et_password;

    private String login_URL="https://baljeet808singh.000webhostapp.com/chandigarh/login.php";                  // paste login file url in this string    it will check that user is present or not
                                                    // by matching email and password in the database
                                                    // sending arguments name > UserName, password use same in php file
                                                      // positive response >> "success" negative response >> "fail"
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // FacebookSdk.sdkInitialize(this);
        setContentView(R.layout.activity_login);

        et_username = (EditText) findViewById(R.id.editText2);
        et_password = (EditText) findViewById(R.id.editText);
        sharedPreferences = getSharedPreferences("userDetail",MODE_PRIVATE);     // SharedPreferences Name >> usrDetail
        editor= sharedPreferences.edit();                                       // SharedPreferences contain >>  email , password , location, sex , age, interests,name , type  of user
        editor.apply();
    }


    public void skip(View v)
    {
        Intent i = new Intent(LoginActivity.this,MainPage.class);
        startActivity(i);
        finish();
        Snackbar.make(getCurrentFocus(),"Moving to MainPage Activity",Snackbar.LENGTH_SHORT).setAction("Action",null).show();
    }

    public void SignUp(View v)                              //  calling signUpForm class to SignUp a user
    {
        Intent s = new Intent(LoginActivity.this,SignupForm.class);
        startActivity(s);
        finish();
        Snackbar.make(getCurrentFocus(),"Moving to SignUpForm Activity",Snackbar.LENGTH_SHORT).setAction("Action",null).show();
    }

    public void forgotPassword(View view)
    {
        Intent i = new Intent(LoginActivity.this,MainPage.class);
        startActivity(i);
        finish();
        Snackbar.make(getCurrentFocus(),"Moving to MainPage Activity",Snackbar.LENGTH_SHORT).setAction("Action",null).show();
    }

    public void simpleLogin(View v)
    {
        Log.d("simpleLogin","\t\t\tentered");
        if(et_username.getText().toString().length()>1 && et_password.getText().toString().length()>1)
        {
            Log.d("first if","\t\t\tchecked length");
            //if(email.contains("@gmail.com") || email.contains("@live.com") || email.contains("@hotmail.com") || email.contains("@yahoo.in"))
            {
                String Password = et_password.getText().toString();
                if(Password.length()>5)
                {
                    Log.d("second if","\t\t\tpassword length checked");
                    final AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    final AlertDialog alert = builder.create();
                    alert.setTitle("Loging In");
                    final ProgressBar progressBar = new ProgressBar(getApplicationContext());
                    alert.setView(progressBar);
                    alert.show();
                    Log.d("alert dialog","\t\t\talert started");
                    StringRequest request = new StringRequest(Request.Method.POST, login_URL, new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            Log.d("Response", response+"ok");
                            if(response.equals("success"))
                            {

                                alert.cancel();
                                Snackbar.make(getCurrentFocus(),"LOGIN SUCCESSFUL",Snackbar.LENGTH_SHORT).setAction("Action",null).show();
                                editor.putString("userName",et_username.getText().toString());
                                editor.putString("password",et_password.getText().toString());
                                editor.apply();
                                Intent i = new Intent(LoginActivity.this,MainPage.class);
                                startActivity(i);
                                finish();
                                Snackbar.make(getCurrentFocus(),"Moving to MainPage Activity",Snackbar.LENGTH_SHORT).setAction("Action",null).show();
                            }
                            else
                            {
                                alert.cancel();
                                Snackbar.make(getCurrentFocus(),"LOGIN FAILED",Snackbar.LENGTH_SHORT).setAction("Action",null).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("ERROR","error => "+error.toString());
                            alert.cancel();
                            Snackbar.make(getCurrentFocus(),error.getMessage(),Snackbar.LENGTH_SHORT).setAction("Action",null).show();
                        }
                    }
                    )
                    {
                        @Override
                        protected Map<String,String> getParams() throws AuthFailureError {
                            Map<String,String> map = new HashMap<>() ;
                            map.put("UserName",et_username.getText().toString());
                            map.put("Password",et_password.getText().toString());
                            return map;
                        }
                    };

                    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                    queue.add(request);

                }
                else
                {
                    Snackbar.make(getCurrentFocus(),"please enter at least 5 character",Snackbar.LENGTH_SHORT).setAction("Action",null).show();
                }
            }

        }

    }
}
