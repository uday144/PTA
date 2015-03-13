package com.ptapp.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ptapp.app.R;

import java.util.ArrayList;

import static com.ptapp.utils.LogUtils.makeLogTag;

/**
 * Created by lifestyle on 30-12-14.
 */
public class SingleChatLvAdapter extends BaseAdapter {

    private static final String TAG = makeLogTag(SingleChatLvAdapter.class);
    private ArrayList<String> msgs;
    private LayoutInflater l_Inflator;
    private Context context;

    public SingleChatLvAdapter(Context context, ArrayList<String> msgs) {
        this.msgs = msgs;
        this.context = context;
        l_Inflator = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if (convertView == null) {
            convertView = l_Inflator.inflate(R.layout.list_item_edu_single_msg, null);
            holder = new ViewHolder();
            holder.txtMsg = (TextView) convertView.findViewById(R.id.msg_txt);
            holder.completeMsg = (FrameLayout) convertView.findViewById(R.id.complete_msg);
            holder.timeTick = (LinearLayout) convertView.findViewById(R.id.tick_time);
            holder.labelContent = (LinearLayout) convertView.findViewById(R.id.label_content);

            if ((position % 2) == 0) {

                //holder.completeMsg.setGravity(Gravity.RIGHT);
                ((FrameLayout.LayoutParams) holder.completeMsg.getLayoutParams()).gravity = Gravity.RIGHT;
                ((FrameLayout.LayoutParams) holder.labelContent.getLayoutParams()).gravity = Gravity.RIGHT;

                RelativeLayout.LayoutParams paramsTime = (RelativeLayout.LayoutParams) holder.timeTick.getLayoutParams();
                paramsTime.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                holder.timeTick.setLayoutParams(paramsTime);

                RelativeLayout.LayoutParams paramsMsg = (RelativeLayout.LayoutParams) holder.txtMsg.getLayoutParams();
                paramsMsg.addRule(RelativeLayout.RIGHT_OF, R.id.tick_time);
                holder.txtMsg.setLayoutParams(paramsMsg);
            } else {
                //holder.completeMsg.setGravity(Gravity.LEFT);
                ((FrameLayout.LayoutParams) holder.completeMsg.getLayoutParams()).gravity = Gravity.LEFT;
                /*((FrameLayout
                        .LayoutParams) holder.labelContent.getLayoutParams()).gravity = Gravity.LEFT;*/
                holder.labelContent.setVisibility(View.INVISIBLE);

                RelativeLayout.LayoutParams paramsTime = (RelativeLayout.LayoutParams) holder.timeTick.getLayoutParams();
                paramsTime.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                holder.timeTick.setLayoutParams(paramsTime);

                RelativeLayout.LayoutParams paramsMsg = (RelativeLayout.LayoutParams) holder.txtMsg.getLayoutParams();
                paramsMsg.addRule(RelativeLayout.LEFT_OF, R.id.tick_time);
                holder.txtMsg.setLayoutParams(paramsMsg);

                //holder.completeMsg.setPadding(0, 0, 50, 0);
            }

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtMsg.setText(msgs.get(position));


        return convertView;
    }

    private static class ViewHolder {
        TextView txtMsg;
        FrameLayout completeMsg;
        LinearLayout timeTick;
        LinearLayout labelContent;
    }


    @Override
    public int getCount() {
        return msgs.size();
    }

    @Override
    public Object getItem(int position) {
        return msgs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
