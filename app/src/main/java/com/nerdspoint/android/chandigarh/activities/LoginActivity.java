package com.nerdspoint.android.chandigarh.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
import com.nerdspoint.android.chandigarh.R;
import com.nerdspoint.android.chandigarh.offlineDB.ipAddress;
import com.nerdspoint.android.chandigarh.sharedPrefs.ActiveUserDetail;

import java.util.HashMap;
import java.util.Map;

import jp.wasabeef.blurry.Blurry;

public class LoginActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private EditText et_username,et_password;
    private TextView ForgotPass;
    RelativeLayout login_activity;

    private String login_URL="/login.php";
    // paste login file url in this string    it will check that user is present or not
    // by matching email and password in the database
    // sending arguments name > UserName, password use same in php file
    // positive response >> "success" negative response >> "fail"
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login_URL= ipAddress.getCustomInstance(getApplicationContext()).getIp()+login_URL;
        ForgotPass=(TextView)findViewById(R.id.ForgotPass);
        et_username = (EditText) findViewById(R.id.editText);
        et_password = (EditText) findViewById(R.id.editText2);
        sharedPreferences = getSharedPreferences("userDetail",MODE_PRIVATE);     // SharedPreferences Name >> usrDetail
        editor= sharedPreferences.edit();                                       // SharedPreferences contain >>  email , password , location, sex , age, interests,name , type  of user
        editor.apply();
        login_activity=(RelativeLayout) findViewById(R.id.activity_login);




       /* WifiManager wm = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
*/
     //   Toast.makeText(getApplicationContext(),"address "+ip,Toast.LENGTH_LONG).show();
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
        Intent i = new Intent(this,FogotPassword.class);
        startActivity(i);
        finish();
        Snackbar.make(getCurrentFocus(),"Moving to MainPage Activity",Snackbar.LENGTH_SHORT).setAction("Action",null).show();
    }

    public void simpleLogin(View v)
    {
        Log.d("simpleLogin","\t\t\tentered");
        if(et_username.getText().toString().length()>1 && et_password.getText().toString().length()>1)
        {
            Log.d("first if","\t\t\tchecked length  "+et_username.getText().toString()+"  "+et_password.getText().toString());
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
                    alert.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            Blurry.delete((ViewGroup) login_activity.getRootView());
                        }
                    });
                    alert.show();
                    Blurry.with(getApplicationContext()).radius(25).sampling(2).onto((ViewGroup) login_activity.getRootView());
                    Log.d("alert dialog","\t\t\talert started");
                    StringRequest request = new StringRequest(Request.Method.POST, login_URL, new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            Log.d("Response", response+"ok");
                            if(response.equals("fail")==false)
                            {

                                alert.cancel();
                                Blurry.delete((ViewGroup) login_activity.getRootView());
                                Snackbar.make(getCurrentFocus(),"LOGIN SUCCESSFUL",Snackbar.LENGTH_SHORT).setAction("Action",null).show();
                                try {

                                    JSONArray jsonArray = new JSONArray(response);
                                    JSONObject jsonObject = jsonArray.getJSONObject(0);

                                    ActiveUserDetail.getCustomInstance(getApplicationContext()).logoutUser();
                                    ActiveUserDetail.getCustomInstance(getApplicationContext()).setEmailAddress(jsonObject.getString("Email"));
                                    ActiveUserDetail.getCustomInstance(getApplicationContext()).setLastName(jsonObject.getString("LastName"));
                                    ActiveUserDetail.getCustomInstance(getApplicationContext()).setFirstName(jsonObject.getString("FirstName"));
                                    ActiveUserDetail.getCustomInstance(getApplicationContext()).setUserName(jsonObject.getString("UserName"));
                                    ActiveUserDetail.getCustomInstance(getApplicationContext()).setPhoneNumber(jsonObject.getString("PhoneNumber"));
                                    ActiveUserDetail.getCustomInstance(getApplicationContext()).setUserType(jsonObject.getString("UserType"));
                                    ActiveUserDetail.getCustomInstance(getApplicationContext()).setUID(jsonObject.getString("UID"));
                                    ActiveUserDetail.getCustomInstance(getApplicationContext()).setPassword(jsonObject.getString("Password"));
                                    ActiveUserDetail.getCustomInstance(getApplicationContext()).setIsActive(true);
                                    ActiveUserDetail.getCustomInstance(getApplicationContext()).setLoginType("Simple");

                                    Snackbar.make(getCurrentFocus(),"Moving to MainPage Activity",Snackbar.LENGTH_SHORT).setAction("Action",null).show();

                                    Intent i= new Intent(LoginActivity.this,MainPage.class);
                                    startActivity(i);
                                    finish();

                                } catch (JSONException e) {
                                    e.printStackTrace();

                                }
                                Snackbar.make(getCurrentFocus(),"Moving to MainPage Activity",Snackbar.LENGTH_SHORT).setAction("Action",null).show();
                            }
                            else
                            {
                                alert.cancel();
                                Blurry.delete((ViewGroup) login_activity.getRootView());
                                Snackbar.make(getCurrentFocus(),"LOGIN FAILED",Snackbar.LENGTH_SHORT).setAction("Action",null).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("ERROR","error => "+error.toString());
                            alert.cancel();
                            Blurry.delete((ViewGroup) login_activity.getRootView());
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
