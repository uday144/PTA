package com.ptapp.provider;


import android.content.ContentProvider;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.ptapp.provider.PTAppContract.Classes;
import com.ptapp.provider.PTAppContract.Events;
import com.ptapp.provider.PTAppDatabase.Tables;
import com.ptapp.utils.SelectionBuilder;

import java.util.ArrayList;
import java.util.Arrays;

import static com.ptapp.utils.LogUtils.LOGV;
import static com.ptapp.utils.LogUtils.makeLogTag;

/**
 * Provider that stores {@link com.ptapp.provider.PTAppContract} data. Data is usually inserted
 * by {@lnk com.google.samples.apps.iosched.sync.SyncHelper},
 * and queried by various
 * {@link android.app.Activity} instances.
 */
public class PTAppProvider extends ContentProvider {
    private static final String TAG = makeLogTag(PTAppProvider.class);

    private PTAppDatabase mOpenHelper;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    /* For the purposes of this tutorial, we will be configuring our UriMatcher instance
     to return a value of 1 when the URI references the entire table, and a value of 2
     when the URI references the ID of a specific row in the table.
     Before working on creating the URIMatcher instance, we will first create two integer variables
     to represent the two URI types, WE WILL start with '100':*/
    private static final int EVENTS = 100;
    private static final int EVENTS_ID = 101;

    private static final int ADDRESS = 200;
    private static final int ADDRESS_ID = 201;

    private static final int INSTITUTE = 300;
    private static final int INSTITUTE_ID = 301;

    private static final int BRANCH = 400;
    private static final int BRANCH_ID = 401;

    private static final int ACADEMIC_SESSION = 500;
    private static final int ACADEMIC_SESSION_ID = 501;

    private static final int SUBJECT = 600;
    private static final int SUBJECT_ID = 601;

    private static final int CLASS = 700;
    private static final int CLASS_ID = 701;

    private static final int CLASS_SUBJECT = 800;
    private static final int CLASS_SUBJECT_ID = 801;
    private static final int KID_CLASS_SUBJECTS = 802;

    private static final int USER = 900;
    private static final int USER_ID = 901;

    private static final int STAFF = 1000;
    private static final int STAFF_JOIN_GROUPS = 1001;
    private static final int STAFF_ID = 1002;

    private static final int STAFF_ENGAGEMENT = 1100;
    private static final int STAFF_ENGAGEMENT_GROUPS = 1101;
    private static final int STAFF_ENGAGEMENT_ID = 1102;

    private static final int STUDENT = 1200;
    private static final int STUDENT_ID = 1201;

    private static final int STUDENT_ASSOCIATION = 1300;
    private static final int STUDENT_ASSOCIATION_ID = 1301;

    private static final int PARENT = 1400;
    private static final int PARENT_ID = 1401;
    private static final int PARENT_RELATION_STUDENT = 1402;

    private static final int PARENT_CHILD_RELATION = 1500;
    private static final int PARENT_CHILD_RELATION_ID = 1501;

    private static final int DEFAULT_GROUPS = 1600;
    private static final int DEFAULT_GROUPS_ID = 1601;

    private static final int PEER2PEER_MESSAGES = 1700;
    private static final int PEER2PEER_MESSAGES_ID = 1701;

    private static final int MESSAGE_DETAILS = 1800;
    private static final int MESSAGE_DETAILS_ID = 1801;


    /**
     * Build and return a {@link UriMatcher} that catches all {@link Uri}
     * variations supported by this {@link ContentProvider}.
     */
    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = PTAppContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, "events", EVENTS);
        matcher.addURI(authority, "events/*", EVENTS_ID);

        matcher.addURI(authority, "address", ADDRESS);
        matcher.addURI(authority, "address/*", ADDRESS_ID);

        matcher.addURI(authority, "institute", INSTITUTE);
        matcher.addURI(authority, "institute/*", INSTITUTE_ID);


        matcher.addURI(authority, "branch", BRANCH);
        matcher.addURI(authority, "branch/*", BRANCH_ID);

        matcher.addURI(authority, "academic_session", ACADEMIC_SESSION);
        matcher.addURI(authority, "academic_session/*", ACADEMIC_SESSION_ID);

        matcher.addURI(authority, "subject", SUBJECT);
        matcher.addURI(authority, "subject/*", SUBJECT_ID);

        matcher.addURI(authority, "class", CLASS);
        matcher.addURI(authority, "class/*", CLASS_ID);

        matcher.addURI(authority, "class_subject", CLASS_SUBJECT);
        matcher.addURI(authority, "class_subject/kid", KID_CLASS_SUBJECTS);
        matcher.addURI(authority, "class_subject/*", CLASS_SUBJECT_ID);


        matcher.addURI(authority, "user", USER);
        matcher.addURI(authority, "user/*", USER_ID);

        matcher.addURI(authority, "staff", STAFF);
        matcher.addURI(authority, "staff/join_groups", STAFF_JOIN_GROUPS);    //Staff's JId for a particular group
        matcher.addURI(authority, "staff/*", STAFF_ID);

        matcher.addURI(authority, "staff_engagement", STAFF_ENGAGEMENT);
        matcher.addURI(authority, "staff_engagement/groups", STAFF_ENGAGEMENT_GROUPS);
        matcher.addURI(authority, "staff_engagement/*", STAFF_ENGAGEMENT_ID);

        matcher.addURI(authority, "student", STUDENT);
        matcher.addURI(authority, "student/*", STUDENT_ID);

        matcher.addURI(authority, "student_association", STUDENT_ASSOCIATION);
        matcher.addURI(authority, "student_association/*", STUDENT_ASSOCIATION_ID);

        matcher.addURI(authority, "parent", PARENT);
        matcher.addURI(authority, "parent/relation_student", PARENT_RELATION_STUDENT);
        matcher.addURI(authority, "parent/*", PARENT_ID);

        matcher.addURI(authority, "parent_child_relation", PARENT_CHILD_RELATION);
        matcher.addURI(authority, "parent_child_relation/*", PARENT_CHILD_RELATION_ID);

        matcher.addURI(authority, "default_groups", DEFAULT_GROUPS);
        matcher.addURI(authority, "default_groups/*", DEFAULT_GROUPS_ID);

        matcher.addURI(authority, "peer2peer_messages", PEER2PEER_MESSAGES);
        matcher.addURI(authority, "peer2peer_messages/*", PEER2PEER_MESSAGES_ID);

        matcher.addURI(authority, "message_details", MESSAGE_DETAILS);
        matcher.addURI(authority, "message_details/*", MESSAGE_DETAILS_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new PTAppDatabase(getContext());
        return true;
    }

    private void deleteDatabase() {
        // TODO: wait for content provider operations to finish, then tear down
        mOpenHelper.close();
        Context context = getContext();
        PTAppDatabase.deleteDatabase(context);
        mOpenHelper = new PTAppDatabase(getContext());
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        final SQLiteDatabase db = mOpenHelper.getReadableDatabase();

        final int match = sUriMatcher.match(uri);

        // avoid the expensive string concatenation below if not loggable
        if (Log.isLoggable(TAG, Log.VERBOSE)) {
            LOGV(TAG, "uri=" + uri + " match=" + match + " proj=" + Arrays.toString(projection) +
                    " selection=" + selection + " args=" + Arrays.toString(selectionArgs) + ")");
        }

        switch (match) {
            default: {
                // Most cases are handled with simple SelectionBuilder
                final SelectionBuilder builder = buildExpandedSelection(uri, match);

                boolean distinct = !TextUtils.isEmpty(
                        uri.getQueryParameter(PTAppContract.QUERY_PARAMETER_DISTINCT));

                Cursor cursor = builder
                        .where(selection, selectionArgs)
                        .query(db, distinct, projection, sortOrder, null);
                Context context = getContext();
                if (null != context) {
                    cursor.setNotificationUri(context.getContentResolver(), uri);
                }
                return cursor;
            }
            /*case SEARCH_SUGGEST: {

            }*/
        }

    }

    /**
     * Build an advanced {@link SelectionBuilder} to match the requested
     * {@link Uri}. This is usually only used by {@link #query}, since it
     * performs table joins useful for {@link Cursor} data.
     */
    private SelectionBuilder buildExpandedSelection(Uri uri, int match) {
        final SelectionBuilder builder = new SelectionBuilder();

        switch (match) {
            case EVENTS: {
                return builder.table(Tables.EVENTS);
            }
            case ADDRESS: {
                return builder.table(Tables.ADDRESS);
            }
            case ADDRESS_ID: {
                final String addressId = PTAppContract.Address.getAddressId(uri);
                return builder.table(Tables.ADDRESS).where(PTAppContract.Address._ID + "=?", addressId);
            }
            case INSTITUTE: {
                return builder.table(Tables.INSTITUTE);
            }
            case INSTITUTE_ID: {
                final String instituteId = PTAppContract.Institute.getInstituteId(uri);
                return builder.table(Tables.INSTITUTE).where(PTAppContract.Institute._ID + "=?", instituteId);
            }
            case BRANCH: {
                return builder.table(Tables.BRANCH);
            }
            case BRANCH_ID: {
                final String branchId = PTAppContract.Branch.getBranchId(uri);
                return builder.table(Tables.BRANCH).where(PTAppContract.Branch._ID + "=?", branchId);
            }
            case ACADEMIC_SESSION: {
                return builder.table(Tables.ACADEMIC_SESSION);
            }
            case ACADEMIC_SESSION_ID: {
                final String academicSessionId = PTAppContract.AcademicSession.getAcademicSessionId(uri);
                return builder.table(Tables.ACADEMIC_SESSION).where(PTAppContract.AcademicSession._ID + "=?", academicSessionId);
            }
            case SUBJECT: {
                return builder.table(Tables.SUBJECT);
            }
            case SUBJECT_ID: {
                final String subjectId = PTAppContract.Subject.getSubjectId(uri);
                return builder.table(Tables.SUBJECT).where(PTAppContract.Subject._ID + "=?", subjectId);
            }
            case CLASS: {
                return builder.table(Tables.CLASS);
            }
            case CLASS_ID: {
                final String classId = Classes.getClassId(uri);
                return builder.table(Tables.CLASS).where(Classes._ID + "=?", classId);
            }
            case CLASS_SUBJECT: {
                return builder.table(Tables.CLASS_SUBJECT);
                /*return builder.table(Tables.STAFF_ENGAGEMENT_JOIN_CLASS_SUBJECT);*/
            }
            case KID_CLASS_SUBJECTS: {
                return builder.table(Tables.STUDENT_ASSOCIATION_JOIN_DEFAULT_GROUPS);
            }
            case USER: {
                return builder.table(Tables.USER);
            }
            case USER_ID: {
                final String userId = PTAppContract.User.getUserId(uri);
                return builder.table(Tables.USER).where(PTAppContract.User._ID + "=?", userId);
            }
            case STAFF: {
                return builder.table(Tables.STAFF);
            }
            case STAFF_JOIN_GROUPS: {
                //Staff's JId for a particular group
                return builder.table(Tables.STAFF_JOIN_ENGAGEMENT_JOIN_DEFAULT_GROUPS);
            }
            case STAFF_ENGAGEMENT: {
                return builder.table(Tables.STAFF_ENGAGEMENT);
            }
            case STAFF_ENGAGEMENT_GROUPS: {
                return builder.table(Tables.STAFF_ENGAGEMENT_JOIN_DEFAULT_GROUPS);
            }
            case STUDENT: {
                return builder.table(Tables.STUDENT);
            }
            case STUDENT_ID: {
                final String studentId = PTAppContract.Student.getStudentId(uri);
                return builder.table(Tables.STUDENT).where(PTAppContract.Student._ID + "=?", studentId);
            }
            case STUDENT_ASSOCIATION: {
                /*return builder.table(Tables.STUDENT_ASSOCIATION);*/
                return builder.table(Tables.STUDENT_ASSOCIATION_JOIN_STUDENT);
            }
            case PARENT: {
                return builder.table(Tables.PARENT);
            }
            case PARENT_RELATION_STUDENT: {
                return builder.table(Tables.PARENT_JOIN_PARENT_CHILD_RELATION_JOIN_STUDENT);
            }
            case PARENT_CHILD_RELATION: {
                return builder.table(Tables.PARENT_JOIN_PARENT_CHILD_RELATION);
            }
            case DEFAULT_GROUPS: {
                return builder.table(Tables.DEFAULT_GROUPS);
            }
            case DEFAULT_GROUPS_ID: {
                final String groupId = PTAppContract.DefaultGroups.getDefaultGroupsId(uri);
                return builder.table(Tables.DEFAULT_GROUPS).where(PTAppContract.DefaultGroups._ID + "=?",
                        groupId);
            }
            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }
    }

    /*This method is used handle requests for the MIME type of the data at the given URI.
    We use either vnd.android.cursor.item or vnd.android.cursor.dir/*/
    /*vnd.android.cursor.item is used to represent specific item. Another one is used to specify all items.*/
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case EVENTS:
                //return CalendarEventMapper.CONTENT_TYPE;
            case EVENTS_ID:
                //return CalendarEventMapper.CONTENT_ITEM_TYPE;
            case ADDRESS:
                return PTAppContract.Address.CONTENT_TYPE;
            case ADDRESS_ID:
                return PTAppContract.Address.CONTENT_ITEM_TYPE;
            case INSTITUTE:
                return PTAppContract.Institute.CONTENT_TYPE;
            case INSTITUTE_ID:
                return PTAppContract.Institute.CONTENT_ITEM_TYPE;
            case BRANCH:
                return PTAppContract.Branch.CONTENT_TYPE;
            case BRANCH_ID:
                return PTAppContract.Branch.CONTENT_ITEM_TYPE;
            case ACADEMIC_SESSION:
                return PTAppContract.AcademicSession.CONTENT_TYPE;
            case ACADEMIC_SESSION_ID:
                return PTAppContract.AcademicSession.CONTENT_ITEM_TYPE;
            case SUBJECT:
                return PTAppContract.Subject.CONTENT_TYPE;
            case SUBJECT_ID:
                return PTAppContract.Subject.CONTENT_ITEM_TYPE;
            case CLASS:
                return PTAppContract.Classes.CONTENT_TYPE;
            case CLASS_ID:
                return Classes.CONTENT_ITEM_TYPE;
            case CLASS_SUBJECT:
                return PTAppContract.ClassSubject.CONTENT_TYPE;
            case CLASS_SUBJECT_ID:
                return PTAppContract.ClassSubject.CONTENT_ITEM_TYPE;
            case USER:
                return PTAppContract.User.CONTENT_TYPE;
            case USER_ID:
                return PTAppContract.User.CONTENT_ITEM_TYPE;
            /*case STAFF_CLASS_SUBJECTS:
                return PTAppContract.ClassSubject.CONTENT_TYPE;*/
            case STAFF:
                return PTAppContract.Staff.CONTENT_TYPE;
            case STAFF_ID:
                return PTAppContract.Staff.CONTENT_ITEM_TYPE;
            case STAFF_ENGAGEMENT:
                return PTAppContract.StaffEngagement.CONTENT_TYPE;
            case STAFF_ENGAGEMENT_ID:
                return PTAppContract.StaffEngagement.CONTENT_ITEM_TYPE;
            case STUDENT:
                return PTAppContract.Student.CONTENT_TYPE;
            case STUDENT_ID:
                return PTAppContract.Student.CONTENT_ITEM_TYPE;
            case STUDENT_ASSOCIATION:
                return PTAppContract.StudentAssociation.CONTENT_TYPE;
            case STUDENT_ASSOCIATION_ID:
                return PTAppContract.StudentAssociation.CONTENT_ITEM_TYPE;
            case PARENT:
                return PTAppContract.Parent.CONTENT_TYPE;
            case PARENT_ID:
                return PTAppContract.Parent.CONTENT_ITEM_TYPE;
            case PARENT_CHILD_RELATION:
                return PTAppContract.ParentChildRelation.CONTENT_TYPE;
            case PARENT_CHILD_RELATION_ID:
                return PTAppContract.ParentChildRelation.CONTENT_ITEM_TYPE;
            case DEFAULT_GROUPS:
                return PTAppContract.DefaultGroups.CONTENT_TYPE;
            case DEFAULT_GROUPS_ID:
                return PTAppContract.DefaultGroups.CONTENT_ITEM_TYPE;
            case PEER2PEER_MESSAGES:
                return PTAppContract.Peer2PeerMessages.CONTENT_TYPE;
            case PEER2PEER_MESSAGES_ID:
                return PTAppContract.Peer2PeerMessages.CONTENT_ITEM_TYPE;
            case MESSAGE_DETAILS:
                return PTAppContract.MessageDetails.CONTENT_TYPE;
            case MESSAGE_DETAILS_ID:
                return PTAppContract.MessageDetails.CONTENT_ITEM_TYPE;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        LOGV(TAG, "insert(uri=" + uri + ", values=" + values.toString());

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case EVENTS: {
                db.insertOrThrow(Tables.EVENTS, null, values);
                notifyChange(uri);
                return Events.buildEventUri(values.getAsString(Events._ID));
            }
            case ADDRESS: {
                db.insertOrThrow(Tables.ADDRESS, null, values);
                notifyChange(uri);
                return PTAppContract.Address.buildAddressUri(values.getAsString(PTAppContract.Address._ID));
            }
            case INSTITUTE: {
                db.insertOrThrow(Tables.INSTITUTE, null, values);
                notifyChange(uri);
                return PTAppContract.Institute.buildInstituteUri(values.getAsString(PTAppContract.Institute._ID));
            }
            case BRANCH: {
                db.insertOrThrow(Tables.BRANCH, null, values);
                notifyChange(uri);
                return PTAppContract.Branch.buildBranchUri(values.getAsString(PTAppContract.Branch._ID));
            }
            case ACADEMIC_SESSION: {
                db.insertOrThrow(Tables.ACADEMIC_SESSION, null, values);
                notifyChange(uri);
                return PTAppContract.AcademicSession.buildAcademicSessionUri(values.getAsString(PTAppContract.AcademicSession._ID));
            }
            case SUBJECT: {
                db.insertOrThrow(Tables.SUBJECT, null, values);
                notifyChange(uri);
                return PTAppContract.Subject.buildSubjectUri(values.getAsString(PTAppContract.Subject._ID));
            }
            case CLASS: {
                db.insertOrThrow(Tables.CLASS, null, values);
                notifyChange(uri);
                return Events.buildEventUri(values.getAsString(Classes._ID));
            }
            case CLASS_SUBJECT: {
                db.insertOrThrow(Tables.CLASS_SUBJECT, null, values);
                notifyChange(uri);
                return PTAppContract.ClassSubject.buildClassSubjectUri(values.getAsString(PTAppContract.ClassSubject._ID));
            }
            case USER: {
                db.insertOrThrow(Tables.USER, null, values);
                notifyChange(uri);
                return PTAppContract.User.buildUserUri(values.getAsString(PTAppContract.User._ID));
            }
            case STAFF: {
                db.insertOrThrow(Tables.STAFF, null, values);
                notifyChange(uri);
                return PTAppContract.Staff.buildStaffUri(values.getAsString(PTAppContract.Staff._ID));
            }
            case STAFF_ENGAGEMENT: {
                db.insertOrThrow(Tables.STAFF_ENGAGEMENT, null, values);
                notifyChange(uri);
                return PTAppContract.StaffEngagement.buildStaffEngagementUri(values.getAsString(PTAppContract.StaffEngagement._ID));
            }
            case STUDENT: {
                db.insertOrThrow(Tables.STUDENT, null, values);
                notifyChange(uri);
                return PTAppContract.Student.buildStudentUri(values.getAsString(PTAppContract.Student._ID));
            }
            case STUDENT_ASSOCIATION: {
                db.insertOrThrow(Tables.STUDENT_ASSOCIATION, null, values);
                notifyChange(uri);
                return PTAppContract.StudentAssociation.buildStudentAssociationUri(values.getAsString(PTAppContract.StudentAssociation._ID));
            }
            case PARENT: {
                db.insertOrThrow(Tables.PARENT, null, values);
                notifyChange(uri);
                return PTAppContract.Parent.buildParentUri(values.getAsString(PTAppContract.Parent._ID));
            }
            case PARENT_CHILD_RELATION: {
                db.insertOrThrow(Tables.PARENT_CHILD_RELATION, null, values);
                notifyChange(uri);
                return PTAppContract.ParentChildRelation.buildParentChildRelationUri(values.getAsString(PTAppContract.ParentChildRelation._ID));
            }
            case DEFAULT_GROUPS: {
                db.insertOrThrow(Tables.DEFAULT_GROUPS, null, values);
                notifyChange(uri);
                return PTAppContract.DefaultGroups.buildDefaultGroupsUri(values.getAsString(PTAppContract.DefaultGroups._ID));
            }
            case PEER2PEER_MESSAGES: {
                long newRowId = db.insertOrThrow(Tables.PEER2PEER_MESSAGES, null, values);
                notifyChange(uri);
                /*return PTAppContract.Peer2PeerMessages.buildPeer2PeerMessagesUri(values.getAsString(PTAppContract.Peer2PeerMessages._ID));*/
                return PTAppContract.Peer2PeerMessages.buildPeer2PeerMessagesUri(String.valueOf(newRowId));
            }
            case MESSAGE_DETAILS: {
                long newRowId = db.insertOrThrow(Tables.MESSAGE_DETAILS, null, values);
                notifyChange(uri);
                /*return PTAppContract.MessageDetails.buildMessageDetailsUri(values.getAsString(PTAppContract.MessageDetails._ID));*/
                return PTAppContract.MessageDetails.buildMessageDetailsUri(String.valueOf(newRowId));
            }

            default: {
                throw new UnsupportedOperationException("Unknown insert uri: " + uri);
            }
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        /*String accountName = getCurrentAccountName(uri, false);*/
        LOGV(TAG, "delete(uri=" + uri);
        if (uri == PTAppContract.BASE_CONTENT_URI) {
            // Handle whole database deletes (e.g. when signing out)
            deleteDatabase();
            notifyChange(uri);
            return 1;
        }
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final SelectionBuilder builder = buildSimpleSelection(uri);
        final int match = sUriMatcher.match(uri);
        /*if (match == MY_SCHEDULE) {
            builder.where(MySchedule.MY_SCHEDULE_ACCOUNT_NAME + "=?", accountName);
        }*/
        int retVal = builder.where(selection, selectionArgs).delete(db);
        notifyChange(uri);
        return retVal;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        /*String accountName = getCurrentAccountName(uri, false);*/
        LOGV(TAG, "update(uri=" + uri + ", values=" + values.toString());
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        /*if (match == SEARCH_INDEX) {
            // update the search index
            ScheduleDatabase.updateSessionSearchIndex(db);
            return 1;
        }*/

        final SelectionBuilder builder = buildSimpleSelection(uri);
        /*if (match == MY_SCHEDULE) {
            values.remove(MySchedule.MY_SCHEDULE_ACCOUNT_NAME);
            builder.where(MySchedule.MY_SCHEDULE_ACCOUNT_NAME + "=?", accountName);
        }*/
        int retVal = builder.where(selection, selectionArgs).update(db, values);
        notifyChange(uri);
        return retVal;
    }

    private void notifyChange(Uri uri) {
        // We only notify changes if the caller is not the sync adapter.
        // The sync adapter has the responsibility of notifying changes (it can do so
        // more intelligently than we can -- for example, doing it only once at the end
        // of the sync instead of issuing thousands of notifications for each record).
        if (!PTAppContract.hasCallerIsSyncAdapterParameter(uri)) {
            Context context = getContext();
            context.getContentResolver().notifyChange(uri, null);

            // Widgets can't register content observers so we refresh widgets separately.
            /*context.sendBroadcast(ScheduleWidgetProvider.getRefreshBroadcastIntent(context, false));*/
        }
    }

    /**
     * Apply the given set of {@link android.content.ContentProviderOperation}, executing inside
     * a {@link SQLiteDatabase} transaction. All changes will be rolled back if
     * any single one fails.
     */
    @Override
    public ContentProviderResult[] applyBatch(ArrayList<ContentProviderOperation> operations)
            throws OperationApplicationException {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            final int numOperations = operations.size();
            final ContentProviderResult[] results = new ContentProviderResult[numOperations];
            for (int i = 0; i < numOperations; i++) {
                results[i] = operations.get(i).apply(this, results, i);
            }
            db.setTransactionSuccessful();
            return results;
        } finally {
            db.endTransaction();
        }
    }

    /**
     * Build a simple {@link SelectionBuilder} to match the requested
     * {@link Uri}. This is usually enough to support {@link #insert},
     * {@link #update}, and {@link #delete} operations.
     */
    private SelectionBuilder buildSimpleSelection(Uri uri) {
        final SelectionBuilder builder = new SelectionBuilder();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case EVENTS: {
                return builder.table(Tables.EVENTS);
            }
            case EVENTS_ID: {
                final String eventId = Events.getEventId(uri);
                return builder.table(Tables.EVENTS).where(Events._ID + "=?", eventId);
            }
            case ADDRESS: {
                return builder.table(Tables.ADDRESS);
            }
            case ADDRESS_ID: {
                final String addressId = PTAppContract.Address.getAddressId(uri);
                return builder.table(Tables.ADDRESS).where(PTAppContract.Address._ID + "=?", addressId);
            }
            case INSTITUTE: {
                return builder.table(Tables.INSTITUTE);
            }
            case INSTITUTE_ID: {
                final String instituteId = PTAppContract.Institute.getInstituteId(uri);
                return builder.table(Tables.INSTITUTE).where(PTAppContract.Institute._ID + "=?", instituteId);
            }
            case BRANCH: {
                return builder.table(Tables.BRANCH);
            }
            case BRANCH_ID: {
                final String branchId = PTAppContract.Branch.getBranchId(uri);
                return builder.table(Tables.BRANCH).where(PTAppContract.Branch._ID + "=?", branchId);
            }
            case ACADEMIC_SESSION: {
                return builder.table(Tables.ACADEMIC_SESSION);
            }
            case ACADEMIC_SESSION_ID: {
                final String academicSessionId = PTAppContract.AcademicSession.getAcademicSessionId(uri);
                return builder.table(Tables.ACADEMIC_SESSION).where(PTAppContract.AcademicSession._ID + "=?", academicSessionId);
            }
            case SUBJECT: {
                return builder.table(Tables.SUBJECT);
            }
            case SUBJECT_ID: {
                final String subjectId = PTAppContract.Subject.getSubjectId(uri);
                return builder.table(Tables.SUBJECT).where(PTAppContract.Subject._ID + "=?", subjectId);
            }
            case CLASS: {
                return builder.table(Tables.CLASS);
            }
            case CLASS_ID: {
                final String classId = Classes.getClassId(uri);
                return builder.table(Tables.CLASS).where(Classes._ID + "=?", classId);
            }
            case CLASS_SUBJECT: {
                return builder.table(Tables.CLASS_SUBJECT);
            }
            case CLASS_SUBJECT_ID: {
                final String classSubjectId = PTAppContract.ClassSubject.getClassSubjectId(uri);
                return builder.table(Tables.CLASS_SUBJECT).where(PTAppContract.ClassSubject._ID + "=?", classSubjectId);
            }
            /*case KID_CLASS_SUBJECTS: {
                final String sessionId = Sessions.getSessionId(uri);
                return builder.table(Tables.MY_SCHEDULE)
                        .where(ScheduleContract.MyScheduleColumns.SESSION_ID + "=?", sessionId);
            }*/
            case USER: {
                return builder.table(Tables.USER);
            }
            case USER_ID: {
                final String userId = PTAppContract.User.getUserId(uri);
                return builder.table(Tables.USER).where(PTAppContract.User._ID + "=?", userId);
            }
            case STAFF: {
                return builder.table(Tables.STAFF);
            }
            case STAFF_ID: {
                final String staffId = PTAppContract.Staff.getStaffId(uri);
                return builder.table(Tables.STAFF).where(PTAppContract.Staff._ID + "=?", staffId);
            }
            case STAFF_ENGAGEMENT: {
                return builder.table(Tables.STAFF_ENGAGEMENT);
            }
            case STAFF_ENGAGEMENT_ID: {
                final String staffEngagementId = PTAppContract.StaffEngagement.getStaffEngagementId(uri);
                return builder.table(Tables.STAFF_ENGAGEMENT).where(PTAppContract.Staff._ID + "=?", staffEngagementId);
            }
            case STUDENT: {
                return builder.table(Tables.STUDENT);
            }
            case STUDENT_ID: {
                final String studentId = PTAppContract.Student.getStudentId(uri);
                return builder.table(Tables.STUDENT).where(PTAppContract.Student._ID + "=?", studentId);
            }
            case STUDENT_ASSOCIATION: {
                return builder.table(Tables.STUDENT_ASSOCIATION);
            }
            case STUDENT_ASSOCIATION_ID: {
                final String studentAssociationId = PTAppContract.StudentAssociation.getStudentAssociationId(uri);
                return builder.table(Tables.STUDENT_ASSOCIATION).where(PTAppContract.StudentAssociation._ID + "=?", studentAssociationId);
            }
            case PARENT: {
                return builder.table(Tables.PARENT);
            }
            case PARENT_ID: {
                final String parentId = PTAppContract.Parent.getParentId(uri);
                return builder.table(Tables.PARENT).where(PTAppContract.Parent._ID + "=?", parentId);
            }
            case PARENT_CHILD_RELATION: {
                return builder.table(Tables.PARENT_CHILD_RELATION);
            }
            case PARENT_CHILD_RELATION_ID: {
                final String parentChildRelationId = PTAppContract.ParentChildRelation.getParentChildRelationId(uri);
                return builder.table(Tables.PARENT_CHILD_RELATION).where(PTAppContract.ParentChildRelation._ID + "=?", parentChildRelationId);
            }
            case DEFAULT_GROUPS: {
                return builder.table(Tables.DEFAULT_GROUPS);
            }
            case DEFAULT_GROUPS_ID: {
                final String defaultGroupsId = PTAppContract.DefaultGroups.getDefaultGroupsId(uri);
                return builder.table(Tables.DEFAULT_GROUPS).where(PTAppContract.DefaultGroups._ID + "=?", defaultGroupsId);
            }
            case PEER2PEER_MESSAGES: {
                return builder.table(Tables.PEER2PEER_MESSAGES);
            }
            case PEER2PEER_MESSAGES_ID: {
                final String peer2peerMessagesId = PTAppContract.Peer2PeerMessages.getPeer2PeerMessagesId(uri);
                return builder.table(Tables.PEER2PEER_MESSAGES).where(PTAppContract.Peer2PeerMessages._ID + "=?", peer2peerMessagesId);
            }
            case MESSAGE_DETAILS: {
                return builder.table(Tables.MESSAGE_DETAILS);
            }
            case MESSAGE_DETAILS_ID: {
                final String messageDetailsId = PTAppContract.MessageDetails.getMessageDetailsId(uri);
                return builder.table(Tables.MESSAGE_DETAILS).where(PTAppContract.MessageDetails._ID + "=?", messageDetailsId);
            }
            default: {
                throw new UnsupportedOperationException("Unknown uri for " + match + ": " + uri);
            }
        }
    }


    /**
     * Returns a tuple of question marks. For example, if count is 3, returns "(?,?,?)".
     */
    private String makeQuestionMarkTuple(int count) {
        if (count < 1) {
            return "()";
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("(?");
        for (int i = 1; i < count; i++) {
            stringBuilder.append(",?");
        }
        stringBuilder.append(")");
        return stringBuilder.toString();
    }
}
