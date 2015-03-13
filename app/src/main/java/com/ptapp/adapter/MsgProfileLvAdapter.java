package com.ptapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;



import com.ptapp.bean.MsgProfileBean;

import com.ptapp.app.R;
import com.ptapp.utils.CommonConstants;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;


public class MsgProfileLvAdapter extends BaseAdapter {
    private static final String TAG = "PTAppUI-MsgProfileLvAdapter";
    private static ArrayList<MsgProfileBean> lstPrfs;
    private final Context context;
    private LayoutInflater l_Inflater;
    //private StudentContextBO stuContext;

    public MsgProfileLvAdapter(Context context,
                               ArrayList<MsgProfileBean> listOfProfiles
    ) {

        lstPrfs = listOfProfiles;
        l_Inflater = LayoutInflater.from(context);
        this.context = context;
        //this.stuContext = stuContext;
    }

    @Override
    public int getCount() {
        return lstPrfs.size();
    }

    @Override
    public Object getItem(int position) {
        return lstPrfs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("NewApi")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = l_Inflater.inflate(R.layout.activity_main_chat_listitem,
                    null);

            holder = new ViewHolder();
            holder.txt_itemName = (TextView) convertView
                    .findViewById(R.id.text1);
            holder.txt_itemCount = (TextView) convertView
                    .findViewById(R.id.text2);
            holder.itemImage = (ImageView) convertView.findViewById(R.id.avatar);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        try {
            holder.txt_itemName.setText(lstPrfs.get(position).getName());
            holder.txt_itemCount.setText(lstPrfs.get(position).getCount());
            Picasso.with(context)
                    //
                    .load(CommonConstants.NO_PHOTO_AVAILABLE)
                            // .resize(64, 64) //
                    .centerInside().fit().placeholder(CommonConstants.LOADING) //
                    .error(CommonConstants.NO_PHOTO_AVAILABLE) //
                    .into(holder.itemImage);

        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
        }
        /**
         * Set onClickListener to listen the click event of the Imageview.
         * needed final variable to pass into the onClick method.
         */

        final String prfId = String.valueOf(lstPrfs.get(position).getId());

        convertView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                Bundle b = new Bundle();
                //b.putInt("eventMonth", eventTime.get(Calendar.MONTH));
                //b.putInt("eventYear", eventTime.get(Calendar.YEAR));

                // open the event detail Activity.
                //Intent intent = new Intent(context, ChatActivity.class);
                //intent.putExtra(CommonConstants.PROFILE_ID, String.valueOf(prfId));
                //intent.putExtra("ChatId", chatId);
                // intent.putExtra("serverEventId", serverEventId);
                // Log.v(TAG, "serverEventId tapped: " + serverEventId);
                //context.startActivity(intent);
            }

        });

//		convertView.setOnTouchListener(new OnTouchListener() {
//
//			@Override
//			public boolean onTouch(View view, MotionEvent event) {
//				switch (event.getAction()) {
//				case MotionEvent.ACTION_DOWN:
//					Log.d("tagcheck", "acton down");
//					view.setBackgroundColor(color.holo_blue_bright);
//					return true;
//				case MotionEvent.ACTION_UP:
//					Log.d("tagcheck", "acton up");
//					view.setBackgroundColor(color.transparent);
//					return true;
//				default:
//					view.performClick();
//					return true;
//				}
//
//				
//			}
//
//		});

        return convertView;
    }

    /**
     * To keep reference of the views as obtained by findViewById(int id). Data
     * is filled by the adapter/getView method corresponding to the position.
     */
    private static class ViewHolder {
        TextView txt_itemName;
        TextView txt_itemCount;
        ImageView itemImage;

    }

}
