package com.nerdspoint.android.chandigarh.adapters;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nerdspoint.android.chandigarh.R;
import com.nerdspoint.android.chandigarh.offlineDB.DBHandler;
import com.nerdspoint.android.chandigarh.sharedPrefs.CategoryDetails;
import com.nerdspoint.android.chandigarh.sharedPrefs.ProductDetails;

import java.util.List;

/**
 * Created by android on 3/28/2017.
 */

public class listCustomAdapter extends BaseAdapter {

    Context context;
    List<ProductDetails> list;
    private static LayoutInflater inflater=null;
    Activity activity;
    ProductDetails productDetails =null;


    public listCustomAdapter(Activity a, Context context, List<ProductDetails> list) {
        this.context=context;
        this.list=list;
        activity=a;

        inflater = ( LayoutInflater )activity.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        if(list.size()<=0)
            return 1;
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

        public TextView text;
        public TextView text1;
        public TextView text2;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        ViewHolder holder;

        if(convertView==null){

            /****** Inflate tabitem.xml file for each row ( Defined below ) *******/
            vi = inflater.inflate(R.layout.custom_list_item, null);

            /****** View Holder Object to contain tabitem.xml file elements ******/

            holder = new ViewHolder();
            holder.text = (TextView) vi.findViewById(R.id.productNamer);
            holder.text1=(TextView)vi.findViewById(R.id.shop_name_inlist);
            holder.text2= (TextView) vi.findViewById(R.id.product_Price);

            /************  Set holder with LayoutInflater ************/
            vi.setTag( holder );
        }

        else {
            holder = (ViewHolder) vi.getTag();
        }


        if(list.size()<=0)
        {
            holder.text.setText("No Data");

        }
        else {
            /***** Get each Model object from Arraylist ********/
            productDetails = null;
            productDetails =list.get(position);

            /************  Set Model values in Holder elements ***********/

            holder.text.setText(productDetails.ProductName);
            holder.text2.setText(productDetails.price);
            Cursor cursor= new DBHandler(context).getShopByID(productDetails.ShopID);
            if(cursor.moveToFirst())
            {
                holder.text1.setText(cursor.getString(cursor.getColumnIndex("ShopName")));
            }
            /******** Set Item Click Listner for LayoutInflater for each row *******/


        }
        return vi;
    }
}
