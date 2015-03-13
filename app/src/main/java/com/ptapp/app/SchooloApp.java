package com.ptapp.app;


import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.Patterns;

import com.ptapp.bo.Role;
import com.ptapp.bo.StudentBO;
import com.ptapp.bo.StudentsBO;
import com.ptapp.utils.CommonConstants;
import com.ptapp.utils.CommonMethods;
import com.ptapp.utils.SharedPrefUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SchooloApp extends Application {

    private static final String TAG = "PTApp - SchooloApp";


    public static String[] email_arr;
    public static SharedPreferences sharedPref; // Persistent storage.


    private StudentsBO boStudents;

    @Override
    public void onCreate() {
        super.onCreate();

        /*
         * Calling 'PreferenceManager.setDefaultValues' during onCreate()
		 * ensures that your application is properly initialized with default
		 * settings, which your application might need to read in order to
		 * determine some behaviors (such as whether to download data while on a
		 * cellular network).
		 *
		 * 'R.xml.preferences' - The resource ID for the preference XML file for
		 * which you want to set the default values.
		 *
		 * 'false'- When false, the system sets the default values only if this
		 * method has never been called in the past (or the
		 * KEY_HAS_SET_DEFAULT_VALUES in the default value shared preferences
		 * file is false).
		 */
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        List<String> emailList = getEmailList();
        email_arr = emailList.toArray(new String[emailList.size()]);
    }


    /*Chat Section*/
    private List<String> getEmailList() {
        List<String> lst = new ArrayList<String>();
        Account[] accounts = AccountManager.get(this).getAccounts();
        for (Account account : accounts) {
            if (Patterns.EMAIL_ADDRESS.matcher(account.name).matches()) {
                lst.add(account.name);
            }
        }
        return lst;
    }


    public static String getCurrentChat() {
        return sharedPref.getString("current_chat", null);
    }

    public static void setCurrentChat(String chatId) {
        sharedPref.edit().putString("current_chat", chatId).commit();
    }

    public static boolean isNotify() {
        return sharedPref.getBoolean("notifications_new_message", true);
    }

    public static String getRingtone() {
        return sharedPref.getString("notifications_new_message_ringtone", android.provider.Settings.System.DEFAULT_NOTIFICATION_URI.toString());
    }
    /*Chat Section finishes*/

    public StudentsBO getStudentsBO() {
        try {
            // Fill the students list data age wise, eldest child first, meaning
            // fill StudentsBO instance.
            if (boStudents == null) {

                boStudents = new StudentsBO();
                setStudentsBO();
            }
        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
        }
        return boStudents;
    }

    private void setStudentsBO() {

        Map<Integer, StudentBO> students = new HashMap<Integer, StudentBO>();

        ArrayList<Role> roles = CommonMethods.getAppUserRoles(getApplicationContext());
        for (Role r : roles) {
            if (r.getRole().equals(CommonConstants.ROLE_PARENT)) {     //if parent, then find kids

                ArrayList<StudentBO> kids = r.getKids();
                for (StudentBO k : kids) {

                    if (k.getStudentId() == 0) {
                        k.setStudentId(3);
                    }
                    students.put(k.getStudentId(), k);
                }
                boStudents.setStudentsAll(students);
                Log.i(TAG, "number of kids: " + students.size());

                // setLastViewedKidStudentId, if it's first-time
                int lastViewedKidStudentId = SharedPrefUtil.getPrefLastViewedKidStudentId(getApplicationContext());
                if (lastViewedKidStudentId < 1) {

                    SharedPrefUtil.setPrefLastViewedKidStudentId(getApplicationContext(), kids.get(0).getStudentId());
                }
            }
        }
    }
}
