package com.ptapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ptapp.bean.MessagesBean;
//import com.ptapp.bo.StudentContextBO;
import com.ptapp.bo.StudentContextBO;

import com.ptapp.app.SchooloApp;
import com.ptapp.utils.CommonConstants;
import com.ptapp.app.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MessagesLvAdapter extends BaseAdapter {
    private static final String TAG = "PTAppUI-MessagesLvAdapter";
    private static ArrayList<MessagesBean> lstMsgs;
    private final Context context;
    private LayoutInflater l_Inflater;
    //private StudentContextBO stuContext;

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final DateFormat[] df = new DateFormat[]{
            DateFormat.getDateInstance(), DateFormat.getTimeInstance()};
    private Date now;

    public MessagesLvAdapter(Context context,
                             ArrayList<MessagesBean> listOfMessages,
                             StudentContextBO stuContext
    ) {

        lstMsgs = listOfMessages;
        l_Inflater = LayoutInflater.from(context);
        this.context = context;
        //this.stuContext = stuContext;
    }


    @SuppressLint("NewApi")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        now = new Date();
        String from = lstMsgs.get(position).getFromCID();
        String to = lstMsgs.get(position).getToCID();

        if (convertView == null) {
            convertView = l_Inflater.inflate(R.layout.activity_chat_listitem,
                    null);

            holder = new ViewHolder();
            holder.txt_msg = (TextView) convertView
                    .findViewById(R.id.text1);
            holder.txt_at = (TextView) convertView
                    .findViewById(R.id.text2);
            holder.txt_from = (TextView) convertView
                    .findViewById(R.id.text3);
            holder.iv_status = (ImageView) convertView
                    .findViewById(R.id.ivMsgStatus);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txt_msg.setText(lstMsgs.get(position).getMsgText());
        holder.txt_at.setText(getDisplayTime(lstMsgs.get(position).getAt()));
        holder.txt_from.setText(from + ":");
        if (lstMsgs.get(position).getStatus() != null) {
            if (lstMsgs.get(position).getStatus().equals(CommonConstants.STATUS_FIRST_TICK)) {
                holder.iv_status.setImageResource(R.drawable.single_check);
            } else if (lstMsgs.get(position).getStatus().equals(CommonConstants.STATUS_SECOND_TICK)) {
                holder.iv_status.setImageResource(R.drawable.double_check);
            }
        } else {
            holder.iv_status.setImageResource(R.drawable.load);
        }


        //set msg right or left
        LinearLayout root = (LinearLayout) holder.txt_msg.getParent().getParent();
        if (from == null) { //sent from this device
            root.setGravity(Gravity.RIGHT);
            root.setPadding(50, 10, 10, 10);
            holder.iv_status.setVisibility(View.VISIBLE);
        } else {    //received from other device
            root.setGravity(Gravity.LEFT);
            root.setPadding(10, 10, 50, 10);
            holder.iv_status.setVisibility(View.GONE);
        }

        //set visibility of from
        if (from == null || SchooloApp.getCurrentChat().equals(from)) //myself or contact
            holder.txt_from.setVisibility(View.GONE);
        else
            holder.txt_from.setVisibility(View.VISIBLE);

        return convertView;
    }

    @Override
    public int getCount() {
        return lstMsgs.size();
    }

    @Override
    public Object getItem(int position) {
        return lstMsgs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    /**
     * To keep reference of the views as obtained by findViewById(int id). Data
     * is filled by the adapter/getView method corresponding to the position.
     */
    private static class ViewHolder {
        TextView txt_msg;
        TextView txt_at;
        TextView txt_from;
        ImageView iv_status;
    }

    private String getDisplayTime(String datetime) {
        //Log.v(TAG, "Datetime: " + datetime);
        String d = "";
        DateFormat writeFormat = new SimpleDateFormat("HH:mm");

        try {
            if (datetime != null) {
                Date dt = sdf.parse(datetime);
                d = writeFormat.format(dt);
                /*if (now.getYear() == dt.getYear() && now.getMonth() == dt.getMonth() && now.getDate() == dt.getDate()) {
                    d = df[1].format(dt);
                } else {
                    d = df[0].format(dt);
                }*/
            }
        } catch (ParseException e) {
            Log.v(TAG, e.getMessage());
        }
        return d;
    }

}
