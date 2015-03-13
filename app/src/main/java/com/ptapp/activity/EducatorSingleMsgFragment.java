package com.ptapp.activity;


import android.app.LoaderManager;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.CursorLoader;
import android.content.Loader;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.RemoteException;
import android.provider.BaseColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ptapp.io.model.MessageDetails;
import com.ptapp.io.model.Peer2PeerMessages;
import com.ptapp.provider.PTAppContract;
import com.ptapp.provider.PTAppDatabase;
import com.ptapp.app.R;
import com.ptapp.utils.CommonConstants;
import com.ptapp.utils.CommonMethods;
import com.ptapp.utils.SharedPrefUtil;

import java.util.ArrayList;

import static com.ptapp.utils.LogUtils.LOGD;
import static com.ptapp.utils.LogUtils.LOGE;
import static com.ptapp.utils.LogUtils.LOGI;
import static com.ptapp.utils.LogUtils.LOGW;
import static com.ptapp.utils.LogUtils.makeLogTag;

/**
 * A simple {@link Fragment} subclass.
 */
//TODO: URI Query > pass the cursor rows to the adpater to bind to the listview > save into db. > pass to the api
public class EducatorSingleMsgFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = makeLogTag("EducatorMsgsFragment");
    private Uri mCurrentUri = PTAppContract.StudentAssociation.CONTENT_URI;
    Cursor mCursor = null;
    private Bundle mArguments;
    private ImageView sendMsg;
    private TextView txtMsg;
    private int studentUserId, studentId;


    //private Uri mCurrentUri = PTAppContract.StudentAssociation.CONTENT_URI;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_educator_single_msg, container, false);
        txtMsg = (TextView) root.findViewById(R.id.type_msg);
        sendMsg = (ImageView) root.findViewById(R.id.send_msg);
        sendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMyMsg();
            }
        });
        return root;
    }


    /*set clearing of the action bar and header bar*/
    public void setContentTopClearance(int clearance) {
        /*if (mCollectionView != null) {
            mCollectionView.setContentTopClearance(clearance);
        }*/
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

        LOGD(TAG, "EducatorSingleMsgsFragment reloading from arguments: " + arguments);
        mCurrentUri = arguments.getParcelable("_uri");
        if (mCurrentUri == null) {
            // if no URI, default to all sessions URI
            LOGD(TAG, "EducatorSingleMsgsFragment did not get a URL, defaulting to all students.");
            arguments.putParcelable("_uri", PTAppContract.StudentAssociation.CONTENT_URI);
            mCurrentUri = PTAppContract.StudentAssociation.CONTENT_URI;
        }
        //reloadSessionData(true); // full reload
        //setFilterAndReload();
        getLoaderManager().restartLoader(ChatQuery._TOKEN, mArguments, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int token, Bundle bundle) {
        if (token == ChatQuery._TOKEN) {
            studentId = Integer.parseInt(bundle.getString("studentId"));
            studentUserId = Integer.parseInt(bundle.getString("studentUserId"));

            String selection = PTAppContract.StudentAssociation.CLASS_SUBJECT_ID + "=?";
            String[] args = new String[]{String.valueOf(studentId)};

            LOGD(TAG, "Starting students query, selection=" + selection + " (classSubjectId=" + studentId);

            return new CursorLoader(getActivity(), mCurrentUri,
                    ChatQuery.PROJECTION, selection, args, null);
        }
        LOGW(TAG, "Invalid query token: " + token);
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        LOGI(TAG, "cursor load load finished.");
        mCursor = cursor;

        //pass to the adapter
        //updateCollectionView();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }


    private void sendMyMsg() {
        if (!txtMsg.getText().toString().isEmpty()) {

            Peer2PeerMessages p2pMessages = new Peer2PeerMessages();
            p2pMessages.senderUserId = CommonMethods.getAppUserId(getActivity());
            p2pMessages.senderRole = SharedPrefUtil.getPrefUserInRole(getActivity());
            p2pMessages.recipientUserId = studentUserId;
            p2pMessages.recipientRole = CommonConstants.ROLE_STUDENT;
            p2pMessages.studentId = studentId;

            MessageDetails message = new MessageDetails();
            //message.messageId = 0;
            //message.p2pId = 0;
            message.messageType = "";
            message.timestamp = System.currentTimeMillis();
            message.status = CommonConstants.STATUS_MSG_PENDING;
            message.data = txtMsg.getText().toString();

            LOGD(TAG, "Building content provider operations for saving the message.");
            // produce the necessary content provider operations
            ArrayList<ContentProviderOperation> batch = new ArrayList<ContentProviderOperation>();
            ContentProviderOperation.Builder builder;
            Uri allPeer2PeerMessagesUri = PTAppContract
                    .addCallerIsSyncAdapterParameter(PTAppContract.Peer2PeerMessages.CONTENT_URI);

            builder = ContentProviderOperation.newInsert(allPeer2PeerMessagesUri);
            builder.withValue(PTAppContract.Peer2PeerMessages.SENDER_USER_ID, p2pMessages.senderUserId)
                    .withValue(PTAppContract.Peer2PeerMessages.SENDER_ROLE, p2pMessages.senderRole)
                    .withValue(PTAppContract.Peer2PeerMessages.RECIPIENT_USER_ID, p2pMessages.recipientUserId)
                    .withValue(PTAppContract.Peer2PeerMessages.RECIPIENT_ROLE, p2pMessages.recipientRole)
                    .withValue(PTAppContract.Peer2PeerMessages.STUDENT_ID, p2pMessages.studentId);
            batch.add(builder.build());

            ContentProviderOperation.Builder builder2;
            Uri allMessagesUri = PTAppContract
                    .addCallerIsSyncAdapterParameter(PTAppContract.MessageDetails.CONTENT_URI);

            builder2 = ContentProviderOperation.newInsert(allMessagesUri);
            builder2.withValueBackReference(PTAppContract.MessageDetails.P2P_ID, 0)
                    .withValue(PTAppContract.MessageDetails.MESSAGE_TYPE, message.messageType)
                    .withValue(PTAppContract.MessageDetails.TIMESTAMP, message.timestamp)
                    .withValue(PTAppContract.MessageDetails.STATUS, message.status)
                    .withValue(PTAppContract.MessageDetails.DATA, message.data);
            batch.add(builder2.build());

            LOGD(TAG, "Total content provider operations: " + batch.size());

            // finally, push the changes into the Content Provider
            LOGD(TAG, "Applying " + batch.size() + " content provider operations.");
            try {
                int operations = batch.size();
                if (operations > 0) {
                    getActivity().getContentResolver().applyBatch(PTAppContract.CONTENT_AUTHORITY, batch);
                }
                LOGD(TAG, "Successfully applied " + operations + " content provider operations.");
            } catch (RemoteException ex) {
                LOGE(TAG, "RemoteException while applying content provider operations.");
                throw new RuntimeException("Error executing content provider batch operation", ex);
            } catch (OperationApplicationException ex) {
                LOGE(TAG, "OperationApplicationException while applying content provider operations.");
                throw new RuntimeException("Error executing content provider batch operation", ex);
            }

            // notify all top-level paths
            LOGD(TAG, "Notifying changes on all top-level paths on Content Resolver.");
            ContentResolver resolver = getActivity().getContentResolver();
            for (String path : PTAppContract.TOP_LEVEL_PATHS) {
                Uri uri = PTAppContract.BASE_CONTENT_URI.buildUpon().appendPath(path).build();
                resolver.notifyChange(uri, null);
            }
            LOGD(TAG, "Done saving message in the database.");

            CommonMethods.takeDbBackup(getActivity());

            //TODO:Check if network is available, then send message to server, else send when network is available
        }
    }

    private interface ChatQuery {
        int _TOKEN = 0x1;
        String[] PROJECTION = {
                PTAppDatabase.Tables.STUDENT_ASSOCIATION + "." + BaseColumns._ID,
                PTAppContract.StudentAssociation.STUDENT_ID,
                PTAppContract.Student.FIRST_NAME,
                PTAppContract.Student.LAST_NAME
        };

        int _ID = 0;
        int STUDENT_ID = 1;
        int FIRST_NAME = 2;
        int LAST_NAME = 3;
    }

    public EducatorSingleMsgFragment() {
        // Required empty public constructor
    }
}
