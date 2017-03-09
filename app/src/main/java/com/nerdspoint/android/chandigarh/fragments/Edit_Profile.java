package com.nerdspoint.android.chandigarh.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.nerdspoint.android.chandigarh.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class Edit_Profile extends Fragment {

    public  Button Done;
    public EditText First_name,Last_name,Password,Email,Phone,Username;
    public ImageButton ChangePass;

    public Edit_Profile() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View  view=inflater.inflate(R.layout.fragment_edit__profile, container, false);

        First_name=(EditText)view.findViewById(R.id.FirstName);
        Last_name=(EditText)view.findViewById(R.id.LastName);
        Password=(EditText)view.findViewById(R.id.password);
        Email=(EditText)view.findViewById(R.id.email);
        Phone=(EditText)view.findViewById(R.id.Phone);
        Username=(EditText)view.findViewById(R.id.username);
        ChangePass=(ImageButton)view.findViewById(R.id.Edit_Password);
        ChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pass();
            }
        });

        return view;

    }
    public void pass()
    {
      Fragment change =new ChangePassword();
        FragmentManager  fm = getFragmentManager();
          FragmentTransaction fragmentTransaction = fm.beginTransaction();

        fragmentTransaction.replace(R.id.frag_holder,change);
        fragmentTransaction.commit();
    }


}
