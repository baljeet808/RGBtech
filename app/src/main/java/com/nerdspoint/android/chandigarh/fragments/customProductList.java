package com.nerdspoint.android.chandigarh.fragments;


import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
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
import com.nerdspoint.android.chandigarh.adapters.listCustomAdapter;
import com.nerdspoint.android.chandigarh.offlineDB.DBHandler;
import com.nerdspoint.android.chandigarh.permissionCheck.checkInternet;
import com.nerdspoint.android.chandigarh.sharedPrefs.CategoryDetails;
import com.nerdspoint.android.chandigarh.sharedPrefs.ProductDetails;
import com.twitter.sdk.android.core.models.Image;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.wasabeef.blurry.Blurry;

/**
 * A simple {@link Fragment} subclass.
 */
public class customProductList extends Fragment implements AdapterView.OnItemClickListener {

    List<ProductDetails> list;
    listCustomAdapter customAdapter;
    DBHandler handler;
    Cursor cursor;
    ListView listView;
    String queryValue;
    AlertDialog dialog;
    AlertDialog.Builder alert;
    String ShopName,shopID,uid,fid,cpid;
   String latti,longi;


    public customProductList() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_custom_product_list, container, false);
        list=new ArrayList<ProductDetails>();
        Bundle bundle= getArguments();
        queryValue= bundle.getString("product");
        handler = new DBHandler(getActivity());
        listView= (ListView) view.findViewById(R.id.custom_product_list);

        cursor=handler.getProduct(queryValue);
        list.clear();
        if(cursor.moveToFirst())
        {
            while (!cursor.isAfterLast())
            {
                ProductDetails  productDetails = new ProductDetails();
                productDetails.ProductName=cursor.getString(cursor.getColumnIndex("ProductName"));
                productDetails.ProductID=cursor.getString(cursor.getColumnIndex("ProductID"));
                productDetails.CustomPID= cursor.getString(cursor.getColumnIndex("CustomPID"));
                productDetails.ShopID= cursor.getString(cursor.getColumnIndex("ShopID"));
                productDetails.price= cursor.getString(cursor.getColumnIndex("Price"));
                list.add(productDetails);
                cursor.moveToNext();
            }
        }
        customAdapter= new listCustomAdapter(getActivity(),getActivity(),list);
        listView.setAdapter(customAdapter);
        listView.setOnItemClickListener(this);
        return  view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TextView textView =(TextView) view.findViewById(R.id.cpid_tv);

        String cpID =  textView.getText().toString();
        cpid =cpID;
        if(dialog!=null) {
            dialog = null;
            alert=null;
        }
        alert = new AlertDialog.Builder(getActivity());

        View view1 = prepareAlertLayout(cpID);
        alert.setView(view1);
        alert.setTitle("QuickView");
        alert.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                try {
                    Blurry.delete((ViewGroup) getView());
                } catch (Exception e) {
                    Toast.makeText(getActivity(), "catch blurry not working " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });


        dialog = alert.create();
        dialog.show();

        try {
            Blurry.with(getActivity()).radius(25).sampling(2).onto((ViewGroup) getView());
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Blurry not working - " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }



    public View prepareAlertLayout(final String cpid)
    {

        LayoutInflater layoutInflater = getLayoutInflater(null);
        View view = layoutInflater.inflate(R.layout.product_info_alert, null);

        ImageView productPhoto = (ImageView) view.findViewById(R.id.product_photo);
        TextView productName = (TextView) view.findViewById(R.id.Product_name_alert);
        TextView address = (TextView) view.findViewById(R.id.address_for_alert);
        TextView price = (TextView) view.findViewById(R.id.product_price_alert);
        TextView description = (TextView) view.findViewById(R.id.description);
        TextView Contact = (TextView) view.findViewById(R.id.Contact_alert);
        shopID = new String();
        String ProductID = new String();
       ShopName = new String();
        latti = new String();
        longi = new String();


        Cursor cursor = new DBHandler(getActivity()).getCustomProductByID(cpid);
        if(cursor.moveToFirst())
        {
            description.setText((cursor.getString(cursor.getColumnIndex("Description"))).equals("") ? "Description  :  not sepcified" : "Description  : \n"+cursor.getString(cursor.getColumnIndex("Description")));
            price.setText("INR. "+cursor.getString(cursor.getColumnIndex("Price")));
            productName.setText(cursor.getString(cursor.getColumnIndex("ProductName")));
            shopID = cursor.getString(cursor.getColumnIndex("ShopID"));
            ProductID = cursor.getString(cursor.getColumnIndex("ProductID"));
        }

        if(checkInternet.getCustomInstance(getActivity()).isConnected())
        {
            getShopFirbaseID(shopID);
        }


        Cursor cursor1  = new DBHandler(getActivity()).getShopByID(shopID);

        if(cursor1.moveToFirst())
        {
            uid = cursor1.getString(cursor1.getColumnIndex("UID"));
            latti = cursor1.getString(cursor1.getColumnIndex("Latitude"));
            longi = cursor1.getString(cursor1.getColumnIndex("Longitude"));
            ShopName = cursor1.getString(cursor1.getColumnIndex("ShopName"));
            address.setText(" ShopName : "+cursor1.getString(cursor1.getColumnIndex("ShopName"))+"\n Address  : SCO "+cursor1.getString(cursor1.getColumnIndex("SCO"))+", Sector "+cursor1.getString(cursor1.getColumnIndex("Sector"))+", "+cursor1.getString(cursor1.getColumnIndex("ShopAddress")));
            Contact.setText(" Call Now  +91 - "+cursor1.getString(cursor1.getColumnIndex("ShopContactNo")));
        }



        Contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "call will be maked", Toast.LENGTH_SHORT).show();
            }
        });

        Button map = (Button) view.findViewById(R.id.Map_from_alert);
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkInternet.getCustomInstance(getActivity()).isConnected())
                {
                  dialog.dismiss();
                    Blurry.delete((ViewGroup) getView());
                    ((MainPage)getActivity()).setMapHistory(ShopName,shopID);
                    ((MainPage)getActivity()).driveToShop(Double.valueOf(latti),Double.valueOf(longi));
                }
                else {
                    Toast.makeText(getActivity(), "no internet connection", Toast.LENGTH_SHORT).show();
                }

            }

        });

        Button notify = (Button) view.findViewById(R.id.notify_from_alert);
        notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkInternet.getCustomInstance(getActivity()).isConnected())
                {
                    dialog.dismiss();
                    Blurry.delete((ViewGroup) getView());
                    ((MainPage)getActivity()).setCreateMessage(ShopName,shopID,fid,cpid);
                 }
                else {
                    Toast.makeText(getActivity(), "check your internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });

        if(checkInternet.getCustomInstance(getActivity()).isConnected()) {

            try {
                GetImages images = new GetImages(getActivity(), ProductID, "ProdcutImages", productPhoto, null, null);
                images.fetchImages();
            }
            catch(Exception e)
            {
                Toast.makeText(getActivity(), "error in uploading image "+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
              }

        else {

             }
             return view;
    }





    public void getShopFirbaseID(final String ShopID)
    {
      //  Toast.makeText(getActivity(), "shopid passed  "+ShopID, Toast.LENGTH_LONG).show();
        StringRequest request = new StringRequest(Request.Method.POST, "https://baljeet808singh.000webhostapp.com/chandigarh/ShopOwnerProfile.php", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {



                // Log.d("Response", response);
               // Toast.makeText(getActivity(), ""+response, Toast.LENGTH_SHORT).show();


                try {

                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    fid= jsonObject.getString("FirebaseID");

                } catch (JSONException e) {
                    e.printStackTrace();
                  //  Toast.makeText(getActivity(), "catch showing "+e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //   Toast.makeText(context,"on response error -"+error.toString(), Toast.LENGTH_LONG).show();
                //  Log.d("ERROR","error => "+error.toString());
            }
        }
        )
        {
            @Override
            protected Map<String,String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>() ;
                map.put("ShopID",ShopID);
                return map;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(request);

    }

}
