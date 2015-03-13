package com.ptapp.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ptapp.adapter.KidsInfoLvAdapter;
import com.ptapp.bo.Role;
import com.ptapp.bo.StudentBO;
import com.ptapp.app.R;
import com.ptapp.utils.CommonConstants;
import com.ptapp.utils.CommonMethods;
import com.ptapp.utils.GcmUtil;

import java.util.ArrayList;
import java.util.List;

public class RegistrationSuccessActivity extends Activity {

    private static final String TAG = "RegistrionSuccessAct";

    private TextView tos_link, tvName, tvClass, tvSchool, tvText1, showMsg, tvText2;
    private CheckBox i_agree;
    private ImageView ivPerson;
    private Boolean isTeacher = false, isParent = false;
    private Role teacher, parent;
    private ProgressBar loading;
    private ListView lvKids;
    private LinearLayout userInfoSection, tosRow, buttonRow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TODO:Image of the teacher or parent should be according to their gender;needed gender info in the api response
        setContentView(R.layout.activity_registration_success);

        tos_link = (TextView) findViewById(R.id.tos_link);
        i_agree = (CheckBox) findViewById(R.id.i_agree);
        tvName = (TextView) findViewById(R.id.tv_name);
        tvClass = (TextView) findViewById(R.id.tv_class);
        tvSchool = (TextView) findViewById(R.id.tv_school);
        tvText1 = (TextView) findViewById(R.id.tv_text1);
        tvText2 = (TextView) findViewById(R.id.tv_text2);
        ivPerson = (ImageView) findViewById(R.id.iv_person);
        loading = (ProgressBar) findViewById(R.id.progress_bar);
        showMsg = (TextView) findViewById(R.id.show_msg);
        lvKids = (ListView) findViewById(R.id.kids_list);
        userInfoSection = (LinearLayout) findViewById(R.id.user_info_section);
        tosRow = (LinearLayout) findViewById(R.id.tos_row);
        buttonRow = (LinearLayout) findViewById(R.id.button_row);

        //up button
        getActionBar().setDisplayHomeAsUpEnabled(true);

        ArrayList<Role> roles = CommonMethods.getAppUserRoles(RegistrationSuccessActivity.this);
        for (Role r : roles) {
            if (r.getRole().equals(CommonConstants.ROLE_STAFF)) {
                isTeacher = true;
                teacher = r;
            } else if (r.getRole().equals(CommonConstants.ROLE_PARENT)) {
                isParent = true;
                parent = r;
            }
        }

        if (isTeacher) {
            //SharedPrefUtil.setPrefUserType(RegistrationSuccessActivity.this, CommonConstants.USER_TYPE_EDUCATOR);
            setTeacherView(teacher);
        } else if ((isTeacher && isParent)) {

            //SharedPrefUtil.setPrefUserType(RegistrationSuccessActivity.this, CommonConstants.USER_TYPE_BOTH);
            setTeacherView(teacher);
        } else if (isParent) {

            //SharedPrefUtil.setPrefUserType(RegistrationSuccessActivity.this, CommonConstants.USER_TYPE_PARENT);
            setParentView(parent);
        } else {

            //SharedPrefUtil.setPrefUserType(RegistrationSuccessActivity.this, CommonConstants.USER_TYPE_STUDENT);
        }
    }


    private void setTeacherView(Role r) {
        tvName.setText(r.getName());
        /*tvClass.setVisibility(View.INVISIBLE);*/
        tvClass.setText(getCommaSeparatedString(r.getClasses()));
        tvSchool.setText(r.getSchool());
        tvText1.setText(R.string.text4);
        if (r.getImageUrl().isEmpty()) {
            if (r.getGender().equals(CommonConstants.GENDER_FEMALE)) {
                ivPerson.setImageResource(R.drawable.teacher);
            } else {
                ivPerson.setImageResource(R.drawable.male_light_icon);
            }
        } else {
            //TODO:show the image in the url
        }

        userInfoSection.setVisibility(View.VISIBLE);
    }

    private void setParentView(Role r) {

        ArrayList<StudentBO> kids = r.getKids();
        if (kids != null) {
            if (kids.size() > 0) {
                lvKids.setAdapter(new KidsInfoLvAdapter(RegistrationSuccessActivity.this, kids));
            }
            lvKids.setVisibility(View.VISIBLE);

            tvText1.setText(R.string.text2);
            //ivPerson.setImageResource(R.drawable.teacher);
        } else {
            if (tvText1 != null) {
                tvText1.setVisibility(View.GONE);
            }
            if (tvText2 != null) {
                tvText2.setVisibility(View.GONE);
            }
            if (tosRow != null) {
                tosRow.setVisibility(View.GONE);
            }
            if (buttonRow != null) {
                buttonRow.setVisibility(View.GONE);
            }
            if (showMsg != null) {
                showMsg.setText(R.string.no_kids);
            }
        }

    }

    public void onClickTosLink(View v) {

        //open the link directly into browser
        String url = CommonConstants.TOS_LINK_APP_INFO_SHARING;
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);

        /*Intent intent = new Intent(RegistrationSuccessActivity.this, OpenTosActivity.class);
        intent.putExtra("linkToS", CommonConstants.TOS_LINK_APP_INFO_SHARING);
        intent.putExtra("activity", RegistrationSuccessActivity.class.getName());
        startActivity(intent);*/
    }

    public void onClickYes(View v) {

        if (i_agree.isChecked()) { //get gcmRegId and register the user at app server, then redirect to the homepage

            try {
                loading.setVisibility(View.VISIBLE);
                GcmUtil gcmUtil = new GcmUtil(RegistrationSuccessActivity.this);
                gcmUtil.registerGCM(RegistrationSuccessActivity.this);
            } catch (Exception ex) {
                Log.w(TAG, "onclick yes error: " + ex.getMessage());
                loading.setVisibility(View.INVISIBLE);
            }
        }
    }

    public void onClickNo(View v) {
        if (isTeacher) {
            showWrongDataDialog(CommonConstants.ROLE_STAFF);
        } else if (isParent) {
            showWrongDataDialog(CommonConstants.ROLE_PARENT);
        } else {
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(RegistrationSuccessActivity.this, OTPActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                Intent intent = new Intent(RegistrationSuccessActivity.this, OTPActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private void showWrongDataDialog(String role) {
        AlertDialog.Builder builder = new AlertDialog.Builder(RegistrationSuccessActivity.this);
        builder.setTitle(R.string.wrong_data_title);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_wrong_data, null);
        final TextView name1 = (TextView) dialogView.findViewById(R.id.name1);
        final TextView class_name1 = (TextView) dialogView.findViewById(R.id.class_name1);
        final TextView school1 = (TextView) dialogView.findViewById(R.id.school1);
        final CheckBox isPictureCorrect = (CheckBox) dialogView.findViewById(R.id.is_picture_correct1);

        if (role.equals(CommonConstants.ROLE_STAFF)) {

            if (teacher != null) {
                name1.setText(teacher.getName());
                String classes = getCommaSeparatedString(teacher.getClasses());
                class_name1.setText(classes);
                school1.setText(teacher.getSchool());
                //TODO:Server needs to send teacher image. If
            }
            builder.setView(dialogView);
            builder.setNegativeButton(R.string.cancel, null);
            builder.setPositiveButton(R.string.submit, null);
            final AlertDialog dialog = builder.create();
            dialog.show();

        } else if (role.equals(CommonConstants.ROLE_PARENT)) {

            if (parent != null) {
                ArrayList<StudentBO> kids = parent.getKids();

                if (kids != null) {
                    name1.setText(kids.get(0).getName());
                    class_name1.setText(kids.get(0).getStuClass());
                    school1.setText(kids.get(0).getSchool());
                    if (kids.get(0).getImageUrl() == null || kids.get(0).getImageUrl() == "") {
                        isPictureCorrect.setVisibility(View.INVISIBLE);
                    } else {
                        isPictureCorrect.setVisibility(View.VISIBLE);
                    }
                    builder.setView(dialogView);
                    builder.setNegativeButton(R.string.cancel, null);
                    builder.setPositiveButton(R.string.submit, null);
                    final AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        }
    }

    private String getCommaSeparatedString(List<String> list) {
        String strCommaSeparated = "";
        if (list != null) {
            for (String str : list) {
                strCommaSeparated += str + ",";
            }
        }
        return strCommaSeparated;
    }
}
