package com.ptapp.activity;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.BaseColumns;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;


import com.ptapp.app.R;
import com.ptapp.widget.CollectionViewCallbacks;

import static com.ptapp.utils.LogUtils.LOGD;
import static com.ptapp.utils.LogUtils.makeLogTag;


public class EducatorCourseStudentsFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor>, CollectionViewCallbacks {

    private static final String TAG = makeLogTag(EducatorCourseStudentsFragment.class);


    public EducatorCourseStudentsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);    //necessary to show action menu from fragment
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_educator_course_students, container, false);
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.educator_course_students, menu);

    }


    //Start - CollectionViewCallbacks methods
    @Override
    public View newCollectionHeaderView(Context context, ViewGroup parent) {
        return null;
    }

    @Override
    public void bindCollectionHeaderView(Context context, View view, int groupId, String headerLabel) {

    }

    @Override
    public View newCollectionItemView(Context context, int groupId, ViewGroup parent) {
        return null;
    }

    @Override
    public void bindCollectionItemView(Context context, View view, int groupId, int indexInGroup, int dataIndex, Object tag) {

    }
    //End - CollectionViewCallbacks methods

    //Start - LoaderManager.LoaderCallbacks
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle data) {
        LOGD(TAG, "onCreateLoader, id=" + id + ", data=" + data);
        final Intent intent = BaseActivity.fragmentArgumentsToIntent(data);
        Uri sessionsUri = intent.getData();
        if ((id == SessionsQuery.NORMAL_TOKEN || id == SessionsQuery.SEARCH_TOKEN) && sessionsUri == null) {
            LOGD(TAG, "intent.getData() is null, setting to default sessions search");
            /*sessionsUri = ScheduleContract.Sessions.CONTENT_URI;*/
            //sessionsUri = PTAppContract.Courses.CONTENT_URI;
        }
        Loader<Cursor> loader = null;

        /*String selectionWhere = PTAppContract.Courses.COURSE_EDUCATOR_ID + " = ?";
        String[] selectionArgs = new String[]{SharedPrefUtil.getPrefAppUserId(getActivity())};
        if (id == SessionsQuery.NORMAL_TOKEN) {
            LOGD(TAG, "Creating educator loader for " + sessionsUri + ", selectionWhere " + selectionWhere);
            loader = new CursorLoader(getActivity(), sessionsUri, SessionsQuery.NORMAL_PROJECTION,
                    selectionWhere, selectionArgs, PTAppContract.Courses.SORT_BY_COURSE_NAME);
        } *//*else if (id == SessionsQuery.SEARCH_TOKEN) {
            LOGD(TAG, "Creating search loader for " + sessionsUri + ", selection " + liveStreamedOnlySelection);
            loader = new CursorLoader(getActivity(), sessionsUri, SessionsQuery.SEARCH_PROJECTION,
                    liveStreamedOnlySelection, null, ScheduleContract.Sessions.SORT_BY_TYPE_THEN_TIME);
        }*//*
        else if (id == TAG_METADATA_TOKEN) {
            LOGD(TAG, "Creating tags metadata loader...");
            *//*loader = TagMetadata.createCursorLoader(getActivity());*//*
        }*/
        return loader;

    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }
    //End - LoaderManager.LoaderCallbacks


    /**
     * {@lin com.google.samples.apps.iosched.provider.ScheduleContract.Sessions}
     * query parameters.
     */
    private interface SessionsQuery {
        int NORMAL_TOKEN = 0x1;
        int SEARCH_TOKEN = 0x3;

        //Need to specify the table names alongwith column names, to avoid ambiguity error in 'SELECT' statement
        String[] NORMAL_PROJECTION = {
                //BaseColumns._ID,
                /*PTAppDatabase.Tables.COURSES + "." + PTAppContract.Courses.COURSE_ID,
                PTAppContract.Courses.COURSE_NAME,
                PTAppDatabase.Tables.COURSES + "." + PTAppContract.Courses.COURSE_STUDENT_ID,
                PTAppContract.Courses.COURSE_EDUCATOR_ID,
                PTAppContract.Courses.COURSE_CLASS_ID,
                PTAppContract.Courses.COURSE_YEAR,
                PTAppContract.Courses.COURSE_GRADE,
                PTAppContract.Classes.CLASS_NAME,*/
        };

        String[] SEARCH_PROJECTION = {
                BaseColumns._ID,
                /*ScheduleContract.Sessions.SESSION_ID,
                ScheduleContract.Sessions.SESSION_TITLE,
                ScheduleContract.Sessions.SESSION_IN_MY_SCHEDULE,
                ScheduleContract.Sessions.SESSION_START,
                ScheduleContract.Sessions.SESSION_END,
                ScheduleContract.Rooms.ROOM_NAME,
                ScheduleContract.Rooms.ROOM_ID,
                ScheduleContract.Sessions.SESSION_HASHTAG,
                ScheduleContract.Sessions.SESSION_URL,
                ScheduleContract.Sessions.SESSION_LIVESTREAM_URL,
                ScheduleContract.Sessions.SESSION_TAGS,
                ScheduleContract.Sessions.SESSION_SPEAKER_NAMES,
                ScheduleContract.Sessions.SESSION_ABSTRACT,
                ScheduleContract.Sessions.SESSION_COLOR,
                ScheduleContract.Sessions.SESSION_PHOTO_URL,
                ScheduleContract.Sessions.SEARCH_SNIPPET,*/
        };


        //int _ID = 0;
        int COURSE_ID = 0;
        int COURSE_NAME = 1;
        int STUDENT_ID = 2;
        int EDUCATOR_ID = 3;
        int CLASS_ID = 4;
        int YEAR = 5;
        int GRADE = 6;
        int CLASS_NAME = 7;
        /*int SESSION_ID = 1;
        int TITLE = 2;
        int IN_MY_SCHEDULE = 3;
        int SESSION_START = 4;
        int SESSION_END = 5;
        int ROOM_NAME = 6;
        int ROOM_ID = 7;
        int HASHTAGS = 8;
        int URL = 9;
        int LIVESTREAM_URL = 10;
        int TAGS = 11;
        int SPEAKER_NAMES = 12;
        int ABSTRACT = 13;
        int COLOR = 14;
        int PHOTO_URL = 15;
        int SNIPPET = 16;*/
    }

    private static final int TAG_METADATA_TOKEN = 0x4;
}
