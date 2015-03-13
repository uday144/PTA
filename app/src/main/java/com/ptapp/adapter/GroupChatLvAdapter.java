package com.ptapp.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ptapp.app.R;

import java.util.ArrayList;

/**
 * Created by lifestyle on 22-12-14.
 */
public class GroupChatLvAdapter extends BaseAdapter {

    private static final String TAG = "PTAppUI-GroupChatLvAdapter";
    private ArrayList<String> msgs;
    private LayoutInflater l_Inflator;
    Context context;

    public GroupChatLvAdapter(Context context, ArrayList<String> msgs) {
        this.msgs = msgs;
        this.context = context;
        l_Inflator = LayoutInflater.from(context);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if (convertView == null) {
            convertView = l_Inflator.inflate(R.layout.list_item_edu_grp_msg, null);
            holder = new ViewHolder();

            holder.tvMsg = (TextView) convertView.findViewById(R.id.msg_txt);
            holder.completeMsg = (LinearLayout) convertView.findViewById(R.id.complete_msg);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvMsg.setText(msgs.get(position));


        if ((position % 2) == 0) {

            //holder.completeMsg.setGravity(Gravity.RIGHT);
            ((FrameLayout.LayoutParams) holder.completeMsg.getLayoutParams()).gravity = Gravity.RIGHT;
            //holder.completeMsg.setPadding(50, 0, 0, 0);
        } else {
            //holder.completeMsg.setGravity(Gravity.LEFT);
            ((FrameLayout.LayoutParams) holder.completeMsg.getLayoutParams()).gravity = Gravity.LEFT;
            //holder.completeMsg.setPadding(0, 0, 50, 0);
        }

        return convertView;
    }

    private static class ViewHolder {
        TextView tvMsg;
        LinearLayout completeMsg;
    }

    @Override
    public int getCount() {
        return msgs.size();
    }

    @Override
    public Object getItem(int pos) {
        return msgs.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

}
