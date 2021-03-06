package com.nerdspoint.android.chandigarh.fragments;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputType;
import android.text.Layout;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.nerdspoint.android.chandigarh.R;
import com.nerdspoint.android.chandigarh.activities.MainPage;
import com.nerdspoint.android.chandigarh.adapters.GetImages;
import com.nerdspoint.android.chandigarh.offlineDB.ipAddress;
import com.nerdspoint.android.chandigarh.sharedPrefs.ActiveUserDetail;

import java.util.HashMap;
import java.util.Map;

import jp.wasabeef.blurry.Blurry;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 */
public class
EditProfile extends Fragment implements View.OnClickListener {

    TextView FirstName,LastName,PhoneNumber,Password,Email,UserName;
    String UID;
    Button Submit;
    private String EditProfile_URL="/EditProfile.php";
    RelativeLayout edit_profilento;

    ImageView userPhoto;
    String[] messahes={"enter First Name","enter Last Name","enter userName","enter email","enter phone","enter password"};
    public EditProfile() {
        // Required empty public constructor
    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_edit_profile, container, false);
        edit_profilento= (RelativeLayout) view.findViewById(R.id.edit_profilento);



       userPhoto=(ImageView) view.findViewById(R.id.photo);
        EditProfile_URL= ipAddress.getCustomInstance(getActivity()).getIp()+EditProfile_URL;
        FirstName=(TextView) view.findViewById(R.id.FirstName);
        LastName=(TextView) view.findViewById(R.id.LastName);
        PhoneNumber=(TextView) view.findViewById(R.id.PhoneNumber);
        Password=(TextView) view.findViewById(R.id.Password);
        Email=(TextView) view.findViewById(R.id.Email);
        UserName=(TextView) view.findViewById(R.id.Username);

        FirstName.setText(ActiveUserDetail.getCustomInstance(getActivity()).getFirstName());
        LastName.setText(ActiveUserDetail.getCustomInstance(getActivity()).getLastName());
        PhoneNumber.setText(ActiveUserDetail.getCustomInstance(getActivity()).getPhoneNumber());
        Password.setText(ActiveUserDetail.getCustomInstance(getActivity()).getPassword());
        Email.setText(ActiveUserDetail.getCustomInstance(getActivity()).getEmailAddress());
        UserName.setText(ActiveUserDetail.getCustomInstance(getActivity()).getUserName());

        UID=(ActiveUserDetail.getCustomInstance(getActivity()).getUID());
        FirstName.setOnClickListener(this);
        LastName.setOnClickListener(this);
        Email.setOnClickListener(this);
        PhoneNumber.setOnClickListener(this);
        Password.setOnClickListener(this);
        UserName.setOnClickListener(this);
        userPhoto.setOnClickListener(this);

        SetImage();

        Submit=(Button) view.findViewById(R.id.DONE);
        Submit.setOnClickListener(this);


        return view;

    }

    public void SetImage()
    {
        GetImages getImages = new GetImages(getActivity(),UID,"UserImages",userPhoto,null,null);
        getImages.fetchImages();
    }

    @Override
    public void onClick(final View view) {
        if(view.getId()==R.id.DONE)
        {
            update(view);

            return;
        }
        if(view.getId()==R.id.photo)
        {
            ((MainPage)getActivity()).setBackStack("user");
            ((MainPage)getActivity()).setProfilePictureParams(getActivity(),UID,"UserImages",userPhoto,null,null);
            ((MainPage)getActivity()).openImageHandler("UserImages",UID);
             return;
        }
        //final String MobilePattern = "[7-9]{10}";
        final EditText editText=new EditText(getActivity());
        final EditText confirmPass = new EditText(getActivity());
        confirmPass.setHint("retype password");


        final AlertDialog.Builder builder=new AlertDialog.Builder(view.getContext());

        if(view.getId()==R.id.Password)
        {
            editText.setHint("enter password");
            LinearLayout layout= new LinearLayout(getActivity());
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.addView(editText);
            layout.addView(confirmPass);
            builder.setView(layout);
            confirmPass.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence s, int start, int before,
                                          int count) {
                    if(confirmPass.getText().toString().equals(editText.getText().toString()))
                    {
                        confirmPass.setTextColor(Color.BLACK);
                    }else
                    {

                        confirmPass.setTextColor(Color.RED);
                        confirmPass.setError("password not matched");
                    }

                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count,
                                              int after) {

                }

                @Override
                public void afterTextChanged(Editable s) {


                }

            });
        }
        else if(view.getId()==R.id.PhoneNumber)
        {
            editText.setInputType(InputType.TYPE_CLASS_NUMBER);
            builder.setView(editText);
            editText.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence s, int start, int before,
                                          int count) {
                    if(editText.getText().toString().length()>10 || editText.getText().toString().length()<10)
                    {
                        editText.setTextColor(Color.RED);
                        editText.setError("number should be of 10 digits");
                    }else
                    {
                        editText.setTextColor(Color.BLACK);

                    }

                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count,
                                              int after) {

                }

                @Override
                public void afterTextChanged(Editable s) {


                }

            });
        }
        else
        {
            builder.setView(editText);
        }


        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Blurry.delete((ViewGroup) edit_profilento.getRootView());
                if(editText.getText().length()>1)
                {

                    switch (view.getId())
                    {
                        case R.id.FirstName :
                        {
                            FirstName.setText(editText.getText());
                        }break;
                        case R.id.LastName :
                        {
                            LastName.setText(editText.getText());
                        }break;
                        case R.id.PhoneNumber :
                        {
                            if(editText.getText().toString().length()==10) {
                                PhoneNumber.setText(editText.getText());
                            }

                            else
                            {
                                Toast.makeText(getActivity(),"number incorrect ",Toast.LENGTH_LONG).show();
                            }
                        }break;
                        case R.id.Password :
                        {

                            if(editText.getText().toString().equals(confirmPass.getText().toString()))
                            {
                                Password.setText(editText.getText());
                            }
                            else
                            {
                                Toast.makeText(getActivity(),"password not matched",Toast.LENGTH_LONG).show();
                            }

                        }break;
                        case R.id.Email :
                        {
                            Email.setText(editText.getText());
                        }break;
                        case R.id.Username :
                        {
                            UserName.setText(editText.getText());
                        }break;

                    }
                }
                else
                {
                    Toast.makeText(getActivity(), "please enter the data", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Blurry.delete((ViewGroup) edit_profilento.getRootView());

            }
        });


        switch (view.getId()) {
            case R.id.FirstName: {
                builder.setMessage("Enter the first Name");

            }
            break;
            case R.id.LastName: {
                builder.setMessage("Enter the Last Name");

            }
            break;
            case R.id.PhoneNumber: {
                builder.setMessage("Enter the  Phone  Number");

            }
            break;
            case R.id.Password: {
                builder.setMessage("Enter the Password");


            }
            break;
            case R.id.Email: {
                builder.setMessage("Enter the Email");

            }
            break;
            case R.id.Username: {
                builder.setMessage("Enter the UserName");

            }
            break;

        }
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                Blurry.delete((ViewGroup) edit_profilento.getRootView());
            }
        });
        builder.show();
        Blurry.with(getActivity().getApplicationContext()).radius(20).sampling(2).onto((ViewGroup) edit_profilento.getRootView() );
    }



    public void update(View view)
    {



        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final AlertDialog alert = builder.create();
        alert.setTitle("Updating");
        final ProgressBar progressBar = new ProgressBar(getActivity());
        alert.setView(progressBar);
        alert.show();

        StringRequest request = new StringRequest(Request.Method.POST, EditProfile_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Response", response+"Done");

                if(response.equals("success"))
                {

                    alert.cancel();

                    Toast.makeText(getActivity(), "update success", Toast.LENGTH_SHORT).show();
                    ((MainPage)getActivity()).resetTabs();

                }
                else
                {
                    Log.d("response",response);
                    alert.cancel();
                    Toast.makeText(getActivity(), "update Fail", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ERROR","error => "+error.toString());
                alert.cancel();
                //    Snackbar.make(v.findFocus(),error.getMessage(),Snackbar.LENGTH_SHORT).setAction("Action",null).show();
            }
        }
        )
        {
            @Override
            protected Map getParams() throws AuthFailureError {
                Map map = new HashMap<>() ;
                map.put("UserName",UserName.getText().toString());
                map.put("Email",Email.getText().toString());
                map.put("Password",Password.getText().toString());
                map.put("PhoneNumber",PhoneNumber.getText().toString());
                map.put("FirstName",FirstName.getText().toString());
                map.put("LastName",LastName.getText().toString());
                map.put("UID",UID);


                return map;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(request);




    }



}
