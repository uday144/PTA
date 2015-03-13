package com.ptapp.provider;

import android.net.Uri;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.text.TextUtils;

/**
 * Contract class for interacting with {@link PTAppProvider}. Unless
 * otherwise noted, all time-based fields are milliseconds since epoch and can
 * be compared against {@link System#currentTimeMillis()}.
 * <p/>
 * The backing {@link android.content.ContentProvider} assumes that {@link android.net.Uri}
 * are generated using stronger {@link String} identifiers, instead of
 * {@code int} {@link android.provider.BaseColumns#_ID} values, which are prone to shuffle during
 * sync.
 */
public class PTAppContract {
    /**
     * Query parameter to create a distinct query.
     */
    public static final String QUERY_PARAMETER_DISTINCT = "distinct";
    public static final String OVERRIDE_ACCOUNTNAME_PARAMETER = "overrideAccount";

    //TODO:Need to fix the crash Trust error.
    //TODO:conversations table - add two columns: IsBroadcast, groupId(before '@broadcast.server')

    //Columns
    public interface SyncColumns {
        /**
         * Last time this entry was updated or synchronized.
         */
        String UPDATED = "updated";
    }

    interface EventsColumns {
        String _ID = "_id"; // PK - server_event_id
        String ANDROID_EVENT_ID = "android_event_id";
        String EVENT_TYPE = "event_type";
        String EVENT_START = "event_start";
        String EVENT_END = "event_end";
        String EVENT_TITLE = "event_title";
        String EVENT_DESCRIPTION = "event_description";
        String EVENT_FOR_CLASSES = "event_for_classes";
    }

    interface AddressColumns {
        String ADDRESS1 = "address1";
        String ADDRESS2 = "address2";
        String ADDRESS3 = "address3";
        String ADDRESS4 = "address4";
        String CITY_CODE = "city_code";
        String CITY_NAME = "city_name";
        String STATE_CODE = "state_code";
        String STATE_NAME = "state_name";
        String COUNTRY_CODE = "country_code";
        String COUNTRY_NAME = "country_name";
    }

    interface InstituteColumns {
        String INSTITUTE_NAME = "institute_name";
        String EMAIL = "email";
        String IS_REGISTERED = "is_registered";
        String PHONE = "phone";
        String COUNTRY_ISO_CODE = "country_iso_code";
        String WEBSITE = "website";
        String ADDRESS_ID = "address_id";
        String ACTIVE = "active";
        String INSTITUTE_TYPE_DESCRIPTION = "institute_type_description";
        String INSTITUTE_TYPE_CODE = "institute_type_code";
    }

    interface BranchColumns {
        String BRANCH_NAME = "branch_name";
        String EMAIL = "email";
        String PHONE = "phone";
        String COUNTRY_ISO_CODE = "country_iso_code";
        String ADDRESS_ID = "address_id";
        String INSTITUTE_ID = "institute_id";
    }

    interface AcademicSessionColumns {
        String START_DATE = "start_date";
        String END_DATE = "end_date";
        String SESSION_DESCRIPTION = "session_description";
        String SESSION_YEAR = "session_year";
        String ACADEMIC_SESSION_TYPE_CODE = "academic_session_type_code";
        String ACADEMIC_SESSION_TYPE_DESCRIPTION = "academic_session_type_description";
    }

    interface SubjectColumns {
        String DESCRIPTION = "description";
        String SUBJECT_CODE = "subject_code";
    }

    interface ClassColumns {
        String CLASS_TYPE_CODE = "class_type_code";
        String CLASS_TYPE_DESCRIPTION = "class_type_description";
        String SECTION_TYPE_CODE = "section_type_code";
        String SECTION_TYPE_DESCRIPTION = "section_type_description";
        String ACADEMIC_SESSION_ID = "academic_session_id";
        String ATTENDANCE_TYPE_CODE = "attendance_type_code";
        String ATTENDANCE_TYPE_DESCRIPTION = "attendance_type_description";
        String BRANCH_ID = "branch_id";
        String CLASS_TEACHER = "class_teacher";
    }

    interface ClassSubjectColumns {
        String CLASS_ID = "class_id";
        String SUBJECT_ID = "subject_id";
        String GROUP_JID = "group_jid";
    }

    interface UserColumns {
        String MOBILE = "mobile";
        String COUNTRY_ISO_CODE = "country_iso_code";
        String REGISTERED = "registered";
        String DELETED = "deleted";
    }

    interface StaffColumns {
        String FIRST_NAME = "first_name";
        String LAST_NAME = "last_name";
        String DISPLAY_NAME = "display_name";
        String EMAIL = "email";
        String IS_ADMIN = "is_admin";
        String MOBILE = "mobile";
        String COUNTRY_ISO_CODE = "country_iso_code";
        String PASSWORD = "password";
        String ADDRESS_ID = "address_id";
        String QUALIFICATION_NAME = "qualification_name";
        String QUALIFICATION_LEVEL = "qualification_level";
        String STAFF_TYPE_CODE = "staff_type_code";
        String STAFF_TYPE_DESCRIPTION = "staff_type_description";
        String BRANCH_ID = "branch_id";
        String IMAGE_URL = "image_url";
        String USER_ID = "user_id";
        String JID = "jid";
    }

    interface StaffEngagementColumns {
        String STAFF_ID = "staff_id";
        String CLASS_SUBJECT_ID = "class_subject_id";
        String ACTIVE = "active";
        String START_DATE = "start_date";
        String END_DATE = "end_date";
    }

    interface StudentColumns {
        String FIRST_NAME = "first_name";
        String LAST_NAME = "last_name";
        String EMAIL = "email";
        String DOB = "dob";
        String GENDER = "gender";
        String MOBILE = "mobile";
        String COUNTRY_ISO_CODE = "country_iso_code";
        String ALLERGIES = "allergies";
        String SPECIAL_INSTRUCTIONS = "special_instructions";
        String ADDRESS_ID = "address_id";
        String BRANCH_ID = "branch_id";
        String IMAGE_URL = "image_url";
        String USER_ID = "user_id";
        String ENROLLMENT_NUM = "enrollment_num";
        String JID = "jid";
    }

    interface StudentAssociationColumns {
        String STUDENT_ID = "student_id";
        String CLASS_SUBJECT_ID = "class_subject_id";
        String YEARLY_RESULT = "yearly_result";
    }

    interface ParentColumns {
        String FIRST_NAME = "first_name";
        String LAST_NAME = "last_name";
        String EMAIL = "email";
        String GENDER = "gender";
        String MOBILE = "mobile";
        String COUNTRY_ISO_CODE = "country_iso_code";
        String QUALIFICATION = "qualification";
        String ADDRESS_ID = "address_id";
        String IMAGE_URL = "image_url";
        String USER_ID = "user_id";
        String JID = "jid";
    }

    interface ParentChildRelationColumns {
        String STUDENT_ID = "student_id";
        String PARENT_ID = "parent_id";
        String PARENT_TYPE = "parent_type";
    }

    interface DefaultGroupsColumns {
        String GROUP_NAME = "group_name";
        String GROUP_DESCRIPTION = "description";
        String GROUP_TYPE = "group_type";
        String IMAGE_URL = "image_url";
        String MEMBER_COUNT = "member_count";
        String JID = "jid";
        String CLASS_SUBJECT_ID = "class_subject_id";
        String STAFF_ENGAGEMENT_ID = "staff_engagement_id";
    }

    interface GroupMasterColumns {
        String GROUP_NAME = "group_name";
        String DESCRIPTION = "description";
        String ADMIN_USER_ID = "admin_user_id";
        String GROUP_TYPE = "group_type";
        String CREATION_DATE = "creation_date";
        String LAST_ACTIVITY_DATE = "last_activity_date";
        String NUM_OF_MEMBERS = "num_of_members";
        String IMAGE_URL = "image_url";
    }

    /*interface DefaultGroupMembersColumns {
        String GROUP_MASTER_ID = "group_master_id";
        String CLASS_SUBJECT_ID = "class_subject_id";
        String STAFF_ENGAGEMENT_ID = "staff_engagement_id";
    }*/


    interface CustomGroupMembersColumns {
        String GROUP_MASTER_ID = "group_master_id";
        String USER_ID = "user_id";
        String ROLE = "role";
        String LINK_TYPE = "link_type";
    }

    interface Peer2PeerMessagesColumns {
        String SENDER_USER_ID = "sender_user_id";
        String SENDER_ROLE = "sender_role";
        String RECIPIENT_USER_ID = "recipient_user_id";
        String RECIPIENT_ROLE = "recipient_role";
        String STUDENT_ID = "student_id";
    }

    interface MessageDetailsColumns {
        String GROUP_MASTER_ID = "group_master_id";
        String P2P_ID = "p2p_id";
        String SENDER_USER_ID = "sender_user_id";
        String SENDER_ROLE = "sender_role";
        String MESSAGE_TYPE = "message_type";
        String RECIPIENT_FILTER = "recipient_filter";
        String TIMESTAMP = "timestamp";    //WHEN message "send" button is clicked
        String SENT_TIMESTAMP = "sent_timestamp";    //When message was actually sent from device (there could be delay due to offline states)
        String RECIPIENT_DEVICE_TIMESTAMP = "recipient_device_timestamp";
        String STATUS = "status";
        String DATA = "data";
        String MEDIA_MIME_TYPE = "media_mime_type";
        String MEDIA_SIZE = "media_size";
        String MEDIA_NAME = "media_name";
        String DURATION = "duration";
        String MEDIA_URL = "media_url";
        String MEDIA_URL_THUMB = "media_url_thumb";
    }

    public static final String CONTENT_AUTHORITY = "com.ptapp.app";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    private static final String PATH_EVENTS = "events";
    private static final String PATH_ADDRESS = "address";
    private static final String PATH_INSTITUTE = "institute";
    private static final String PATH_BRANCH = "branch";
    private static final String PATH_ACADEMIC_SESSION = "academic_session";
    private static final String PATH_SUBJECT = "subject";
    private static final String PATH_CLASS = "class";
    private static final String PATH_CLASS_SUBJECT = "class_subject";
    private static final String PATH_USER = "user";
    private static final String PATH_STAFF = "staff";
    private static final String PATH_STAFF_ENGAGEMENT = "staff_engagement";
    private static final String PATH_STUDENT = "student";
    private static final String PATH_STUDENT_ASSOCIATION = "student_association";
    private static final String PATH_PARENT = "parent";
    private static final String PATH_PARENT_CHILD_RELATION = "parent_child_relation";
    private static final String PATH_GROUP_MASTER = "group_master";
    /*private static final String PATH_DEFAULT_GROUP_MEMBERS = "default_group_members";*/
    private static final String PATH_DEFAULT_GROUPS = "default_groups";
    private static final String PATH_CUSTOM_GROUP_MEMBERS = "custom_group_members";
    private static final String PATH_PEER2PEER_MESSAGES = "peer2peer_messages";
    private static final String PATH_MESSAGE_DETAILS = "message_details";

    public static final String[] TOP_LEVEL_PATHS = {
            PATH_EVENTS,
            PATH_ADDRESS,
            PATH_INSTITUTE,
            PATH_BRANCH,
            PATH_ACADEMIC_SESSION,
            PATH_SUBJECT,
            PATH_CLASS,
            PATH_CLASS_SUBJECT,
            PATH_USER,
            PATH_STAFF,
            PATH_STAFF_ENGAGEMENT,
            PATH_STUDENT,
            PATH_STUDENT_ASSOCIATION,
            PATH_PARENT,
            PATH_PARENT_CHILD_RELATION,
            PATH_GROUP_MASTER,
            PATH_DEFAULT_GROUPS,
            /*PATH_DEFAULT_GROUP_MEMBERS,*/
            PATH_CUSTOM_GROUP_MEMBERS,
            PATH_PEER2PEER_MESSAGES,
            PATH_MESSAGE_DETAILS
    };

    public static class Events implements EventsColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_EVENTS).build();
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.schoolo.event";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.schoolo.event";

        /**
         * Build {@link Uri} for requested {@link #_ID}.
         */
        public static Uri buildEventUri(String eventId) {
            return CONTENT_URI.buildUpon().appendPath(eventId).build();
        }

        /**
         * Read {@link #_ID} from {@link com.ptapp.provider.PTAppContract.Events} {@link Uri}.
         */
        public static String getEventId(Uri uri) {
            return uri.getPathSegments().get(1);
        }

    }

    public static class Address implements AddressColumns, BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_ADDRESS).build();
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.schoolo.address";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.schoolo.address";

        /**
         * Build {@link Uri} for requested {@link #_ID}.
         */
        public static Uri buildAddressUri(String addressId) {
            return CONTENT_URI.buildUpon().appendPath(addressId).build();
        }

        /**
         * Read {@link #_ID} from {@link com.ptapp.provider.PTAppContract.Address} {@link Uri}.
         */
        public static String getAddressId(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }

    public static class Institute implements InstituteColumns, BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_INSTITUTE).build();
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.schoolo.institute";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.schoolo.institute";

        /**
         * Build {@link Uri} for requested {@link #_ID}.
         */
        public static Uri buildInstituteUri(String instituteId) {
            return CONTENT_URI.buildUpon().appendPath(instituteId).build();
        }

        /**
         * Read {@link #_ID} from {@link com.ptapp.provider.PTAppContract.Institute} {@link Uri}.
         */
        public static String getInstituteId(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }

    public static class Branch implements BranchColumns, BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_BRANCH).build();
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.schoolo.branch";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.schoolo.branch";

        /**
         * Build {@link Uri} for requested {@link #_ID}.
         */
        public static Uri buildBranchUri(String branchId) {
            return CONTENT_URI.buildUpon().appendPath(branchId).build();
        }

        /**
         * Read {@link #_ID} from {@link com.ptapp.provider.PTAppContract.Branch} {@link Uri}.
         */
        public static String getBranchId(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }

    public static class AcademicSession implements AcademicSessionColumns, BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_ACADEMIC_SESSION).build();
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.schoolo.academic_session";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.schoolo.academic_session";

        /**
         * Build {@link Uri} for requested {@link #_ID}.
         */
        public static Uri buildAcademicSessionUri(String AcademicSessionId) {
            return CONTENT_URI.buildUpon().appendPath(AcademicSessionId).build();
        }

        /**
         * Read {@link #_ID} from {@link com.ptapp.provider.PTAppContract.AcademicSession} {@link Uri}.
         */
        public static String getAcademicSessionId(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }

    public static class Subject implements SubjectColumns, BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_SUBJECT).build();
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.schoolo.subject";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.schoolo.subject";

        /**
         * Build {@link Uri} for requested {@link #_ID}.
         */
        public static Uri buildSubjectUri(String subjectId) {
            return CONTENT_URI.buildUpon().appendPath(subjectId).build();
        }

        /**
         * Read {@link #_ID} from {@link com.ptapp.provider.PTAppContract.Subject} {@link Uri}.
         */
        public static String getSubjectId(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }

    public static class Classes implements ClassColumns, BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_CLASS).build();
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.schoolo.classes";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.schoolo.classes";

        /**
         * Build {@link Uri} for requested {@link #_ID}.
         */
        public static Uri buildClassUri(String classId) {
            return CONTENT_URI.buildUpon().appendPath(classId).build();
        }

        /**
         * Read {@link #_ID} from {@link com.ptapp.provider.PTAppContract.Classes} {@link Uri}.
         */
        public static String getClassId(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }

    public static class ClassSubject implements ClassSubjectColumns, BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_CLASS_SUBJECT).build();
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.schoolo.class_subject";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.schoolo.class_subject";

        public static final Uri CONTENT_KID_CLASS_SUBJECT_URI = CONTENT_URI.buildUpon().appendPath("kid").build();

        /**
         * Build {@link Uri} for requested {@link #_ID}.
         */
        public static Uri buildClassSubjectUri(String classSubjectId) {
            return CONTENT_URI.buildUpon().appendPath(classSubjectId).build();
        }

        /**
         * Read {@link #_ID} from {@link com.ptapp.provider.PTAppContract.ClassSubject} {@link Uri}.
         */
        public static String getClassSubjectId(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }

    public static class User implements UserColumns, BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_USER).build();
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.schoolo.user";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.schoolo.user";

        /**
         * Build {@link Uri} for requested {@link #_ID}.
         */
        public static Uri buildUserUri(String userId) {
            return CONTENT_URI.buildUpon().appendPath(userId).build();
        }

        /**
         * Read {@link #_ID} from {@link com.ptapp.provider.PTAppContract.User} {@link Uri}.
         */
        public static String getUserId(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }

    public static class Staff implements StaffColumns, BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_STAFF).build();
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.schoolo.staff";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.schoolo.staff";

        //Staff's JId for a particular group
        public static final Uri CONTENT_STAFF_JID_URI = CONTENT_URI.buildUpon().appendPath("join_groups").build();

        /**
         * Build {@link Uri} for requested {@link #_ID}.
         */
        public static Uri buildStaffUri(String staffId) {
            return CONTENT_URI.buildUpon().appendPath(staffId).build();
        }

        /**
         * Read {@link #_ID} from {@link com.ptapp.provider.PTAppContract.Staff} {@link Uri}.
         */
        public static String getStaffId(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }

    public static class StaffEngagement implements StaffEngagementColumns, BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_STAFF_ENGAGEMENT).build();
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.schoolo.staff_engagement";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.schoolo.staff_engagement";

        public static final Uri CONTENT_STAFF_GROUPS_URI = CONTENT_URI.buildUpon().appendPath("groups").build();

        /**
         * Build {@link Uri} for requested {@link #_ID}.
         */
        public static Uri buildStaffEngagementUri(String staffEngagementId) {
            return CONTENT_URI.buildUpon().appendPath(staffEngagementId).build();
        }

        /**
         * Read {@link #_ID} from {@link com.ptapp.provider.PTAppContract.Classes} {@link Uri}.
         */
        public static String getStaffEngagementId(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }

    public static class Student implements StudentColumns, BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_STUDENT).build();
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.schoolo.student";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.schoolo.student";

        /**
         * Build {@link Uri} for requested {@link #_ID}.
         */
        public static Uri buildStudentUri(String studentId) {
            return CONTENT_URI.buildUpon().appendPath(studentId).build();
        }

        /**
         * Read {@link #_ID} from {@link com.ptapp.provider.PTAppContract.Student} {@link Uri}.
         */
        public static String getStudentId(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }

    public static class StudentAssociation implements StudentAssociationColumns, BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_STUDENT_ASSOCIATION).build();
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.schoolo.student_association";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.schoolo.student_association";

        /**
         * Build {@link Uri} for requested {@link #_ID}.
         */
        public static Uri buildStudentAssociationUri(String studentAssociationId) {
            return CONTENT_URI.buildUpon().appendPath(studentAssociationId).build();
        }

        /**
         * Read {@link #_ID} from {@link com.ptapp.provider.PTAppContract.StudentAssociation} {@link Uri}.
         */
        public static String getStudentAssociationId(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }

    public static class Parent implements ParentColumns, BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_PARENT).build();
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.schoolo.parent";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.schoolo.parent";

        public static final Uri CONTENT_PARENT_RELATION_STUDENT_URI = CONTENT_URI.buildUpon()
                .appendPath("relation_student").build();

        /**
         * Build {@link Uri} for requested {@link #_ID}.
         */
        public static Uri buildParentUri(String parentId) {
            return CONTENT_URI.buildUpon().appendPath(parentId).build();
        }

        /**
         * Read {@link #_ID} from {@link com.ptapp.provider.PTAppContract.Parent} {@link Uri}.
         */
        public static String getParentId(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }

    public static class ParentChildRelation implements ParentChildRelationColumns, BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_PARENT_CHILD_RELATION).build();
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.schoolo.parent_child_relation";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.schoolo.parent_child_relation";

        /**
         * Build {@link Uri} for requested {@link #_ID}.
         */
        public static Uri buildParentChildRelationUri(String parentChildRelationId) {
            return CONTENT_URI.buildUpon().appendPath(parentChildRelationId).build();
        }

        /**
         * Read {@link #_ID} from {@link com.ptapp.provider.PTAppContract.ParentChildRelation} {@link Uri}.
         */
        public static String getParentChildRelationId(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }

    public static class GroupMaster implements GroupMasterColumns, BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_GROUP_MASTER).build();
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.schoolo.group_master";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.schoolo.group_master";

        /**
         * Build {@link Uri} for requested {@link #_ID}.
         */
        public static Uri buildGroupMasterUri(String groupMasterId) {
            return CONTENT_URI.buildUpon().appendPath(groupMasterId).build();
        }

        /**
         * Read {@link #_ID} from {@link com.ptapp.provider.PTAppContract.GroupMaster} {@link Uri}.
         */
        public static String getGroupMasterId(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }

    public static class DefaultGroups implements DefaultGroupsColumns, BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_DEFAULT_GROUPS).build();
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.schoolo.default_groups";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.schoolo.default_groups";

        /**
         * Build {@link Uri} for requested {@link #_ID}.
         */
        public static Uri buildDefaultGroupsUri(String defaultGroupsId) {
            return CONTENT_URI.buildUpon().appendPath(defaultGroupsId).build();
        }

        /**
         * Read {@link #_ID} from {@link com.ptapp.provider.PTAppContract.DefaultGroups} {@link Uri}.
         */
        public static String getDefaultGroupsId(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }
    /*public static class DefaultGroupMembers implements DefaultGroupMembersColumns, BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_DEFAULT_GROUP_MEMBERS).build();
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.schoolo.default_group_members";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.schoolo.default_group_members";

        *//**
     * Build {@link Uri} for requested {@link #_ID}.
     *//*
        public static Uri buildDefaultGroupMembersUri(String defaultGroupMembersId) {
            return CONTENT_URI.buildUpon().appendPath(defaultGroupMembersId).build();
        }

        */

    /**
     * Read {@link #_ID} from {@link com.ptapp.provider.PTAppContract} {@link Uri}.
     *//*
        public static String getDefaultGroupMembersId(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }*/

    public static class CustomGroupMembers implements CustomGroupMembersColumns, BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_CUSTOM_GROUP_MEMBERS).build();
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.schoolo.custom_group_members";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.schoolo.custom_group_members";

        /**
         * Build {@link Uri} for requested {@link #_ID}.
         */
        public static Uri buildCustomGroupMembersUri(String customGroupMembersId) {
            return CONTENT_URI.buildUpon().appendPath(customGroupMembersId).build();
        }

        /**
         * Read {@link #_ID} from {@link com.ptapp.provider.PTAppContract.CustomGroupMembers} {@link Uri}.
         */
        public static String getCustomGroupMembersId(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }

    public static class Peer2PeerMessages implements Peer2PeerMessagesColumns, BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_PEER2PEER_MESSAGES).build();
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.schoolo.peer2peer_messages";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.schoolo.peer2peer_messages";

        /**
         * Build {@link Uri} for requested {@link #_ID}.
         */
        public static Uri buildPeer2PeerMessagesUri(String peer2PeerMessagesId) {
            return CONTENT_URI.buildUpon().appendPath(peer2PeerMessagesId).build();
        }

        /**
         * Read {@link #_ID} from {@link com.ptapp.provider.PTAppContract.Peer2PeerMessages} {@link Uri}.
         */
        public static String getPeer2PeerMessagesId(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }

    public static class MessageDetails implements MessageDetailsColumns, BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MESSAGE_DETAILS).build();
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.schoolo.message_details";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.schoolo.message_details";

        /**
         * Build {@link Uri} for requested {@link #_ID}.
         */
        public static Uri buildMessageDetailsUri(String messageDetailsId) {
            return CONTENT_URI.buildUpon().appendPath(messageDetailsId).build();
        }

        /**
         * Read {@link #_ID} from {@link com.ptapp.provider.PTAppContract.MessageDetails} {@link Uri}.
         */
        public static String getMessageDetailsId(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }

    public static Uri addCallerIsSyncAdapterParameter(Uri uri) {
        // We only notify changes if the caller is not the sync adapter.
        // The sync adapter has the responsibility of notifying changes (it can do so
        // more intelligently than we can -- for example, doing it only once at the end
        // of the sync instead of issuing thousands of notifications for each record).
        /*return uri.buildUpon().appendQueryParameter(
                ContactsContract.CALLER_IS_SYNCADAPTER, "true").build();*/
        return uri.buildUpon().appendQueryParameter(
                ContactsContract.CALLER_IS_SYNCADAPTER, "false").build();
    }

    public static boolean hasCallerIsSyncAdapterParameter(Uri uri) {
        return TextUtils.equals("true",
                uri.getQueryParameter(ContactsContract.CALLER_IS_SYNCADAPTER));
    }

    private PTAppContract() {
    }
}
