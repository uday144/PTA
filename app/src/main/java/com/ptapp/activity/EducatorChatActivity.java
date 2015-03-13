package com.ptapp.activity;

import android.app.ActionBar;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ptapp.bean.MessagesBean;
import com.ptapp.bean.ParentBean;
import com.ptapp.dao.EducatorDAO;
import com.ptapp.dao.MessagesDAO;
import com.ptapp.dao.ParentDAO;
import com.ptapp.dao.StudentDAO;

import com.ptapp.io.model.Student;
import com.ptapp.app.R;
import com.ptapp.app.SchooloApp;
import com.ptapp.utils.ApiMethodsUtils;
import com.ptapp.utils.CommonConstants;
import com.ptapp.utils.SharedPrefUtil;
import com.ptapp.widget.DrawShadowFrameLayout;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class EducatorChatActivity extends BaseActivity {
    private static final String TAG = "PTAppUI-EducatorChatActivity";

    private String userId = ""; //this can be educatorId or parentId depends upon the user_type;
    // at some places it might be referred as chatId
    private String userType = "";
    //String subject = "";
    private String stuId = "";  //studentId
    private Student student = null;

    private String name = "";
    private String email = "";
    private String phone = "";
    private String imgUrl = "";

    //private StudentsBO boStudents;
    //private StudentContextBO stuContext;


    //private ArrayList<CourseBean> lst_Courses = null;
    private ArrayList<ParentBean> lst_Parents = null;

    LinearLayout demographics;
    private EditText msgEdit;
    ImageView iv_upDown;
    TextView txt_name_value, txt_email_value, txt_phone_value, txt_type_value;
    RelativeLayout msgBox;
    ListView lv1;

    private DrawShadowFrameLayout mDrawShadowFrameLayout;
    private int mMode = MODE_EXPLORE;
    // How is this Activity being used?
    private static final int MODE_EXPLORE = 0; // as top-level "Explore" screen

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_educator_chat);

        findViews();
        mDrawShadowFrameLayout = (DrawShadowFrameLayout) findViewById(R.id.main_content);
        iv_upDown.setImageResource(R.drawable.navigate_down);
        demographics.setVisibility(View.GONE);

        if (getIntent() != null) {
            userId = getIntent().getStringExtra("userId");
            userType = getIntent().getStringExtra("userType");
            stuId = getIntent().getStringExtra("studentId");
            //subject = getIntent().getStringExtra("subject");
            loadContent();
        }


    }

    private void loadContent() {
        if (!userType.isEmpty() && !userId.isEmpty()) {
            if (userType.equals(CommonConstants.USER_TYPE_PARENT)) {
                try {
                    // Get Parent Details -
                    ParentDAO dcPar = new ParentDAO(EducatorChatActivity.this);
                    /*ParentBean par = dcPar.getParent(userId);
                    if (par != null) {
                        // fill the text values
                        txt_name_value.setText(par.getfName() + " " + par.getlName());
                        txt_email_value.setText(par.getEmail());
                        txt_phone_value.setText(par.getPhone());
                        //txt_type_value.setText(edu.getType());
                        name = par.getfName() + " " + par.getlName();
                        email = par.getEmail();
                        phone = par.getPhone();
                        imgUrl = par.getImgPath();
                    } else {
                        Log.d(TAG, "No Parent details found.");
                    }*/
                } catch (Exception ex) {
                    if (ex.getMessage() != null) {
                        Log.e(TAG, ex.getMessage());
                    } else {
                        Log.e(TAG, "Parent Details, no error msg returned.");
                    }
                }
            }


            StudentDAO daoStu = new StudentDAO(EducatorChatActivity.this);
            /*student = daoStu.getStudent(stuId);
            if (student != null) {
                customActionBar();
                showMessages(userId);

                Log.v(TAG, "sss: userId:" + userId + " usertype: " + userType);

                //process navdrawer
                if (userType.equals(CommonConstants.USER_TYPE_PARENT)) {
                    ParentDAO daoParent = new ParentDAO(EducatorChatActivity.this);
                    lst_Parents = daoParent.getParents();

                    Log.v(TAG, "num of parents:" + lst_Parents.size());
                }
            }*/
        }
    }

    /**
     * Show messages.
     */
    private void showMessages(String chatId) {

        //TextView tv_noMsgs = (TextView) findViewById(R.id.tv_no_events);

        MessagesDAO daoMsg = new MessagesDAO(EducatorChatActivity.this);
        /*final ArrayList<MessagesBean> lstMsgs = daoMsg.getMessages(chatId, EducatorChatActivity.this);

        if (lstMsgs.size() > 0) {
            lv1.setAdapter(new MessagesLvAdapter(EducatorChatActivity.this, lstMsgs, null));

            //tv_noMsgs.setText(null); // Set "no events found" text null.
            //tv_noMsgs.setVisibility(View.GONE);
        } else {
            try {
                lv1.setAdapter(null);
                Log.v(TAG, "num of msgs -:" + lstMsgs.size());
                //tv_noMsgs.setText(R.string.no_messages_found);
                //tv_noMsgs.setVisibility(View.VISIBLE);
            } catch (Exception ex) {
                Log.e(TAG, ex.getMessage());
            }
        }
*/
    }


    private void customActionBar() {
        ActionBar ab = getActionBar();
        if (ab != null) {

            // add the custom view to the action bar
            ab.setCustomView(R.layout.ab_subject_msgs);
            ab.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM
                    | ActionBar.DISPLAY_SHOW_HOME
                    | ActionBar.DISPLAY_HOME_AS_UP);

            // change the label
            TextView txt_Title = (TextView) ab.getCustomView().findViewById(
                    R.id.txt_Title);
            txt_Title.setText("Teacher");

            // show child image
            try {
                ImageView img2 = (ImageView) ab.getCustomView().findViewById(
                        R.id.img2);
                /*File f = CommonMethods.getFileImgKidProfile(student.getRollNumber());

                if (f.exists()) {

                    Log.v(TAG, "picas:" + f.getPath());
                    Picasso.with(EducatorChatActivity.this).load(f).centerCrop()
                            .fit().placeholder(CommonConstants.LOADING)
                            .error(CommonConstants.NO_PHOTO_AVAILABLE) //
                            .into(img2);

                } else {
                    // get the image from link saved in imgPath column of table.
                    Picasso.with(EducatorChatActivity.this)
                            .load(student.getImgPath()) //
                            .placeholder(CommonConstants.LOADING) //
                            .error(CommonConstants.NO_PHOTO_AVAILABLE) //
                            .fit() //
                            .into(img2);
                }
*/
                // on click of this image
                img2.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        //goToHomeScreen();
                        //finish();
                    }

                });
            } catch (Exception ex) {
                Log.i(TAG, ex.getMessage());
            }

        }
    }


    public void onClickToggleDemographics(View v) {
        if (demographics.isShown()) {
            demographics.setVisibility(View.GONE);
            iv_upDown.setImageResource(R.drawable.navigate_down);
        } else {
            demographics.setVisibility(View.VISIBLE);
            iv_upDown.setImageResource(R.drawable.navigate_up);
        }
    }

    // On click of Calender box.
    public void onClickSend(View view) {
        String msg = msgEdit.getText().toString();
        if (!TextUtils.isEmpty(msg)) {
            //save msg to db
            MessagesBean mb = new MessagesBean();
            mb.setMsgText(msg);
            mb.setToCID(userId);
            MessagesDAO daoMsg = new MessagesDAO(EducatorChatActivity.this);
            /*long msgId = daoMsg.addMessage(mb);

            if (msgId == 0) { //msg could not be stored in database.
                //TODO: need to handle this
                Log.w(TAG, "Error: Message could not be stored in the message table.");
            } else {
                send(msg, String.valueOf(msgId)); //msgId needed for status
                msgEdit.setText(null);

            }*/

        }
    }

    private void send(final String msg, final String msgId) {
        new AsyncTask<Void, Void, String>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                showMessages(userId);
                Log.i(TAG, "text saved and shown in chat, sending to app server...");
            }

            @Override
            protected String doInBackground(Void... params) {
                String res = "";
                try {
                    /*res = ServerUtilities.send(msgId,
                            msg,
                            userId,
                            userType,
                            SharedPrefUtil.getPrefUserType(ChatActivity.this),
                            ChatActivity.this);*/

                    res = ApiMethodsUtils.sendMsg(getResources().getString(
                                    R.string.first_part_api_link)
                                    + "/" + msgId,
                            userId,
                            msg,
                            SharedPrefUtil.getPrefAppUserId(EducatorChatActivity.this),
                            "0",
                            "");

                } catch (Exception ex) {
                    res = ex.getMessage();
                }
                return res;
            }

            @Override
            protected void onPostExecute(String res) {
                if (!TextUtils.isEmpty(res)) {
                    Log.i(TAG,"response send msg: "+res);
                    Toast.makeText(getApplicationContext(), res, Toast.LENGTH_LONG).show();

                }
            }
        }.execute(null, null, null);
    }


    private void findViews() {
        txt_name_value = (TextView) findViewById(R.id.txt_name_value);
        txt_email_value = (TextView) findViewById(R.id.txt_email_value);
        txt_phone_value = (TextView) findViewById(R.id.txt_phone_value);
        txt_type_value = (TextView) findViewById(R.id.txt_type_value);

        demographics = (LinearLayout) findViewById(R.id.ll_demographics);
        msgEdit = (EditText) findViewById(R.id.msg_edit);
        msgBox = (RelativeLayout) findViewById(R.id.rl_msg);
        iv_upDown = (ImageView) findViewById(R.id.iv_upDown);
        lv1 = (ListView) findViewById(R.id.listChat);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.educator_chat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        Log.v(TAG, "chat on pause");
        if (userType.equals(CommonConstants.USER_TYPE_PARENT)) {
            //search in Parent table
            ParentDAO daoPar = new ParentDAO(EducatorChatActivity.this);
            /*int count = daoPar.getParent(userId).getMsgCount();
            daoPar.updateMsgCount(count + 1, userId);*/
        } else if (userType.equals(CommonConstants.USER_TYPE_EDUCATOR)) {
            //search in Educator table
            EducatorDAO daoEdu = new EducatorDAO(EducatorChatActivity.this);
            /*int count = daoEdu.getEducator(userId).getMsgCount();
            daoEdu.updateMsgCount(count + 1, userId);*/
        }
        /*MsgProfileDAO daoMsg = new MsgProfileDAO(ChatActivity.this);
        daoMsg.updateMsgCount(0, profileId);*/

        SchooloApp.setCurrentChat(null);
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v(TAG, "chat on resume");
        SchooloApp.setCurrentChat(userId);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        showMessages(userId);
    }


    //Nav drawer
    @Override
    protected int getSelfNavDrawerItem() {
        Log.v(TAG, "..into getSelfNavDrawerItem");
        // we only have a nav drawer if we are in top-level Explore mode.
        return mMode == MODE_EXPLORE ? NAVDRAWER_ITEM_EXPLORE : NAVDRAWER_ITEM_INVALID;
    }

    @Override
    protected void createNavDrawerItems() {
        Log.v(TAG, "..into createNavDrawerItems");
        mDrawerItemsListContainer = (ViewGroup) findViewById(R.id.navdrawer_items_list);
        if (mDrawerItemsListContainer == null) {
            return;
        }

        mDrawerItemsListContainer.removeAllViews();
        if (userType.equals(CommonConstants.USER_TYPE_PARENT)) {
            mNavDrawerItemViews = new View[lst_Parents.size()];
            int i = 0;
            for (ParentBean item : lst_Parents) {
                //mNavDrawerItemViews[i] = makeNavDrawerItem(item, mDrawerItemsListContainer);
                //mDrawerItemsListContainer.addView(mNavDrawerItemViews[i]);
                ++i;
            }
        }
    }


    /*private View makeNavDrawerItem(final Course bean, ViewGroup container) {
        Log.v(TAG, "..into makeNavDrawerItem");
        //boolean selected = getSelfNavDrawerItem() == itemId;

        int itemId = 555;
        int layoutToInflate = 0;
        if (itemId == NAVDRAWER_ITEM_SEPARATOR) {
            layoutToInflate = R.layout.navdrawer_separator;
        } else if (itemId == NAVDRAWER_ITEM_SEPARATOR_SPECIAL) {
            layoutToInflate = R.layout.navdrawer_separator;
        } else {
            layoutToInflate = R.layout.navdrawer_item;
        }
        View view = getLayoutInflater().inflate(layoutToInflate, container, false);

        if (isSeparator(itemId)) {
            // we are done
            UIUtils.setAccessibilityIgnore(view);
            return view;
        }

        ImageView iconView = (ImageView) view.findViewById(R.id.icon);
        TextView titleView = (TextView) view.findViewById(R.id.title);

        EducatorDAO daoEdu = new EducatorDAO(EducatorChatActivity.this);
        //final Educator eduBean = daoEdu.getEducator(bean.getEducatorId());

        // set icon and text
        //iconView.setVisibility(iconId > 0 ? View.VISIBLE : View.GONE);
        // Trigger download of the URL asynchronously into imageview.
        *//*Picasso.with(EducatorChatActivity.this) //
                .load(eduBean.getImagePath()) //
                .placeholder(R.drawable.placeholder) //
                .error(R.drawable.error) //
                .fit() //
                .into(iconView);*//*

        //titleView.setText(bean.getCourseName() + "(" + eduBean.getfName() + " " + eduBean.getlName() + ")");

        //formatNavDrawerItem(view, itemId, selected);

        *//*view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNavDrawerItemClicked(bean, eduBean);
            }
        });*//*

        return view;
    }
*/
    /*private void onNavDrawerItemClicked(final Course crsBean, final Educator eduBean) {
        *//*if (itemId == getSelfNavDrawerItem()) {
            mDrawerLayout.closeDrawer(Gravity.START);
            return;
        }*//*

        *//*if (isSpecialItem(itemId)) {
            goToNavDrawerItem(itemId);
        } else {*//*
        // launch the target Activity after a short delay, to allow the close animation to play
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                goToNavDrawerItem(crsBean, eduBean);
            }
        }, NAVDRAWER_LAUNCH_DELAY);

        // change the active item on the list so the user can see the item changed
        ///setSelectedNavDrawerItem(itemId);
        // fade out the main content
        *//*View mainContent = findViewById(R.id.main_content);
        if (mainContent != null) {
            mainContent.animate().alpha(0).setDuration(MAIN_CONTENT_FADEOUT_DURATION);
        }*//*
        //}

        mDrawerLayout.closeDrawer(Gravity.START);
    }*/

    /*private void goToNavDrawerItem(Course crs, Educator edu) {
        userType = CommonConstants.USER_TYPE_EDUCATOR;
        userId = edu.getId();
        //subject = crs.getCourseName();
        loadContent();
        createNavDrawerItems();
        setupAccountBox();
        *//*Intent intent = new Intent(ChatActivity.this, ChatActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("subject", crs.getCourseName());
        intent.putExtra("userId", edu.getId());
        intent.putExtra("userType", CommonConstants.USER_TYPE_EDUCATOR);
        startActivity(intent);*//*
        //finish();
    }*/

    /**
     * Sets up the account box. The account box is the area at the top of the nav drawer that
     * shows which account the user is logged in as, and lets them switch accounts. It also
     * shows the user's Google+ cover photo as background.
     */
    @Override
    protected void setupAccountBox() {
        mAccountListContainer = (LinearLayout) findViewById(R.id.account_list);

        if (mAccountListContainer == null) {
            //This activity does not have an account box
            return;
        }

        final View chosenAccountView = findViewById(R.id.chosen_account_view);
        /*Account chosenAccount = AccountUtils.getActiveAccount(this);
        if (chosenAccount == null) {
            // No account logged in; hide account box
            chosenAccountView.setVisibility(View.GONE);
            mAccountListContainer.setVisibility(View.GONE);
            return;
        } else {
            chosenAccountView.setVisibility(View.VISIBLE);
            mAccountListContainer.setVisibility(View.INVISIBLE);
        }

        AccountManager am = AccountManager.get(this);
        Account[] accountArray = am.getAccountsByType(GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE);
        List<Account> accounts = new ArrayList<Account>(Arrays.asList(accountArray));
        accounts.remove(chosenAccount);*/

        ImageView coverImageView = (ImageView) chosenAccountView.findViewById(R.id.profile_cover_image);
        ImageView profileImageView = (ImageView) chosenAccountView.findViewById(R.id.profile_image);
        TextView nameTextView = (TextView) chosenAccountView.findViewById(R.id.profile_name_text);
        TextView emailTextView = (TextView) chosenAccountView.findViewById(R.id.profile_email_text);
        TextView phoneTextView = (TextView) chosenAccountView.findViewById(R.id.profile_phone_text);
        mExpandAccountBoxIndicator = (ImageView) findViewById(R.id.expand_account_box_indicator);

        //String name = AccountUtils.getPlusName(this);
        if (name == null) {
            nameTextView.setVisibility(View.GONE);
        } else {
            nameTextView.setText(name);
        }

        /*String imageUrl = AccountUtils.getPlusImageUrl(this);
        if (imageUrl != null) {
            mImageLoader.loadImage(imageUrl, profileImageView);
        }*/
        if (imgUrl != "") {
            Picasso.with(EducatorChatActivity.this) //
                    .load(imgUrl) //
                    .placeholder(R.drawable.placeholder) //
                    .error(R.drawable.error_image) //
                    .fit() //
                    .into(profileImageView);
        }

        /*String coverImageUrl = AccountUtils.getPlusCoverUrl(this);
        if (coverImageUrl != null) {
            mImageLoader.loadImage(coverImageUrl, coverImageView);
        } else {
            coverImageView.setImageResource(R.drawable.default_cover);
        }*/

        //email.setText(chosenAccount.name);
        emailTextView.setText(email);
        phoneTextView.setText(phone);

        /*if (accounts.isEmpty()) {
            // There's only one account on the device, so no need for a switcher.
            mExpandAccountBoxIndicator.setVisibility(View.GONE);
            mAccountListContainer.setVisibility(View.GONE);
            chosenAccountView.setEnabled(false);
            return;
        }*/

        chosenAccountView.setEnabled(true);

        /*mExpandAccountBoxIndicator.setVisibility(View.VISIBLE);
        chosenAccountView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAccountBoxExpanded = !mAccountBoxExpanded;
                setupAccountBoxToggle();
            }
        });
        setupAccountBoxToggle();*/

        //populateAccountList(accounts);
    }
}
