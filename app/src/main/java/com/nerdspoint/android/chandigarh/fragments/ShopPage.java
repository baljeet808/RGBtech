package com.nerdspoint.android.chandigarh.fragments;


import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nerdspoint.android.chandigarh.R;
import com.nerdspoint.android.chandigarh.activities.MainPage;
import com.nerdspoint.android.chandigarh.offlineDB.DBHandler;
import com.nerdspoint.android.chandigarh.permissionCheck.checkInternet;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import jp.wasabeef.blurry.Blurry;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShopPage extends Fragment implements View.OnClickListener {

    TextView shopName,Category,Address,contact;
    ImageButton map;
    ImageView profilePic;
    Button ownerDetail,Products;
    String Shopid;
    View userProfile;
    AlertDialog.Builder alert;
    AlertDialog dialog;



    public ShopPage() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_shop_page, container, false);

        Bundle bundle= getArguments();

        Shopid=bundle.getString("SHOPID");

        shopName= (TextView) view.findViewById(R.id.shop_name);
        Category= (TextView) view.findViewById(R.id.category);
        Address= (TextView) view.findViewById(R.id.address);
        contact= (TextView) view.findViewById(R.id.contact);
        profilePic= (ImageView) view.findViewById(R.id.shop_image);

        userProfile=prepareUserProfile();

        map= (ImageButton) view.findViewById(R.id.mapbutton);
        map.setOnClickListener(this);

        ownerDetail= (Button) view.findViewById(R.id.owner_detail);
        ownerDetail.setOnClickListener(this);

        Products= (Button) view.findViewById(R.id.products);
        Products.setOnClickListener(this);

        getValues();


        return view;
    }

    public View prepareUserProfile()
    {
        if(checkInternet.getCustomInstance(getActivity()).isConnected()) {
            LayoutInflater layoutInflater = getLayoutInflater(null);
            View view = layoutInflater.inflate(R.layout.owner_detail, null);

            return  new DBHandler(getActivity()).getShopOwnerDetail(Shopid,view);

        }
        else
        {
            Toast.makeText(getActivity(), "net not connected", Toast.LENGTH_SHORT).show();

            return null;
        }
    }


    public void getValues()
    {
        Cursor cursor= new DBHandler(getActivity()).getShopByID(Shopid);
        if(cursor.moveToFirst())
        {
            shopName.setText(cursor.getString(cursor.getColumnIndex("ShopName")));
            Address.setText(cursor.getString(cursor.getColumnIndex("ShopAddress"))+", Sector "+cursor.getString(cursor.getColumnIndex("Sector"))+", SCO "+cursor.getString(cursor.getColumnIndex("SCO")));
            contact.setText(cursor.getString(cursor.getColumnIndex("ShopContactNo")));
            Cursor cursor3 = new DBHandler(getActivity()).getCategoryName(cursor.getString(cursor.getColumnIndex("CategoryID")));
            if(cursor3.moveToFirst())
            {
                Category.setText(cursor3.getString(cursor3.getColumnIndex("CategoryName")));
            }
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.owner_detail :
            {
                try {


                    if(dialog!=null)
                    {
                        dialog.show();
                    }
                    else {
                    alert = new AlertDialog.Builder(getActivity());
                        if(userProfile!=null) {
                            alert.setView(userProfile);
                        }
                        else {
                            alert.setTitle("We hit a Wall !! ");
                            alert.setMessage("Profile Available in online mode");
                        }
                            alert.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            Toast.makeText(getActivity(), "cancel called", Toast.LENGTH_LONG).show();
                            try {
                                Blurry.delete((ViewGroup) getView());

                            } catch (Exception e) {
                                Toast.makeText(getActivity(), "catch blurry not working " + e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });


                        dialog = alert.create();
                        dialog.show();
                    }
                        try {
                        Blurry.with(getActivity()).radius(25).sampling(2).onto((ViewGroup) getView());
                    } catch (Exception e) {
                        Toast.makeText(getActivity(), "Blurry not working - " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
                catch(Exception e)
                {
                    Toast.makeText(getActivity(), "catch showing - "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }break;
            case R.id.products :
            {

            }break;
            case R.id.mapbutton :
            {

            }break;
        }
    }
}
