package com.nerdspoint.android.chandigarh.fragments;


import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputType;
import android.text.Layout;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nerdspoint.android.chandigarh.R;
import com.nerdspoint.android.chandigarh.sharedPrefs.ActiveUserDetail;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditProfile extends Fragment implements View.OnClickListener {

    TextView FirstName,LastName,Phone,Password,Email,UserName,UID;
    Button Submit;
    String[] messahes={"enter firstname","eter lastname","enter userName","enter email","eter phome","enter password"};
    public EditProfile() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view =inflater.inflate(R.layout.fragment_edit_profile, container, false);

        FirstName=(TextView) view.findViewById(R.id.FirstName);
        LastName=(TextView) view.findViewById(R.id.LastName);
        Phone=(TextView) view.findViewById(R.id.PhoneNumber);
        Password=(TextView) view.findViewById(R.id.Password);
        Email=(TextView) view.findViewById(R.id.Email);
        UserName=(TextView) view.findViewById(R.id.Username);

         FirstName.setText(ActiveUserDetail.getCustomInstance(getActivity()).getFirstName());
        LastName.setText(ActiveUserDetail.getCustomInstance(getActivity()).getLastName());
        Phone.setText(ActiveUserDetail.getCustomInstance(getActivity()).getPhoneNumber());
        Password.setText(ActiveUserDetail.getCustomInstance(getActivity()).getPassword());
        Email.setText(ActiveUserDetail.getCustomInstance(getActivity()).getEmailAddress());
        UserName.setText(ActiveUserDetail.getCustomInstance(getActivity()).getUserName());
        UID.setText(ActiveUserDetail.getCustomInstance(getActivity()).getUID());
        FirstName.setOnClickListener(this);
        LastName.setOnClickListener(this);
        Email.setOnClickListener(this);
        Phone.setOnClickListener(this);
        Password.setOnClickListener(this);
        UserName.setOnClickListener(this);

        Submit=(Button) view.findViewById(R.id.DONE);


        return view;

    }
    @Override
    public void onClick(final View view) {

            final String MobilePattern = "[7-9]{10}";
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


        builder.setCancelable(false);
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
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
                             if(editText.getText().toString().matches(MobilePattern)) {
                                 Phone.setText(editText.getText());
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
        builder.show();

    }


}
