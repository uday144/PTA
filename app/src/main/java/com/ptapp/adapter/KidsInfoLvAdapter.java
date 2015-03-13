package com.ptapp.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ptapp.bo.StudentBO;
import com.ptapp.app.R;
import com.ptapp.utils.CommonConstants;

import java.util.ArrayList;

/**
 * Fills the view with the kids info
 */
public class KidsInfoLvAdapter extends BaseAdapter {

    private static final String TAG = "PTAppUI-KidsInfoLvAdapter";
    private static ArrayList<StudentBO> kids;
    private final Context context;
    private LayoutInflater l_Inflater;

    public KidsInfoLvAdapter(Context context, ArrayList<StudentBO> kids) {

        this.kids = kids;
        this.context = context;
        l_Inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if (convertView == null) {
            convertView = l_Inflater.inflate(R.layout.list_item_user, null);

            holder = new ViewHolder();
            holder.admissionNo = (TextView) convertView.findViewById(R.id.admission_no);
            holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tvClass = (TextView) convertView.findViewById(R.id.tv_class);
            holder.tvSchool = (TextView) convertView.findViewById(R.id.tv_school);
            holder.ivPerson = (ImageView) convertView.findViewById(R.id.iv_person);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.admissionNo.setText(kids.get(position).getRollNumber());
        holder.tvName.setText(kids.get(position).getName());
        holder.tvClass.setText(kids.get(position).getStuClass());
        holder.tvSchool.setText(kids.get(position).getSchool());
        if (kids.get(position).getImageUrl().isEmpty()) {
            if (kids.get(position).getGender().equals(CommonConstants.GENDER_FEMALE)) {
                holder.ivPerson.setImageResource(R.drawable.child_female_light_256);
            } else {
                holder.ivPerson.setImageResource(R.drawable.student);
            }

        } else {

            //TODO:show the image which is in URL
        }

        return convertView;
    }

    @Override
    public int getCount() {
        return kids.size();
    }

    @Override
    public Object getItem(int position) {
        return kids.get(position);
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
        TextView tvName, tvClass, tvSchool, admissionNo;
        ImageView ivPerson;
    }
}
