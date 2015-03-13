package com.ptapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

//import com.ptapp.activity.ChatActivity;
import com.ptapp.activity.EducatorChatActivity;
import com.ptapp.bean.ParentBean;
import com.ptapp.app.R;
import com.ptapp.utils.CommonConstants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class ParentsLvAdapter extends BaseAdapter {
    private static final String TAG = "PTApp-ParentsLvAdapter";
    private static ArrayList<ParentBean> lstParents;
    private final Context context;
    private LayoutInflater l_Inflater;
    //private StudentContextBO stuContext;

    public ParentsLvAdapter(Context context,
                            ArrayList<ParentBean> listOfProfiles
    ) {

        lstParents = listOfProfiles;
        l_Inflater = LayoutInflater.from(context);
        this.context = context;
        //this.stuContext = stuContext;
    }

    @Override
    public int getCount() {
        return lstParents.size();
    }

    @Override
    public Object getItem(int position) {
        return lstParents.get(position);
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
            convertView = l_Inflater.inflate(R.layout.activity_parent_listitem,
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
            holder.txt_itemName.setText(lstParents.get(position).getfName());
            //holder.txt_itemCount.setText(lstParents.get(position).getCount());
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

        final String parId = String.valueOf(lstParents.get(position).getId());

        convertView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                Bundle b = new Bundle();

                // open the event detail Activity.
                Intent intent = new Intent(context, EducatorChatActivity.class);
                //intent.putExtra("subject", subject);
                intent.putExtra("userId", parId);
                intent.putExtra("userType", CommonConstants.USER_TYPE_PARENT);
                //intent.putExtra(CommonConstants.PROFILE_ID, String.valueOf(prfId));
                context.startActivity(intent);
            }

        });

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
