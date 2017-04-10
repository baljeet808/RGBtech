package com.nerdspoint.android.chandigarh.adapters;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.nerdspoint.android.chandigarh.R;
import com.nerdspoint.android.chandigarh.activities.MainPage;
import com.nerdspoint.android.chandigarh.sharedPrefs.ProductDetails;
import com.nerdspoint.android.chandigarh.sharedPrefs.ShopDetails;

import java.util.List;

/**
 * Created by Gaurav Kumar on 04-Apr-17.
 */

public class ShopMangerAdapter extends RecyclerView.Adapter<ShopMangerAdapter.MyViewHolder>{

    String shopid;
    Context context;
    List<ShopDetails> list;



    public ShopMangerAdapter( Context context, List<ShopDetails> list ) {
        this.context = context;
        this.list = list;
      //  Toast.makeText(context, "entered in addpater", Toast.LENGTH_SHORT).show();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.cardviewforshopinfo,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {


            final ShopDetails details = list.get(position);
            final String shopName = details.shopName;
            String category=details.category;

            String contactNumber = details.contactNumber;
            String address = details.address;
            final String ShopId = details.ShopID;
            if(shopName!=null) {
                holder.shopName.setText(shopName);
            }else {
                holder.shopName.setText("sandhu jewellers");
            }
            if(contactNumber!=null) {
                holder.contactNumber.setText(contactNumber);
            }else {
                holder.contactNumber.setText("1234567890");
            }
            if(address!=null) {
                holder.address.setText(address);
            }else {
                holder.address.setText("chandigarh sector 17 sco 88");
            }
            if(category!=null)
            {
                holder.category.setText(category);

            }
            else
            {
                holder.category.setText("ornaments");
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    ((MainPage)context).ShopInfo(details.ShopID);
                }
            });
        holder.AddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                ((MainPage)context).AddProducts(ShopId);


            }
        });




    }

    @Override
    public int getItemCount() {
            return list.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {


        TextView shopName,category,address,contactNumber;
        Button AddProduct;
        public MyViewHolder(View itemView) {
            super(itemView);

            shopName=(TextView) itemView.findViewById(R.id.Shopname);
            category=(TextView) itemView.findViewById(R.id.category);

            address=(TextView) itemView.findViewById(R.id.Shopaddress);
            contactNumber=(TextView) itemView.findViewById(R.id.PhoneNumber);
            AddProduct=(Button) itemView.findViewById(R.id.AddProduct);

        }
    }
}
