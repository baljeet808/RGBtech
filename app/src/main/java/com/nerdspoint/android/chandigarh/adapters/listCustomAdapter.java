package com.nerdspoint.android.chandigarh.adapters;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nerdspoint.android.chandigarh.R;
import com.nerdspoint.android.chandigarh.sharedPrefs.ShopDetails;

import java.util.List;

public class listCustomAdapter extends BaseAdapter{

    Context context;

    private static LayoutInflater inflater=null;
    List<ShopDetails> list;

    public listCustomAdapter(Activity activity, List<ShopDetails> list) {
        // TODO Auto-generated constructor stub

        context=activity;
        this.list=list;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        View rowView;
        rowView = inflater.inflate(R.layout.cardviewforsearch, null);

        rowView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

            }
        });
        return rowView;
    }

}