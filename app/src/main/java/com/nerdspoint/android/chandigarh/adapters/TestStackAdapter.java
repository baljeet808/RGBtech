package com.nerdspoint.android.chandigarh.adapters;

import android.content.Context;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.loopeer.cardstack.CardStackView;
import com.loopeer.cardstack.StackAdapter;
import com.nerdspoint.android.chandigarh.R;
import com.nerdspoint.android.chandigarh.offlineDB.DBHandler;
import com.nerdspoint.android.chandigarh.sharedPrefs.ShopDetails;

import java.util.ArrayList;
import java.util.List;

public class TestStackAdapter extends StackAdapter<Integer> {

    static Context context;

    static  List<ShopDetails> shops;
   static ShopDetails details;

    public TestStackAdapter(Context context) {
        super(context);
        this.context= context;
        shops= new ArrayList<>();
        Cursor cursor= new DBHandler(context).getAll("ShopMasterTable");

        if(cursor.moveToFirst())
        {
            while (!cursor.isAfterLast())
            {
                details = new ShopDetails();
                details.ShopID= cursor.getString(cursor.getColumnIndex("ShopID"));
                details.shopName = cursor.getString(cursor.getColumnIndex("ShopName"));
                details.contactNumber= cursor.getString(cursor.getColumnIndex("ShopContactNo"));
                details.address = cursor.getString(cursor.getColumnIndex("ShopAddress"))+", SCO - "+cursor.getString(cursor.getColumnIndex("SCO"))+", Sector "+cursor.getString(cursor.getColumnIndex("Sector"));
                shops.add(details);
                cursor.moveToNext();
            }
        }

    }

    @Override
    public void bindView(Integer data, int position, CardStackView.ViewHolder holder) {
        if (holder instanceof ColorItemLargeHeaderViewHolder) {
            ColorItemLargeHeaderViewHolder h = (ColorItemLargeHeaderViewHolder) holder;
            h.onBind(data, position);
        }

        if (holder instanceof ColorItemWithNoHeaderViewHolder) {
            ColorItemWithNoHeaderViewHolder h = (ColorItemWithNoHeaderViewHolder) holder;
            h.onBind(data, position);
        }
        if (holder instanceof ColorItemViewHolder) {
            ColorItemViewHolder h = (ColorItemViewHolder) holder;
            h.onBind(data, position);
        }

    }

    @Override
    protected CardStackView.ViewHolder onCreateView(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case R.layout.list_card_item_larger_header:
                view = getLayoutInflater().inflate(R.layout.list_card_item_larger_header, parent, false);
                return new ColorItemLargeHeaderViewHolder(view);
            case R.layout.list_card_item_with_no_header:
                view = getLayoutInflater().inflate(R.layout.list_card_item_with_no_header, parent, false);
                return new ColorItemWithNoHeaderViewHolder(view);
            default:
                view = getLayoutInflater().inflate(R.layout.list_card_item, parent, false);
                return new ColorItemViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {

            return R.layout.list_card_item;

    }

    static class ColorItemViewHolder extends CardStackView.ViewHolder {
        View mLayout;
        View mContainerContent;
        TextView mTextTitle;
        TextView body;
        TextView offer;

        public ColorItemViewHolder(View view) {
            super(view);
            mLayout = view.findViewById(R.id.frame_list_card_item);
            mContainerContent = view.findViewById(R.id.container_list_content);
            mTextTitle = (TextView) view.findViewById(R.id.text_list_card_title);
            body=(TextView) view.findViewById(R.id.body);
            offer = (TextView) view.findViewById(R.id.offer);
            mTextTitle.setTextSize(18);
        }

        @Override
        public void onItemExpand(boolean b) {
            mContainerContent.setVisibility(b ? View.VISIBLE : View.GONE);
        }

        public void onBind(Integer data, int position) {
            details = new ShopDetails();
            details = shops.get(position);

            mLayout.getBackground().setColorFilter(ContextCompat.getColor(getContext(), data), PorterDuff.Mode.SRC_IN);
            mTextTitle.setText(details.shopName+" Offers");
            if(position==1)
            {
                offer.setText("Flat 80% OFF Buy Now");
            }
            else if(position==2)
            {
                offer.setText("BUY Two Get One Free");

            }
            else if(position==3)
            {
                offer.setText("Shop 10000 get %50 Off");
            }
            else if(position== 4)
            {
                offer.setText("Get 30% on Shoes");
            }
            body.setText(" Address > "+details.address+" \n\n Contact number > "+details.contactNumber);
        }

    }

    static class ColorItemWithNoHeaderViewHolder extends CardStackView.ViewHolder {
        View mLayout;
        TextView mTextTitle;
        TextView body;

        public ColorItemWithNoHeaderViewHolder(View view) {
            super(view);
            mLayout = view.findViewById(R.id.frame_list_card_item);
            mTextTitle = (TextView) view.findViewById(R.id.text_list_card_title);

        }

        @Override
        public void onItemExpand(boolean b) {
        }

        public void onBind(Integer data, int position) {
            details = new ShopDetails();
            details = shops.get(position);

            mLayout.getBackground().setColorFilter(ContextCompat.getColor(getContext(), data), PorterDuff.Mode.SRC_IN);
            mTextTitle.setText(details.shopName+" Offers");
            body.setText(" Address > "+details.address+" \n\n Contact number > "+details.contactNumber);
        }

    }

    static class ColorItemLargeHeaderViewHolder extends CardStackView.ViewHolder {
        View mLayout;
        View mContainerContent;
        TextView mTextTitle;
        TextView body;

        public ColorItemLargeHeaderViewHolder(View view) {
            super(view);
            mLayout = view.findViewById(R.id.frame_list_card_item);
            mContainerContent = view.findViewById(R.id.container_list_content);
            mTextTitle = (TextView) view.findViewById(R.id.text_list_card_title);
            body=(TextView) view.findViewById(R.id.text_view);
        }

        @Override
        public void onItemExpand(boolean b) {
            mContainerContent.setVisibility(b ? View.VISIBLE : View.GONE);
        }

        @Override
        protected void onAnimationStateChange(int state, boolean willBeSelect) {
            super.onAnimationStateChange(state, willBeSelect);
            if (state == CardStackView.ANIMATION_STATE_START && willBeSelect) {
                onItemExpand(true);
            }
            if (state == CardStackView.ANIMATION_STATE_END && !willBeSelect) {
                onItemExpand(false);
            }
        }

        public void onBind(Integer data, int position) {
            details = new ShopDetails();
            details = shops.get(position);

            mLayout.getBackground().setColorFilter(ContextCompat.getColor(getContext(), data), PorterDuff.Mode.SRC_IN);
            mTextTitle.setText(details.shopName+" Offers");
            body.setText(" Address > "+details.address+" \n\n Contact number > "+details.contactNumber);
            itemView.findViewById(R.id.text_view).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((CardStackView)itemView.getParent()).performItemClick(ColorItemLargeHeaderViewHolder.this);
                }
            });
        }

    }

}
