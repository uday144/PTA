package com.ptapp.activity;

import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.ptapp.adapter.ChildBarPagerAdapter;
import com.ptapp.bo.StudentBO;
import com.ptapp.bo.StudentsBO;
import com.ptapp.dao.CourseDAO;
import com.ptapp.entities.Account;
import com.ptapp.provider.PTAppContract;
import com.ptapp.app.R;
import com.ptapp.app.SchooloApp;
import com.ptapp.service.XmppConnectionService;
import com.ptapp.utils.CommonMethods;
import com.ptapp.utils.ExceptionHelper;
import com.ptapp.utils.SharedPrefUtil;
import com.ptapp.widget.CollectionView;
import com.ptapp.widget.DrawShadowFrameLayout;
import com.ptapp.xmpp.jid.InvalidJidException;
import com.ptapp.xmpp.jid.Jid;
import com.ptapp.xmpp.pep.Avatar;

import java.util.ArrayList;

import static com.ptapp.utils.LogUtils.LOGE;
import static com.ptapp.utils.LogUtils.LOGI;
import static com.ptapp.utils.LogUtils.makeLogTag;

public class ParentHomeActivity extends XmppActivity implements XmppConnectionService.OnAccountUpdate {

    private static final String TAG = makeLogTag(ParentHomeActivity.class);

    private ParentHomeFragment mParentHomeFrag = null;
    private DrawShadowFrameLayout mDrawShadowFrameLayout;
    /*private String courseName = "";
    private String className = "";*/
    //public String classId = "";
    //public String studentId = "";
    private StudentsBO boStudents;

    /*xmpp*/
    private Account mAccount;

    //temp
    TextView online, abName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO: Remove this, as it's only for testing purpose.
        CommonMethods.takeDbBackup(getApplicationContext());

        if (isFinishing()) {
            return;
        }

        setContentView(R.layout.activity_parent_home);
        mDrawShadowFrameLayout = (DrawShadowFrameLayout) findViewById(R.id.main_content);

        // TODO: Remove this, as it's only for testing purpose.
        CommonMethods.takeDbBackup(getApplicationContext());

        boStudents = ((SchooloApp) getApplicationContext()).getStudentsBO();
        if (boStudents != null) {

            showChildActionBar();

            // load Educators images and subject names
            /*GridView gv = (GridView) findViewById(R.id.gv_Educators);
            gv.setAdapter(new PicassoGvAdapter(this, stuContext));*/
        }
        //From another screen- get Intent data
        if (getIntent() != null) {

            String classSubjectId = getIntent().getStringExtra("classSubjectId");
            /*className = getIntent().getStringExtra("className");
            courseName = getIntent().getStringExtra("courseName");*/
            if (classSubjectId != null) {
                try {
                    //TODO: classId can be attached to each box, and send through bundle, here.

                    Cursor c = null;
                    try {

                        //Uri courseUri = PTAppContract.Courses.buildCourseUri(courseId);
                        /*Log.d(TAG, "courseUri: " + courseUri);
                        c = getContentResolver().query(courseUri, null, null, null, null);*/
                        if (c.moveToFirst()) {

                            /*studentId = c.getString(c.getColumnIndex(PTAppContract.Courses.COURSE_STUDENT_ID));
                            courseName = c.getString(c.getColumnIndex(PTAppContract.Courses.COURSE_NAME));*/
                        }
                    } catch (Exception ex) {
                        Log.e(TAG, ex.getMessage());
                    } finally {
                        c.close(); // Closing the cursor
                    }
                    //Log.d(TAG, "retrieve studentId: " + studentId);
                    // Get Course Details -
                    CourseDAO dcCourse = new CourseDAO(ParentHomeActivity.this);
                    /*Course crs = dcCourse.getCourse(courseId);

                    if (crs != null) {
                        // fill the text values
                        courseName = crs.getCourseName();
                        classId = crs.getClassId();
                    } else {
                        Log.d(TAG, "No Course found");
                    }*/
                } catch (Exception ex) {
                    if (ex.getMessage() != null) {
                        Log.e(TAG, ex.getMessage());
                    } else {
                        Log.e(TAG, "Course Details, no error msg returned.");
                    }
                }
            }

            /*//set Action Bar(Custom view)
            ActionBar ab = getActionBar();
            if (ab != null) {
                // add the custom view to the action bar
                ab.setCustomView(R.layout.ab_subject_msgs);
                ab.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM
                        | ActionBar.DISPLAY_SHOW_HOME
                        | ActionBar.DISPLAY_HOME_AS_UP);

                //showABarCourseImage(ab);

                // change the label
                TextView txt_Title = (TextView) ab.getCustomView().findViewById(R.id.txt_Title);
                ImageView img2 = (ImageView) ab.getCustomView().findViewById(R.id.img2);


                txt_Title.setText(className + " : " + courseName);
                // don't show child image
                img2.setVisibility(View.GONE);


            }*/
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        enableActionBarAutoHide((CollectionView) findViewById(R.id.classes_list_collection_view));

        mParentHomeFrag = (ParentHomeFragment) getFragmentManager().findFragmentById(R.id.parent_home_fragment);
        if (mParentHomeFrag != null && savedInstanceState == null) {
            Bundle args = intentToFragmentArguments(getIntent());
            mParentHomeFrag.reloadFromArguments(args);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private void showChildActionBar() {
        try {
            ActionBar actionBar = getActionBar();

            // add the custom view to the action bar
            actionBar.setCustomView(R.layout.ab_childbar);
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME);

            online = (TextView) actionBar.getCustomView().findViewById(R.id.ab_online);
            abName = (TextView) actionBar.getCustomView().findViewById(R.id.ab_name);

            Cursor c = getContentResolver().query(PTAppContract.Student.buildStudentUri(
                            String.valueOf(
                                    SharedPrefUtil.getPrefLastViewedKidStudentId(
                                            ParentHomeActivity.this))),
                    new String[]{PTAppContract.Student.FIRST_NAME}, null, null, null);
            if (c.moveToFirst()) {
                //TODO:show here the name which is coming in the cursor, but need to check first the other end student name
                abName.setText("Karan");
            }

            /*Map<Integer, StudentBO> mapStus = boStudents.getStudentsAll();
            ArrayList<StudentBO> lstChildInfo = new ArrayList<StudentBO>();
            for (StudentBO item : mapStus.values()) {
                lstChildInfo.add(item);
                Log.v(TAG, "studentId: " + item.getStudentId());
            }
            Log.v(TAG, "number of kids:" + lstChildInfo.size());*/

            ArrayList<StudentBO> lstChildInfo = CommonMethods.getParentKids(ParentHomeActivity.this);
            if (lstChildInfo.size() > 0) {
                // Setting child bar pager adapter.
                ChildBarPagerAdapter adapterChildBar = new ChildBarPagerAdapter(getSupportFragmentManager(), ParentHomeActivity.this, lstChildInfo);

                ViewPager childPager = (ViewPager) actionBar.getCustomView().findViewById(R.id.child_pager);
                childPager.setAdapter(adapterChildBar);
            } else {
                Log.wtf(TAG, "No kid found.");
            }
        } catch (Exception ex) {
            if (ex.getMessage() != null) {
                Log.e(TAG, ex.getMessage());
            } else {
                Log.e(TAG,
                        "showChildActionBar - No exception message for this error.");
            }
        }
    }

    @Override
    protected int getSelfNavDrawerItem() {
        /*return NAVDRAWER_ITEM_VIDEO_LIBRARY;*/
        //Disabling the nav drawer for now
        return NAVDRAWER_ITEM_INVALID;
    }

    @Override
    public void onResume() {
        super.onResume();
        invalidateOptionsMenu();

        Fragment frag = getFragmentManager().findFragmentById(R.id.msgs_fragment);
        if (frag != null) {
            // configure events fragment's top clearance to take our overlaid controls (Action Bar
            // and spinner box) into account.
            //int actionBarSize = UIUtils.calculateActionBarSize(this);
            /*int filterBarSize = getResources().getDimensionPixelSize(R.dimen.filterbar_height);*/
            //int filterBarSize = 0;
            /*mDrawShadowFrameLayout.setShadowTopOffset(actionBarSize + filterBarSize);
            ((EducatorMsgsFragment) frag).setContentTopClearance(actionBarSize + filterBarSize
                    + getResources().getDimensionPixelSize(R.dimen.explore_grid_padding));*/
        }
    }

    @Override
    protected void onActionBarAutoShowOrHide(boolean shown) {
        super.onActionBarAutoShowOrHide(shown);
        mDrawShadowFrameLayout.setShadowVisible(shown, shown);
    }

    @Override
    protected void onNavDrawerStateChanged(boolean isOpen, boolean isAnimating) {
        super.onNavDrawerStateChanged(isOpen, isAnimating);
        updateActionBarNavigation();
    }

    private void updateActionBarNavigation() {
        boolean show = !isNavDrawerOpen();
        if (getLPreviewUtils().shouldChangeActionBarForDrawer()) {
            ActionBar ab = getActionBar();
            ab.setDisplayShowTitleEnabled(show);
            ab.setDisplayUseLogoEnabled(!show);
        }
    }

    private void setJabberIdAndConnect() {
        if (mAccount != null && mAccount.getStatus() == Account.State.DISABLED) {
            mAccount.setOption(Account.OPTION_DISABLED, false);
            xmppConnectionService.updateAccount(mAccount);
            return;
        }

        //TODO:Fetch this JID from the db or sharedPref, instead of hardcoded strings

        //getting Jid
        /*String makeJid = null;
        Uri parentUri = PTAppContract.Parent.CONTENT_URI;
        String[] projection ={PTAppContract.Parent.JID};
        String selection = PTAppContract.Parent.USER_ID + "=?";
        String[] args = {String.valueOf(CommonMethods.getAppUserId(ParentHomeActivity.this))};
        Cursor c = getContentResolver().query(parentUri, projection, selection, args, null);
        if (c.moveToFirst()) {
            makeJid = c.getString(c.getColumnIndex(PTAppContract.Parent.JID));
        }
        if (c.getCount() < 1) {
            makeJid = CommonMethods.getAppUserId(ParentHomeActivity.this) + ".p@" + getString(R.string.chat_server);
        }*/
        /*String makeJid = "jassie3@xmpp.jp";*/

        //TODO:complete this when we get the
        /*String makeJid = "";
        ArrayList<Role> userRoles = CommonMethods.getAppUserRoles(ParentHomeActivity.this);
        for(Role r:userRoles){
            if(r.equals(CommonConstants.ROLE_PARENT)){
                *//*makeJid = r.get*//*
            }
        }*/

        String makeJid = CommonMethods.getAppUserId(ParentHomeActivity.this) + ".p@54.88.152.125";
        final Jid jid;
        try {
            jid = Jid.fromString(makeJid);
        } catch (final InvalidJidException e) {
            LOGE(TAG, getString(R.string.invalid_jid));
            return;
        }
        if (jid.isDomainJid()) {
            LOGE(TAG, getString(R.string.invalid_jid));
            return;
        }
        final String password = "1234";
        try {
            mAccount = xmppConnectionService.findAccountByJid(Jid.fromString(makeJid));
            if (mAccount != null) {
                LOGI(TAG, getString(R.string.account_already_exists));
                xmppConnectionService.updateAccount(mAccount);
            } else {
                mAccount = new Account(jid.toBareJid(), password);
                mAccount.setOption(Account.OPTION_USETLS, true);
                mAccount.setOption(Account.OPTION_USECOMPRESSION, true);
                    /*mAccount.setOption(Account.OPTION_REGISTER, registerNewAccount);*/
                xmppConnectionService.createAccount(mAccount);
            }
        } catch (final InvalidJidException e) {
            return;
        }
        updateSaveButton();
        //updateAccountInformation();
    }

    protected void updateSaveButton() {
        if (mAccount != null && mAccount.getStatus() == Account.State.CONNECTING) {
            LOGI(TAG, getString(R.string.account_status_connecting));
            updateStatusUI(getString(R.string.account_status_connecting));

        } else if (mAccount != null && mAccount.getStatus() == Account.State.DISABLED) {
            LOGI(TAG, getString(R.string.enable));

        } else if (mAccount.getStatus() == Account.State.NO_INTERNET) {

            updateStatusUI(getString(R.string.account_status_no_internet));
        } else {

            LOGI(TAG, getString(R.string.next)); //----
        }
    }

    protected void finishInitialSetup(final Avatar avatar) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                final Intent intent;

                //TODO:What to do here?
                if (mAccount.getStatus() == Account.State.ONLINE) {

                    updateStatusUI(getString(R.string.account_status_online));
                }
            }
        });
    }

    private void updateStatusUI(String status) {
        if (online != null) {
            online.setText(status);
        }
    }

    @Override
    public void onAccountUpdate() {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                invalidateOptionsMenu();

                if (mAccount != null && mAccount.getStatus() == Account.State.ONLINE) {
                    finishInitialSetup(null);
                } else {
                    updateSaveButton();
                }
            }
        });
    }

    @Override
    void onBackendConnected() {
        this.xmppConnectionService.getNotificationService().setIsInForeground(true);

        setJabberIdAndConnect();

        ExceptionHelper.checkForCrash(this, this.xmppConnectionService);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (this.xmppConnectionServiceBound) {
            this.onBackendConnected();
        }


        //updateActionBarNavigation();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_parent_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
