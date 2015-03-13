package com.ptapp.activity;

import android.app.ActionBar;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;


import com.ptapp.bean.MessageBean;
import com.ptapp.bo.StudentContextBO;
import com.ptapp.bo.StudentsBO;
import com.ptapp.dao.EducatorDAO;
import com.ptapp.dao.MessageDAO;
import com.ptapp.dao.MessagesDAO;
import com.ptapp.dao.ParentDAO;
import com.ptapp.app.R;
import com.ptapp.app.SchooloApp;
import com.ptapp.utils.CommonConstants;
import com.ptapp.utils.GcmUtil;
import com.ptapp.utils.SharedPrefUtil;

/**
 * opens on click of Educator images in the gv.
 */

public class SubjectMsgsActivity extends BaseActivity {
    private static String TAG = "PTApp-EducatorMsgs ";

    TextView txt_name_value, txt_email_value, txt_phone_value, txt_type_value;
    EditText et_txt_Msg;
    ImageView iv_upDown;
    ScrollView sv_chat;
    LinearLayout demographics;
    RelativeLayout msgBox;

    private String educatorId = "";
    private String studentId = "";

    private StudentsBO boStudents;
    private StudentContextBO stuContext;

    //get screen height without actionbar
    static int ScreenWidth;
    static int ScreenHeight;
    static int ScreenWidthLandscape;
    static int ScreenHeightLandscape;

    private RelativeLayout layout;
    private RelativeLayout.LayoutParams params;
    private View top;
    private View bottom;

    private GcmUtil gcmUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_msgs);

        findViews();
        iv_upDown.setImageResource(R.drawable.navigate_down);
        demographics.setVisibility(View.GONE);

        //registerHideableHeaderView(demographics);
        //registerScrollUpView(msgBox);

        if (getIntent() != null) {

            educatorId = getIntent().getStringExtra("educatorId");
            String subject = getIntent().getStringExtra("subject");

            boStudents = ((SchooloApp) getApplicationContext()).getStudentsBO();
            if (boStudents != null) {
                stuContext = boStudents.getStudent(
                        (SharedPrefUtil.getPrefLastViewedKidStudentId(SubjectMsgsActivity.this))).getStudentContextBO(
                        getApplicationContext());

                if (stuContext != null) {

                    //studentId = stuContext.getStudent().getId();

                    customActionBar(subject);

                    showMessages();
                    Log.v(TAG, "height: " + msgBox.getHeight());
                    Log.v(TAG, "measured height: " + msgBox.getMeasuredHeight());
                    Log.v(TAG, "measured height and state: " + msgBox.getMeasuredHeightAndState());
                    Log.v(TAG, "action bar height: " + getActionBar().getHeight());
                }
            }


        }

        try {
            // Get Educator Details
            EducatorDAO dcEdu = new EducatorDAO(SubjectMsgsActivity.this);
            /*Educator edu = dcEdu.getEducator(educatorId);
            if (edu != null) {
                // fill the text values
                txt_name_value.setText(edu.getfName() + " " + edu.getlName());
                txt_email_value.setText(edu.getEmail());
                txt_phone_value.setText(edu.getPhone());
                txt_type_value.setText(edu.getType());
            } else {
                Log.d(TAG, "No Educator details found.");
            }*/
        } catch (Exception ex) {
            if (ex.getMessage() != null) {
                Log.e(TAG, ex.getMessage());
            } else {
                Log.e(TAG, "Educator Details, no error msg returned.");
            }
        }

        // MessageDAO dcMsg = new MessageDAO(EducatorMsgsActivity.this);
        // ArrayList<MessageBean> lst_boMsgs = dcMsg.getStudentEducatorChat(
        // studentId, educatorId);

        registerReceiver(registrationStatusReceiver, new IntentFilter(CommonConstants.ACTION_REGISTER));
        gcmUtil = new GcmUtil(getApplicationContext());
    }

    private BroadcastReceiver registrationStatusReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.v(TAG, "in gcm broadcast Receiver");
            Log.v(TAG, "intent.getAction(): " + intent.getAction());
            Log.v(TAG, "extras: " + intent.getExtras());

            if (intent != null && CommonConstants.ACTION_REGISTER.equals(intent.getAction())) {
                /*switch (intent.getIntExtra(Common.EXTRA_STATUS, 100)) {
                    case Common.STATUS_SUCCESS:
                        getActionBar().setSubtitle("online");
                        break;

                    case Common.STATUS_FAILED:
                        getActionBar().setSubtitle("offline");
                        break;
                }*/
            }
        }
    };

    public void onClickToggleDemographics(View v) {
        if (demographics.isShown()) {
            demographics.setVisibility(View.GONE);
            iv_upDown.setImageResource(R.drawable.navigate_down);
        } else {
            demographics.setVisibility(View.VISIBLE);
            iv_upDown.setImageResource(R.drawable.navigate_up);
        }
    }

    /**
     * Show messages.
     */
    private void showMessages() {

        TextView tv_noMsgs = (TextView) findViewById(R.id.tv_no_events);

        // Getting list of current month events, from app's db.
        MessagesDAO daoMsg = new MessagesDAO(SubjectMsgsActivity.this);

        /*final ArrayList<MessagesBean> lstMsgs = daoMsg.getMessages(educatorId);

        Log.v(TAG, "number of messages:" + lstMsgs.size());

        if (lstMsgs.size() > 0) {

            // Setting list-view adapter.
            final ListView lv1 = (ListView) findViewById(R.id.listV_main);
            lv1.setAdapter(new MessagesLvAdapter(SubjectMsgsActivity.this,
                    lstMsgs, stuContext));

            tv_noMsgs.setText(null); // Set "no events found" text null.
            tv_noMsgs.setVisibility(View.GONE);

        } else {
            tv_noMsgs.setText(R.string.no_messages_found);
            tv_noMsgs.setVisibility(View.VISIBLE);
        }*/
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        //CollectionView collectionView = (CollectionView) findViewById(R.id.sessions_collection_view);
        /*ScrollView msgView = (ScrollView) findViewById(R.id.sv_chat); */
        ListView lvEve = (ListView) findViewById(R.id.listV_main);
        if (lvEve != null) {
            enableActionBarAutoHide(lvEve);
        }


        //registerHideableHeaderView(findViewById(R.id.headerbar));
    }

    private void findViews() {
        txt_name_value = (TextView) findViewById(R.id.txt_name_value);
        txt_email_value = (TextView) findViewById(R.id.txt_email_value);
        txt_phone_value = (TextView) findViewById(R.id.txt_phone_value);
        txt_type_value = (TextView) findViewById(R.id.txt_type_value);
        et_txt_Msg = (EditText) findViewById(R.id.et_txt_Msg);
        demographics = (LinearLayout) findViewById(R.id.ll_demographics);
        msgBox = (RelativeLayout) findViewById(R.id.rl_msg);
        iv_upDown = (ImageView) findViewById(R.id.iv_upDown);
//		sv_chat = (ScrollView) findViewById(R.id.sv_chat);
    }

    private void customActionBar(String subject) {
        ActionBar ab = getActionBar();
        if (ab != null) {

            // add the custom view to the action bar
            ab.setCustomView(R.layout.ab_subject_msgs);
            ab.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM
                    | ActionBar.DISPLAY_SHOW_HOME
                    | ActionBar.DISPLAY_HOME_AS_UP);

            // show subject icon
            if (subject.equalsIgnoreCase("math")) {
                // ab.setLogo was not working on ICS
                ab.setIcon(R.drawable.logo_math);
            } else if (subject.equalsIgnoreCase("english")) {
                ab.setIcon(R.drawable.logo_english);
            }

            // change the label
            TextView txt_Title = (TextView) ab.getCustomView().findViewById(
                    R.id.txt_Title);
            txt_Title.setText(subject);

            // show child image
            try {
                ImageView img2 = (ImageView) ab.getCustomView().findViewById(
                        R.id.img2);
                /*File f = CommonMethods.getFileImgKidProfile(stuContext
                        .getStudent().getRollNumber());

                if (f.exists()) {

                    Log.v(TAG, "picas:" + f.getPath());
                    Picasso.with(SubjectMsgsActivity.this).load(f).centerCrop()
                            .fit().placeholder(CommonConstants.LOADING)
                            .error(CommonConstants.NO_PHOTO_AVAILABLE) //
                            .into(img2);

                } else {
                    // get the image from link saved in imgPath column of table.
                    Picasso.with(SubjectMsgsActivity.this)
                            .load(stuContext.getStudent().getImgPath()) //
                            .placeholder(CommonConstants.LOADING) //
                            .error(CommonConstants.NO_PHOTO_AVAILABLE) //
                            .fit() //
                            .into(img2);
                }*/

                // on click of this image
                img2.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        goToHomeScreen();
                        finish();
                    }

                });
            } catch (Exception ex) {
                Log.i(TAG, ex.getMessage());
            }

        }
    }

    public void onClickSendChat(View view) {
        try {
            Log.v(TAG, "send chat clicked.");
            SharedPreferences sharedPref = PreferenceManager
                    .getDefaultSharedPreferences(this);
            String sPref_gcalEmail = sharedPref.getString(
                    "pref_txt_EmailAccount", "");
            Log.i(TAG, "app's stored email: " + sPref_gcalEmail);

            ParentDAO dcParent = new ParentDAO(SubjectMsgsActivity.this);
            //ParentBean pb = dcParent.getParentByEmailId(sPref_gcalEmail);

            Log.v(TAG, "msg: " + et_txt_Msg.getText().toString());

            MessageBean msg = new MessageBean();
            //msg.setParentId(pb.getId());
            msg.setEducatorId(educatorId);
            msg.setStudentId(studentId);
            msg.setNeedPush("true");
            msg.setMsgTxt(et_txt_Msg.getText().toString());
            // msg.setSendTimestamp(sendTimestamp)

            MessageDAO dcMsg = new MessageDAO(SubjectMsgsActivity.this);
            //dcMsg.saveSingleMessage(msg);

            TextView tv = new TextView(SubjectMsgsActivity.this);
            tv.setText(et_txt_Msg.getText().toString());

            sv_chat.addView(tv);

        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                // app icon in action bar clicked; go home
                goToHomeScreen();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void goToHomeScreen() {
        Intent intentHome = new Intent(SubjectMsgsActivity.this,
                HomeActivity.class);
        intentHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intentHome);
    }
}
