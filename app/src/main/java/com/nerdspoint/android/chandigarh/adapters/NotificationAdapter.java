package com.nerdspoint.android.chandigarh.adapters;

import android.app.Service;
import android.content.Context;
import android.database.Cursor;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nerdspoint.android.chandigarh.R;
import com.nerdspoint.android.chandigarh.offlineDB.DBHandler;
import com.nerdspoint.android.chandigarh.sharedPrefs.NotificatioDetail;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by android on 4/1/2017.
 */

public class NotificationAdapter extends BaseAdapter {

    private static LayoutInflater inflater;
    View view;
    Context context;
    List<NotificatioDetail> list;

    NotificatioDetail detail=null;

    public NotificationAdapter(Context context, List<NotificatioDetail> list) {

        this.list = list;
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Service.LAYOUT_INFLATER_SERVICE);
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
        public long getItemId ( int position){
            return 0;
        }

        public static class ViewHolder {
            public TextView text;
            public TextView text1;
            public TextView text2;
            public TextView text3;
            public TextView text4;
        }


        @Override
        public View getView ( int position, View convertView, ViewGroup parent){
            View vi = convertView;
            ViewHolder holder;

            if(convertView==null){

                /****** Inflate tabitem.xml file for each row ( Defined below ) *******/
                vi = inflater.inflate(R.layout.notification_item, null);

                /****** View Holder Object to contain tabitem.xml file elements ******/

                holder = new ViewHolder();

                holder.text = (TextView) vi.findViewById(R.id.header);
                holder.text1=(TextView)vi.findViewById(R.id.footer);
                holder.text2 = (TextView) vi.findViewById(R.id.typeTag);
                holder.text3 =(TextView) vi.findViewById(R.id.message_title);
                holder.text4 = (TextView) vi.findViewById(R.id.time);
                /************  Set holder with LayoutInflater ************/
                vi.setTag( holder );
            }
            else {
                holder = (ViewHolder) vi.getTag();
            }



                /***** Get each Model object from Arraylist ********/
                detail = null;
                detail =list.get(position);

                /************  Set Model values in Holder elements ***********/


                if(detail.ShopName!=null) {
                    holder.text.setText(detail.ShopName);
                }else if ( detail.visitorName!=null){
                    holder.text.setText(detail.visitorName);
                }
                    holder.text2.setText(detail.type);
                holder.text3.setText(detail.title);
                holder.text1.setText(detail.Message);
                holder.text4.setText(detail.timestamp);

                /******** Set Item Click Listner for LayoutInflater for each row *******/

                vi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       Gson gson = new Gson();
                        Type type = new TypeToken<ArrayList<String>>() {}.getType();

                        ArrayList<String> finalOutputString = gson.fromJson(detail.cpids, type);
                        for(int i =0;i<finalOutputString.size();i++)
                        {
                            Cursor cursor = new DBHandler(context).getCustomProductByID(finalOutputString.get(i));
                            if(cursor.moveToFirst())
                            {
                                Toast.makeText(context, "product - "+cursor.getString(cursor.getColumnIndex("ProductName"))+" price - "+cursor.getString(cursor.getColumnIndex("Price")), Toast.LENGTH_LONG).show();
                            }
                        }

                    }
                });


            return vi;


        }

}
