package com.ptapp.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import com.ptapp.provider.PTAppContract.ClassColumns;
import com.ptapp.provider.PTAppContract.EventsColumns;
import com.ptapp.provider.PTAppContract.ParentColumns;
import com.ptapp.provider.PTAppContract.StudentColumns;
import com.ptapp.provider.PTAppContract.SyncColumns;

import static com.ptapp.utils.LogUtils.makeLogTag;


/**
 * Helper for managing {@link SQLiteDatabase} that stores data for
 * {@link com.ptapp.provider.PTAppProvider}.
 */
public class PTAppDatabase extends SQLiteOpenHelper {
    private static final String TAG = makeLogTag(PTAppDatabase.class);

    private static final String DATABASE_NAME = "PTApp.db";

    // NOTE: carefully update onUpgrade() when bumping database versions to make
    // sure user data is saved.

    private static final int VER_2014_RELEASE_A = 122; // app version 2.0.0, 2.0.1
    private static final int VER_2014_RELEASE_C = 207; // app version 2.1.x
    private static final int CUR_DATABASE_VERSION = VER_2014_RELEASE_C;

    private final Context mContext;

    public interface Tables {
        String ADDRESS = "address";
        String INSTITUTE = "institute";
        String BRANCH = "branch";
        String ACADEMIC_SESSION = "academic_session";
        String SUBJECT = "subject";
        String CLASS = "class";
        String CLASS_SUBJECT = "class_subject";
        String USER = "user";
        String STAFF = "staff";
        String STAFF_ENGAGEMENT = "staff_engagement";
        String STUDENT = "student";
        String STUDENT_ASSOCIATION = "student_association";
        String PARENT = "parent";
        String PARENT_CHILD_RELATION = "parent_child_relation";
        String GROUP_MASTER = "group_master";
        /*String DEFAULT_GROUP_MEMBERS = "default_group_members";*/
        String DEFAULT_GROUPS = "default_groups";
        String CUSTOM_GROUP_MEMBERS = "custom_group_members";
        String PEER2PEER_MESSAGES = "peer2peer_messages";
        String MESSAGE_DETAILS = "message_details";
        String EVENTS = "events";

        //teacher teaching subjects.
        /*String STAFF_ENGAGEMENT_JOIN_CLASS_SUBJECT = "staff_engagement "
                + "LEFT OUTER JOIN class_subject ON staff_engagement.class_subject_id = class_subject._id "
                + "LEFT OUTER JOIN class ON class_subject.class_id = class._id "
                + "LEFT OUTER JOIN subject ON class_subject.subject_id = subject._id ";*/
        String STAFF_ENGAGEMENT_JOIN_DEFAULT_GROUPS = "staff_engagement "
                + "LEFT OUTER JOIN default_groups ON staff_engagement._id=default_groups.staff_engagement_id ";


        //Parent kid's subjects.
        String STUDENT_ASSOCIATION_JOIN_DEFAULT_GROUPS = "student_association "
                + "LEFT OUTER JOIN default_groups ON student_association.class_subject_id = default_groups.class_subject_id "
                + "LEFT OUTER JOIN staff_engagement ON default_groups.class_subject_id=staff_engagement.class_subject_id "
                + "LEFT OUTER JOIN staff ON staff_engagement.staff_id = staff._id ";


        /*String STUDENT_ASSOCIATION_JOIN_CLASS_SUBJECT = "student_association "
                + "LEFT OUTER JOIN class_subject ON student_association.class_subject_id = class_subject._id "
                + "LEFT OUTER JOIN class ON class_subject.class_id = class._id "
                + "LEFT OUTER JOIN subject ON class_subject.subject_id = subject._id "
                + "LEFT OUTER JOIN staff_engagement ON class_subject._id = staff_engagement.class_subject_id "
                + "LEFT OUTER JOIN staff ON staff_engagement.staff_id = staff._id";*/


        //Staff's JId for a particular group
        String STAFF_JOIN_ENGAGEMENT_JOIN_DEFAULT_GROUPS = "staff "
                + "LEFT OUTER JOIN staff_engagement ON staff._id=staff_engagement.staff_id "
                + "LEFT OUTER JOIN default_groups ON staff_engagement._id = default_groups.staff_engagement_id ";

        //students in a group(class_subject)
        /*SELECT * FROM student_association
        LEFT OUTER JOIN student ON student_association.student_id = student._id
        LEFT OUTER JOIN parent_child_relation ON student._id=parent_child_relation.student_id
        LEFT OUTER JOIN parent ON parent_child_relation.parent_id = parent._id
        where student_association.class_subject_id = 4*/
        String STUDENT_ASSOCIATION_JOIN_STUDENT = "student_association "
                + "LEFT OUTER JOIN student ON student_association.student_id = student._id ";
        /*String STUDENT_ASSOCIATION_JOIN_STUDENT = "student_association "
                + "LEFT OUTER JOIN class_subject ON student_association.class_subject_id = class_subject._id "
                + "LEFT OUTER JOIN student ON student_association.student_id = student._id "
                + "LEFT OUTER JOIN parent_child_relation ON student_association.student_id = parent_child_relation.student_id "
                + "LEFT OUTER JOIN parent ON parent_child_relation.parent_id = parent._id "
                + "AND parent_child_relation.parent_type = 'M' ";*/


        //[Caution] This Join between parent and Parent_Child_Relation is being used at 2 places.
        //One is to get ParentType flags while getting messages.
        //Second is to get a student's parents Jid.
        String PARENT_JOIN_PARENT_CHILD_RELATION = "parent_child_relation "
                + "LEFT OUTER JOIN parent ON parent_child_relation.parent_id= parent._id ";

        /*//Parent kids - TODO:need to remove this , once I get the StudentId in the validateOTP response
        String PARENT_CHILD_RELATION_JOIN_PARENT = "parent_child_relation "
                + "LEFT OUTER JOIN parent_child_relation ON parent_child_relation.parent_id=parent._id "*/;

        String PARENT_JOIN_PARENT_CHILD_RELATION_JOIN_STUDENT = "parent "
                + "LEFT OUTER JOIN parent_child_relation ON parent._id=parent_child_relation.parent_id "
                + "LEFT OUTER JOIN student ON parent_child_relation.student_id = student._id ";

        String SESSIONS_SEARCH = "sessions_search";

        String SEARCH_SUGGEST = "search_suggest";

        /*String COURSE_JOIN_CLASS = COURSES + " LEFT OUTER JOIN " + CLASSES + " ON "
                + COURSES + "." + CourseColumns.COURSE_CLASS_ID + " = " + CLASSES + "." + ClassColumns.CLASS_ID;*/

        String SESSIONS_JOIN_MYSCHEDULE = "sessions "
                + "LEFT OUTER JOIN myschedule ON sessions.session_id=myschedule.session_id "
                + "AND myschedule.account_name=? ";

        String SESSIONS_JOIN_ROOMS_TAGS = "sessions "
                + "LEFT OUTER JOIN myschedule ON sessions.session_id=myschedule.session_id "
                + "AND myschedule.account_name=? "
                + "LEFT OUTER JOIN rooms ON sessions.room_id=rooms.room_id "
                + "LEFT OUTER JOIN sessions_tags ON sessions.session_id=sessions_tags.session_id";

        String SESSIONS_JOIN_ROOMS_TAGS_FEEDBACK_MYSCHEDULE = "sessions "
                + "LEFT OUTER JOIN myschedule ON sessions.session_id=myschedule.session_id "
                + "AND myschedule.account_name=? "
                + "LEFT OUTER JOIN rooms ON sessions.room_id=rooms.room_id "
                + "LEFT OUTER JOIN sessions_tags ON sessions.session_id=sessions_tags.session_id "
                + "LEFT OUTER JOIN feedback ON sessions.session_id=feedback.session_id";

        // When tables get deprecated, add them to this list (so they get correctly deleted
        // on database upgrades)
        interface DeprecatedTables {
            String TRACKS = "tracks";
            String SESSIONS_TRACKS = "sessions_tracks";
            String SANDBOX = "sandbox";
        }

        ;
    }

    private interface Triggers {
        // Deletes from dependent tables when corresponding sessions are deleted.
        String SESSIONS_TAGS_DELETE = "sessions_tags_delete";
        String SESSIONS_SPEAKERS_DELETE = "sessions_speakers_delete";
        String SESSIONS_MY_SCHEDULE_DELETE = "sessions_myschedule_delete";
        String SESSIONS_FEEDBACK_DELETE = "sessions_feedback_delete";

        // When triggers get deprecated, add them to this list (so they get correctly deleted
        // on database upgrades)
        interface DeprecatedTriggers {
            String SESSIONS_TRACKS_DELETE = "sessions_tracks_delete";
        }

        ;
    }

    public PTAppDatabase(Context context) {
        super(context, DATABASE_NAME, null, CUR_DATABASE_VERSION);
        mContext = context;
    }


    //Table Creation SQL Statement
    private static final String SQL_CREATE_Address = "CREATE TABLE " + Tables.ADDRESS + " ("
            + BaseColumns._ID + " INTEGER PRIMARY KEY ,"
            + PTAppContract.AddressColumns.ADDRESS1 + " TEXT ,"
            + PTAppContract.AddressColumns.ADDRESS2 + " TEXT ,"
            + PTAppContract.AddressColumns.ADDRESS3 + " TEXT , "
            + PTAppContract.AddressColumns.ADDRESS4 + " TEXT ,"
            + PTAppContract.AddressColumns.CITY_CODE + " TEXT ,"
            + PTAppContract.AddressColumns.CITY_NAME + " TEXT ,"
            + PTAppContract.AddressColumns.STATE_CODE + " TEXT , "
            + PTAppContract.AddressColumns.STATE_NAME + " TEXT ,"
            + PTAppContract.AddressColumns.COUNTRY_CODE + " TEXT ,"
            + PTAppContract.AddressColumns.COUNTRY_NAME + " TEXT )";

    private static final String SQL_CREATE_Institute = "CREATE TABLE " + Tables.INSTITUTE + " ("
            + BaseColumns._ID + " INTEGER PRIMARY KEY,"
            + PTAppContract.InstituteColumns.INSTITUTE_NAME + " TEXT ,"
            + PTAppContract.InstituteColumns.EMAIL + " TEXT ,"
            + PTAppContract.InstituteColumns.IS_REGISTERED + " TEXT , "
            + PTAppContract.InstituteColumns.PHONE + " TEXT ,"
            + PTAppContract.InstituteColumns.COUNTRY_ISO_CODE + " TEXT ,"
            + PTAppContract.InstituteColumns.WEBSITE + " TEXT ,"
            + PTAppContract.InstituteColumns.ADDRESS_ID + " INTEGER ,"
            + PTAppContract.InstituteColumns.ACTIVE + " TEXT ,"
            + PTAppContract.InstituteColumns.INSTITUTE_TYPE_DESCRIPTION + " TEXT ,"
            + PTAppContract.InstituteColumns.INSTITUTE_TYPE_CODE + " TEXT   )";

    private static final String SQL_CREATE_Branch = "CREATE TABLE " + Tables.BRANCH + " ("
            + BaseColumns._ID + " INTEGER PRIMARY KEY,"
            + PTAppContract.BranchColumns.BRANCH_NAME + " TEXT ,"
            + PTAppContract.BranchColumns.EMAIL + " TEXT ,"
            + PTAppContract.BranchColumns.PHONE + " TEXT , "
            + PTAppContract.BranchColumns.COUNTRY_ISO_CODE + " TEXT ,"
            + PTAppContract.BranchColumns.ADDRESS_ID + " INTEGER ,"
            + PTAppContract.BranchColumns.INSTITUTE_ID + " INTEGER )";

    private static final String SQL_CREATE_AcademicSession = "CREATE TABLE " + Tables.ACADEMIC_SESSION + " ("
            + BaseColumns._ID + " INTEGER PRIMARY KEY,"
            + PTAppContract.AcademicSessionColumns.START_DATE + " TEXT ,"
            + PTAppContract.AcademicSessionColumns.END_DATE + " TEXT ,"
            + PTAppContract.AcademicSessionColumns.SESSION_DESCRIPTION + " TEXT , "
            + PTAppContract.AcademicSessionColumns.SESSION_YEAR + " TEXT ,"
            + PTAppContract.AcademicSessionColumns.ACADEMIC_SESSION_TYPE_CODE + " TEXT ,"
            + PTAppContract.AcademicSessionColumns.ACADEMIC_SESSION_TYPE_DESCRIPTION + " TEXT )";

    private static final String SQL_CREATE_Subject = "CREATE TABLE " + Tables.SUBJECT + " ("
            + BaseColumns._ID + " INTEGER PRIMARY KEY,"
            + PTAppContract.SubjectColumns.DESCRIPTION + " TEXT ,"
            + PTAppContract.SubjectColumns.SUBJECT_CODE + " TEXT )";

    private static final String SQL_CREATE_Class = "CREATE TABLE " + Tables.CLASS + " ("
            + BaseColumns._ID + " INTEGER PRIMARY KEY,"
            + ClassColumns.CLASS_TYPE_CODE + " TEXT ,"
            + ClassColumns.CLASS_TYPE_DESCRIPTION + " TEXT ,"
            + ClassColumns.SECTION_TYPE_CODE + " TEXT ,"
            + ClassColumns.SECTION_TYPE_DESCRIPTION + " TEXT ,"
            + ClassColumns.ACADEMIC_SESSION_ID + " INTEGER ,"
            + ClassColumns.ATTENDANCE_TYPE_CODE + " TEXT ,"
            + ClassColumns.ATTENDANCE_TYPE_DESCRIPTION + " TEXT ,"
            + ClassColumns.BRANCH_ID + " INTEGER ,"
            + ClassColumns.CLASS_TEACHER + " INTEGER )";

    private static final String SQL_CREATE_ClassSubject = "CREATE TABLE " + Tables.CLASS_SUBJECT + " ("
            + BaseColumns._ID + " INTEGER PRIMARY KEY,"
            + PTAppContract.ClassSubjectColumns.CLASS_ID + " INTEGER ,"
            + PTAppContract.ClassSubjectColumns.SUBJECT_ID + " INTEGER ,"
            + PTAppContract.ClassSubjectColumns.GROUP_JID + " TEXT )";

    private static final String SQL_CREATE_User = "CREATE TABLE " + Tables.USER + " ("
            + BaseColumns._ID + " INTEGER PRIMARY KEY,"
            + PTAppContract.UserColumns.MOBILE + " TEXT ,"
            + PTAppContract.UserColumns.COUNTRY_ISO_CODE + " TEXT ,"
            + PTAppContract.UserColumns.REGISTERED + " TEXT ,"
            + PTAppContract.UserColumns.DELETED + " TEXT )";

    private static final String SQL_CREATE_Staff = "CREATE TABLE " + Tables.STAFF + " ("
            + BaseColumns._ID + " INTEGER PRIMARY KEY,"
            + PTAppContract.StaffColumns.FIRST_NAME + " TEXT ,"
            + PTAppContract.StaffColumns.LAST_NAME + " TEXT ,"
            + PTAppContract.StaffColumns.DISPLAY_NAME + " TEXT ,"
            + PTAppContract.StaffColumns.EMAIL + " TEXT ,"
            + PTAppContract.StaffColumns.IS_ADMIN + " TEXT ,"
            + PTAppContract.StaffColumns.MOBILE + " TEXT ,"
            + PTAppContract.StaffColumns.COUNTRY_ISO_CODE + " TEXT ,"
            + PTAppContract.StaffColumns.PASSWORD + " TEXT ,"
            + PTAppContract.StaffColumns.ADDRESS_ID + " INTEGER ,"
            + PTAppContract.StaffColumns.QUALIFICATION_NAME + " TEXT ,"
            + PTAppContract.StaffColumns.QUALIFICATION_LEVEL + " TEXT ,"
            + PTAppContract.StaffColumns.STAFF_TYPE_CODE + " TEXT ,"
            + PTAppContract.StaffColumns.STAFF_TYPE_DESCRIPTION + " TEXT ,"
            + PTAppContract.StaffColumns.BRANCH_ID + " INTEGER ,"
            + PTAppContract.StaffColumns.IMAGE_URL + " TEXT ,"
            + PTAppContract.StaffColumns.USER_ID + " INTEGER ,"
            + PTAppContract.StaffColumns.JID + " TEXT )";

    private static final String SQL_CREATE_StaffEngagement = "CREATE TABLE " + Tables.STAFF_ENGAGEMENT + " ("
            + BaseColumns._ID + " INTEGER PRIMARY KEY,"
            + PTAppContract.StaffEngagementColumns.STAFF_ID + " INTEGER ,"
            + PTAppContract.StaffEngagementColumns.CLASS_SUBJECT_ID + " INTEGER ,"
            + PTAppContract.StaffEngagementColumns.ACTIVE + " TEXT ,"
            + PTAppContract.StaffEngagementColumns.START_DATE + " TEXT ,"
            + PTAppContract.StaffEngagementColumns.END_DATE + " TEXT )";

    private static final String SQL_CREATE_Student = "CREATE TABLE " + Tables.STUDENT + " ("
            + BaseColumns._ID + " INTEGER PRIMARY KEY,"
            + StudentColumns.FIRST_NAME + " TEXT ,"
            + StudentColumns.LAST_NAME + " TEXT ,"
            + StudentColumns.DOB + " TEXT , "
            + StudentColumns.EMAIL + " TEXT ,"
            + StudentColumns.GENDER + " TEXT ,"
            + StudentColumns.MOBILE + " TEXT ,"
            + StudentColumns.COUNTRY_ISO_CODE + " TEXT , "
            + StudentColumns.ALLERGIES + " TEXT ,"
            + StudentColumns.SPECIAL_INSTRUCTIONS + " TEXT ,"
            + StudentColumns.ADDRESS_ID + " INTEGER ,"
            + StudentColumns.BRANCH_ID + " INTEGER ,"
            + StudentColumns.IMAGE_URL + " TEXT ,"
            + StudentColumns.USER_ID + " INTEGER ,"
            + StudentColumns.ENROLLMENT_NUM + " TEXT ,"
            + StudentColumns.JID + " TEXT )";

    private static final String SQL_CREATE_StudentAssociation = "CREATE TABLE " + Tables.STUDENT_ASSOCIATION + " ("
            + BaseColumns._ID + " INTEGER PRIMARY KEY,"
            + PTAppContract.StudentAssociationColumns.STUDENT_ID + " INTEGER ,"
            + PTAppContract.StudentAssociationColumns.CLASS_SUBJECT_ID + " INTEGER ,"
            + PTAppContract.StudentAssociationColumns.YEARLY_RESULT + " TEXT )";

    private static final String SQL_CREATE_Parent = "CREATE TABLE " + Tables.PARENT + " ("
            + BaseColumns._ID + " INTEGER PRIMARY KEY, "
            + ParentColumns.FIRST_NAME + " TEXT ,"
            + ParentColumns.LAST_NAME + " TEXT ,"
            + ParentColumns.EMAIL + " TEXT ,"
            + ParentColumns.GENDER + " TEXT ,"
            + ParentColumns.MOBILE + " TEXT , "
            + ParentColumns.COUNTRY_ISO_CODE + " TEXT ,"
            + ParentColumns.QUALIFICATION + " TEXT ,"
            + ParentColumns.ADDRESS_ID + " INTEGER ,"
            + ParentColumns.IMAGE_URL + " TEXT ,"
            + ParentColumns.USER_ID + " INTEGER ,"
            + ParentColumns.JID + " TEXT )";

    private static final String SQL_CREATE_ParentChildRelation = "CREATE TABLE " + Tables.PARENT_CHILD_RELATION + " ("
            + BaseColumns._ID + " INTEGER PRIMARY KEY, "
            + PTAppContract.ParentChildRelationColumns.STUDENT_ID + " INTEGER ,"
            + PTAppContract.ParentChildRelationColumns.PARENT_ID + " INTEGER ,"
            + PTAppContract.ParentChildRelationColumns.PARENT_TYPE + " TEXT )";

    private static final String SQL_CREATE_GroupMaster = "CREATE TABLE " + Tables.GROUP_MASTER + " ("
            + BaseColumns._ID + " INTEGER PRIMARY KEY, "
            + PTAppContract.GroupMasterColumns.GROUP_NAME + " TEXT ,"
            + PTAppContract.GroupMasterColumns.DESCRIPTION + " TEXT ,"
            + PTAppContract.GroupMasterColumns.ADMIN_USER_ID + " INTEGER ,"
            + PTAppContract.GroupMasterColumns.GROUP_TYPE + " TEXT ,"
            + PTAppContract.GroupMasterColumns.CREATION_DATE + " INTEGER ,"
            + PTAppContract.GroupMasterColumns.LAST_ACTIVITY_DATE + " INTEGER ,"
            + PTAppContract.GroupMasterColumns.NUM_OF_MEMBERS + " INTEGER ,"
            + PTAppContract.GroupMasterColumns.IMAGE_URL + " TEXT )";

    /*private static final String SQL_CREATE_DefaultGroupMembers = "CREATE TABLE " + Tables.DEFAULT_GROUP_MEMBERS + " ("
            + BaseColumns._ID + " INTEGER PRIMARY KEY, "
            + PTAppContract.DefaultGroupMembersColumns.GROUP_MASTER_ID + " INTEGER ,"
            + PTAppContract.DefaultGroupMembersColumns.CLASS_SUBJECT_ID + " INTEGER ,"
            + PTAppContract.DefaultGroupMembersColumns.STAFF_ENGAGEMENT_ID + " INTEGER )";*/

    private static final String SQL_CREATE_DefaultGroups = "CREATE TABLE " + Tables.DEFAULT_GROUPS + " ("
            + BaseColumns._ID + " INTEGER PRIMARY KEY, "
            + PTAppContract.DefaultGroups.GROUP_NAME + " TEXT ,"
            + PTAppContract.DefaultGroups.GROUP_DESCRIPTION + " TEXT ,"
            + PTAppContract.DefaultGroups.GROUP_TYPE + " TEXT ,"
            + PTAppContract.DefaultGroups.IMAGE_URL + " TEXT ,"
            + PTAppContract.DefaultGroups.MEMBER_COUNT + " INTEGER ,"
            + PTAppContract.DefaultGroups.JID + " TEXT ,"
            + PTAppContract.DefaultGroups.CLASS_SUBJECT_ID + " INTEGER ,"
            + PTAppContract.DefaultGroups.STAFF_ENGAGEMENT_ID + " INTEGER )";

    private static final String SQL_CREATE_CustomGroupMembers = "CREATE TABLE " + Tables.CUSTOM_GROUP_MEMBERS + " ("
            + BaseColumns._ID + " INTEGER PRIMARY KEY, "
            + PTAppContract.CustomGroupMembersColumns.GROUP_MASTER_ID + " INTEGER ,"
            + PTAppContract.CustomGroupMembersColumns.USER_ID + " INTEGER ,"
            + PTAppContract.CustomGroupMembersColumns.ROLE + " TEXT ,"
            + PTAppContract.CustomGroupMembersColumns.LINK_TYPE + " TEXT )";

    private static final String SQL_CREATE_Peer2PeerMessages = "CREATE TABLE " + Tables.PEER2PEER_MESSAGES + " ("
            + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + PTAppContract.Peer2PeerMessagesColumns.SENDER_USER_ID + " INTEGER ,"
            + PTAppContract.Peer2PeerMessagesColumns.SENDER_ROLE + " TEXT ,"
            + PTAppContract.Peer2PeerMessagesColumns.RECIPIENT_USER_ID + " INTEGER ,"
            + PTAppContract.Peer2PeerMessagesColumns.RECIPIENT_ROLE + " TEXT ,"
            + PTAppContract.Peer2PeerMessagesColumns.STUDENT_ID + " INTEGER )";

    private static final String SQL_CREATE_MessageDetails = "CREATE TABLE " + Tables.MESSAGE_DETAILS + " ("
            + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + PTAppContract.MessageDetailsColumns.GROUP_MASTER_ID + " INTEGER ,"
            + PTAppContract.MessageDetailsColumns.P2P_ID + " INTEGER ,"
            + PTAppContract.MessageDetailsColumns.SENDER_USER_ID + " INTEGER ,"
            + PTAppContract.MessageDetailsColumns.SENDER_ROLE + " TEXT ,"
            + PTAppContract.MessageDetailsColumns.MESSAGE_TYPE + " TEXT ,"
            + PTAppContract.MessageDetailsColumns.RECIPIENT_FILTER + " TEXT ,"
            + PTAppContract.MessageDetailsColumns.TIMESTAMP + " INTEGER ,"
            + PTAppContract.MessageDetailsColumns.SENT_TIMESTAMP + " INTEGER ,"
            + PTAppContract.MessageDetailsColumns.RECIPIENT_DEVICE_TIMESTAMP + " INTEGER ,"
            + PTAppContract.MessageDetailsColumns.STATUS + " TEXT ,"
            + PTAppContract.MessageDetailsColumns.DATA + " TEXT ,"
            + PTAppContract.MessageDetailsColumns.MEDIA_MIME_TYPE + " TEXT ,"
            + PTAppContract.MessageDetailsColumns.MEDIA_SIZE + " TEXT ,"
            + PTAppContract.MessageDetailsColumns.MEDIA_NAME + " TEXT ,"
            + PTAppContract.MessageDetailsColumns.DURATION + " TEXT ,"
            + PTAppContract.MessageDetailsColumns.MEDIA_URL + " TEXT ,"
            + PTAppContract.MessageDetailsColumns.MEDIA_URL_THUMB + " TEXT )";

    private static final String SQL_CREATE_Event = "CREATE TABLE " + Tables.EVENTS + " ("
            + EventsColumns._ID + " TEXT PRIMARY KEY, "
            + SyncColumns.UPDATED + " INTEGER NOT NULL,"
            + EventsColumns.ANDROID_EVENT_ID + " INTEGER, "
            + EventsColumns.EVENT_TYPE + " TEXT, "
            + EventsColumns.EVENT_START + " INTEGER, "
            + EventsColumns.EVENT_END + " INTEGER, "
            + EventsColumns.EVENT_TITLE + " TEXT, "
            + EventsColumns.EVENT_DESCRIPTION + " TEXT, "
            + EventsColumns.EVENT_FOR_CLASSES + " TEXT )";


    /**
     * Table drop statement
     */
    private static final String SQL_DELETE_Address = "DROP TABLE IF EXISTS "
            + Tables.ADDRESS;
    private static final String SQL_DELETE_Institute = "DROP TABLE IF EXISTS "
            + Tables.INSTITUTE;
    private static final String SQL_DELETE_Branch = "DROP TABLE IF EXISTS "
            + Tables.BRANCH;
    private static final String SQL_DELETE_AcademicSession = "DROP TABLE IF EXISTS "
            + Tables.ACADEMIC_SESSION;
    private static final String SQL_DELETE_Subject = "DROP TABLE IF EXISTS "
            + Tables.SUBJECT;
    private static final String SQL_DELETE_Class = "DROP TABLE IF EXISTS "
            + Tables.CLASS;
    private static final String SQL_DELETE_ClassSubject = "DROP TABLE IF EXISTS "
            + Tables.CLASS_SUBJECT;
    private static final String SQL_DELETE_User = "DROP TABLE IF EXISTS "
            + Tables.USER;
    private static final String SQL_DELETE_Staff = "DROP TABLE IF EXISTS "
            + Tables.STAFF;
    private static final String SQL_DELETE_StaffEngagement = "DROP TABLE IF EXISTS "
            + Tables.STAFF_ENGAGEMENT;
    private static final String SQL_DELETE_Student = "DROP TABLE IF EXISTS "
            + Tables.STUDENT;
    private static final String SQL_DELETE_StudentAssociation = "DROP TABLE IF EXISTS "
            + Tables.STUDENT_ASSOCIATION;
    private static final String SQL_DELETE_Parent = "DROP TABLE IF EXISTS "
            + Tables.PARENT;
    private static final String SQL_DELETE_ParentChildRelation = "DROP TABLE IF EXISTS "
            + Tables.PARENT_CHILD_RELATION;
    private static final String SQL_DELETE_GroupMaster = "DROP TABLE IF EXISTS "
            + Tables.GROUP_MASTER;
    /*private static final String SQL_DELETE_DefaultGroupMembers = "DROP TABLE IF EXISTS "
            + Tables.DEFAULT_GROUP_MEMBERS;*/
    private static final String SQL_DELETE_DefaultGroups = "DROP TABLE IF EXISTS "
            + Tables.DEFAULT_GROUPS;
    private static final String SQL_DELETE_CustomGroupMembers = "DROP TABLE IF EXISTS "
            + Tables.CUSTOM_GROUP_MEMBERS;
    private static final String SQL_DELETE_Peer2PeerMessages = "DROP TABLE IF EXISTS "
            + Tables.PEER2PEER_MESSAGES;
    private static final String SQL_DELETE_MessageDetails = "DROP TABLE IF EXISTS "
            + Tables.MESSAGE_DETAILS;
    private static final String SQL_DELETE_Event = "DROP TABLE IF EXISTS "
            + Tables.EVENTS;

    /**
     * Called when the database is created for the first time. This is where the
     * creation of tables and the initial population of the tables should
     * happen.
     */
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_Address);
        db.execSQL(SQL_CREATE_Institute);
        db.execSQL(SQL_CREATE_Branch);
        db.execSQL(SQL_CREATE_AcademicSession);
        db.execSQL(SQL_CREATE_Subject);
        db.execSQL(SQL_CREATE_Class);
        db.execSQL(SQL_CREATE_ClassSubject);
        db.execSQL(SQL_CREATE_User);
        db.execSQL(SQL_CREATE_Staff);
        db.execSQL(SQL_CREATE_StaffEngagement);
        db.execSQL(SQL_CREATE_Student);
        db.execSQL(SQL_CREATE_StudentAssociation);
        db.execSQL(SQL_CREATE_Parent);
        db.execSQL(SQL_CREATE_ParentChildRelation);
        db.execSQL(SQL_CREATE_GroupMaster);
        /*db.execSQL(SQL_CREATE_DefaultGroupMembers);*/
        db.execSQL(SQL_CREATE_DefaultGroups);
        db.execSQL(SQL_CREATE_CustomGroupMembers);
        db.execSQL(SQL_CREATE_Peer2PeerMessages);
        db.execSQL(SQL_CREATE_MessageDetails);
        db.execSQL(SQL_CREATE_Event);

        Log.v(TAG, "Fresh Database created.");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy
        // is
        // to simply to discard the data and start over

        Log.w(PTAppDatabase.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will destroy all old data.");

        db.execSQL(SQL_DELETE_Address);
        db.execSQL(SQL_DELETE_Institute);
        db.execSQL(SQL_DELETE_Branch);
        db.execSQL(SQL_DELETE_AcademicSession);
        db.execSQL(SQL_DELETE_Subject);
        db.execSQL(SQL_DELETE_Class);
        db.execSQL(SQL_DELETE_ClassSubject);
        db.execSQL(SQL_DELETE_User);
        db.execSQL(SQL_DELETE_Staff);
        db.execSQL(SQL_DELETE_StaffEngagement);
        db.execSQL(SQL_DELETE_Student);
        db.execSQL(SQL_DELETE_StudentAssociation);
        db.execSQL(SQL_DELETE_Parent);
        db.execSQL(SQL_DELETE_ParentChildRelation);
        db.execSQL(SQL_DELETE_GroupMaster);
        /*db.execSQL(SQL_DELETE_DefaultGroupMembers);*/
        db.execSQL(SQL_DELETE_DefaultGroups);
        db.execSQL(SQL_DELETE_CustomGroupMembers);
        db.execSQL(SQL_DELETE_Peer2PeerMessages);
        db.execSQL(SQL_DELETE_MessageDetails);
        db.execSQL(SQL_DELETE_Event);

        // Create tables again
        onCreate(db);
    }

    public void onDelete(SQLiteDatabase db) {

        Log.w(PTAppDatabase.class.getName(),
                "Deleting database and creating again"
                        + ", which will destroy all old data.");

        db.execSQL(SQL_DELETE_Address);
        db.execSQL(SQL_DELETE_Institute);
        db.execSQL(SQL_DELETE_Branch);
        db.execSQL(SQL_DELETE_AcademicSession);
        db.execSQL(SQL_DELETE_Subject);
        db.execSQL(SQL_DELETE_Class);
        db.execSQL(SQL_DELETE_ClassSubject);
        db.execSQL(SQL_DELETE_User);
        db.execSQL(SQL_DELETE_Staff);
        db.execSQL(SQL_DELETE_StaffEngagement);
        db.execSQL(SQL_DELETE_Student);
        db.execSQL(SQL_DELETE_StudentAssociation);
        db.execSQL(SQL_DELETE_Parent);
        db.execSQL(SQL_DELETE_ParentChildRelation);
        db.execSQL(SQL_DELETE_GroupMaster);
        /*db.execSQL(SQL_DELETE_DefaultGroupMembers);*/
        db.execSQL(SQL_DELETE_DefaultGroups);
        db.execSQL(SQL_DELETE_CustomGroupMembers);
        db.execSQL(SQL_DELETE_Peer2PeerMessages);
        db.execSQL(SQL_DELETE_MessageDetails);
        db.execSQL(SQL_DELETE_Event);

        // Create tables again
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public static void deleteDatabase(Context context) {
        context.deleteDatabase(DATABASE_NAME);
    }
}
