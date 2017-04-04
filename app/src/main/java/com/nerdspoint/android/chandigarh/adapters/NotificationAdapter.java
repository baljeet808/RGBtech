package com.nerdspoint.android.chandigarh.adapters;

import android.app.Service;
import android.content.Context;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.nerdspoint.android.chandigarh.R;
import com.nerdspoint.android.chandigarh.sharedPrefs.NotificatioDetail;

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
        if (list.size() <= 0)
            return 1;
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

                /************  Set holder with LayoutInflater ************/
                vi.setTag( holder );
            }
            else {
                holder = (ViewHolder) vi.getTag();
            }


            if(list.size()<=0)
            {

                holder.text.setText("nerdspoint");
                holder.text1.setText("Welcoming you by RGBTech");
                holder.text2.setText("Recieved");
            }
            else {
                /***** Get each Model object from Arraylist ********/
                detail = null;
                detail =list.get(position);

                /************  Set Model values in Holder elements ***********/

                holder.text1.setText(detail.Message);
                holder.text.setText(detail.ShopName);
                holder.text2.setText(detail.type);


                /******** Set Item Click Listner for LayoutInflater for each row *******/


            }
            return vi;


        }

}
