/*
 * Copyright 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ptapp.app;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
//import com.ptapp.activity.ChatActivity;
import com.ptapp.bean.api.jsonString;
import com.ptapp.bean.MessagesBean;
import com.ptapp.bo.StudentsBO;
import com.ptapp.dao.EducatorDAO;
import com.ptapp.dao.MessagesDAO;
import com.ptapp.dao.ParentDAO;
import com.ptapp.provider.PTAppContract;
import com.ptapp.service.ApiClient;
import com.ptapp.service.EventsSH;
import com.ptapp.service.FTLoadSH;
import com.ptapp.sync.PTAppDataHandler;
import com.ptapp.utils.CommonConstants;
import com.ptapp.utils.CommonMethods;
import com.ptapp.utils.Config;
import com.ptapp.utils.ServerUtilities;
import com.ptapp.utils.SharedPrefUtil;

import java.io.IOException;

import retrofit.RetrofitError;

import static com.ptapp.utils.CommonConstants.SENDER_ID;
import static com.ptapp.utils.LogUtils.LOGD;
import static com.ptapp.utils.LogUtils.LOGE;
import static com.ptapp.utils.LogUtils.LOGI;

//import com.ptapp.dao.MsgProfileDAO;
/*
import com.ptapp.activity.HomeActivity;
import com.ptapp.bean.CalendarEventMapperBean;
import com.ptapp.bean.MessagesBean;
import com.ptapp.bo.SeverCalendarBO;
import com.ptapp.bo.StudentsBO;
import com.ptapp.dao.MessagesDAO;
import com.ptapp.service.ApiClient;
import com.ptapp.service.ApiClient.FTEventFeed;
import com.ptapp.service.ApiClient.StudentInfo;
import com.ptapp.service.EventsSH;
import com.ptapp.service.FTLoadSH;
*/
//import retrofit.RetrofitError;

/**
 * This {@code IntentService} does the actual handling of the GCM message.
 * {@code GcmBroadcastReceiver} (a {@code WakefulBroadcastReceiver}) holds a
 * partial wake lock for this service while the service does its work. When the
 * service is finished, it calls {@code completeWakefulIntent()} to release the
 * wake lock.
 */
public class GCMIntentService extends IntentService {

    private static final String TAG = "GCMIntentService";

    public GCMIntentService() {
        super(SENDER_ID);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        Log.i(TAG, "Received message. Extras: " + extras);

        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);
        Log.i(TAG, "msg type: " + messageType);

        if (extras != null && !extras.isEmpty()) { // has effect of unparcelling Bundle
            /*
             * Filter messages based on message type. Since it is likely that
			 * GCM will be extended in the future with new message types, just
			 * ignore any message types you're not interested in, or that you
			 * don't recognize.
			 */
            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR
                    .equals(messageType)) {
                Log.w(TAG, "Send error: " + extras.toString());
                //sendNotification("Send error", false);

            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED
                    .equals(messageType)) {
                Log.i(TAG, "Deleted messages on server: " + extras.toString());
                //sendNotification("Deleted messages on server", false);

                // If it's a regular GCM message, do some work.
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE
                    .equals(messageType)) {

                Log.i(TAG, "gcm received is: " + extras);
                //sendNotification("gcm : " + extras, true, getApplicationContext());

                gcmHandle(getApplicationContext(), extras);

                Log.i(TAG, "Completed work @ " + SystemClock.elapsedRealtime());

            }
        }

        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    /**
     * @param context
     * @param b       = bundle, extras - intent.getExtras();
     */
    private void gcmHandle(Context context, Bundle b) {

        String gcmNotfType = b.getString("notf.typ");
        //String gcmMsgs = b.getString("message");

        if (gcmNotfType != null) {
            if (gcmNotfType.equalsIgnoreCase("EVT")) {
                gcmEvents(context, b);
            }
            if (gcmNotfType.equalsIgnoreCase("FTL")) {
                gcmFTLoad(context, b);
            }
        }
        if (b != null) {

            // TODO: handle Chat messages - gcmMsgEvents(context, b);

            Log.i(TAG, "bundle : " + b);

            String msg = b.getString(CommonConstants.MSG);
            String from = b.getString(CommonConstants.FROM);
            String userTypeFrom = b.getString(CommonConstants.USER_TYPE_FROM);
            String to = b.getString(CommonConstants.TO);
            String status = b.getString(CommonConstants.CHAT_STATUS);
            String msgId = b.getString(CommonConstants.MSG_ID);

            MessagesDAO daoMsg = new MessagesDAO(context);

            if (msg != null) { //chat msg from someone(device)
                String contactName = null;
                if (userTypeFrom.equals(CommonConstants.USER_TYPE_PARENT)) {
                    //search in Parent table
                    ParentDAO daoPar = new ParentDAO(context);
                    //contactName = daoPar.getParent(from).getfName();
                } else if (userTypeFrom.equals(CommonConstants.USER_TYPE_EDUCATOR)) {
                    //search in Educator table
                    EducatorDAO daoEdu = new EducatorDAO(context);
                    //contactName = daoEdu.getEducator(from).getfName();
                }
                if (contactName == null) return;

                MessagesBean m = new MessagesBean();
                m.setMsgText(msg);
                m.setFromCID(from);
                m.setToCID(to);
                //m.setStatus(CommonConstants.STATUS_SECOND_TICK); //not needed as of now.
                //long newmsgId = daoMsg.addMessage(m);
                /*if (newmsgId > 0) { //msg is stored in db

                    if (!from.equals(SchooloApp.getCurrentChat()) && !to.equals(SchooloApp.getCurrentChat())) {
                        if (SchooloApp.isNotify()) {
                            sendNotification(contactName + ": " + msg, true, context, from, userTypeFrom);
                        }
                        incrementMessageCount(context, from, to, userTypeFrom);
                    } else {
                        //the activity is opened
                        //update the chat
                        Intent intent = new Intent(context, ChatActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        //intent.putExtra("data","from outside");
                        context.startActivity(intent);
                    }

                    //send intimation to the device who had sent it in first place
                    send(msgId, CommonConstants.STATUS_SECOND_TICK, from, context, userTypeFrom);
                }*/
            }
            if (status != null) {   //it's not a msg from someone, instead it's a status about the msg this device had sent earlier to someone
                if (status.equals(CommonConstants.STATUS_FIRST_TICK)) {
                    //show first tick on the msg
                    // * need to have some type of Id to find out that this status is for which message.(eg.: msgId)
                    //update the status of the message sent earlier
                    //daoMsg.updateMsgStatus(msgId, CommonConstants.STATUS_FIRST_TICK);
                    Log.i(TAG, "Received status: First tick for msg between sender=" + from + "and sentTo=" + to + " and the msgId is: " + msgId);

                } else if (status.equals(CommonConstants.STATUS_SECOND_TICK)) {
                    //show second tick on the msg
                    //daoMsg.updateMsgStatus(msgId, CommonConstants.STATUS_SECOND_TICK);
                    Log.i(TAG, "Received status: Second tick for msg between sender=" + from + "and sentTo=" + to + " and the msgId is: " + msgId);
                }
                if (!from.equals(SchooloApp.getCurrentChat()) && !to.equals(SchooloApp.getCurrentChat())) {

                } else {
                    //the activity is opened
                    //update the chat
                    /*Intent intent = new Intent(context, ChatActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    //intent.putExtra("data","from outside");
                    context.startActivity(intent);*/
                }
            }
        }

        // TODO: Do something with the acknowledgement paramaeter.
        // String gcmAckMsg = b.getString("ack.msg");
    }

    //send status of the msg it has received
    private void send(final String msgId, final String status, final String to, final Context context, final String userTypeTo) {
        new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... params) {
                String res = "";
                try {
                    res = ServerUtilities.sendStatus(msgId, status, to, context, userTypeTo);

                } catch (IOException ex) {
                    res = ex.getMessage();
                }
                return res;
            }

            @Override
            protected void onPostExecute(String res) {
                if (!TextUtils.isEmpty(res)) {

                    //Toast.makeText(getApplicationContext(), res, Toast.LENGTH_LONG).show();

                }
            }
        }.execute(null, null, null);
    }

    private void incrementMessageCount(Context context, String from, String to, String userTypeFrom) {
        String chatId;
        if (!SharedPrefUtil.getPrefAppUserId(context).equals(to)) {//group
            chatId = to;
        } else {
            chatId = from;
        }

        if (userTypeFrom.equals(CommonConstants.USER_TYPE_PARENT)) {
            //search in Parent table
            ParentDAO daoPar = new ParentDAO(context);
            /*int count = daoPar.getParent(chatId).getMsgCount();
            daoPar.updateMsgCount(count + 1, chatId);*/
        } else if (userTypeFrom.equals(CommonConstants.USER_TYPE_EDUCATOR)) {
            //search in Educator table
            EducatorDAO daoEdu = new EducatorDAO(context);
            /*int count = daoEdu.getEducator(chatId).getMsgCount();
            daoEdu.updateMsgCount(count + 1, chatId);*/
        }
        /*MsgProfileDAO daoPrf = new MsgProfileDAO(context);
        int count = daoPrf.getMsgProfileByChatId(chatId).getCount();
        daoPrf.updateMsgCount(count + 1, chatId);*/
    }

    private void sendNotification(String text, boolean launchApp, Context context, String from, String userTypeFrom) {
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification.Builder mBuilder = new Notification.Builder(context)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText(text);

        if (!TextUtils.isEmpty(SchooloApp.getRingtone())) {
            mBuilder.setSound(Uri.parse(SchooloApp.getRingtone()));
        }

        if (launchApp) {
            /*Intent intent = new Intent(context, ChatActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            if (userTypeFrom.equals(CommonConstants.USER_TYPE_PARENT)) {
                intent.putExtra("userId", from);
                intent.putExtra("userType", CommonConstants.USER_TYPE_PARENT);
            } else if (userTypeFrom.equals(CommonConstants.USER_TYPE_EDUCATOR)) {
                intent.putExtra("userId", from);
                intent.putExtra("userType", CommonConstants.USER_TYPE_EDUCATOR);
            }
            PendingIntent pi = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(pi);*/
        }

        mNotificationManager.notify(1, mBuilder.getNotification());
    }

    /**
     * @param b
     */
    private void gcmEvents(Context context, Bundle b) {

        if (b.getString("wpayld").equalsIgnoreCase("T")) {

            // parse the payload/content/repeated list
            EventsSH.getInstance(this).parseJSON(b.getString("payld"));

        } else {
            Log.i(TAG, "Api link to hit: " + b.getString("lnk.api"));
            String apiLink = b.getString("lnk.api");
            Log.i(TAG, "Hitting the api link to fetch events json...");
            /*ArrayList<Event> lstEvents = eventsFromApiLink(apiLink);*/
            String eventsJson = eventsFromApiLink(apiLink);
            LOGD(TAG, "Events json fetched from an api link.");
            //TODO:Waiting for the amazon server to be ready to store the gcm RegId and to have a html page from where
            //I can push the events through gcm, which should be received by the GCMIntentService

            insertEvents(context, eventsJson);
        }
    }

    private void insertEvents(Context context, String eventsJson) {
        try {
            //---------content provider-------
            LOGD(TAG, "Starting events insertion process.");
            // Apply the data we read to the database with the help of the ConferenceDataHandler
            PTAppDataHandler dataHandler = new PTAppDataHandler(context);
            //TODO:Change the BOOTSTRAP_DATA_TIMESTAMP with appropiate time
            dataHandler.applyConferenceData(new String[]{eventsJson},
                    Config.BOOTSTRAP_DATA_TIMESTAMP, false);
            //SyncHelper.performPostSyncChores(appContext);
            LOGI(TAG, "End of events insertion process -- successful.");
            //SharedPrefUtil.markSyncSucceededNow(appContext);

            getContentResolver().notifyChange(Uri.parse(PTAppContract.CONTENT_AUTHORITY),
                    null, false);
            //---------content provider-------
            CommonMethods.takeDbBackup(context);

            /*EventsSH.getInstance(context).processEvents(lstEvents, true);*/
        } catch (Exception ex) {
            if (ex.getMessage() != null) {
                LOGE(TAG, ex.getMessage());
            } else {
                LOGE(TAG, "insertEvents: No exception message for this error.");
            }
        }
    }

    /**
     * @param apiLink
     * @return
     *//*
    private ArrayList<Event> eventsFromApiLink(String apiLink) {
        ArrayList<Event> lstSrvEvts = null;
        try {
            String eventId = apiLink.substring(apiLink.lastIndexOf("/") + 1);
            String endPt = apiLink.substring(0, apiLink.lastIndexOf("/"));
            Log.d(TAG, "id: " + eventId);
            Log.d(TAG, "endpt: " + endPt);

            ApiClient.FTEventFeed ftEventFeed = ApiClient.getFTEventFeed(endPt);
            SeverCalendarBO servCal = ftEventFeed.getEvent(eventId);
            Log.v(TAG, "retrofit " + servCal);
            lstSrvEvts = servCal.getVCALENDAR().getLstEvents();
            Log.v(TAG, "retrofit list size:" + lstSrvEvts.size());
        } catch (RetrofitError rex) {
            Log.i(TAG, "retorfit err");
        } catch (Exception ex) {
            if (ex.getMessage() != null) {
                Log.e(TAG, ex.getMessage());
            } else {
                Log.e(TAG, "gcmEvents: No exception message for this error.");
            }
        }
        return lstSrvEvts;
    }*/

    /**
     * @param apiLink
     * @return
     */
    private String eventsFromApiLink(String apiLink) {
        String str = null;
        try {
            String eventId = apiLink.substring(apiLink.lastIndexOf("/") + 1);
            String endPt = apiLink.substring(0, apiLink.lastIndexOf("/"));
            Log.d(TAG, "id: " + eventId);
            Log.d(TAG, "endpt: " + endPt);

            ApiClient.EventFeed eventFeed = ApiClient.getEventFeed(endPt);
            jsonString servCal = eventFeed.getEvent(eventId);
            Log.v(TAG, "retrofit " + servCal);
            str = servCal.getStrJSON();
            Log.v(TAG, "events JSON string:" + str);
        } catch (RetrofitError rex) {
            Log.i(TAG, "retorfit err");
        } catch (Exception ex) {
            if (ex.getMessage() != null) {
                Log.e(TAG, ex.getMessage());
            } else {
                Log.e(TAG, "gcmEvents: No exception message for this error.");
            }
        }
        return str;
    }

    /**
     * @param b
     */
    private void gcmFTLoad(Context context, Bundle b) {

        if (b.getString("wpayld").equalsIgnoreCase("T")) {

            // parse the payload/content/repeated list
            // EventsSH.getInstance(this).parseJSON(b.getString("payld"));

        } else {
            try {
                Log.i(TAG, "api link to hit: " + b.getString("lnk.api"));
                String apiLink = b.getString("lnk.api");
                // TODO: use Retrofit to hit api link
                // 1. Use retrofit - to get Calendar events in JSON format and
                // make
                // out objects out of JSON format.
                // 2. Fill the objects and it's properties of the classes used
                // for
                // inserting events into device's calendar.
                // 3. insert events into calendar.

                StudentsBO ff = fTLoadFromApiLink(apiLink);

                FTLoadSH.getInstance(context).processFTFeed(ff);

            } catch (RetrofitError rex) {
                Log.i(TAG, "retorfit err");
            } catch (Exception ex) {
                if (ex.getMessage() != null) {
                    Log.e(TAG, ex.getMessage());
                } else {
                    Log.e(TAG,
                            "gcmEvents: No exception message for this error.");
                }
            }
        }
    }

    /**
     * @param apiLink
     * @return
     */
    private StudentsBO fTLoadFromApiLink(String apiLink) {
        String ftlId = apiLink.substring(apiLink.lastIndexOf("/") + 1);
        String endPt = apiLink.substring(0, apiLink.lastIndexOf("/"));
        Log.d(TAG, "id: " + ftlId);
        Log.d(TAG, "endpt: " + endPt);

        ApiClient.StudentInfo ftLoadFeed = ApiClient.getStudentInfo(endPt);
        StudentsBO stus = ftLoadFeed.getStu(ftlId);

        return stus;
    }

}
