package com.ptapp.activity;


import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.BaseColumns;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ptapp.bo.StudentBO;
import com.ptapp.entities.Account;
import com.ptapp.entities.Conversation;
import com.ptapp.provider.PTAppContract;
import com.ptapp.provider.PTAppDatabase;
import com.ptapp.app.R;
import com.ptapp.utils.CommonMethods;
import com.ptapp.utils.ImageLoader;
import com.ptapp.widget.CollectionView;
import com.ptapp.widget.CollectionViewCallbacks;
import com.ptapp.xmpp.jid.InvalidJidException;
import com.ptapp.xmpp.jid.Jid;

import java.util.ArrayList;

import static com.ptapp.utils.LogUtils.LOGD;
import static com.ptapp.utils.LogUtils.LOGE;
import static com.ptapp.utils.LogUtils.LOGI;
import static com.ptapp.utils.LogUtils.LOGW;
import static com.ptapp.utils.LogUtils.makeLogTag;

public class ParentHomeFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, CollectionViewCallbacks {

    private static final String TAG = makeLogTag(ParentHomeFragment.class);

    private static final String STUDENT_ID = "student_id";

    CollectionView mCollectionView = null;
    View mEmptyView = null;
    Cursor mCursor = null;

    ImageLoader mImageLoader;

    private Bundle mArguments;
    private Uri mCurrentUri = PTAppContract.ClassSubject.CONTENT_KID_CLASS_SUBJECT_URI;

    private static final int GROUP_ID_HERO = 1000;
    private static final int GROUP_ID_NORMAL = 1001;

    /*private String classTitle = "";
    private String groupJid = "";
    private int groupId = 0;
    private String grpName = "";*/
    private int studentId = -1;

    public ParentHomeFragment() {
        // Required empty public constructor
    }

    void reloadFromArguments(Bundle arguments) {
        // Load new arguments
        if (arguments == null) {
            arguments = new Bundle();
        } else {
            // since we might make changes, don't meddle with caller's copy
            arguments = (Bundle) arguments.clone();
        }

        // save arguments so we can reuse it when reloading from content observer events
        mArguments = arguments;

        LOGD(TAG, "reloading from arguments: " + arguments);
        mCurrentUri = arguments.getParcelable("_uri");
        if (mCurrentUri == null) {
            // if no URI, default to all kid class subject URI
            LOGD(TAG, "did not get a URL, defaulting to all class_subjects.");
            arguments.putParcelable("_uri", PTAppContract.ClassSubject.CONTENT_KID_CLASS_SUBJECT_URI);
            mCurrentUri = PTAppContract.ClassSubject.CONTENT_KID_CLASS_SUBJECT_URI;
        }
        //reloadSessionData(true); // full reload
        setFilterAndReload();
    }

    public void setFilterAndReload() {

        //get the student's userId from the sharedPref, which one is first
            /*int studentId = 6;*/
            /*int studentId = -1;*/
        //TODO:need to get from roles sharedpref, when server will start sending
        ArrayList<StudentBO> kids = CommonMethods.getParentKids(getActivity());
        /*if (kids != null) {
            studentId = kids.get(0).getStudentId();
        }*/
        if (studentId < 1) {
            studentId = 6;
        }

        mArguments.putInt(STUDENT_ID, studentId);
        getLoaderManager().restartLoader(GrpQuery._TOKEN, mArguments, this);
    }

    /*set clearing of the action bar and header bar*/
    public void setContentTopClearance(int clearance) {
        if (mCollectionView != null) {
            mCollectionView.setContentTopClearance(clearance);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_parent_home, container, false);
        mCollectionView = (CollectionView) root.findViewById(R.id.classes_list_collection_view);
        mEmptyView = root.findViewById(android.R.id.empty);
        return root;
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
        return inflater.inflate(R.layout.list_item_msgs, parent, false);
    }

    @Override
    public void bindCollectionItemView(Context context, View view, int groupId, int indexInGroup, int dataIndex, Object tag) {
        if (!mCursor.moveToPosition(dataIndex)) {
            return;
        }

        ImageView thumbnailView = (ImageView) view.findViewById(R.id.thumbnail);
        TextView titleView = (TextView) view.findViewById(R.id.title);
        //TextView speakersView = (TextView) view.findViewById(R.id.speakers);
        TextView descriptionView = (TextView) view.findViewById(R.id.description);
        TextView countView = (TextView) view.findViewById(R.id.msg_count);

        if(countView!=null){
            countView.setText(mCursor.getString(GrpQuery.MEMBER_COUNT));
        }

        Boolean isClassRow = false;
        /*if (mCursor.getString(MsgsQuery._ID).equals(GROUP_ROW_ID)) {
            isClassRow = true;
        }*/

        /*String course = mCursor.getString(GrpQuery.SUBJECT_NAME);
        String className = mCursor.getString(GrpQuery.CLASS_NAME);
        titleView.setText(course + ", " + className + " " + mCursor.getString(GrpQuery.SECTION));*/
        String groupName = mCursor.getString(GrpQuery.GROUP_NAME);
        titleView.setText(groupName);
        //speakersView.setText("");
        //descriptionView.setText("dummy test message");

        /*String thumbUrl = mCursor.getString(VideosQuery.THUMBNAIL_URL);*/
        String thumbUrl = "";
        if (TextUtils.isEmpty(thumbUrl)) {
            //thumbnailView.setImageResource(android.R.color.transparent);
            /*thumbnailView.setImageResource(R.drawable.nophotoavailable);*/
            thumbnailView.setImageResource(CommonMethods.getSubjectResId(groupName));
        } else {
            mImageLoader.loadImage(thumbUrl, thumbnailView);
        }

        final Boolean isGroupRow = isClassRow;
        /*final int student_Id = 6;*/
        final int student_Id = studentId;
        /*final int studentUserId = mCursor.getInt(MsgsQuery.STUDENT_USER_ID);*/
        //final String teacherJid = mCursor.getString(GrpQuery.EDUCATOR_JID);
        Jid groupJid = null;
        try {
            groupJid = Jid.fromString(mCursor.getString(GrpQuery.GROUP_JID));
            /*groupJid = Jid.fromString("x4.g@xmpp.jp");*/
        } catch (InvalidJidException e) {
            e.printStackTrace();
        }

        final int grpId = mCursor.getInt(GrpQuery.GROUP_ID);
        final String grpName = groupName;
        final Jid grpJid = groupJid;

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isGroupRow) {
                    LOGD(TAG, "Group Message item clicked.");
                    Intent intent = new Intent(getActivity(), EducatorGroupMsgActivity.class);
                    startActivity(intent);
                } else {
                    LOGD(TAG, "Student Message item clicked.");

                    Jid appUserJid = null;
                    Jid otherEndJid = null;
                    try {
                        //TODO: AppUserJid will be stored in the roles, in sharedpref, when we will get from server
                        String makeJid = null;
                        Uri parentUri = PTAppContract.Parent.CONTENT_URI;
                        String[] projection = {PTAppContract.Parent.JID};
                        String selection = PTAppContract.Parent.USER_ID + "=?";
                        String[] args = {String.valueOf(CommonMethods.getAppUserId(getActivity()))};
                        Cursor c = getActivity().getContentResolver().query(parentUri, projection, selection, args, null);
                        if (c.moveToFirst()) {
                            makeJid = c.getString(0);
                        }
                        /*appUserJid = Jid.fromString("x" + CommonMethods.getAppUserId(getActivity()) + ".p@" + getString(R.string.chat_server));
                        if (!CommonMethods.getAppUserJid(getActivity()).isEmpty()) {
                            appUserJid = Jid.fromString(CommonMethods.getAppUserJid(getActivity()));
                        }*/
                        appUserJid = Jid.fromString(makeJid);
                        /*otherEndJid = Jid.fromString(teacherJid);*/
                        otherEndJid = grpJid;

                    } catch (final InvalidJidException e) {
                        LOGE(TAG, getString(R.string.invalid_jid));
                        //return;
                    }
                    Account account = ((ParentHomeActivity) getActivity()).xmppConnectionService.findAccountByJid(appUserJid);
                    Conversation conversation = ((ParentHomeActivity) getActivity()).xmppConnectionService
                            .findOrCreateConversation(account, otherEndJid, false, grpName, grpId, student_Id, 0);

                    ((ParentHomeActivity) getActivity()).switchToConversation(conversation);

                    /*Intent intent = new Intent(getActivity(), EducatorSingleMsgActivity.class);
                    intent.putExtra("studentId", student_Id);
                    intent.putExtra("studentUserId", studentUserId);
                    startActivity(intent);*/
                }
            }
        });
    }

    @Override
    public Loader<Cursor> onCreateLoader(int token, Bundle bundle) {
        if (token == GrpQuery._TOKEN) {

            //int studentId = SharedPrefUtil.getPrefLastViewedKidStudentId(getActivity());


            String selectionWhere = PTAppContract.StudentAssociation.STUDENT_ID + " = ?";
            String[] selectionArgs = new String[]{String.valueOf(bundle.getInt(STUDENT_ID))};
            LOGD(TAG, "Creating parent loader for " + mCurrentUri + ", selectionWhere " + selectionWhere);

            return new CursorLoader(getActivity(), mCurrentUri, GrpQuery.PROJECTION, selectionWhere, selectionArgs, null);
        }
        LOGW(TAG, "Invalid query token: " + token);
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        LOGI(TAG, "Going to add an extra row into the cursor, for a group...");
        /*MatrixCursor extraRows = new MatrixCursor(MsgsQuery.PROJECTION);
        extraRows.addRow(new String[]{GROUP_ROW_ID, GROUP_ROW_STUDENT_ID, GROUP_ROW_FIRST_NAME, GROUP_ROW_LAST_NAME, "0", ""});
        Cursor c = new MergeCursor(new Cursor[]{extraRows, cursor});*/
        mCursor = cursor;
        updateCollectionView();
    }

    private void updateCollectionView() {
        LOGD(TAG, "Updating messages collection view.");
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
            String groupName = "";  // not showing the header, so need to give any groupName

            if (curGroup == null || !curGroup.getHeaderLabel().equals(groupName)) {
                if (curGroup != null) {
                    inventory.addGroup(curGroup);
                }
                curGroup = new CollectionView.InventoryGroup(
                        isHero ? GROUP_ID_HERO : GROUP_ID_NORMAL)
                        .setDataIndexStart(dataIndex)
                        .setHeaderLabel(groupName)
                        .setShowHeader(false)
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
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    //This is called after onCreateView and before onViewStateRestored(Bundle).
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mImageLoader = new ImageLoader(getActivity(), android.R.color.transparent);
        //setFilterAndReload();
    }

    private interface GrpQuery {
        int _TOKEN = 0x1;
        String[] PROJECTION = {
                PTAppDatabase.Tables.DEFAULT_GROUPS + "." + BaseColumns._ID,
                PTAppContract.DefaultGroups.GROUP_NAME,
                PTAppDatabase.Tables.DEFAULT_GROUPS + "." + PTAppContract.DefaultGroups.JID,
                PTAppDatabase.Tables.DEFAULT_GROUPS + "." + PTAppContract.DefaultGroups.MEMBER_COUNT
                /*PTAppDatabase.Tables.STAFF + "." + PTAppContract.Staff.JID*/
        };

        //int _ID = 0;
        int GROUP_ID = 0;
        int GROUP_NAME = 1;
        int GROUP_JID = 2;
        int MEMBER_COUNT = 3;
        //int EDUCATOR_JID = 3;

    }
}
