package com.ptapp.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.ptapp.adapter.ChildBarPagerAdapter;
import com.ptapp.adapter.PicassoGvAdapter;
import com.ptapp.bo.StudentBO;
import com.ptapp.bo.StudentContextBO;
import com.ptapp.bo.StudentsBO;
import com.ptapp.dao.CalendarEventMapperDAO;
import com.ptapp.app.R;
import com.ptapp.app.SchooloApp;
import com.ptapp.utils.CommonMethods;
import com.ptapp.utils.SharedPrefUtil;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Map;

/**
 * First page of the app.
 */
public class HomeActivity extends BaseActivity {
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments representing each object in a collection. We use a
     * {@link android.support.v4.app.FragmentStatePagerAdapter} derivative,
     * which will destroy and re-create fragments as needed, saving and
     * restoring their state in the process. This is important to conserve
     * memory and is a best practice when allowing navigation between objects in
     * a potentially large collection.
     */


    protected static final int REQUEST_CAMERA = 1;
    protected static final int SELECT_FILE = 2;
    private String TAG = "PTApp-act_Home: ";

    ViewPager viewPager;

    private StudentContextBO stuContext;
    private StudentsBO boStudents;

    FrameLayout fl_kidsPicture, fl_msgBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        fl_kidsPicture = (FrameLayout) findViewById(R.id.fl_kidsPicture);
        fl_msgBox = (FrameLayout) findViewById(R.id.fl_msgBox);

        try {
            afterDatabaseInstallation();

        } catch (Exception ex) {
            if (ex.getMessage() != null) {
                Log.e(TAG, ex.getMessage());
            } else {
                Log.e(TAG, "onCreate - No exception message for this error.");
            }
        }
    }

    /**
     *
     */
    private void afterDatabaseInstallation() {
        try {
            // TODO: Remove this, as it's only for testing purpose.
            CommonMethods.takeDbBackup(getApplicationContext());

            boStudents = ((SchooloApp) getApplicationContext()).getStudentsBO();
            if (boStudents != null) {

                stuContext = boStudents.getStudent((SharedPrefUtil.getPrefLastViewedKidStudentId(HomeActivity.this))).getStudentContextBO(
                        getApplicationContext());

                if (stuContext != null) {

                    setKidProfileImg();

                    // load Educators images and subject names
                    GridView gv = (GridView) findViewById(R.id.gv_Educators);
                    gv.setAdapter(new PicassoGvAdapter(this, stuContext));

                    /** Image Gallery */
                    showChildImageGallery();

                    showLatestEvents();

                    // Child images on action bar
                    showChildActionBar();
                } else {
                    Log.wtf(TAG, "Student Context is null.");
                    hideViews();
                    CommonMethods
                            .showOkDialog(CommonMethods
                                            .getApplicationName(HomeActivity.this),
                                    "Student Context is null.",
                                    HomeActivity.this);
                }
            } else {
                Log.wtf(TAG, "List of studentsBO is null");
                hideViews();
                CommonMethods.showOkDialog(
                        CommonMethods.getApplicationName(HomeActivity.this),
                        "List of studentsBO is null", HomeActivity.this);
            }
        } catch (Exception ex) {
            if (ex.getMessage() != null) {
                Log.e(TAG, ex.getMessage());
            } else {
                Log.e(TAG, "after db install - No msg for error.");
            }
            hideViews();
            CommonMethods
                    .showOkDialog(
                            "Oops!",
                            "Encountered some unexpected problem. Re-installing the app might fix the problem.",
                            HomeActivity.this);
        }

    }

    private void hideViews() {
        try {
            fl_kidsPicture.setVisibility(View.INVISIBLE);
            fl_msgBox.setVisibility(View.INVISIBLE);
        } catch (Exception ex) {
            if (ex.getMessage() != null) {
                Log.e(TAG, ex.getMessage());
            } else {
                Log.e(TAG, "hideViews - no msg for this error.");
            }
        }
    }

    /**
     *
     */
    private void showChildImageGallery() {
        // Create an adapter that when requested, will return a fragment
        // representing an object in the collection.
        //
        // ViewPager and its adapters use support library fragments, so we
        // must use getSupportFragmentManager.
        /*HomeGalleryPagerAdapter cpadapter = new HomeGalleryPagerAdapter(
                getSupportFragmentManager(), HomeActivity.this, stuContext
                .getStudent().getRollNumber());*/
        viewPager = (ViewPager) findViewById(R.id.pager);
        //viewPager.setAdapter(cpadapter);
    }

    /**
     *
     */
    private void showChildActionBar() {
        try {
            ActionBar actionBar = getActionBar();

            // add the custom view to the action bar
            actionBar.setCustomView(R.layout.ab_childbar);

            Map<Integer, StudentBO> mapStus = boStudents.getStudentsAll();
            ArrayList<StudentBO> lstChildInfo = new ArrayList<StudentBO>();
            for (StudentBO item : mapStus.values()) {
                lstChildInfo.add(item);
            }

            Log.v(TAG, "number of Stu basic infos:" + lstChildInfo.size());
            //Log.v(TAG, "sstudentId: " + lstChildInfo.get(0).getStudentId());

            if (lstChildInfo.size() > 0) {

                // Setting child bar pager adapter.
                ChildBarPagerAdapter adapterChildBar = new ChildBarPagerAdapter(
                        getSupportFragmentManager(), HomeActivity.this,
                        lstChildInfo);
                ViewPager childPager = (ViewPager) actionBar.getCustomView()
                        .findViewById(R.id.child_pager);
                childPager.setAdapter(adapterChildBar);

            } else {
                Log.i(TAG, "No Child Basic Info found.");
            }

            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM
                    | ActionBar.DISPLAY_SHOW_HOME);
        } catch (Exception ex) {
            if (ex.getMessage() != null) {
                Log.e(TAG, ex.getMessage());
            } else {
                Log.e(TAG,
                        "showChildActionBar - No exception message for this error.");
            }
        }
    }

    /**
     * Show events in Events in Home Page.
     */
    // get latest 5 events.
    private void showLatestEvents() {

        TextView tv_noEvents = (TextView) findViewById(R.id.tv_no_events);

        // Getting list of current month events, from app's db.
        CalendarEventMapperDAO daoCEM = new CalendarEventMapperDAO(
                HomeActivity.this);
        /*final ArrayList<CalendarEventMapperBean> lstEvents = daoCEM
                .getLatestEvents(Integer.parseInt(SharedPrefUtil.getPrefTxtNumOfRows(HomeActivity.this)));*/
        /*Log.v(TAG, "number of latest events:" + lstEvents.size());

        if (lstEvents.size() > 0) {

            // Setting list-view adapter.
            final ListView lv1 = (ListView) findViewById(R.id.listV_main);
            lv1.setAdapter(new HomeEventsLvAdapter(HomeActivity.this,
                    lstEvents, stuContext));

            tv_noEvents.setText(null); // Set "no events found" text null.
            tv_noEvents.setVisibility(View.GONE);

        } else {
            tv_noEvents.setText(R.string.no_events_found);
            tv_noEvents.setVisibility(View.VISIBLE);
        }*/
    }


    // On click of Calender box.
    public void onClickCalendarBox(View view) {
        Intent intent = new Intent(HomeActivity.this,
                EventsMonthlyActivity.class);
        startActivity(intent);
    }

    // On click of Kid picture, open the child profile activity.
    public void onClickKidPic(View view) {
        try {
            // ImageView ivKidPic = (ImageView) findViewById(R.id.kid_pic);
            Intent intent = new Intent(HomeActivity.this,
                    ChildProfileActivity.class);
            // intent.putExtra("StudentId", ivKidPic.getTag().toString());
            startActivity(intent);
        } catch (Exception ex) {
            if (ex.getMessage() != null) {
                Log.e(TAG, ex.getMessage());
            } else {
                Log.e(TAG,
                        "onClickKidPic - No exception message for this error.");
            }
        }
    }

    public void onClickCamera(View view) {
        selectImage();
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from gallery",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
        builder.setTitle("Add Photo");
        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (items[which].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    // To be safe, you should check that the SDCard is mounted
                    // using Environment.getExternalStorageState() before doing
                    // this.
                    if (CommonMethods.isExternalStorageWritable()) {
                        /*File imgFile = CommonMethods
                                .getFileImgKidProfile(stuContext.getStudent()
                                        .getRollNumber());
                        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                                Uri.fromFile(imgFile));
                        startActivityForResult(intent, REQUEST_CAMERA);*/
                    }
                    //TODO: else save in internal-inbuilt sd-storage
                    // if memory is full, it should show toast msg to user.
                } else if (items[which].equals("Choose from gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(
                            Intent.createChooser(intent, "Select File"),
                            SELECT_FILE);
                } else if (items[which].equals("Cancel")) {
                    dialog.dismiss();
                }
            }

        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                setKidProfileImg();
            } else if (requestCode == SELECT_FILE) {
                Uri selectedImageUri = data.getData();
                /*String tempPath = getPath(selectedImageUri, HomeActivity.this);
                File f = CommonMethods.getFileImgKidProfile(stuContext
                        .getStudent().getRollNumber());
                String targetPath = f.getAbsolutePath();
                try {
                    copyFile(tempPath, targetPath);
                } catch (IOException ex) {
                    Log.e(TAG, ex.getMessage());
                    ex.printStackTrace();
                }*/
                setKidProfileImg();
            }

            showChildActionBar();
        }
    }

    public String getPath(Uri uri, Activity activity) {
        String[] projection = {MediaColumns.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null,
                null);
        int column_index = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    /**
     * Set the captured image to the kid's profile imageview
     */
    private void setKidProfileImg() {
        try {
            ImageView ivKidPic = (ImageView) findViewById(R.id.kid_picHome);

            /*File f = CommonMethods.getFileImgKidProfile(stuContext.getStudent()
                    .getRollNumber());
            if (f.exists()) {

                Picasso.with(HomeActivity.this) //
                        .load(f) //
                        .placeholder(CommonConstants.LOADING) //
                        .error(CommonConstants.NO_PHOTO_AVAILABLE) //
                        .fit() //
                        .into(ivKidPic);

            } else {
                Log.i(TAG,
                        "Kid's picture inside schoolo folder on sd-card not found.");
                // get the image from link saved in imgPath column of table.
                Picasso.with(HomeActivity.this) //
                        .load(stuContext.getStudent().getImgPath()) //
                        .placeholder(CommonConstants.LOADING) //
                        .error(CommonConstants.NO_PHOTO_AVAILABLE) //
                        .fit() //
                        .into(ivKidPic);

            }*/
        } catch (Exception ex) {
            if (ex.getMessage() != null) {
                Log.e(TAG, ex.getMessage());
            } else {
                Log.e(TAG,
                        "setKidProfileImg - No exception message for this error.");
            }
        }
    }

    /**
     * Copies one image to other
     */
    public void copyFile(String selectedImagePath, String targetPath)
            throws IOException {
        InputStream in = new FileInputStream(selectedImagePath);
        OutputStream out = new FileOutputStream(targetPath);

        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }

    /**
     * Options Menu Start
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

		/*
         * It should return true for the menu to appear, if returns false the
		 * menu will be invisible.
		 */
        return true;
    }

    /*
     * Handles the activity's Optionsmenu selection, selected by user.
     *
     * return true for every handled menu item.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.app_settings:
                Intent settingsActivity = new Intent(getBaseContext(),
                        SettingsActivity.class);
                startActivity(settingsActivity);
                return true;
            case R.id.signout:

                /*try {
                    Log.d(TAG, "adapter: " + adapter);
                    if (adapter != null) {
                        Log.i(TAG, "Provider name: "
                                + adapter.getCurrentProvider().getProviderId());

                        // signout method clears cookies.
                        adapter.signOut(getBaseContext(), adapter
                                .getCurrentProvider().getProviderId());
                    } else {
                        String providerName = sam.getCurrentAuthProvider()
                                .getProviderId();
                        if (sam.isConnected(providerName)) {
                            Log.i(TAG, "Provider is connected.");

                            // To avoid IllegalStateException: Call
                            // CookieSyncManager::createInstance() or create a
                            // webview before using CookieManager class
                            @SuppressWarnings("unused")
                            CookieSyncManager cookieSyncMngr = CookieSyncManager
                                    .createInstance(getApplicationContext());

                            CookieManager cookieManager = CookieManager
                                    .getInstance();
                            cookieManager.removeAllCookie();

                            if (providerName != null) {

                                if (sam.getConnectedProvidersIds().contains(
                                        providerName))
                                    sam.disconnectProvider(providerName);

                                Editor edit = PreferenceManager
                                        .getDefaultSharedPreferences(
                                                getApplicationContext()).edit();
                                edit.remove(providerName + " key");
                                edit.commit();

                                Log.d("SocialAuthAdapter", "Disconnecting Provider");

                                SharedPrefUtil.setPrefSauthAccessGrant(HomeActivity.this, "");

                                Log.i(TAG, "SignOut successfully.");

                            } else {
                                Log.d("SocialAuthAdapter",
                                        "The provider name should be same");

                            }
                        }
                    }
                } catch (Exception ex) {
                    if (ex.getMessage() != null) {
                        Log.e(TAG, "case R.id.signout: err_msg= " + ex.getMessage());
                    } else {
                        Log.e(TAG, "No exception message for this error.");
                    }
                }
                finish();*/
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Options Menu End
     */

    protected void onRestart() {
        super.onRestart();
        Log.v(TAG, "onRestart.");

        // In jelly bean, this activity restarts, on back press from the
        // settings activity, but in ICS, activity recreates, so restart method
        // doesn't call.
        // Call showChildBar method, to reflect the changes in the kids color,
        // in Jelly bean.
        showChildActionBar();
    }

    protected void onResume() {
        super.onResume();
        Log.v(TAG, "onResume.");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.v(TAG, "onStart.");
    }

    protected void onPause() {
        super.onPause();
        Log.v(TAG, "onPause.");
    }

    protected void onStop() {
        super.onStop();
        Log.v(TAG, "onStop.");
    }

    protected void onDestroy() {
        super.onDestroy();
        Log.v(TAG, "onDestroy.");
    }
}
