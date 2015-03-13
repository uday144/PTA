package com.ptapp.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.ptapp.activity.EducatorHomeActivity;
import com.ptapp.activity.ParentHomeActivity;
import com.ptapp.bean.api.UserIdsBean;
import com.ptapp.bo.Role;
import com.ptapp.app.R;

import java.util.ArrayList;
import java.util.Random;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by lifestyle on 06-09-14.
 */
public class GcmUtil {
    private Context context;
    private static final String TAG = "GcmUtil";

    // GCM
    /*GoogleCloudMessaging gcm;
    String regid;*/

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String SERVICE_NOT_AVAILABLE = "SERVICE_NOT_AVAILABLE";
    private final int MAX_ATTEMPTS = 3;
    private final int BACKOFF_MILLI_SECONDS = 2000;
    private final Random random = new Random();

    //using for backend (java development server)
    //private static Registration regService = null;

    public GcmUtil(Context context) {
        this.context = context;
    }

    public boolean registerGCM(Activity activity) {
        // Check device for Play Services APK. If check succeeds, proceed with
        // GCM registration.

        //TODO:Either use or remove this...
        /*if (checkPlayServices(activity)) {
            gcm = GoogleCloudMessaging.getInstance(context);
            regid = getRegistrationId(context);
            Log.v(TAG, "regId: " + regid);

            if (regid.isEmpty()) {
                registerInBackground();

            } else {
                Log.i(TAG, regid);
                return true;
            }
        } else {
            //scenario can be - new device, no google account yet created or added. if google account is present in the device, then google play services not found dialog is displayed with link to get google play services.
            // one way is to start the google play (Intent), to prompt user to add a google Account.
            Log.i(TAG, "No valid Google Play Services APK found.");
            Toast.makeText(context, "No valid Google Play Services APK found." +
                            "Please install one or upgrade the previously installed, " +
                            "for proper functioning of this app.",
                    Toast.LENGTH_SHORT);
        }
        return false;*/

        registerInBackground(activity);
        return true;
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If it
     * doesn't, display a dialog that allows users to download the APK from the
     * Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices(Activity activity) {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(context);
        Log.v(TAG, "is service available: " + resultCode);
        Log.v(TAG, "is service available suc: " + ConnectionResult.SUCCESS);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, activity,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                activity.finish();
            }
            return false;
        }
        return true;
    }

    /**
     * Gets the current registration ID for application on GCM service, if there
     * is one.
     * <p/>
     * If result is empty, the app needs to register.
     *
     * @return registration ID, or empty string if there is no existing
     * registration ID.
     * <p/>
     * Check if app was updated; if so, it must clear the registration ID
     * since the existing regID is not guaranteed to work with the new
     * app version.
     */
    private String getRegistrationId(Context context) {

        String registrationId = SharedPrefUtil.getPrefGcmRegId(context);
        if (registrationId.isEmpty()) {
            Log.i(TAG, "Registration Id not found.");
            return "";
        }

        // Check if app was updated; if so, it must clear the registration ID
        // since the existing regID is not guaranteed to work with the new
        // app version.
        int registeredVersion = SharedPrefUtil.getPrefAppVersion(context);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i(TAG, "App version changed. Need to register with GCM again.");
            return "";
        }
        return registrationId;
    }

    /**
     * @return Application's {@code SharedPreferences}.
     */
    private SharedPreferences getGcmPreferences(Context context) {
        // This sample app persists the registration ID in shared preferences,
        // but
        // how you store the regID in your app is up to you.
        return context.getSharedPreferences(context.getPackageName() + ".GCM",
                Context.MODE_PRIVATE);
    }

    /**
     * @return Application's version code from the {@code PackageManager}.
     */
    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    /**
     * Registers the application with GCM servers asynchronously.
     * <p/>
     * Stores the registration ID and the app versionCode in the application's
     * shared preferences.
     * <p/>
     * Sends the regId to the app server and receives the UserId(s) from the server, then redirect
     * the user to the homepage
     */
    private void registerInBackground(final Activity activity) {
        new AsyncTask<Void, Void, UserIdsBean>() {

            @Override
            protected UserIdsBean doInBackground(Void... params) {
                Log.i(TAG, "Registering with the GCM....");

                UserIdsBean ff = null;
                long backoff = BACKOFF_MILLI_SECONDS + random.nextInt(1000);
                for (int i = 1; i <= MAX_ATTEMPTS; i++) {
                    if (isCancelled()) {
                        break;
                    }

                    Log.d(TAG, "Attempt #" + i + " to register");
                    try {
                        /*if (gcm == null) {
                            gcm = GoogleCloudMessaging
                                    .getInstance(context);

                        }
                        regid = gcm.register(SENDER_ID);*/

                        // You should send the registration ID to your server
                        // over HTTP, so it can use GCM/HTTP or CCS to send
                        // messages to your app.
                        /*try {*/
                        //TODO: fill real values for "Android"
                        Log.i(TAG, "Registering with the app server...");
                        ApiMethodsUtils.registerUser(context.getResources().getString(
                                        R.string.first_part_api_link),
                                CommonMethods.getAppUserId(context),
                                "Android",
                                "regid",
                                "Android",
                                "4.4",
                                new Callback<Response>() {
                                    @Override
                                    public void success(Response response, Response response2) {
                                        postRegistrationSuccess(activity);
                                    }

                                    @Override
                                    public void failure(RetrofitError retrofitError) {

                                        Response r = retrofitError.getResponse();
                                        if (r != null) {
                                            Log.e(TAG, "Error code: " + r.getStatus());
                                            Log.e(TAG, "Error: " + r);

                                            if (r.getStatus() == 400) {
                                                Log.e(TAG, "Error code: 400");
                                            } else if (r.getStatus() == 401) { //Not Registered flow
                                                Log.e(TAG, "Error code: 401");
                                            } else if (r.getStatus() == 500) {
                                                Log.e(TAG, "Error code: 500");
                                            } else if (r.getStatus() == 303) {
                                                Log.e(TAG, "Error code: 303");
                                            } else if (r.getStatus() == 404) { //Api link not found.
                                                Log.e(TAG, "Error code: 404");
                                            }
                                        } else {
                                            Log.wtf(TAG, "Unknown retrofit error: " + retrofitError.getMessage());
                                        }
                                    }
                                }
                        );
                        /*} catch (Exception ex) {
                            Log.w(TAG, "Unknown error - register in background: " + ex.getMessage());
                        }*/
                        break;
                    } catch (Exception ex) {
                        //msg = "Error on register :" + ex.getMessage();
                        if (ex.getMessage().equals(SERVICE_NOT_AVAILABLE)) {
                            Log.e(TAG, "Failed to register on attempt " + i
                                    + " out of " + MAX_ATTEMPTS + " :" + ex);

                            if (i == MAX_ATTEMPTS) {
                                break;
                                // TODO: what to do, incase all attempts fails
                                // register to GCM fails?
                            }
                            try {
                                Log.d(TAG, "Sleeping for " + backoff
                                        + " ms before retry");
                                Thread.sleep(backoff);

                            } catch (InterruptedException e1) {
                                // Activity finished before we complete - exit.
                                Log.d(TAG,
                                        "Thread interrupted: abort remaining retries!");
                                Thread.currentThread().interrupt();
                            }

                            // increase backoff exponentially
                            backoff *= 2;
                        }
                    }
                }
                return ff;
            }

            @Override
            protected void onPostExecute(UserIdsBean result) {

                if (result != null) {

                }

                //String resp = sendRegistrationIdToBackend(context, regid);

                //Log.i(TAG, "Response from app server(Registered UserId): " + resp);
                //Only for testing purpose, backend(java development server) locally
                //regService.register(regid).execute();

            }
        }.execute(null, null, null);
    }

    private void postRegistrationSuccess(Activity activity) {
        Log.i(TAG, "App/User has been registered with the server.");

        //TODO:either use or remove
        // Storing the regID in sharedPref - no need to register again.
        //storeRegistrationId(context, regid);
        Boolean isTeacher = false, isParent = false;

        ArrayList<Role> roles = CommonMethods.getAppUserRoles(context);
        for (Role r : roles) {
            if (r.getRole().equals(CommonConstants.ROLE_STAFF)) {

                isTeacher = true;
            } else if (r.getRole().equals(CommonConstants.ROLE_PARENT)) {

                isParent = true;
            }
        }

        //set first time launch to false.
        SharedPrefUtil.setPrefFirstTimeLaunch(context, false);
        Log.i(TAG, "First time launch flag has been set to false.");

        Intent intent = null;
        //Redirect the user to Homepage
        if (isTeacher) {    //if it's teacher

            SharedPrefUtil.setPrefUserInRole(context, CommonConstants.ROLE_STAFF);
            Log.i(TAG, " Present role has been set to staff, now redirecting to the teacher home...");
            intent = new Intent(context, EducatorHomeActivity.class);

        } else if (isParent) {  //if it's parent

            SharedPrefUtil.setPrefUserInRole(context, CommonConstants.ROLE_PARENT);
            Log.i(TAG, "Present role has been set to parent, now redirecting to the parent home...");
            intent = new Intent(context, ParentHomeActivity.class);
        }
        if (intent != null) {
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            activity.finish();
        }
    }


    /**
     * Sends the registration ID to your server over HTTP, so it can use
     * GCM/HTTP or CCS to send messages to your app. Not needed for this demo
     * since the device sends upstream messages to a server that echoes back the
     * message using the 'from' address in the message.
     */
    private String sendRegistrationIdToBackend(Context context, String regId) {
        // Your implementation here.

        //return ServerUtilities.register(context, regId);
        //return ServerUtilities.register(context, regId, SchooloApp.getChatId());
        //using get Retrofit Amazon API service
        String res = ApiMethodsUtils.gcmRegIdToAppServer(
                context.getResources().getString(
                        R.string.first_part_api_link) + "/" + SharedPrefUtil.getPrefAppUserId(context).toString(),
                SharedPrefUtil.getPrefUserType(context),
                regId, "Andriod", "", "");
        return res;
    }

    /**
     * Stores the registration ID and the app versionCode in the application's
     * {@code SharedPreferences}.
     *
     * @param context application's context.
     * @param regId   registration ID
     */
    protected void storeRegistrationId(Context context, String regId) {
        Log.i(TAG, "Now, storing the regId and appVersion in the sharedPref...");

        int appVersion = getAppVersion(context);
        SharedPrefUtil.setPrefGcmRegId(context, regId);
        SharedPrefUtil.setPrefAppVersion(context, appVersion);
        Log.i(TAG, "RegId and appVersion has been stored, regId: " + regId + " and appVersion: " + appVersion);
    }

    /**
     * Un-registers the application with GCM servers asynchronously.
     * <p/>
     * Sets the registration ID to empty and the app versionCode in the
     * application's shared preferences.
     */
    public void unregisterInBackground() {

        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";

                /*try {
                    gcm.unregister();
                } catch (IOException ex) {
                    Log.wtf(TAG, "Error on gcm unregister: " + ex.getMessage());

                }*/

                try {
                    // unregister from app server
                    // ServerUtilities.unregister(getApplicationContext(),
                    // regid);

                    // At this point the device is unregistered from GCM, but
                    // still registered in the our server. We could try to
                    // unregister again, but it is not necessary: if the server
                    // tries to send a message to the device, it will get
                    // a "NotRegistered" error message and should unregister the
                    // device.

                    // empty the regid stored in the preferences.
                    storeRegistrationId(context, "");

                    msg = "Device unregistered from the GCM.";
                } catch (Exception ex) {
                    msg = "Error on app backend unregister :" + ex.getMessage();
                    // If there is an error, don't just keep trying to
                    // unregister.
                    // Require the user to click a button again, or perform
                    // exponential back-off.
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                Log.i(TAG, msg);

            }
        }.execute(null, null, null);
    }

    public void cleanup() {
        /*if (gcm != null) {
            gcm.close();
        }*/
    }
}
