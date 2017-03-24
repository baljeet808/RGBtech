package com.nerdspoint.android.chandigarh.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.nerdspoint.android.chandigarh.R;
import com.nerdspoint.android.chandigarh.sharedPrefs.ShopDetails;

import java.util.List;

/**
 * Created by android on 09-03-2017.
 */

public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.MyViewHolder>{


    Context context;
    List<ShopDetails> list;

    public ShopAdapter(Context context, List<ShopDetails> list) {
        this.context = context;
        this.list = list;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.cardviewforsearch,parent,false);
        return new ShopAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        ShopDetails details = list.get(position);
        final String shopName = details.shopName;
        String product = details.productName;
        String price = details.price;
        String contactNumber = details.contactNumber;
        String address = details.address;
        final String ShopId= details.ShopID;

        holder.shopName.setText(shopName);
        holder.contactNumber.setText(contactNumber);
        holder.address.setText(address);
        holder.price.setText(price);
        holder.product.setText(product);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
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
