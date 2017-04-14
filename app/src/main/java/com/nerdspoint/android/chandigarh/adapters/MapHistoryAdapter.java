package com.nerdspoint.android.chandigarh.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nerdspoint.android.chandigarh.R;
import com.nerdspoint.android.chandigarh.sharedPrefs.ShopDetails;

import java.util.List;

/**
 * Created by android on 4/11/2017.
 */

public class MapHistoryAdapter extends BaseAdapter {

    ShopDetails details;
    List<ShopDetails> list;
    Context context;
    LayoutInflater inflater;

    public MapHistoryAdapter(Context context, List<ShopDetails> list)
    {
        this.list = list;
        this.context= context;

        inflater = (LayoutInflater)context.
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
    public static class VHolder{


        public TextView text1;
        public TextView id;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View vi = convertView;
        VHolder holder;
        if(convertView==null)
        {
            holder= new VHolder();
            vi = inflater.inflate(R.layout.map_history_item,null);

            holder.text1= (TextView) vi.findViewById(R.id.place_name);
            holder.id= (TextView) vi.findViewById(R.id.place_id);

            vi.setTag(holder);

        }
        else
        {
            holder = (MapHistoryAdapter.VHolder) vi.getTag();
        }

        details=null;
        details= list.get(position);

        if(details.shopName!=null)
        {
            holder.text1.setText(details.shopName);
            holder.id.setText(details.ShopID);
        }


        return vi;
    }
}
