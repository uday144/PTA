package com.ptapp.activity;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.MergeCursor;
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

import com.ptapp.entities.Account;
import com.ptapp.entities.Conversation;
import com.ptapp.provider.PTAppContract;
import com.ptapp.provider.PTAppDatabase;
import com.ptapp.app.R;
import com.ptapp.service.XmppConnectionService;
import com.ptapp.utils.CommonMethods;
import com.ptapp.utils.ImageLoader;
import com.ptapp.widget.CollectionView;
import com.ptapp.widget.CollectionViewCallbacks;
import com.ptapp.xmpp.jid.InvalidJidException;
import com.ptapp.xmpp.jid.Jid;

import java.util.ArrayList;
import java.util.List;

import static com.ptapp.utils.LogUtils.LOGD;
import static com.ptapp.utils.LogUtils.LOGE;
import static com.ptapp.utils.LogUtils.LOGI;
import static com.ptapp.utils.LogUtils.LOGW;
import static com.ptapp.utils.LogUtils.makeLogTag;

public class EducatorMsgsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, CollectionViewCallbacks {

    private static final String TAG = makeLogTag("EducatorMsgsFragment");

    CollectionView mCollectionView = null;
    View mEmptyView = null;
    Cursor mCursor = null;

    ImageLoader mImageLoader;

    private Bundle mArguments;
    private Uri mCurrentUri = PTAppContract.StudentAssociation.CONTENT_URI;

    private static final int GROUP_ID_HERO = 1000;
    private static final int GROUP_ID_NORMAL = 1001;

    private String classTitle = "";
    private Jid groupJid = null;
    private int groupId = 0;
    private String grpName = "";

    //set values for the columns of the class/group row to add
    private static final String GROUP_ROW_ID = "-1";
    private static final String GROUP_ROW_STUDENT_ID = "-1";
    private static final String GROUP_ROW_FIRST_NAME = "Class/Group";
    private static final String GROUP_ROW_LAST_NAME = "";

    /*Region xmpp*/
    private Account mAccount;
    private List<Conversation> conversationList = new ArrayList<>();
    private Conversation mSelectedConversation = null;

    /*End region xmpp*/

    public EducatorMsgsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_educator_msgs, container, false);
        mCollectionView = (CollectionView) root.findViewById(R.id.msgs_collection_view);
        mEmptyView = root.findViewById(android.R.id.empty);
        return root;
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

        LOGD(TAG, "EducatorMsgsFragment reloading from arguments: " + arguments);
        mCurrentUri = arguments.getParcelable("_uri");
        if (mCurrentUri == null) {
            // if no URI, default to all sessions URI
            LOGD(TAG, "EducatorMsgsFragment did not get a URL, defaulting to all students.");
            arguments.putParcelable("_uri", PTAppContract.StudentAssociation.CONTENT_URI);
            mCurrentUri = PTAppContract.StudentAssociation.CONTENT_URI;
        }
        //reloadSessionData(true); // full reload
        setFilterAndReload();
    }

    //This is called after onCreateView and before onViewStateRestored(Bundle).
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mImageLoader = new ImageLoader(getActivity(), android.R.color.transparent);
        //setFilterAndReload();
    }

    /*set clearing of the action bar and header bar*/
    public void setContentTopClearance(int clearance) {
        if (mCollectionView != null) {
            mCollectionView.setContentTopClearance(clearance);
        }
    }

    public void setFilterAndReload() {

        getLoaderManager().restartLoader(MsgsQuery._TOKEN, mArguments, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int token, Bundle bundle) {
        if (token == MsgsQuery._TOKEN) {

            int classSubjectId = Integer.parseInt(bundle.getString(EducatorMsgsActivity.BUNDLE_CLASS_SUBJECT_ID));
            //classTitle = bundle.getString("classTitle");
            try {
                groupJid = Jid.fromString(bundle.getString(EducatorMsgsActivity.BUNDLE_GROUP_JID));
            } catch (InvalidJidException e) {
                //TODO:include the Error reporting functionality, printing stacktrace and sending to app server.
            }
            groupId = bundle.getInt(EducatorMsgsActivity.BUNDLE_GROUP_ID);
            /*grpName = bundle.getString(EducatorMsgsActivity.BUNDLE_CLASS_NAME) + ":"
                    + bundle.getString(EducatorMsgsActivity.BUNDLE_COURSE_NAME);*/
            grpName = bundle.getString(EducatorMsgsActivity.BUNDLE_COURSE_NAME);
            /*ArrayList<String> selectionArgs = new ArrayList<String>();
            ArrayList<String> selectionClauses = new ArrayList<String>();*/

            /*String selection = selectionClauses.isEmpty() ? null :
                    ParserUtils.joinStrings(" AND ", selectionClauses, null);
            String[] args = selectionArgs.isEmpty() ? null : selectionArgs.toArray(new String[0]);*/

            String selection = PTAppContract.StudentAssociation.CLASS_SUBJECT_ID + "=? ";
            String[] args = new String[]{String.valueOf(classSubjectId)};

            LOGD(TAG, "Starting students query, selection=" + selection + " (classSubjectId=" + classSubjectId);

            return new CursorLoader(getActivity(), mCurrentUri,
                    MsgsQuery.PROJECTION, selection, args, null);
        }
        LOGW(TAG, "Invalid query token: " + token);
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {

        LOGI(TAG, "Going to add an extra row into the cursor, for a group...");
        MatrixCursor extraRows = new MatrixCursor(MsgsQuery.PROJECTION);
        extraRows.addRow(new String[]{GROUP_ROW_ID, GROUP_ROW_STUDENT_ID, GROUP_ROW_FIRST_NAME, GROUP_ROW_LAST_NAME});
        Cursor c = new MergeCursor(new Cursor[]{extraRows, cursor});
        mCursor = c;
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

    @Override
    public View newCollectionHeaderView(Context context, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        return inflater.inflate(R.layout.list_item_explore_header, parent, false);
    }

    @Override
    public void bindCollectionHeaderView(Context context, View view, int groupId, String headerLabel) {
        ((TextView) view.findViewById(android.R.id.text1)).setText(headerLabel);
        //view.setVisibility(View.GONE);
    }

    @Override
    public View newCollectionItemView(Context context, int groupId, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        // TODO: if groupId is GROUP_ID_HERO, inflate different layout
        return inflater.inflate(R.layout.list_item_msgs, parent, false);
    }

    @Override
    public void bindCollectionItemView(Context context, View view, int groupId,
                                       int indexInGroup, int dataIndex, Object tag) {
        if (!mCursor.moveToPosition(dataIndex)) {
            return;
        }

        ImageView thumbnailView = (ImageView) view.findViewById(R.id.thumbnail);
        TextView titleView = (TextView) view.findViewById(R.id.title);
        //TextView speakersView = (TextView) view.findViewById(R.id.speakers);
        TextView descriptionView = (TextView) view.findViewById(R.id.description);
        TextView countView = (TextView) view.findViewById(R.id.msg_count);


        Boolean isClassRow = false;
        if (mCursor.getString(MsgsQuery._ID).equals(GROUP_ROW_ID)) {
            isClassRow = true;
        }

        final String studentFullName = mCursor.getString(MsgsQuery.FIRST_NAME) + " " + mCursor.getString(MsgsQuery.LAST_NAME);
        titleView.setText(studentFullName);
        //speakersView.setText("");
        //descriptionView.setText("dummy test message");

        /*String thumbUrl = mCursor.getString(VideosQuery.THUMBNAIL_URL);*/
        String thumbUrl = "";
        if (TextUtils.isEmpty(thumbUrl)) {
            //thumbnailView.setImageResource(android.R.color.transparent);
            thumbnailView.setImageResource(R.drawable.nophotoavailable);
        } else {
            mImageLoader.loadImage(thumbUrl, thumbnailView);
        }

        final Boolean isGroupRow = isClassRow;
        final int student_Id = mCursor.getInt(MsgsQuery.STUDENT_ID);
        final Jid groupJid = this.groupJid;
        final int grpId = this.groupId;
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isGroupRow) {
                    LOGD(TAG, "Group Message item clicked.");
                    Intent intent = new Intent(getActivity(), EducatorGroupMsgActivity.class);
                    startActivity(intent);
                } else {
                    LOGD(TAG, "Student Message item clicked.");

                    //TODO:Fetch the App user's JID from the sharedPref, instead of hardcoded string
                    //TODO:Integrate the Birender's code into this project, fix the look of the 1-1 msgs screen and then start the parent screens


                    Jid appUserJid = null;
                    Jid otherEndJid = null;
                    try {
                        //TODO: AppUserJid will be stored in the roles, in sharedpref, when we will get from server
                        appUserJid = Jid.fromString(CommonMethods.getAppUserId(getActivity()) + ".e@" + getString(R.string.chat_server));
                        if (!CommonMethods.getAppUserJid(getActivity()).isEmpty()) {
                            appUserJid = Jid.fromString(CommonMethods.getAppUserJid(getActivity()));
                        }
                        otherEndJid = groupJid;

                    } catch (final InvalidJidException e) {
                        LOGE(TAG, getString(R.string.invalid_jid));
                        //return;
                    }

                    //SCH: Reuse existing connection
                    XmppConnectionService xmppConnectionService = XmppConnectionService.getXmppConnectionService();
//                    if (null != xmppConnectionService){
                        Account account = xmppConnectionService.findAccountByJid(appUserJid);
                        Conversation conversation = xmppConnectionService
                                .findOrCreateConversation(account, otherEndJid, false, grpName, grpId, student_Id, 0);

                        CommonMethods.switchToConversation(getActivity(), conversation);
//                    }
                    /*Intent intent = new Intent(getActivity(), EducatorSingleMsgActivity.class);
                    intent.putExtra("studentId", student_Id);
                    intent.putExtra("studentUserId", studentUserId);
                    startActivity(intent);*/
                }
            }
        });
    }

    public Conversation getSelectedConversation() {
        return this.mSelectedConversation;
    }

    public void setSelectedConversation(Conversation conversation) {
        this.mSelectedConversation = conversation;
    }

    private interface MsgsQuery {
        int _TOKEN = 0x1;
        String[] PROJECTION = {
                PTAppDatabase.Tables.STUDENT_ASSOCIATION + "." + BaseColumns._ID,
                PTAppDatabase.Tables.STUDENT_ASSOCIATION + "." + PTAppContract.StudentAssociation.STUDENT_ID,
                PTAppDatabase.Tables.STUDENT + "." + PTAppContract.Student.FIRST_NAME,
                PTAppDatabase.Tables.STUDENT + "." + PTAppContract.Student.LAST_NAME,
        };

        int _ID = 0;
        int STUDENT_ID = 1;
        int FIRST_NAME = 2;
        int LAST_NAME = 3;
    }
}
