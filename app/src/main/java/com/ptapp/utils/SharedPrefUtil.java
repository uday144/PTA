package com.ptapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.TimeZone;

/**
 * Created by lifestyle on 25-09-14.
 */
public class SharedPrefUtil {

    public SharedPrefUtil() {
    }

    /**
     * Boolean preference that when checked, indicates that the user would like to see times
     * in their local timezone throughout the app.
     */
    public static final String PREF_LOCAL_TIMES = "pref_local_times";
    /**
     * Boolean preference that when checked, indicates that the user will be attending the
     * conference.
     */
    public static final String PREF_ATTENDEE_AT_VENUE = "pref_attendee_at_venue";


    /**
     * Boolean preference that indicates whether we installed the bootstrap data or not.
     */
    public static final String PREF_DATA_BOOTSTRAP_DONE = "pref_data_bootstrap_done";


    private static final String PREF_FIRST_TIME_LAUNCH = "FirstTimeLaunch";

    /**
     * need for automatically re-login
     */
    private static final String PREF_SAUTH_ACCESS_GRANT = "SAuthAccessGrant";

    //EMAIL of the logged-in app user
    private static final String PREF_EMAIL_ACCOUNT = "pref_txt_email_account";

    //TODO:need to remove this, as it's being replaced by UserInRole below
    //Type of user - teacher or parent or both or student
    private static final String PREF_USER_TYPE = "pref_user_type";

    //Id of the logged-in user; can be parentId or EducatorId as per the user(using the app)
    private static final String PREF_APP_USER_ID = "pref_app_user_id";

    //private static final String PREF_TXT_NUM_OF_ROWS = "pref_txt_numOfRows";

    //Phone number used for installing the app, during registration process
    private static final String PREF_REGISTRATION_PHONE_NUMBER = "pref_registration_phone_number";

    //ISO Country code used to register with us/app
    private static final String PREF_ISO_COUNTRY_CODE_TO_REGISTER_WITH_APP = "pref_iso_country_code_to_register_with_app";

    //ApiRoles object storing in the form of string, received after submitting the OTP to server(Installation flow)
    private static final String PREF_API_ROLES_JSON_STRING = "pref_api_roles_json_string";

    //SchoolsBean object storing in the form of JSON string, received from the server(Installation flow)
    private static final String PREF_SCHOOLS_BEAN_JSON_STRING = "pref_schools_bean_json_string";

    //stores gcm registration Id
    private static final String PREF_GCM_REG_ID = "pref_gcm_registration_id";

    //stores app version
    private static final String PREF_APP_VERSION = "pref_app_version";

    //at present, user is in which role. At first time launch this will be empty.
    private static final String PREF_USER_IN_ROLE = "pref_user_in_role";

    //kid viewed at present/last viewed kid, when user leaves the app or switches role
    private static final String PREF_LAST_VIEWED_KID_STUDENT_ID = "pref_last_viewed_kid_student_id";

    //has confirmed/accepted the details about kids/teacher/student shown during registration/installation process
    //private static final String PREF_HAS_ACCEPTED_USER_DETAILS = "pref_has_accepted_user_details";


    public static TimeZone getDisplayTimeZone(Context context) {
        TimeZone defaultTz = TimeZone.getDefault();
        return (isUsingLocalTime(context) && defaultTz != null)
                ? defaultTz : Config.CONFERENCE_TIMEZONE;
    }

    public static boolean isUsingLocalTime(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean(PREF_LOCAL_TIMES, false);
    }

    public static void setUsingLocalTime(final Context context, final boolean usingLocalTime) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putBoolean(PREF_LOCAL_TIMES, usingLocalTime).commit();
    }

    public static void markDataBootstrapDone(final Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putBoolean(PREF_DATA_BOOTSTRAP_DONE, true).commit();
    }

    public static boolean isDataBootstrapDone(final Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean(PREF_DATA_BOOTSTRAP_DONE, false);
    }


    public static void setPrefSauthAccessGrant(final Context context, String authAccessGrant) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putString(PREF_SAUTH_ACCESS_GRANT, authAccessGrant).commit();
    }

    public static String getPrefSAuthAccessGrant(final Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString(PREF_SAUTH_ACCESS_GRANT, "");
    }

    public static void setPrefEmailAccount(final Context context, final String email) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putString(PREF_EMAIL_ACCOUNT, email).commit();
    }

    public static String getPrefEmailAccount(final Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString(PREF_EMAIL_ACCOUNT, "");
    }

    //TODO:This method may not need anymore, as user role is being saved in RolesBean.
    public static void setPrefUserType(final Context context, final String type) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putString(PREF_USER_TYPE, type).commit();
    }
    //TODO:This method may not need anymore, as user role is being saved in RolesBean.
    public static String getPrefUserType(final Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString(PREF_USER_TYPE, "");
    }

    public static void setPrefAppUserId(final Context context, String userId) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putString(PREF_APP_USER_ID, userId).commit();
    }

    public static String getPrefAppUserId(final Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString(PREF_APP_USER_ID, "");
    }

    public static void setPrefFirstTimeLaunch(final Context context, Boolean isFirstTimeLaunch) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putBoolean(PREF_FIRST_TIME_LAUNCH, isFirstTimeLaunch).commit();
    }

    public static Boolean getPrefFirstTimeLaunch(final Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean(PREF_FIRST_TIME_LAUNCH, true);
    }

    /*public static void setPrefTxtNumOfRows(final Context context, String numOfRows) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putString(PREF_TXT_NUM_OF_ROWS, numOfRows).commit();
    }

    public static String getPrefTxtNumOfRows(final Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString(PREF_TXT_NUM_OF_ROWS, "2");
    }*/

    public static void setPrefRegistrationPhoneNumber(final Context context, String phone) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putString(PREF_REGISTRATION_PHONE_NUMBER, phone).commit();
    }

    public static String getPrefRegistrationPhoneNumber(final Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString(PREF_REGISTRATION_PHONE_NUMBER, "");
    }

    public static String getPrefIsoCountryCodeToRegisterWithApp(final Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString(PREF_ISO_COUNTRY_CODE_TO_REGISTER_WITH_APP, "");
    }

    public static void setPrefIsoCountryCodeToRegisterWithApp(final Context context, String isoCountryCode) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putString(PREF_ISO_COUNTRY_CODE_TO_REGISTER_WITH_APP, isoCountryCode).commit();
    }


    public static void setPrefApiRolesJsonString(final Context context, String json) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putString(PREF_API_ROLES_JSON_STRING, json).commit();
    }

    public static String getPrefApiRolesJsonString(final Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString(PREF_API_ROLES_JSON_STRING, "");
    }

    public static void setPrefSchoolsBeanJsonString(final Context context, String json) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putString(PREF_SCHOOLS_BEAN_JSON_STRING, json).commit();
    }

    public static String getPrefSchoolsBeanJsonString(final Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString(PREF_SCHOOLS_BEAN_JSON_STRING, "");
    }

    /*public static void setPrefTeacherUserId(final Context context, String userId) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putString(PREF_TEACHER_USERID, userId).commit();
    }

    public static String getPrefTeacherUserId(final Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString(PREF_TEACHER_USERID, "");
    }

    public static void setPrefParentUserId(final Context context, String userId) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putString(PREF_PARENT_USERID, userId).commit();
    }

    public static String getPrefParentUserId(final Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString(PREF_PARENT_USERID, "");
    }*/

    public static void setPrefGcmRegId(final Context context, String regId) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putString(PREF_GCM_REG_ID, regId).commit();
    }

    public static String getPrefGcmRegId(final Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString(PREF_GCM_REG_ID, "");
    }

    public static void setPrefAppVersion(final Context context, int appVersion) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putInt(PREF_APP_VERSION, appVersion).commit();
    }

    public static int getPrefAppVersion(final Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getInt(PREF_APP_VERSION, Integer.MIN_VALUE);
    }

    public static void setPrefUserInRole(final Context context, String role) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putString(PREF_USER_IN_ROLE, role).commit();
    }

    public static String getPrefUserInRole(final Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString(PREF_USER_IN_ROLE, "");
    }

    public static void setPrefLastViewedKidStudentId(final Context context, int kidStudentId) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putInt(PREF_LAST_VIEWED_KID_STUDENT_ID, kidStudentId).commit();
    }

    public static int getPrefLastViewedKidStudentId(final Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getInt(PREF_LAST_VIEWED_KID_STUDENT_ID, 0);
    }

    /*public static void setPrefHasAcceptedUserDetails(final Context context, Boolean hasAccepted) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putBoolean(PREF_HAS_ACCEPTED_USER_DETAILS, hasAccepted).commit();
    }

    public static Boolean getPrefHasAcceptedUserDetails(final Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean(PREF_HAS_ACCEPTED_USER_DETAILS, false);
    }*/
}
