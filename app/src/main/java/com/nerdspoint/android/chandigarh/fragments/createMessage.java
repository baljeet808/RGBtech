package com.nerdspoint.android.chandigarh.fragments;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.nerdspoint.android.chandigarh.R;
import com.nerdspoint.android.chandigarh.activities.MainPage;
import com.nerdspoint.android.chandigarh.adapters.productListAdapter;
import com.nerdspoint.android.chandigarh.offlineDB.DBHandler;
import com.nerdspoint.android.chandigarh.service.notify;
import com.nerdspoint.android.chandigarh.sharedPrefs.ActiveUserDetail;
import com.nerdspoint.android.chandigarh.sharedPrefs.ProductDetails;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class createMessage extends Fragment {

    ImageButton add_btn;
    Spinner shopProducts;
    EditText et_customMessage;
    Button back,sendMessage;
    ListView listView;
    List<ProductDetails> products;
    ArrayAdapter arrayAdapter;
    List<String> addedProducts;
    List<String> cpIds;
    productListAdapter adapter;
    ProductDetails details;
    String Shopid,fid;
    TextView bill;
    int price=0;

    public createMessage() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_message, container, false);

        add_btn=(ImageButton) view.findViewById(R.id.add_btn);
        listView=(ListView) view.findViewById(R.id.lv_addProducts);
        back=(Button) view.findViewById(R.id.back_btn);
        sendMessage=(Button) view.findViewById(R.id.coming_btn);
        et_customMessage= (EditText) view.findViewById(R.id.et_customMessage);
        shopProducts=(Spinner) view.findViewById(R.id.product_spinner_of_shop);
        bill =(TextView) view.findViewById(R.id.bill);


        products= new ArrayList<>();
        addedProducts= new ArrayList<>();
        cpIds= new ArrayList<>();

        Bundle bundle = getArguments();
        Shopid= bundle.getString("shopid");
        fid= bundle.getString("fid");

        Cursor cursor= new DBHandler(getActivity()).getShopProducts(Shopid);
        if(cursor.moveToFirst())
        {
            while(!cursor.isAfterLast())
            {
                details = new ProductDetails();
                details.CustomPID= cursor.getString(cursor.getColumnIndex("CustomPID"));
                details.IsActive= cursor.getString(cursor.getColumnIndex("IsActive"));
                details.ProductName=cursor.getString(cursor.getColumnIndex("ProductName"));
                details.price=cursor.getString(cursor.getColumnIndex("Price"));
                products.add(details);
                cursor.moveToNext();
            }
        }

        adapter = new productListAdapter(getActivity(),products);
        shopProducts.setAdapter(adapter);
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ProductDetails detail = products.get(shopProducts.getSelectedItemPosition());

                cpIds.add(detail.CustomPID);
                addedProducts.add(detail.ProductName+"    Rs. "+detail.price);
                price=price+Integer.parseInt(detail.price);
                arrayAdapter.notifyDataSetChanged();
                bill.setText("INR. "+price+"");
            }
        });

        arrayAdapter= new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1,addedProducts);
        listView.setAdapter(arrayAdapter);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainPage)getActivity()).setShopAgain(Shopid);
            }
        });

        final Cursor cursor1 = new DBHandler(getActivity()).getShopByID(Shopid);

            Toast.makeText(getActivity(), "firebase id of shop recieved " + fid, Toast.LENGTH_SHORT).show();

            sendMessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (addedProducts.size() > 0) {
                        new notify(getActivity()).sendNotification("" + ActiveUserDetail.getCustomInstance(getActivity()).getFirstName() + " sent u a notification", "looking for purchase", fid,cpIds);

                    } else {
                        Toast.makeText(getActivity(), "please add any product", Toast.LENGTH_SHORT).show();
                    }
                }
            });


        return view;
    }

}
