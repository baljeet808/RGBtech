package com.nerdspoint.android.chandigarh.adapters;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.nerdspoint.android.chandigarh.R;
import com.nerdspoint.android.chandigarh.offlineDB.DBHandler;
import com.nerdspoint.android.chandigarh.sharedPrefs.ProductDetails;

import java.util.List;

/**
 * Created by android on 3/30/2017.
 */

public class productListAdapter extends BaseAdapter {
    Context context;
    List<ProductDetails> list;
    private static LayoutInflater inflater=null;

    ProductDetails productDetails =null;


    public productListAdapter( Context context, List<ProductDetails> list) {
        this.context=context;
        this.list=list;

        inflater = ( LayoutInflater )context.
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

        public TextView cpId;
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
            vi = inflater.inflate(R.layout.products_add_display, null);

            /****** View Holder Object to contain tabitem.xml file elements ******/

            holder = new ViewHolder();
            holder.cpId = (TextView) vi.findViewById(R.id.customPid_tv);
            holder.text = (TextView) vi.findViewById(R.id.textView_productName);
            holder.text1=(TextView)vi.findViewById(R.id.textView_productAval);
            holder.text2= (TextView) vi.findViewById(R.id.textView_productPrice);

            /************  Set holder with LayoutInflater ************/
            vi.setTag( holder );
        }
        else {
            holder = (ViewHolder) vi.getTag();
        }


        if(list.size()<=0)
        {

            holder.text.setText("No Data");
            holder.text1.setText("NO");
            holder.text2.setText("_");
        }

        else {
            /***** Get each Model object from Arraylist ********/
            productDetails = null;
            productDetails =list.get(position);

            /************  Set Model values in Holder elements ***********/

            holder.text.setText(productDetails.ProductName);
            if(productDetails.CustomPID!=null)
            {
                holder.cpId.setText(productDetails.CustomPID);
            }
            if(productDetails.IsActive.equals("1")) {
                holder.text1.setText("YES");
            }
            else
            {
                holder.text1.setText("NO");
            }
                holder.text2.setText(productDetails.price);
            /******** Set Item Click Listner for LayoutInflater for each row *******/


        }
        return vi;
    }
}
