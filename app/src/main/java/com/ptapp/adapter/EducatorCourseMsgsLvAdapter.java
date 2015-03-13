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

import com.ptapp.io.model.Student;
import com.ptapp.dao.MessagesDAO;
import com.ptapp.app.R;

import java.util.ArrayList;

import static com.ptapp.utils.LogUtils.makeLogTag;


public class EducatorCourseMsgsLvAdapter extends BaseAdapter {
    private static final String TAG = makeLogTag(EducatorCourseMsgsLvAdapter.class);
    private ArrayList<Student> students = new ArrayList<Student>();
    private final Context context;
    private LayoutInflater l_Inflater;

    public EducatorCourseMsgsLvAdapter(Context context, ArrayList<Student> students) {

        this.students = students;
        l_Inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public int getCount() {
        return students.size();
    }

    @Override
    public Student getItem(int position) {
        return students.get(position);
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
            holder.txt_itemName = (TextView) convertView.findViewById(R.id.text1);
            holder.txt_itemCount = (TextView) convertView.findViewById(R.id.text2);
            holder.itemImage = (ImageView) convertView.findViewById(R.id.avatar);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        try {
            Student student = getItem(position);

            MessagesDAO daoMsg = new MessagesDAO(context);
            //int numOfMsgs = daoMsg.getCountOfMsgsForMeFrom(SharedPrefUtil.getPrefAppUserId(context), par.getId());
            //Log.i(TAG, "numof msgs: " + numOfMsgs);

            /*holder.txt_itemName.setText(lstParents.get(position).getfName()+" "+lstParents.get(position).getlName());*/
            //holder.txt_itemName.setText(student.getfName() + " " + student.getlName());

            //holder.txt_itemCount.setText(String.valueOf(numOfMsgs));
            /*Picasso.with(context)
                    .load(student.getImgPath())
                            // .resize(64, 64) //
                    .centerInside().fit().placeholder(CommonConstants.LOADING) //
                    .error(CommonConstants.NO_PHOTO_AVAILABLE) //
                    .into(holder.itemImage);*/


            /**
             * Set onClickListener to listen the click event of the Imageview.
             * needed final variable to pass into the onClick method.
             */

            /*final String parId = String.valueOf(lstParents.get(position).getId());*/
            /*final String parId = String.valueOf(par.getId());
            final String stuId = stu.getId();*/

            convertView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {

                    Bundle b = new Bundle();

                    // open the event detail Activity.
                    /*Intent intent = new Intent(context, EducatorSingleMsgActivity.class);*/
                    //intent.putExtra("subject", subject);
                    /*intent.putExtra("userId", parId);
                    intent.putExtra("userType", CommonConstants.USER_TYPE_PARENT);
                    intent.putExtra("studentId", stuId);*/
                    //intent.putExtra(CommonConstants.PROFILE_ID, String.valueOf(prfId));
                    /*context.startActivity(intent);*/
                }
            });
        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
        }
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
