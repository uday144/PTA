package com.ptapp.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.util.Log;

import com.ptapp.bo.StudentsBO;
import com.ptapp.app.R;
import com.ptapp.utils.SharedPrefUtil;
//import com.ptapp.service.UpdateRemindersIntentService;

//import net.margaritov.preference.colorpicker.ColorPickerPreference;


public class SettingsFragment extends PreferenceFragment implements
        OnSharedPreferenceChangeListener {

    private static String TAG = "PTApp-SettingFragment";

    private StudentsBO students;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);

        Preference chatPref = findPreference("chat_id");
        chatPref.setSummary(SharedPrefUtil.getPrefAppUserId(getActivity()));

        // fetch the category where you wish to insert, in this case a
        // PreferenceCategory with key "catMain"
        PreferenceCategory catMain = (PreferenceCategory) findPreference("catMain");

		/*students = ((SchooloApp) getActivity().getApplicationContext())
                .getBoStudents();
		if (students != null) {

			Map<String, StudentBO> map = students.getStudentsAll();
			if (map != null) {

				for (Map.Entry<String, StudentBO> entry : map.entrySet()) {

					ColorPickerPreference cpp = new ColorPickerPreference(
							getActivity());
					cpp.setKey(entry.getKey()); // key is StudentId (PK)
					cpp.setPersistent(true);
					cpp.setTitle("Set color for " + entry.getValue().getName());

					catMain.addPreference(cpp);
				}
			} else {
				Log.wtf(TAG, "map is null.");
			}
		} else {
			Log.wtf(TAG, "students is null.");
		}*/
    }

    @Override
    public void onResume() {
        super.onResume();

        // Registers a callback method to be called whenever a user changes any
        // preference.

        getPreferenceManager().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();

        // Unregisters the listener set in onResume().
        // It's best practice to unregister listeners when your app isn't using
        // them to cut down on
        // unnecessary system overhead. You do this in onPause().
        getPreferenceManager().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    // Fires when the user changes a preference.
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                                          String key) {
        if (key.equals("pref_txt_numOfRows")) {
            Log.i(TAG,
                    "home events rows: " + sharedPreferences.getString(key, ""));
        }
        if (key.equals("color1")) {
            Log.i(TAG, "color : " + sharedPreferences.getInt(key, 1));
        }
        if (key.equals("pref_color_pick")) {
            Log.v("Schoolo", "prefernce changed. Color code is: "
                    + sharedPreferences.getString(key, ""));
        }
        if (key.equals("pref_txt_EmailAccount")) {
            Log.v("Schoolo", "prefernce changed");

            // Intent intent = new Intent();
            // intent.setClass(getActivity(), DownloadIntentService.class);
            // getActivity().startService(intent);
        }
        if (key.equals("pref_txt_reminderTime")) {
            Log.d(TAG, "into onSharedPreferenceChanged. key:" + key);

            try {
                /**
                 * Cannot refer to a non-final variable 'mins' inside an inner
                 * class defined in a different method
                 *
                 * - in other words, cannot refer to non-final variable inside
                 * setPositiveButton's OnClick method.
                 */
                final String mins = sharedPreferences.getString(key, "");

                if (!mins.isEmpty()) {
                    // ask user for confirmation to update reminders.
                    AlertDialog.Builder builder = new AlertDialog.Builder(
                            this.getActivity());
                    builder.setTitle("Confirm Reminders Change");
                    builder.setCancelable(true);
                    builder.setIcon(android.R.drawable.ic_menu_more);

                    builder.setMessage("Do you want to change the reminder time for existing events also?");
                    builder.setPositiveButton(android.R.string.yes,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    startUpdateRemindersIntentService(mins);
                                }
                            });
                    builder.setNegativeButton(android.R.string.no,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    Log.i(TAG,
                                            "User doesn't want to change the reminder time for existing events.");
                                }
                            });
                    builder.show();

                } else {
                    Log.d(TAG, "sharedPrefernces returned empty mins.");
                }
            } catch (Exception ex) {
                Log.e(TAG, ex.getMessage());
            }
        }
    }

    /**
     * Update all reminders(assuming reminders are set only for kids during
     * insertion) in a background service thread(IntentService).
     *
     * @param mins
     */
    private void startUpdateRemindersIntentService(String mins) {

        try {
            Intent intent = new Intent();
            intent.putExtra("mins", Integer.parseInt(mins));
            ////intent.setClass(this.getActivity(), UpdateRemindersIntentService.class);
            this.getActivity().startService(intent);
        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
        }
    }
}
