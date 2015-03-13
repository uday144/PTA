/**
 *
 */
package com.ptapp.utils;

import com.ptapp.app.R;

/**
 * Helper class providing constants common to other classes in the app.
 */
public final class CommonConstants {

    public static final int NO_PHOTO_AVAILABLE = R.drawable.nophotoavailable;
    public static final int LOADING = R.drawable.loading;
    public static final int ERROR_IMAGE = R.drawable.error_image;

    /**
     * Base URL of the Demo Server (such as http://my_host:8080/gcm-demo)
     */
    /*public static final String SERVER_URL = "http://192.168.1.105:8080/gcm-demo/";*/
    /*public static final String SERVER_URL = "http://192.168.1.105:8080";*/
    public static final String SERVER_URL = "https://analog-arbor-540.appspot.com/";

    /**
     * Google API project id registered to use GCM.
     */
    public static final String SENDER_ID = "1050600610499";

    /**
     * Intent used to display a message in the screen.
     */
    public static final String DISPLAY_MESSAGE_ACTION = "com.google.android.gcm.demo.app.DISPLAY_MESSAGE";

    /**
     * Intent's extra that contains the message to be displayed.
     */
    public static final String EXTRA_MESSAGE = "message";

    //gcm
    public static final String ACTION_REGISTER = "com.google.android.c2dm.intent.REGISTRATION";

    /*chat variables*/
    public static final String PROFILE_ID = "profile_id";

    //parameters recognized by demo server
    public static final String FROM = "userId";
    public static final String REG_ID = "regId";
    public static final String MSG = "msg";
    public static final String MSG_ID = "msgId";
    public static final String TO = "userId2";
    public static final String CHAT_STATUS = "status";
    public static final String STATUS_FIRST_TICK = "sentToGcm";
    public static final String STATUS_SECOND_TICK = "gotTheMsg";
    public static final String STATUS_BLUE_DOUBLE_TICK = "hasReadTheMsg";
    public static final String USER_TYPE_TO = "userTypeTo";
    public static final String USER_TYPE_FROM = "userTypeFrom";
    /*chat variables ends*/


    //TODO:After checking, need to remove this user types, as Roles are defined below
    public static final String USER_TYPE_EDUCATOR = "STAFF";
    public static final String USER_TYPE_PARENT = "PARENT";
    //Both as a Teacher as well as Parent
    public static final String USER_TYPE_BOTH = "B";
    //confirm and change, if USER_TYPE_STUDENT = STUDENT.
    public static final String USER_TYPE_STUDENT = "S";


    //Intimation msg to the app server
    //public static final String FIRST_LOAD = "FT";

    public static final String TOS_LINK_APP_INSTALLATION = "http://www.google.com/intl/en/policies/terms/";
    public static final String TOS_LINK_APP_INFO_SHARING = "https://twitter.com/tos";

    /*public static enum Roles {
        STAFF, STUDENT, PARENT
    }*/
    //Roles - use these roles instead of user types above
    public static final String ROLE_STAFF = "STAFF";
    public static final String ROLE_PARENT = "PARENT";
    public static final String ROLE_STUDENT = "STUDENT";


    public static final String GENDER_MALE = "M";
    public static final String GENDER_FEMALE = "F";

    //Message Packet Element/Attributes...
    public static final String M_CUSTOM = "custom";
    public static final String M_BODY = "body";
    public static final String M_MESSAGE = "message";
    public static final String M_GROUP_ID = "groupid";
    public static final String M_GROUP_JID = "groupjid";
    public static final String M_IS_BROADCAST = "broadcast";
    public static final String M_STUDENT_ID = "stuId";

    //status for the chat messages
    public static final String STATUS_MSG_PENDING = "p";
    public static final String STATUS_MSG_SENT = "s";
    public static final String STATUS_MSG_DELIVERED = "d";
    public static final String STATUS_MSG_DELIVERY_FAILURE = "f";
    public static final String STATUS_MSG_VIEWED = "v";

}
