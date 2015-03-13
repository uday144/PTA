package com.ptapp.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.ptapp.bo.StudentsBO;
import com.ptapp.dao.CalendarEventMapperDAO;
import com.ptapp.app.R;
import com.ptapp.app.SchooloApp;

import java.util.Calendar;
import java.util.Locale;

/**
 * Main UI for the demo app.
 */
public class EventsMonthlyActivity extends BaseActivity {
    // TODO: highlight the current happening event.(how to do)

    private static final String TAG = "PTAppUI - EventListActivity";
    boolean isURLBasedEvents;
    // From, stop showing popup again & again, and pushing to calendar.
    boolean isEventPushedToCalendar = false;
    TextView tvNoEvents, tvWhichMonth;
    ListView lvMain;

    private SharedPreferences sharedPref;
    private String month;
    private String year;

    private StudentsBO boStudents;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_list);
        tvNoEvents = (TextView) findViewById(R.id.tv_no_events);
        tvWhichMonth = (TextView) findViewById(R.id.tv_whichMonth);
        lvMain = (ListView) findViewById(R.id.listV_main);

        try {
            sharedPref = SchooloApp.sharedPref;
            boStudents = ((SchooloApp) getApplicationContext()).getStudentsBO();

        } catch (Exception ex) {
            if (ex.getMessage() != null) {
                Log.e(TAG, ex.getMessage());
            } else {
                Log.e(TAG, "No exception message for this error.");
            }
        }

        Calendar c = Calendar.getInstance(); // getting current month, date

        if (null != savedInstanceState) {

            // Restore UI state from the savedInstanceState.
            month = savedInstanceState.getString("month");
            year = savedInstanceState.getString("year");
        } else {
            // and year..
            // add +1 to month, as month is 0 based index.
            month = addZeroOrNot(c.get(Calendar.MONTH) + 1 + ""); // current
            // month.
            year = String.valueOf(c.get(Calendar.YEAR)); // current year.

            try {
                Log.i(TAG, "mn: " + month + ", " + year);

                // if it's intent, show events of month specified in the extras.
                if (getIntent() != null) {
                    isURLBasedEvents = getIntent().getBooleanExtra(
                            "isURLBasedEvents", false);

                    Bundle b = getIntent().getBundleExtra(
                            "com.ptapp.activity.ShowHomeEvent");
                    if (b != null) {

                        Log.i(TAG, "extras: " + b);
                        int eventMonth = b.getInt("eventMonth",
                                c.get(Calendar.MONTH)); // default is current
                        // month.
                        int eventYear = b.getInt("eventYear",
                                c.get(Calendar.YEAR));

                        month = addZeroOrNot(eventMonth + 1 + "");
                        year = eventYear + "";
                        Log.i(TAG, "from home events month, year: " + month
                                + ", " + year);
                    }
                }
            } catch (Exception ex) {
                if (ex.getMessage() != null) {
                    Log.e(TAG, ex.getMessage());
                } else {
                    Log.e(TAG, "No exception message for this error.");
                }
            }
        }

        showMonthName();
        getEventsForMonth();
    }

    /**
     * Checks if length of month is double_tick digit, if not, adds 0 in-front of the
     * digit.
     *
     * @return double_tick digit month as String.
     */
    private String addZeroOrNot(String month) {
        if (month.length() < 2) {
            month = "0" + month;
        }
        return month;
    }

    /**
     * @throws NumberFormatException
     */
    private void showMonthName() throws NumberFormatException {
        // show month name.
        Calendar engMonth = Calendar.getInstance();
        engMonth.set(Calendar.MONTH, Integer.parseInt(month) - 1);
        tvWhichMonth.setText("Events of month: "
                + engMonth.getDisplayName(Calendar.MONTH, Calendar.LONG,
                Locale.US) + ", " + year);
    }

    /**
     * Getting list of monthly events, from app's db.
     */
    private void getEventsForMonth() {

        CalendarEventMapperDAO daoCEM = new CalendarEventMapperDAO(
                EventsMonthlyActivity.this);
        /*final ArrayList<CalendarEventMapperBean> lstEvents = daoCEM
                .getMonthWiseEvents(year + "-" + month);
        Log.v(TAG, "number of this month events:" + lstEvents.size());

        if (lstEvents.size() > 0) {

            // This view is invisible, and it doesn't take any space for
            // layout purposes.
            tvNoEvents.setVisibility(View.GONE);

            Map<String, StudentBO> mapStus = boStudents.getStudentsAll();
            ArrayList<StudentBO> lstChildInfo = new ArrayList<StudentBO>();
            for (StudentBO item : mapStus.values()) {
                lstChildInfo.add(item);
            }
            // Setting list-view adapter.
            lvMain.setAdapter(new EventsMonthlyLvAdapter(this, lstEvents,
                    lstChildInfo));

            // Pop-up message to inform user about saving events in
            // Calendar.
            showPopupDialogEvents(isURLBasedEvents);

        } else {
            tvNoEvents.setText(R.string.no_events_found);
            tvNoEvents.setVisibility(View.VISIBLE);
            lvMain.setAdapter(null);
        }*/
    }

    /**
     * Popup dialog to inform user about saving events in Calendar.
     */
    private void showPopupDialogEvents(boolean isURLBasedEvents) {

        try {
            if (isEventPushedToCalendar == false && isURLBasedEvents == true) {
                boolean isSharedPrefCbxDontShowFlag = sharedPref.getBoolean(
                        "IsCbxDontShowFlag", false);
                Log.i(TAG, "SharedPref Checbox DontShowFlag: "
                        + isSharedPrefCbxDontShowFlag);

                if (isSharedPrefCbxDontShowFlag == false) {
                    showPopupMsg();
                } else {
                    // directly save the events to calendar.
                    pushEventsToCalendar();
                    isEventPushedToCalendar = true;
                    Log.i(TAG,
                            "flag is true, so no need to show popup, directly saved events to calendar.");
                }
            }
        } catch (Exception ex) {
            if (ex.getMessage() != null) {
                Log.e(TAG, ex.getMessage());
            } else {
                Log.e(TAG,
                        "showPopupDialogEvents - No exception message for this error.");
            }
        }

    }

    /**
     *
     */
    private void showPopupMsg() {
        LayoutInflater layInflater = LayoutInflater
                .from(EventsMonthlyActivity.this);

        View vwCheckbox = layInflater.inflate(R.layout.checkbox, null);
        final CheckBox dontShowAgain = (CheckBox) vwCheckbox
                .findViewById(R.id.dont);

        // ///Change check-box text on click of it.
        dontShowAgain.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.v(TAG, "onchkbx clicked.");
                if (dontShowAgain.isChecked()) {
                    dontShowAgain.setText(R.string.dont_show_checked_text);
                } else {
                    dontShowAgain.setText(R.string.dont_show_text);
                }
            }

        });

        AlertDialog.Builder builder = new AlertDialog.Builder(
                EventsMonthlyActivity.this);
        builder.setTitle(R.string.event_popup_title);
        builder.setMessage(R.string.event_popup_msg);
        builder.setCancelable(false);
        builder.setIcon(android.R.drawable.ic_menu_more);
        builder.setView(vwCheckbox);

        builder.setPositiveButton(android.R.string.yes,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        try {

                            pushEventsToCalendar();

                            // If check-box is checked, change flag to
                            // true.
                            if (dontShowAgain.isChecked()) {

                                Editor ed = sharedPref.edit();
                                ed.putBoolean("IsCbxDontShowFlag", true);
                                boolean res = ed.commit();
                                if (res) {
                                    Log.i(TAG,
                                            "dontshow checkbox value is set to true(checked).");

                                } else {
                                    Log.i(TAG,
                                            "setting dontshow checkbox value to true(checked) has been failed.");
                                }
                            }

                            isEventPushedToCalendar = true;
                            Log.i(TAG,
                                    "Ok button clicked - events saved into calendar.");

                        } catch (Exception ex) {
                            if (ex.getMessage() != null) {
                                Log.e(TAG, ex.getMessage());
                            } else {
                                Log.e(TAG,
                                        "No exception message for this error.");
                            }
                        }
                    }

                });
        builder.show();
    }

    /**
     *
     */
    private void pushEventsToCalendar() {
        // get all events which have not saved yet to
        // calendar, means which has andriodEventId = null
        // or 0
        try {
            CalendarEventMapperDAO daoCEM = new CalendarEventMapperDAO(
                    EventsMonthlyActivity.this);
            /*final ArrayList<CalendarEventMapperBean> lstEvents = daoCEM
                    .getEventsNotSavedToCalendar();

            ServerUtilities.saveEventToCalendar(EventsMonthlyActivity.this,
                    lstEvents);*/
        } catch (Exception ex) {
            if (ex.getMessage() != null) {
                Log.e(TAG, ex.getMessage());
            } else {
                Log.e(TAG,
                        "pushEventsToCalendar - No exception message for this error.");
            }
        }
    }

    // On click of previous month button.
    public void onClickPreviousMonth(View view) {
        try {

            month = addZeroOrNot(String.valueOf(Integer.parseInt(month) - 1));
            if (month.equalsIgnoreCase("00")) {
                month = "12"; // change it to month december.
                year = String.valueOf(Integer.parseInt(year) - 1);
            }
            Log.i(TAG, "prev month, year: " + month + ", " + year);
            showMonthName();
            getEventsForMonth();

        } catch (Exception ex) {
            if (ex.getMessage() != null) {
                Log.e(TAG, ex.getMessage());
            } else {
                Log.e(TAG, "No exception message for this error.");
            }
        }
    }

    // On click of next month button.
    public void onClickNextMonth(View view) {
        try {

            month = addZeroOrNot(String.valueOf(Integer.parseInt(month) + 1));
            if (month.equalsIgnoreCase("13")) {
                month = "01"; // change it to month January.
                year = String.valueOf(Integer.parseInt(year) + 1);
            }
            Log.i(TAG, "next month, year: " + month + ", " + year);
            showMonthName();
            getEventsForMonth();

        } catch (Exception ex) {
            if (ex.getMessage() != null) {
                Log.e(TAG, ex.getMessage());
            } else {
                Log.e(TAG, "No exception message for this error.");
            }
        }
    }

    /**
     * Save UI state changes to the savedInstanceState. This bundle will be
     * passed to onCreate if the process is killed and restarted. The Bundle is
     * essentially a way of storing a NVP ("Name-Value Pair") map, and it will
     * get passed in to onCreate and also onRestoreInstanceState
     */
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        // Pass values, to get on activity onCreate method
        savedInstanceState.putString("month", month);
        savedInstanceState.putString("year", year);

    }

}