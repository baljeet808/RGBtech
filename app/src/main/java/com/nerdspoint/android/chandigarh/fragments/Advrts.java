package com.nerdspoint.android.chandigarh.fragments;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.loopeer.cardstack.CardStackView;
import com.nerdspoint.android.chandigarh.R;
import com.nerdspoint.android.chandigarh.adapters.TestStackAdapter;
import com.nerdspoint.android.chandigarh.offlineDB.DBHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class Advrts extends Fragment implements CardStackView.ItemExpendListener {





    public static Integer[] TEST_DATAS = new Integer[]{
            R.color.color_1,
            R.color.color_2,
            R.color.color_3,
            R.color.color_4,
            R.color.color_5,
            R.color.color_6,
            R.color.color_7,
            R.color.color_8,
            R.color.color_9,
            R.color.color_10,
            R.color.color_11,
            R.color.color_12,
            R.color.color_13,
            R.color.color_14,
            R.color.color_15,
            R.color.color_16,
            R.color.color_17,
            R.color.color_18,
            R.color.color_19,
            R.color.color_20,
            R.color.color_21,
            R.color.color_22,
            R.color.color_23,
            R.color.color_24,
            R.color.color_25,
            R.color.color_26
    };


    public static List<Integer> Colors;
    CardStackView mStackView;
    TestStackAdapter mTestStackAdapter;


    public Advrts() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_advrts, container, false);

        Colors= new ArrayList<>();

        Cursor cursor= new DBHandler(getContext()).getAll("ShopMasterTable");
        int i =0;
        if(cursor.moveToFirst())
        {
            while(!cursor.isAfterLast())
            {
                Colors.add(TEST_DATAS[i]);
                i++;
                cursor.moveToNext();
            }
        }

        mStackView = (CardStackView) view.findViewById(R.id.stackview_main);
        mTestStackAdapter = new TestStackAdapter(getActivity());
        mStackView.setAdapter(mTestStackAdapter);
        mStackView.setItemExpendListener(this);
        mTestStackAdapter.updateData(Colors);


        return view;
    }

    @Override
    public void onItemExpend(boolean expend) {
    }
}
