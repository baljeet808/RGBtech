package com.nerdspoint.android.chandigarh.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.nerdspoint.android.chandigarh.R;
import com.nerdspoint.android.chandigarh.sharedPrefs.ProductDetails;

import java.util.List;

/**
 * Created by Naruto on 13-Apr-17.
 */

public class AddProductListAdapter extends BaseAdapter {


    Context context;
    List<ProductDetails> list;
    private static LayoutInflater inflater=null;

    ProductDetails productDetails =null;


    public  AddProductListAdapter( Context context, List<ProductDetails> list) {
        this.context=context;
        this.list=list;

        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {

        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    public static class ViewHolder{

        public TextView productname;
        public EditText price;
        public ImageView productPhoto;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
         ViewHolder holder;

        if(convertView==null){

            /****** Inflate tabitem.xml file for each row ( Defined below ) *******/
            vi = inflater.inflate(R.layout.custom_listview, null);

            /****** View Holder Object to contain tabitem.xml file elements ******/

            holder = new  ViewHolder();
            holder.productname = (TextView) vi.findViewById(R.id.textView_productName);
            holder.price = (EditText) vi.findViewById(R.id.productPrice);
            holder.productPhoto = (ImageView) vi.findViewById(R.id.product_image_iv);


            /************  Set holder with LayoutInflater ************/
            vi.setTag( holder );
        }
        else {
            holder = ( ViewHolder) vi.getTag();
        }




           {
            /***** Get each Model object from Arraylist ********/
            productDetails = null;
            productDetails =list.get(position);

            /************  Set Model values in Holder elements ***********/

            holder.productname.setText(productDetails.ProductName);


            holder.price.setText(productDetails.price);
            /******** Set Item Click Listner for LayoutInflater for each row *******/


        }
        return vi;
    }
}
