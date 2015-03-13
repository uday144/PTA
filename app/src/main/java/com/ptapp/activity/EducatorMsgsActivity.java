package com.ptapp.activity;

import android.app.ActionBar;
import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ptapp.dao.CourseDAO;
import com.ptapp.app.R;
import com.ptapp.utils.CommonMethods;
import com.ptapp.widget.CollectionView;
import com.ptapp.widget.DrawShadowFrameLayout;

import static com.ptapp.utils.LogUtils.makeLogTag;

public class EducatorMsgsActivity extends BaseActivity {

    private static final String TAG = makeLogTag(EducatorMsgsActivity.class);

    public static final String BUNDLE_CLASS_SUBJECT_ID = "classSubjectId";
    public static final String BUNDLE_CLASS_NAME = "className";
    public static final String BUNDLE_COURSE_NAME = "courseName";
    public static final String BUNDLE_GROUP_JID = "groupJid";
    public static final String BUNDLE_GROUP_ID = "groupId";

    private EducatorMsgsFragment mMessagesFrag = null;
    private DrawShadowFrameLayout mDrawShadowFrameLayout;
    private String courseName = "";
    /*private String className = "";*/
    //public String classId = "";
    public String studentId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (isFinishing()) {
            return;
        }

        setContentView(R.layout.activity_educator_msgs);
        mDrawShadowFrameLayout = (DrawShadowFrameLayout) findViewById(R.id.main_content);


        //From another screen- get Intent data
        if (getIntent() != null) {

            String classSubjectId = getIntent().getStringExtra("classSubjectId");
            /*className = getIntent().getStringExtra("className");*/
            courseName = getIntent().getStringExtra("courseName");
            if (!classSubjectId.isEmpty()) {
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
                    Log.d(TAG, "retrieve studentId: " + studentId);
                    // Get Course Details -
                    CourseDAO dcCourse = new CourseDAO(EducatorMsgsActivity.this);
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

            //set Action Bar(Custom view)
            ActionBar ab = getActionBar();
            if (ab != null) {
                // add the custom view to the action bar
                ab.setCustomView(R.layout.ab_subject_msgs);
                ab.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME);
                ab.setDisplayHomeAsUpEnabled(true);
                ab.setHomeButtonEnabled(true);


                /*CommonMethods.showABarCourseImage(ab, courseName);*/
                ab.setIcon(CommonMethods.getSubjectResId(courseName));
                // change the label
                TextView txt_Title = (TextView) ab.getCustomView().findViewById(
                        R.id.txt_Title);
                txt_Title.setText(courseName);

                // don't show child image
                ImageView img2 = (ImageView) ab.getCustomView().findViewById(
                        R.id.img2);
                img2.setVisibility(View.GONE);
            }
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        enableActionBarAutoHide((CollectionView) findViewById(R.id.msgs_collection_view));

        mMessagesFrag = (EducatorMsgsFragment) getFragmentManager().findFragmentById(R.id.msgs_fragment);
        if (mMessagesFrag != null && savedInstanceState == null) {
            Bundle args = intentToFragmentArguments(getIntent());
            mMessagesFrag.reloadFromArguments(args);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.educator_msgs, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
