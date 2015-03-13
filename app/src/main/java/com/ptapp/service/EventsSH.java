package com.ptapp.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.gson.Gson;
import com.ptapp.activity.EventsMonthlyActivity;
import com.ptapp.io.model.Event;
import com.ptapp.bo.EventsBO;
import com.ptapp.app.R;
import com.ptapp.utils.CommonMethods;
import com.ptapp.utils.ServerUtilities;

import java.util.ArrayList;

/** Service handler for events. - to parse and insert events to calendar. */

public class EventsSH {
	private static final String TAG = "PTAppUI - EventsSH";
	private static EventsSH objEventsSH = null;
	private Context context;

	// Sets an ID for the notification - to update ntification later on.
	public static final int NOTIFICATION_ID = 001;

	/*
	 * A private Constructor prevents any other class from instantiating.
	 */
	private EventsSH(Context context) {
		this.context = context;
	}

	// changing the context to context.getApplicationContext(), as per this
	// link: http://www.doubleencore.com/2013/06/context/
	public static EventsSH getInstance(Context context) {
		if (objEventsSH == null) {
			objEventsSH = new EventsSH(context.getApplicationContext());
		}
		return objEventsSH;
	}

	// Get the Events JSON String from Stub.
	public String getJSONStringEvents() {
		ServiceProxy sp = new ServiceProxy();
		return sp.getJSONEvents();
	}

	// Parsing and inserting events.
	public void parseJSON(String js) {
		try {

			// convert from JSON string to events object.
			Gson gs = new Gson();
			EventsBO ev = gs.fromJson(js, EventsBO.class);

			// ArrayList<CalendarEventMapperBean> lstEvents = ev.getLstEvents();
			ArrayList<Event> lstEvents = null;
			Log.v(TAG, "Num of events: " + lstEvents.size());

			// TODO: 1. Save into app's database, the annual calendar
			// events/feed.
			// Q: What if, somehow event could not be saved into db, then what
			// to in that case. Should we proceed furthet or not.
			processEvents(lstEvents, false);

		} catch (Exception ex) {
			Log.e(TAG, ex.getMessage());
		}
	}

	/**
	 * @param lstEvents
	 * @param isURLBasedEvents
	 * @throws Exception
	 */
	public void processEvents(ArrayList<Event> lstEvents,
			boolean isURLBasedEvents) {
		// call method to save to App's db
		ServerUtilities.saveEventToAppsDb(context, lstEvents);

		// 2. Show notification depending upon the Events data
		String desc = "Events of your child learning journey.";
		String title = "Schoolo's annual calendar event.";

		// Post notification of received message.
		generateEventsNotification(context, title, desc, isURLBasedEvents, "",
				lstEvents.size()); // notifies user

		// 3. On tap on notification, open a Events screen, with popup on
		// it.
		// 4. IN the popup, show message with "Do not show it again" option
		// and OK button.
		// 5. In the events screen, Show a calendar on the upper portion and
		// in the remaining, show a listview with events in it, with kids
		// image on the right side.

		// 6. On click of popup's OK button - save into google calendar only
		// those events which are only
		// his/her kids classes. Then Updates the app's db with
		// androidEventId(Device's Calendar EventId).
		// Q: What if, somehow event could not be saved into calendar, then
		// what to in that case. Should we proceed furthet or not.
		try {
			// TODO:After, tapping of ok button in Events screen's popup.
			// ServerUtilities.saveEventToCalendar(context, lstEvents);
		} catch (Exception ex) {
			Log.e(TAG, ex.getMessage());
			ex.printStackTrace();
		}
	}

	/**
	 * Issues a notification to inform the user that server has sent a message.
	 * 
	 * @param evtType
	 */
	// TODO: LargeIcon depends upon the notification type(GCM message type).(not
	// on event type)
	private void generateEventsNotification(Context context, String title,
			String desc, boolean isURLBasedEvents, String evtType,
			int numOfEvents) {

		// set Pending Intent
		Intent notificationIntent = new Intent(context,
				EventsMonthlyActivity.class);
		notificationIntent.putExtra("isURLBasedEvents", isURLBasedEvents);
		// set intent so it does not start a new activity
		// notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
		// Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent intent = PendingIntent.getActivity(context, 0,
				notificationIntent, 0);

		// Notification sound
		Uri alarmSound = RingtoneManager
				.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

		// Build notification
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				context)
				.setLargeIcon(CommonMethods.getBitmapForEvent(context, evtType))
				// .setSmallIcon(CommonMethods.getImgForEvent(evtType))
				.setSmallIcon(R.drawable.ic_launcher).setContentTitle(title)
				.setContentText(desc)
				.setContentInfo(String.valueOf(numOfEvents))
				.setContentIntent(intent).setAutoCancel(true)
				.setSound(alarmSound);

		// send notification
		// Gets an instance of the NotificationManager service
		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(NOTIFICATION_ID, mBuilder.build());

		// long when = System.currentTimeMillis();
		// Notification notification = new Notification(icon, message, when);

	}

}
