package com.ptapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ptapp.activity.HomeActivity;
import com.ptapp.adapter.ChildBarPagerAdapter;
import com.ptapp.bo.StudentBO;
import com.ptapp.bo.StudentContextBO;
import com.ptapp.app.R;
import com.ptapp.utils.CommonConstants;
import com.ptapp.utils.CommonMethods;
import com.ptapp.utils.SharedPrefUtil;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

public final class ChildBarFragment extends Fragment {

    private static final String TAG = "ChildBarFragment";

    ArrayList<StudentBO> lstSBInfo = null;
    StudentContextBO stuContext;

    public ChildBarFragment() {
        lstSBInfo = ChildBarPagerAdapter.lstSBInfo;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstance) {

        /*String activeStuId = "";*/
        int activeStuId = -1;
        // get current/selected student context.

        /*StudentsBO boStudents = ((SchooloApp) getActivity().getApplicationContext()).getStudentsBO();
        if (boStudents != null) {

            stuContext = boStudents.getStudent(
                    (SharedPrefUtil.getPrefLastViewedKidStudentId(getActivity()))).getStudentContextBO(
                    getActivity().getApplicationContext());
        }*/

        View rootView = inflater.inflate(R.layout.fragment_childbar, container, false);
        final ImageView img2 = ((ImageView) rootView.findViewById(R.id.img2));
        try {
            Bundle args = getArguments();
            final int i = args.getInt("position");

            if (lstSBInfo != null) {
                img2.setTag(lstSBInfo.get(i).getStudentId());
                /*if (stuContext != null) {*/

                /*activeStuId = stuContext.getStudent().getId();*/
                activeStuId = SharedPrefUtil.getPrefLastViewedKidStudentId(getActivity());

                if (lstSBInfo.get(i).getStudentId() == activeStuId) {

                    // Drawable d = null;
                    // img2.setImageDrawable(d);

                    Log.i(TAG, "stu Id: " + activeStuId);

                    /*img2.setBackgroundColor(CommonMethods.getChildColor(activeStuId, getActivity()));*/
                    img2.setBackgroundColor(getResources().getColor(R.color.refresh_progress_1));
                    img2.setPadding(1, 1, 1, 1);

                    /*Log.i(TAG, " jassi color: " + CommonMethods.getChildColor(activeStuId, getActivity()));*/
                }
                /*}*/

                try {
                    File f = CommonMethods.getFileImgKidProfile(lstSBInfo.get(i).getRollNumber());
                    if (f.exists()) {
                        Log.v(TAG, "picas:" + f.getPath());
                        // Trigger loading of the URL asynchronously into
                        // imageview.
                        // .fit() cannot be used with resize
                        Picasso.with(getActivity())
                                //
                                .load(f)
                                        // .resize(64, 64) //
                                .centerCrop().fit()
                                .placeholder(CommonConstants.LOADING) //
                                .error(CommonConstants.NO_PHOTO_AVAILABLE) //
                                .into(img2);

                    } else {
                        // get the image from link saved in imgPath column of table.
                        if (lstSBInfo.get(i).getImageUrl().isEmpty()) {
                            Picasso.with(getActivity()) //
                                    .load(CommonConstants.NO_PHOTO_AVAILABLE) //
                                    .placeholder(CommonConstants.LOADING) //
                                    .error(CommonConstants.NO_PHOTO_AVAILABLE) //
                                    .fit() //
                                    .into(img2);
                        } else {
                            Picasso.with(getActivity()) //
                                    .load(lstSBInfo.get(i).getImageUrl()) //
                                    .placeholder(CommonConstants.LOADING) //
                                    .error(CommonConstants.NO_PHOTO_AVAILABLE) //
                                    .fit() //
                                    .into(img2);
                        }
                    }
                } catch (Exception ex) {
                    Log.i(TAG, ex.getMessage());
                }

                // set onclick of image in view pager.
                img2.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Log.d(TAG, "stuId clicked: " + img2.getTag());

                        if (String.valueOf(img2.getTag()).isEmpty()) {
                            Log.i(TAG, "StudId not found.");
                        } else {

                            // set this new studentId in sharedpref.
                            /*((SchooloApp) getActivity().getApplicationContext()).setStudentIdInSharedPref(img2.getTag().toString());*/

                            // start home intent.
                            Intent intent = new Intent(getActivity(),
                                    HomeActivity.class);

                            // to clear the home activity and re-start it
                            // again.
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    }
                });

            } else {
                Log.d(TAG, "No child image in bar found.");
            }

        } catch (Exception ex) {
            if (ex.getMessage() != null) {
                Log.e(TAG, ex.getMessage());
            } else {
                Log.e(TAG, "ChildBar Fragment - No msg for this error.");
            }
        }
        return rootView;
    }
}
