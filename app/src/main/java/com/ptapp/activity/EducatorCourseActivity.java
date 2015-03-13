package com.ptapp.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ptapp.dao.CourseDAO;
import com.ptapp.app.R;

import java.util.Locale;

import static com.ptapp.utils.LogUtils.makeLogTag;

public class EducatorCourseActivity extends Activity implements ActionBar.TabListener {

    //TODO:Make this page similar like 'IOSCHED's MySchedule' page(with tabs, nav drawer, multiSwipeRefresh)

    private static final String TAG = makeLogTag(EducatorCourseMessagesFragment.class);

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v13.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    private String courseName = "";
    protected String classId = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_educator_course);

        // Set up the action bar.
        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }


        //From another screen- get Intent data
        if (getIntent() != null) {
            String tabName = getIntent().getStringExtra("selectTab");
            String courseId = getIntent().getStringExtra("courseId");
            if (!tabName.isEmpty()) {
                if (tabName.equals("messages")) {
                    actionBar.setSelectedNavigationItem(1);
                }
            }

            if (!courseId.isEmpty()) {
                try {
                    // Get Course Details -
                    CourseDAO dcCourse = new CourseDAO(EducatorCourseActivity.this);
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
                ab.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM
                        | ActionBar.DISPLAY_SHOW_HOME
                        | ActionBar.DISPLAY_HOME_AS_UP);

                // show subject icon
                //TODO: make it fetch Path of Image from database

                if (courseName.equals("Math2")) {
                    // ab.setLogo was not working on ICS
                    ab.setIcon(R.drawable.logo_math);
                } else if (courseName.equalsIgnoreCase("English2")) {
                    ab.setIcon(R.drawable.logo_english);
                } else if (courseName.equalsIgnoreCase("French2")) {
                    ab.setIcon(R.drawable.course_french);
                } else if (courseName.equalsIgnoreCase("German2")) {
                    ab.setIcon(R.drawable.course_german);
                } else if (courseName.equalsIgnoreCase("Punjabi2")) {
                    ab.setIcon(R.drawable.course_punjabi);
                } else if (courseName.equalsIgnoreCase("Hindi2")) {
                    ab.setIcon(R.drawable.course_hindi);
                } else {
                    ab.setIcon(R.drawable.nophotoavailable);
                }

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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.educator_course, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        /*int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }*/
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            switch (position) {
                case 0:
                    /*return PlaceholderFragment.newInstance(position + 1);*/
                    return new EducatorCourseStudentsFragment();
                case 1:
                    return new EducatorCourseMessagesFragment();
            }
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return "Students";
                case 1:
                    return "Messages";

            }
            return null;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_educator_course_messages, container, false);
            return rootView;
        }
    }

}