package com.nerdspoint.android.chandigarh.adapters;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nerdspoint.android.chandigarh.R;
import com.nerdspoint.android.chandigarh.offlineDB.DBHandler;
import com.nerdspoint.android.chandigarh.sharedPrefs.ProductDetails;
import com.nerdspoint.android.chandigarh.sharedPrefs.ShopDetails;

import java.util.List;

import static com.nerdspoint.android.chandigarh.R.id.holder;

/**
 * Created by android on 3/30/2017.
 */

public class TempShopAdapter extends BaseAdapter {

   Context context;
    List<String> list;
    LayoutInflater inflater;
    String shopid;

   public TempShopAdapter( Context context,List<String> shopIDs)
   {
       this.context=context;
       this.list=shopIDs;


       inflater = (LayoutInflater)context.
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
    public static class VHolder{

        public ImageView imageView;
        public TextView text1;
        public TextView id;

    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        VHolder holder;

        if(convertView==null){

            /****** Inflate tabitem.xml file for each row ( Defined below ) *******/
            vi = inflater.inflate(R.layout.temp_shop_layout, null);

            /****** View Holder Object to contain tabitem.xml file elements ******/

            holder = new VHolder();
            holder.imageView = (ImageView) vi.findViewById(R.id.shop_image);
            holder.text1= (TextView) vi.findViewById(R.id.shop_N);
            holder.id= (TextView) vi.findViewById(R.id.s_id);
            /************  Set holder with LayoutInflater ************/
            vi.setTag( holder );
        }
        else {
            holder = (TempShopAdapter.VHolder) vi.getTag();
        }


        if(list.size()<=0)
        {
            holder.text1.setText("No Data");


        }
        else {
            /***** Get each Model object from Arraylist ********/

            shopid = list.get(position);
            /************  Set Model values in Holder elements ***********/


            holder.id.setText(shopid);
            Cursor cursor= new DBHandler(context).getShopByID(shopid);
            if(cursor.moveToFirst())
            {
                holder.text1.setText(cursor.getString(cursor.getColumnIndex("ShopName")));
            }
            /******** Set Item Click Listner for LayoutInflater for each row *******/


        }
        return vi;
    }
}
