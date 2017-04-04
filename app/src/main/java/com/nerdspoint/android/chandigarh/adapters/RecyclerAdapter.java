package com.nerdspoint.android.chandigarh.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.nerdspoint.android.chandigarh.R;
import com.nerdspoint.android.chandigarh.activities.MainPage;
import com.nerdspoint.android.chandigarh.sharedPrefs.ProductDetails;
import com.nerdspoint.android.chandigarh.sharedPrefs.ShopDetails;

import java.util.List;

/**
 * Created by android on 09-03-2017.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>{


    Context context;
    List<ShopDetails> list;
    List<ProductDetails> list1;
    String type;


    public RecyclerAdapter(Activity activity,Context context, List<ShopDetails> list, List<ProductDetails>  list1, String type) {
        this.context = context;
            this.list1=list1;
        this.list = list;
        this.type=type;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.cardviewforsearch,parent,false);
        return new RecyclerAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        if(type=="shop") {
            ShopDetails details = list.get(position);
            final String shopName = details.shopName;
            String product = details.productName;
            String price = details.price;
            String contactNumber = details.contactNumber;
            String address = details.address;
            final String ShopId = details.ShopID;
            if(shopName!=null) {
                holder.shopName.setText(shopName);
            }else {
                holder.shopName.setVisibility(View.INVISIBLE);
            }
            if(contactNumber!=null) {
                holder.contactNumber.setText(contactNumber);
            }else {
                holder.contactNumber.setVisibility(View.INVISIBLE);
            }
            if(address!=null) {
                holder.address.setText(address);
            }else {
                holder.address.setVisibility(View.INVISIBLE);
            }
            if(price!=null) {
                holder.price.setText("INR. "+price);
            }else {
                holder.price.setVisibility(View.INVISIBLE);
            }
            if(product!=null)
            {
                holder.product.setText(product);
            }else {
                holder.product.setVisibility(View.INVISIBLE);
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    ((MainPage)context).showShop(ShopId,shopName);
                }
            });
        }
        else if(type=="product")
        {

            final ProductDetails productDetails= list1.get(position);

            if(productDetails.ShopName!=null) {
                holder.shopName.setText(productDetails.ShopName);
            }else {
                holder.shopName.setVisibility(View.INVISIBLE);
            }
            if(productDetails.ShopContactNo!=null) {
                holder.contactNumber.setText(productDetails.ShopContactNo);
            }else {
                holder.contactNumber.setVisibility(View.INVISIBLE);
            }
            if(productDetails.price!=null) {
                holder.price.setText("INR. "+productDetails.price);
            }else {
                holder.price.setVisibility(View.INVISIBLE);
            }
            if(productDetails.ProductName!=null) {
                holder.product.setText(productDetails.ProductName);
            }else {
                holder.product.setVisibility(View.INVISIBLE);
            }
            if(productDetails.Address!=null)
            {
                holder.address.setText(productDetails.Address);
            }else {
                holder.address.setVisibility(View.INVISIBLE);
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {



                    ((MainPage)context).showShop(productDetails.ShopID,productDetails.ShopName);



                }
            });
        }
    }

    @Override
    public int getItemCount() {


        if(type.equals("shop")) {
            return list.size();
        }else
        {
            return list1.size();
        }

    }



    public class MyViewHolder extends RecyclerView.ViewHolder{


        TextView shopName,product,price,address,contactNumber;

        public MyViewHolder(View itemView) {
            super(itemView);

            shopName=(TextView) itemView.findViewById(R.id.shopNameHolder);
            price=(TextView) itemView.findViewById(R.id.priceHolder);
            product=(TextView) itemView.findViewById(R.id.productHolder);
            address=(TextView) itemView.findViewById(R.id.AddressHolder);
            contactNumber=(TextView) itemView.findViewById(R.id.ContactNumberHolder);
        }
    }
}
