package com.ptapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.provider.CalendarContract.Calendars;
import android.util.Log;

import com.ptapp.io.model.Event;
import com.ptapp.dao.CalendarEventMapperDAO;
import com.ptapp.dao.EventDAO;
import com.ptapp.app.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import static com.ptapp.utils.CommonConstants.SERVER_URL;

/**
 * Helper class used to communicate with the GCM demo server.
 */
// TODO: Change this registration/unregistration of the regId to/from the app
// server, to the new GoogleCloudMessaging API code. After change, there will be
// no need for gcm.jar file in libs.

// At present it's using old dem server fro testing purposes.
public final class ServerUtilities {

    private static final int MAX_ATTEMPTS = 3;
    private static final int BACKOFF_MILLI_SECONDS = 2000;
    private static final Random random = new Random();
    private static final String TAG = "PTAppUI - ServerUtilities";
    private static final String GCMTAG = "GCM - ServerUtilities";

    //private static final String FROM = "from";
    //private static final String TO = "to";
    //private static final String MSG = "msg";

    //TODO:This was used when using GCM, remove if it doesn't require anymore
    /**
     * Register this account/device pair within the server.
     */
    public static String register(final Context context, final String regId) {
        String res = null;
        Log.i(GCMTAG, "registering device (regId = " + regId + ")");

        String serverUrl = SERVER_URL + "/register";
        Map<String, String> params = new HashMap<String, String>();
        params.put(CommonConstants.FROM, SharedPrefUtil.getPrefAppUserId(context));
        params.put(CommonConstants.USER_TYPE_FROM, SharedPrefUtil.getPrefUserType(context));
        params.put(CommonConstants.REG_ID, regId);
        //params.put(FROM, getParentEmailFromSharedPref(context));

        // Once GCM returns a registration id, we need to register it in the
        // demo server. As the server might be down, we will retry it a couple
        // times.


        try {
            CommonMethods.displayMessage(context, context.getString(
                    R.string.server_registering));

            res = post(serverUrl, params);

            // GCMRegistrar.setRegisteredOnServer(context, true);
            String message = context.getString(R.string.server_registered);
            CommonMethods.displayMessage(context, message);
            // TODO: Call the method(from schooloApp) to save 2 flags for
            // gcm
            // and server to save in sharedpref.
            return res;
        } catch (IOException ex) {
            Log.e(TAG, ex.getMessage());

            String message = context.getString(R.string.server_register_error,
                    MAX_ATTEMPTS);
            CommonMethods.displayMessage(context, message);
        }
        return res;
    }

    /**
     * Unregister this account/device pair within the server.
     */
    public static void unregister(final Context context, final String regId) {
        Log.i(GCMTAG, "unregistering device (regId = " + regId + ")");
        String serverUrl = SERVER_URL + "/unregister";
        Map<String, String> params = new HashMap<String, String>();
        params.put("regId", regId);
        try {
            post(serverUrl, params);
            // GCMRegistrar.setRegisteredOnServer(context, false);
            String message = context.getString(R.string.server_unregistered);
            CommonMethods.displayMessage(context, message);
        } catch (IOException e) {
            // At this point the device is unregistered from GCM, but still
            // registered in the server.
            // We could try to unregister again, but it is not necessary:
            // if the server tries to send a message to the device, it will get
            // a "NotRegistered" error message and should unregister the device.
            String message = context.getString(
                    R.string.server_unregister_error, e.getMessage());
            CommonMethods.displayMessage(context, message);
        }
    }

    /**
     * Send a message.
     */
    public static String send(String msgId,
                              String msg,
                              String to,
                              String userTypeTo,
                              String userTypeFrom,
                              Context context) throws IOException {
        //Log.i(TAG, "sending message (msg = " + msg + ")");
        String serverUrl = SERVER_URL + "/chat";
        Map<String, String> params = new HashMap<String, String>();
        params.put(CommonConstants.MSG, msg);
        params.put(CommonConstants.FROM, SharedPrefUtil.getPrefAppUserId(context));
        params.put(CommonConstants.TO, to);
        params.put(CommonConstants.MSG_ID, msgId);
        params.put(CommonConstants.USER_TYPE_TO, userTypeTo);
        params.put(CommonConstants.USER_TYPE_FROM, userTypeFrom);

        return post(serverUrl, params);
    }

    /**
     * Send a status for receiving the message.
     */
    public static String sendStatus(String msgId, String status, String to, Context context, String userTypeTo) throws IOException {
        //Log.i(TAG, "sending message (msg = " + msg + ")");
        String serverUrl = SERVER_URL + "/chat";
        Map<String, String> params = new HashMap<String, String>();
        params.put(CommonConstants.MSG_ID, msgId);
        params.put(CommonConstants.CHAT_STATUS, status);
        params.put(CommonConstants.FROM, SharedPrefUtil.getPrefAppUserId(context));
        params.put(CommonConstants.TO, to);
        params.put(CommonConstants.USER_TYPE_TO, userTypeTo);


        return post(serverUrl, params);
    }

    /**
     * Send a message to app server(with chatId), to send the first load api link.
     *//*
    public static String sendFirstLoadLink() throws IOException {

        String serverUrl = SERVER_URL + "/firstload";
        Map<String, String> params = new HashMap<String, String>();
        //params.put(SchooloApp.MSG, CommonConstants.FIRST_LOAD);
        params.put(SchooloApp.FROM, SchooloApp.getChatId());
        //params.put(SchooloApp.TO, to);

        return post(serverUrl, params);
    }*/

    /**
     * Create a group.
     */
    public static String create() {
        //Log.i(TAG, "creating group");
        String serverUrl = SERVER_URL + "/group";
        Map<String, String> params = new HashMap<String, String>();

        try {
            return post(serverUrl, params);
        } catch (IOException e) {
        }
        return null;
    }

    /**
     * Join a group.
     */
    public static void join(String to, Context context) throws IOException {
        //Log.i(TAG, "joining group");
        String serverUrl = SERVER_URL + "/join";
        Map<String, String> params = new HashMap<String, String>();
        params.put(CommonConstants.FROM, getParentEmailFromSharedPref(context));
        params.put(CommonConstants.TO, to);

        post(serverUrl, params);
    }

    /**
     * Issue a POST request to the server.
     *
     * @param endpoint POST address.
     * @param params   request parameters.
     * @return response
     * @throws java.io.IOException propagated from POST.
     */
    private static String executePost(String endpoint, Map<String, String> params) throws IOException {
        URL url;
        StringBuffer response = new StringBuffer();
        try {
            url = new URL(endpoint);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("invalid url: " + endpoint);
        }
        StringBuilder bodyBuilder = new StringBuilder();
        Iterator<Entry<String, String>> iterator = params.entrySet().iterator();
        // constructs the POST body using the parameters
        while (iterator.hasNext()) {
            Entry<String, String> param = iterator.next();
            bodyBuilder.append(param.getKey()).append('=').append(param.getValue());
            if (iterator.hasNext()) {
                bodyBuilder.append('&');
            }
        }
        String body = bodyBuilder.toString();
        Log.v(TAG, "Posting '" + body + "' to " + url);

        byte[] bytes = body.getBytes();
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setFixedLengthStreamingMode(bytes.length);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");

            // post the request
            OutputStream out = conn.getOutputStream();
            out.write(bytes);
            out.close();

            // handle the response
            int status = conn.getResponseCode();
            if (status != 200) {
                throw new IOException("Post failed with error code " + status);

            } else {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
            }
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return response.toString();
    }


    /**
     * Issue a POST with exponential backoff
     */
    private static String post(String endpoint, Map<String, String> params) throws IOException {
        long backoff = BACKOFF_MILLI_SECONDS + random.nextInt(1000);
        for (int i = 1; i <= MAX_ATTEMPTS; i++) {
            Log.d(GCMTAG, "Attempt #" + i + " to connect to app server");
            try {

                return executePost(endpoint, params);
            } catch (IOException e) {
                // Here we are simplifying and retrying on any error; in a real
                // application, it should retry only on unrecoverable errors
                // (like HTTP error code 503).
                Log.e(GCMTAG, "Failed to register on attempt " + i + ":" + e);
                if (i == MAX_ATTEMPTS) {
                    break;
                }
                try {
                    Log.d(GCMTAG, "Sleeping for " + backoff
                            + " ms before retry");
                    Thread.sleep(backoff);
                } catch (InterruptedException e1) {
                    // Activity finished before we complete - exit.
                    Log.d(GCMTAG,
                            "Thread interrupted: abort remaining retries!");
                    Thread.currentThread().interrupt();
                    return null;
                }
                // increase backoff exponentially
                backoff *= 2;
            } catch (IllegalArgumentException e) {
                throw new IOException(e.getMessage(), e);
            }
        }
        return null;
    }

    /**
     * Saves event to app's database.
     *
     * @param
     */
    public static void saveEventToAppsDb(Context context,
                                         ArrayList<Event> lstEvents) {

        for (Event item : lstEvents) {

            EventDAO evDAO = new EventDAO(context);
            CalendarEventMapperDAO ceDAO = new CalendarEventMapperDAO(context);

            // Checking whether to add new event or update the old one.
            /*CalendarEventMapperBean ce = evDAO.IsEventDuplicate(item
                    .getServerEventId());
            if (null == ce) { // new event- doesn't exist in app's db.
                ceDAO.addEvent(item);
            } else { // an old event- exists in app's db.

                int rows = ceDAO.updateEvent(item);
                if (rows > 0) {
                    Log.i(TAG, " Event has been updated in app's db.");
                } else {
                    Log.i(TAG,
                            " Due to some reasons, event couldn't updated in app's db.");
                }
            }*/
        }
        CommonMethods.takeDbBackup(context);
        Log.v(TAG, "Db backup taken.");
    }

    /**
     * Saves/pushes event into device's calendar, like google calendar.
     *
     * @param lstEvents - list of events(CalendarEventMapperBean objects)
     * @throws Exception
     */
    public static void saveEventToCalendar(Context context,
                                           ArrayList<Event> lstEvents) throws Exception {
        // get parent Email
        String pEmail = getParentEmailFromSharedPref(context);

        // getting calendar Id corresponding to the parent email account.
        long calId = getCalendarId(context, pEmail);
        if (calId == -1) { // accounts found, but none matched.
            Log.i(TAG,
                    "Please ensure that you have set the correct email in the settings. Because this email doesn't match with the google calendar's email.");
        } else if (calId == -2) { // No gmail account found in google
            // calendar
            Log.i(TAG,
                    "No google account found in user's device google calendar. ");
        } else {
            Log.i(TAG,
                    "CalendarId of the matched account with app's settings, is: "
                            + calId);
            // Insert or/and Update in google calendar.
            insertUpdateEventAndReminder(context, calId, pEmail, lstEvents);
        }
    }

    private static long getCalendarId(Context context, String pEmail)
            throws Exception {

        // specifying the columns to fetch.
        String[] projection = new String[]{Calendars._ID,
                Calendars.ACCOUNT_NAME};

        /**
         * Querying the Calendar 'Calendars.CONTENT_URI' - Path of the calendar
         * table in android 'projection' - specifying the columns to fetch
         * 'Calendars.VISIBLE + "= 1"' - fetch calendar accounts which are set
         * active by user. (like a where clause in SQL) 'Calendars._ID + " ASC"'
         * - order by clause
         */
        Cursor c = context.getContentResolver().query(Calendars.CONTENT_URI,
                null, null, null, Calendars._ID + " ASC");

        // ArrayList<String> listAcctNames = new ArrayList<String>();

        // getting total numbers of calendar accounts.
        int numOfCalendarAccounts = c.getCount();

        if (numOfCalendarAccounts > 0) {

            long addEventsToAcctid = -1;
            try {
                while (c.moveToNext()) {
                    // getting Calendar Id e.g. 1,2,3..
                    String acctId = c
                            .getString(c.getColumnIndex(Calendars._ID));

                    // getting Calendar name e.g. test@gmail.com.
                    String calendarName = c.getString(c
                            .getColumnIndex(Calendars.NAME));

                    // If the gmail account in the App settings matches with
                    // gmail account in the device google calendar,
                    // then get that account's calendar Id, to insert events.
                    if (pEmail.equalsIgnoreCase(calendarName)) {
                        addEventsToAcctid = c.getLong(c
                                .getColumnIndex(Calendars._ID));
                    }

                    Log.i(TAG, "calendarAcct id found:" + acctId);

                    Log.i(TAG, "calendar name: " + calendarName);

                    // adding into list listAcctNames.add(acctId + ". " +
                    // acctName);
                    // listAcctNames.add(acctName);

                }
                return addEventsToAcctid;

            } catch (Exception ex) {
                throw ex;
            } finally {
                c.close();
            }
        } else { // No gmail account found in device's calendar
            return -2;
        }
    }

    /**
     * @return
     */
    private static String getParentEmailFromSharedPref(Context context) {
        SharedPreferences sharedPref = PreferenceManager
                .getDefaultSharedPreferences(context);

        // Parent email Id stored in Preferences.
        String pEmail = sharedPref.getString("pref_txt_EmailAccount", "");
        Log.i(TAG, "parent email: " + pEmail);
        return pEmail;
    }

    /**
     * @param calId
     * @param
     * @param
     */
    private static void insertUpdateEventAndReminder(Context context,
                                                     long calId, String email,
                                                     ArrayList<Event> lstEvents) {
        int EventsSaved = 0;
        int EventsUpdated = 0;

        boolean eventAdded;
        for (Event item : lstEvents) {
            item.setCalId(calId);

            // Pass the EventBO to DAO
            EventDAO eveDAO = new EventDAO(context);
            /*eventAdded = eveDAO.insertEventAndReminder(item);
            if (eventAdded) {
                EventsSaved++;
            } else {
                EventsUpdated++;
            }*/
        }

		/*
         * Start an activity to display the dialog box to inform the user that
		 * events and reminders has been added or updated.
		 */
        // Intent savedMsg = new Intent();
        // savedMsg.putExtra("EmailAccount", email);
        // savedMsg.putExtra("EventsSaved", EventsSaved);
        // savedMsg.putExtra("EventsUpdated", EventsUpdated);
        // savedMsg.setClass(context, EventsSavedMsg.class);
        // savedMsg.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // savedMsg.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        // context.startActivity(savedMsg);
    }
}
