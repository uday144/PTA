package com.ptapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

//import com.ptapp.activity.ChatActivity;
import com.ptapp.bean.ClassBean;
/*import com.ptapp.io.model.Course;
import com.ptapp.io.model.Educator;*/
import com.ptapp.bo.StudentContextBO;
import com.ptapp.app.R;
import com.ptapp.utils.SquaredImageView;
import com.squareup.picasso.Picasso;

import static android.widget.ImageView.ScaleType.CENTER_CROP;
import static android.widget.ImageView.ScaleType.FIT_START;

public final class PicassoGvAdapter extends BaseAdapter {

    private static final String TAG = "PTApp-PicassoGvAdapter";
    private final Context context;
    /*private ArrayList<Educator> lst_Educators;
    private ArrayList<Course> lst_Courses;*/
    private ClassBean boClass;
    private String studentId = "";

    public PicassoGvAdapter(Context context, StudentContextBO boStudentContext) {
        this.context = context;

        try {

            /*lst_Educators = boStudentContext.getLstEducators();
            lst_Courses = boStudentContext.getLstCourses();*/
            boClass = boStudentContext.getSchoolClass();
            /*studentId = boStudentContext.getStudent().getId();*/

        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
        }
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        //Educator objEduBO = getItem(position);

        /**
         * The View Holder pattern is about reducing the number of
         * findViewById() calls in the adapter�s getView(). In practice, the
         * View Holder is a lightweight inner class that holds direct references
         * to all inner views from a row. You store it as a tag in the row�s
         * view after inflating it. This way you�ll only have to use
         * findViewById() when you first create the layout.
         */
        ViewHolder holder;

        View myView = convertView;
        if (myView == null) {

            // Fill the gridview cell with the gv_educators_cell layout.
            LayoutInflater li = ((Activity) context).getLayoutInflater();
            myView = li.inflate(R.layout.gv_educators_cell, null);
            /*
             * By default, with FrameLayout, Child views are drawn in a stack,
			 * with the most recently added child on top. Therefore, if we don't
			 * specify here the 'index' parameter for 'addView()', then we end
			 * up the image on the top, which will hide the TextView.
			 */
            FrameLayout fl = (FrameLayout) myView
                    .findViewById(R.id.fl_gvEducator);
            // Educator's profile image.
            SquaredImageView sqImg = new SquaredImageView(context);
            sqImg.setId(1);
            sqImg.setScaleType(CENTER_CROP);

            // sqImg.setPadding(10, 0, 0, 10);
            fl.addView(sqImg, 0);

            // Class Incharge crown image.
            ImageView ivClsIn = new ImageView(context);
            ivClsIn.setId(2);
            ivClsIn.setScaleType(FIT_START);
            ivClsIn.setImageResource(R.drawable.crown);
            fl.addView(ivClsIn, 1);

            // Add the views into ViewHolder, to avoid find them again.
            holder = new ViewHolder();
            holder.sqImgView = (SquaredImageView) myView.findViewById(1);
            holder.tv = (TextView) myView.findViewById(R.id.tv_Course);
            holder.ivClsIn = (ImageView) myView.findViewById(2);

            myView.setTag(holder);
            myView.setPadding(10, 0, 0, 10);
        } else {
            holder = (ViewHolder) myView.getTag();
        }

        // Show the crown image, if Educator is Class Incharge.
        /*if (objEduBO.getId().equals(boClass.getClsInchargeEducatorId())) {
            if (holder.ivClsIn != null) {
                holder.ivClsIn.setVisibility(ImageView.VISIBLE);
            }
        } else {
            if (holder.ivClsIn != null) {
                holder.ivClsIn.setVisibility(ImageView.GONE);
            }
        }*/
        // Set the Educator Id of kid's course teachers
        //holder.educatorId = objEduBO.getId();

        // Set the text for the courses.
        //holder.tv.setText(lst_Courses.get(position).getCourseName().toString());

        int subjPath = R.drawable.nophotoavailable;
        //TODO:Temporary task to generate screenshots
        /*if (lst_Courses.get(position).getCourseName().toString().contains("English")) {
            subjPath = R.drawable.logo_english;
        } else if (lst_Courses.get(position).getCourseName().toString().contains("Math")) {
            subjPath = R.drawable.logo_math;
        } else if (lst_Courses.get(position).getCourseName().toString().contains("Punjabi")) {
            subjPath = R.drawable.course_punjabi;
        } else if (lst_Courses.get(position).getCourseName().toString().contains("Hindi")) {
            subjPath = R.drawable.course_hindi;
        } else if (lst_Courses.get(position).getCourseName().toString().contains("German")) {
            subjPath = R.drawable.course_german;
        } else if (lst_Courses.get(position).getCourseName().toString().contains("Dutch")) {
            subjPath = R.drawable.course_dutch;
        } else if (lst_Courses.get(position).getCourseName().toString().contains("Science")) {
            subjPath = R.drawable.course_science;
        } else if (lst_Courses.get(position).getCourseName().toString().contains("French")) {
            subjPath = R.drawable.course_french;
        }*/


        // Trigger download of the URL asynchronously into imageview.
        /*Picasso.with(context) //
                .load(objEduBO.getImagePath()) //
                .placeholder(R.drawable.placeholder) //
                .error(R.drawable.error) //
                .fit() //
                .into(holder.sqImgView);*/
        Picasso.with(context) //
                .load(subjPath) //
                .placeholder(R.drawable.placeholder) //
                .error(R.drawable.error_image) //
                .fit() //
                .into(holder.sqImgView);

        /**
         * Set onClickListener to listen the click event of the Imageview.
         * needed final variable to pass into the onClick method.
         */
        final String eId = holder.educatorId;
        //final String subject = lst_Courses.get(position).getCourseName();
        holder.sqImgView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // open the Educator Messages Activity.
                /*Intent intent = new Intent(context, ChatActivity.class);
                //intent.putExtra("subject", subject);
                intent.putExtra("userId", eId);
                intent.putExtra("userType", CommonConstants.USER_TYPE_EDUCATOR);*/

                /*intent.putExtra("studentId", studentId);
                intent.putExtra(CommonConstants.PROFILE_ID, String.valueOf(0));*/

                //context.startActivity(intent);
            }
        });

        return myView;
    }

    /**
     * To keep reference of the views as obtained by findViewById(int id). Data
     * is filled by the adapter/getView method corresponding to the position.
     */
    private static class ViewHolder {
        String educatorId;
        public SquaredImageView sqImgView;
        public TextView tv; // for courses name.
        public ImageView ivClsIn;
    }
}
