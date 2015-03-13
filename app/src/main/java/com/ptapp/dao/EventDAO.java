package com.ptapp.dao;

import android.content.Context;

/**
 * The class contains methods regarding saving, updating and fetching from the
 * device's calendar.
 */

public class EventDAO {

	private static final String TAG = "PTAppUI - EventsDAO";
	private Context context;

	public EventDAO(Context context) {
		this.context = context;
	}

	/**
	 * Inserting Events and Reminders into device's Google Calendar.
	 * 
	 * @param EventBO
	 *            object created, after parsing the JSON response from the
	 *            server.
	 * 
	 *            returns true - if event has been inserted, successfully.
	 *            returns false - if event has been updated, successfully.
	 */
	/*public boolean insertEventAndReminder(CalendarEventMapperBean event) {

		Log.v(TAG, "Entered into insertEventAndReminder method");

		// caution: month index starts with 0, instead of 1.
		long startMillis = 0;
		long endMillis = 0;

		String dateFormat = "yyyy-MM-dd HH:mm:ss";
		Calendar beginTime = Calendar.getInstance();
		Calendar endTime = Calendar.getInstance();
		beginTime.setTime(CommonMethods.getDateFromFormat(
				event.getStartDatetime(), dateFormat));
		startMillis = beginTime.getTimeInMillis();

		endTime.setTime(CommonMethods.getDateFromFormat(event.getEndDatetime(),
				dateFormat));
		endMillis = endTime.getTimeInMillis();

		ContentResolver cr = context.getContentResolver();
		*//* Events values *//*
		ContentValues values = new ContentValues();
		values.put(Events.DTSTART, startMillis);
		values.put(Events.DTEND, endMillis);
		values.put(Events.TITLE, event.getEventTitle());
		values.put(Events.DESCRIPTION, event.getEventDescription());
		values.put(Events.CALENDAR_ID, event.getCalId());
		values.put(Events.EVENT_TIMEZONE, TimeZone.getDefault().getID());

		*//* Reminder values *//*
		ContentValues reminders = new ContentValues();
		reminders.put(Reminders.METHOD, Reminders.METHOD_ALERT);
		reminders.put(Reminders.MINUTES, 1440); // 1440 = 1day.

		// Check whether to add new event into calendar or update an old one.
		CalendarEventMapperBean ce = IsEventDuplicate(event.getServerEventId());
		if (null != ce) {

			long androidEventId = ce.getAndroidEventId();
			if (androidEventId == 0) { // not pushed to calendar yet, new event.

				Log.i(TAG,
						"insert into calendar, because androidEventId in app's db is 0. androidEventId="
								+ androidEventId);
				insertEventIntoCalendar(event, cr, values, reminders);
				return true;
			} else { // an old event, has been pushed to calendar.

				Log.i(TAG,
						"update calendar, because androidEventId in app's db > 0. androidEventId="
								+ androidEventId);
				updateEventInCalendar(cr, values, reminders, androidEventId);
				return false;
			}
		} else {
			Log.v(TAG,
					"Error: Event is not available in app's db. Cannot push event into calendar, before saving in the app's db.");
			return false;
		}
	}

	*//**
	 * Updates old event and reminder in device's calendar.
	 * 
	 * @param cr
	 * @param values
	 * @param reminders
	 * @param androidEventId
	 *//*
	private void updateEventInCalendar(ContentResolver cr,
			ContentValues values, ContentValues reminders, long androidEventId) {
		// the number of rows updated.
		String[] selArgs = new String[] { Long.toString(androidEventId) };
		int updatedEvent = cr.update(Events.CONTENT_URI, values, Events._ID
				+ " =? ", selArgs);
		Log.i(TAG, "Events - num of rows updated : " + updatedEvent);

		*//* Update Reminder *//*
		// need to see if, reminder needs different mapping table or not.
		int updatedReminder = cr.update(Reminders.CONTENT_URI, reminders,
				Reminders.EVENT_ID + " =? ", selArgs);
		Log.i(TAG, "Reminders - num of rows updated: " + updatedReminder);
	}

	*//**
	 * Inserts new event into device's calendar. Adds reminder for kids classes
	 * only.
	 * 
	 * @param event
	 * @param cr
	 * @param values
	 * @param reminders
	 * @throws NumberFormatException
	 *//*
	private void insertEventIntoCalendar(CalendarEventMapperBean event,
			ContentResolver cr, ContentValues values, ContentValues reminders)
			throws NumberFormatException {

		// insert into calendar
		Uri uri = cr.insert(Events.CONTENT_URI, values);
		// get newly inserted eventID, which is last element in the Uri.
		long eventID = Long.parseLong(uri.getLastPathSegment());
		Log.i(TAG, "New event has been inserted into calendar, eventId: "
				+ eventID);

		// update the andriodEventId in the app's database.
		UpdateAndroidEventId(event.getServerEventId(), eventID);
		Log.i(TAG,
				"new androidEventId has been updated into the app's db. AndroidEventId: "
						+ eventID + " and serverEventId: "
						+ event.getServerEventId());

		*//**
		 * Add reminders only for those classes which matches with the
		 * kidsClasses or has parameter 'forClasses' set to "all".
		 *//*
		Log.d(TAG, "Adding reminders forClasses: " + event.getForClasses());
		if (event.getForClasses().contains("all")) {
			reminders.put(Reminders.EVENT_ID, eventID);
			Uri uri2 = cr.insert(Reminders.CONTENT_URI, reminders);
			long reminderID = Long.parseLong(uri2.getLastPathSegment());
			Log.i(TAG, "New inserted android ReminderId: " + reminderID);
		} else {
			if (!kidsClasses().isEmpty()) {
				String[] dbClasses = kidsClasses().split(",");
				String[] serverClasses = event.getForClasses().split(",");

				for (int i = 0; i < dbClasses.length; i++) {
					for (int j = 0; j < serverClasses.length; j++) {
						Log.d(TAG, "dbClass: " + dbClasses[i]
								+ ", serverClass: " + serverClasses[j]);
						if (dbClasses[i].trim().equalsIgnoreCase(
								serverClasses[j].trim())) {

							*//* Add reminder *//*
							reminders.put(Reminders.EVENT_ID, eventID);
							Uri uri2 = cr.insert(Reminders.CONTENT_URI,
									reminders);
							long reminderID = Long.parseLong(uri2
									.getLastPathSegment());
							Log.i(TAG, "New inserted android ReminderId: "
									+ reminderID + " for the class: "
									+ dbClasses[i]);
						}
					}
				}
			} else {
				Log.i(TAG, "No kids classes found in database.");
			}
		}
	}

	*//** Checks for Duplicate event. *//*
	// It is also used inside EventsSH class.
	// TODO:Fix this method, return cem, so that I can check android ID in
	// method insertEventAndReminder(). And serverEventId in the method
	// "saveEventToAppsDb()" in EventsSH.java.
	public CalendarEventMapperBean IsEventDuplicate(String serverEventId) {

		CalendarEventMapperDAO dsCEM = new CalendarEventMapperDAO(context);
		CalendarEventMapperBean cem = new CalendarEventMapperBean();
		cem = dsCEM.getCalendarEventMapperByServerEventId(serverEventId);
		// if (null == cem) {
		// return 0;
		// }
		return cem; // Event is already stored in app's db,
					// so check androidEventId. If
					// androidEvent is 0, means event is not
					// yet saved into device's calendar,
					// it's new event. Otherwise, it's an
					// old one, need to be updated.

	}

	*//**
	 * Updates the android generated event Id in the CalendarEventMapper table
	 *//*
	private void UpdateAndroidEventId(String serverEventId, long androidEventId) {
		// getting dataContext - table's data source object
		CalendarEventMapperDAO dsCEM = new CalendarEventMapperDAO(context);
		// dsCEM.addToCalendarEventMapper(androidEventID, serverEventId);
		dsCEM.updateAndroidEventId(androidEventId, serverEventId);
	}

	*//**
	 * @return comma-separated string of all kids classes, fetched from the
	 *         database tables.
	 *//*
	private String kidsClasses() {
		String kidsClasses = "";
		ClassDAO dcClass = new ClassDAO(context);
		StudentDAO dcStu = new StudentDAO(context);
		ArrayList<Long> stuIds = dcStu.getAllStudentIds();
		if (stuIds != null) {
			for (long item : stuIds) {
				String classId = dcStu.getClassIdOfStudent(item);
				String className = dcClass.getNameOfClass(classId);
				kidsClasses = kidsClasses + className + ",";
			}
		} else {
			Log.d(TAG, "No Student(Id) found in the database.");
		}

		// remove the last comma from the string.
		if (kidsClasses.endsWith(",")) {
			kidsClasses = kidsClasses.substring(0, kidsClasses.length() - 1);
		}
		Log.v(TAG, "dbKidsClasses: " + kidsClasses);
		return kidsClasses;
	}
*/
}
