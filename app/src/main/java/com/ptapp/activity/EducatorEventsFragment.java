package com.ptapp.activity;


import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.BaseColumns;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ptapp.provider.PTAppContract;
import com.ptapp.app.R;
import com.ptapp.utils.ImageLoader;
import com.ptapp.utils.ParserUtils;
import com.ptapp.widget.CollectionView;
import com.ptapp.widget.CollectionViewCallbacks;

import java.util.ArrayList;
import java.util.Calendar;

import static com.ptapp.utils.LogUtils.LOGD;
import static com.ptapp.utils.LogUtils.LOGW;
import static com.ptapp.utils.LogUtils.makeLogTag;


public class EducatorEventsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>,
        CollectionViewCallbacks {

    private static final String TAG = makeLogTag("EducatorEventsFragment");

    CollectionView mCollectionView = null;
    View mEmptyView = null;
    Cursor mCursor = null;

    ImageLoader mImageLoader;

    private static final String LOADER_ARG_CURRENT_MONTH_START = "LOADER_ARG_CURRENT_MONTH_START";
    private static final String LOADER_ARG_NEXT_MONTH_START = "LOADER_ARG_NEXT_MONTH_START";

    private static final int GROUP_ID_HERO = 1000;
    private static final int GROUP_ID_NORMAL = 1001;

    //start of current and next month in millis
    private long currentMonthInMillis = 0, nextMonthInMillis = 0;

    public EducatorEventsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_educator_events, container, false);
        mCollectionView = (CollectionView) root.findViewById(R.id.events_collection_view);
        mEmptyView = root.findViewById(android.R.id.empty);
        return root;
    }

    //This is called after onCreateView and before onViewStateRestored(Bundle).
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mImageLoader = new ImageLoader(getActivity(), android.R.color.transparent);
        setFilterAndReload(0, null);
    }

    /*set clearing of the action bar and header bar*/
    public void setContentTopClearance(int clearance) {
        if (mCollectionView != null) {
            mCollectionView.setContentTopClearance(clearance);
        }
    }

    public void setFilterAndReload(int year, String topic) {
        //mFilterYear = year;
        //mFilterTopic = topic;
        currentMonthInMillis = getCurrentMonthInMillis();
        nextMonthInMillis = getNextMonthInMillis();
        Bundle args = new Bundle();
        args.putLong(LOADER_ARG_CURRENT_MONTH_START, currentMonthInMillis);
        args.putLong(LOADER_ARG_NEXT_MONTH_START, nextMonthInMillis);
        getLoaderManager().restartLoader(EventsQuery._TOKEN, args, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int token, Bundle bundle) {
        if (token == EventsQuery._TOKEN) {
            long startCurrentMonth = bundle.getLong(LOADER_ARG_CURRENT_MONTH_START);
            long startNextMonth = bundle.getLong(LOADER_ARG_NEXT_MONTH_START);

            ArrayList<String> selectionArgs = new ArrayList<String>();
            ArrayList<String> selectionClauses = new ArrayList<String>();

            if (startCurrentMonth > 0 && startNextMonth > 0) {
                selectionClauses.add(PTAppContract.Events.EVENT_START + ">=?");
                selectionArgs.add(String.valueOf(startCurrentMonth));

                selectionClauses.add(PTAppContract.Events.EVENT_START + "<?");
                selectionArgs.add(String.valueOf(startNextMonth));
            }

            String selection = selectionClauses.isEmpty() ? null :
                    ParserUtils.joinStrings(" AND ", selectionClauses, null);
            String[] args = selectionArgs.isEmpty() ? null : selectionArgs.toArray(new String[0]);

            LOGD(TAG, "Starting events query, selection=" + selection + " (startCurrentMonth=" + startCurrentMonth
                    + ", startNextMonth=" + startNextMonth);

            return new CursorLoader(getActivity(), PTAppContract.Events.CONTENT_URI,
                    EventsQuery.PROJECTION, selection, args, null);
        }
        LOGW(TAG, "Invalid query token: " + token);
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursor = data;
        updateCollectionView();
    }

    private void updateCollectionView() {
        LOGD(TAG, "Updating events collection view.");
        CollectionView.InventoryGroup curGroup = null;
        CollectionView.Inventory inventory = new CollectionView.Inventory();
        mCursor.moveToPosition(-1);
        int dataIndex = -1;
        int normalColumns = getResources().getInteger(R.integer.events_library_columns);

        boolean isEmpty = mCursor.getCount() == 0;

        while (mCursor.moveToNext()) {
            ++dataIndex;
            /*String topic = mCursor.getString(VideosQuery.TOPIC);
            boolean isHero = TextUtils.isEmpty(topic);
            int year = mCursor.getInt(VideosQuery.YEAR);
            String groupName = TextUtils.isEmpty(topic) ?
                    getString(R.string.google_i_o_year, year) : topic + " (" + year + ")";*/

            //let's keep it simple for now, keep Group_Id to normal
            boolean isHero = false;
            String groupName = "Jan 2015";

            if (curGroup == null || !curGroup.getHeaderLabel().equals(groupName)) {
                if (curGroup != null) {
                    inventory.addGroup(curGroup);
                }
                curGroup = new CollectionView.InventoryGroup(
                        isHero ? GROUP_ID_HERO : GROUP_ID_NORMAL)
                        .setDataIndexStart(dataIndex)
                        .setHeaderLabel(groupName)
                        .setShowHeader(true)
                        .setDisplayCols(isHero ? 1 : normalColumns)
                        .setItemCount(1);
            } else {
                curGroup.incrementItemCount();
            }
        }

        if (curGroup != null) {
            inventory.addGroup(curGroup);
        }

        mCollectionView.setCollectionAdapter(this);
        mCollectionView.updateInventory(inventory);

        mEmptyView.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public View newCollectionHeaderView(Context context, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        return inflater.inflate(R.layout.list_item_explore_header, parent, false);
    }

    @Override
    public void bindCollectionHeaderView(Context context, View view, int groupId, String headerLabel) {
        ((TextView) view.findViewById(android.R.id.text1)).setText(headerLabel);
    }

    @Override
    public View newCollectionItemView(Context context, int groupId, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        // TODO: if groupId is GROUP_ID_HERO, inflate different layout
        return inflater.inflate(R.layout.list_item_events, parent, false);
    }

    @Override
    public void bindCollectionItemView(Context context, View view, int groupId,
                                       int indexInGroup, int dataIndex, Object tag) {
        if (!mCursor.moveToPosition(dataIndex)) {
            return;
        }

        ImageView thumbnailView = (ImageView) view.findViewById(R.id.thumbnail);
        TextView titleView = (TextView) view.findViewById(R.id.title);
        TextView speakersView = (TextView) view.findViewById(R.id.speakers);
        TextView descriptionView = (TextView) view.findViewById(R.id.description);
        titleView.setText(mCursor.getString(EventsQuery.TITLE));
        speakersView.setText(mCursor.getString(EventsQuery.FOR_CLASSES));
        descriptionView.setText(mCursor.getString(EventsQuery.DESC));

        /*String thumbUrl = mCursor.getString(VideosQuery.THUMBNAIL_URL);*/
        String thumbUrl = "";
        if (TextUtils.isEmpty(thumbUrl)) {
            thumbnailView.setImageResource(android.R.color.transparent);
        } else {
            mImageLoader.loadImage(thumbUrl, thumbnailView);
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LOGD(TAG, "Event item clicked.");
            }
        });
    }

    private interface EventsQuery {
        int _TOKEN = 0x1;
        String[] PROJECTION = {
                BaseColumns._ID,
                PTAppContract.Events.EVENT_TITLE,
                PTAppContract.Events.EVENT_DESCRIPTION,
                PTAppContract.Events.EVENT_TYPE,
                PTAppContract.Events.EVENT_FOR_CLASSES,
                PTAppContract.Events.EVENT_START,
                PTAppContract.Events.EVENT_END
        };

        int _ID = 0;
        int TITLE = 1;
        int DESC = 2;
        int TYPE = 3;
        int FOR_CLASSES = 4;
        int START = 5;
        int END = 6;
        /*int VIDEO_ID = 1;
        int YEAR = 2;
        int TITLE = 3;
        int DESC = 4;
        int VID = 5;
        int TOPIC = 6;
        int SPEAKERS = 7;
        int THUMBNAIL_URL = 8;*/
    }

    /*Gets current month in milliseconds*/
    private long getCurrentMonthInMillis() {
        //first get today and clear the time off
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);    //reset the hour of the day to 0(clear will not reset)
        cal.clear(Calendar.MINUTE);     //reset/clear the min, sec, millis
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);

        //get start of the month in millis
        cal.set(Calendar.DAY_OF_MONTH, 1);
        return cal.getTimeInMillis();
    }

    /*Gets next month in milliseconds*/
    private long getNextMonthInMillis() {
        //first get today and clear the time off
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);    //reset the hour of the day to 0(clear will not reset)
        cal.clear(Calendar.MINUTE);     //reset/clear the min, sec, millis
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);

        //set start of the month to date 1
        cal.set(Calendar.DAY_OF_MONTH, 1);

        //start of the next month in millis
        cal.add(Calendar.MONTH, 1);  //by adding month like this, changes the year too eg. from dec 2015 to jan 2016
        return cal.getTimeInMillis();
    }
}
