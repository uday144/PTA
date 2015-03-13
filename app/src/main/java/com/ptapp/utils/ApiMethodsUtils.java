package com.ptapp.utils;

import android.util.Log;

import com.google.gson.JsonElement;
import com.ptapp.bean.api.GcmUserBean;
import com.ptapp.bean.api.RolesBean;
import com.ptapp.bean.api.SchoolsBean;
import com.ptapp.bean.api.TestBean;
import com.ptapp.bo.StudentsBO;
import com.ptapp.service.ApiClient;

import java.io.UnsupportedEncodingException;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;
import retrofit.mime.TypedInput;

import static com.ptapp.utils.LogUtils.LOGD;
import static com.ptapp.utils.LogUtils.LOGE;
import static com.ptapp.utils.LogUtils.LOGI;

/**
 * Methods to call Retrofit ApiClient{@link com.ptapp.service.ApiClient}
 */
public class ApiMethodsUtils {

    private static final String TAG = "PTApp - ApiMethodsUtils";

    /**
     * @param apiLink
     * @return
     */
    public static StudentsBO fTLoadFromApiLink(String apiLink) {
        String ftlId = apiLink.substring(apiLink.lastIndexOf("/") + 1);
        String endPt = apiLink.substring(0, apiLink.lastIndexOf("/"));
        Log.d(TAG, "id: " + ftlId);
        Log.d(TAG, "endpt: " + endPt);

        ApiClient.StudentInfo ftLoadFeed = ApiClient.getStudentInfo(endPt);
        StudentsBO stus = ftLoadFeed.getStu(ftlId);

        return stus;
    }

    /**
     * @param apiLink
     * @return
     */
    public static String gcmRegIdToAppServer(String apiLink, String role, String regId, String devicePlatform, String deviceModel, String osVersion) {
        String ftlId = apiLink.substring(apiLink.lastIndexOf("/") + 1);
        String endPt = apiLink.substring(0, apiLink.lastIndexOf("/"));
        Log.d(TAG, "id: " + ftlId);
        Log.d(TAG, "endpt: " + endPt);

        ApiClient.RegIdToServer regIdToServer = ApiClient.getRegIdToServer(endPt);
        GcmUserBean resp = regIdToServer.getRegis(ftlId, role, regId, devicePlatform, deviceModel, osVersion);

        Log.w(TAG, "amazon server response for GCM: " + resp);
        return resp.getUserId();

    }

    /**
     * @param apiLink
     * @return
     */
    public static String sendMsg(String apiLink, String to, String msg, String from, String isGroup, String status) {
        String ftlId = apiLink.substring(apiLink.lastIndexOf("/") + 1);
        String endPt = apiLink.substring(0, apiLink.lastIndexOf("/"));
        Log.d(TAG, "id: " + ftlId);
        Log.d(TAG, "endpt: " + endPt);

        ApiClient.SendChatMsg sendChatMsg = ApiClient.getSendChatMsg(endPt);
        String resp = sendChatMsg.getSendMsg(ftlId, to, msg, from, isGroup, status);

        Log.w(TAG, "amazon server response for GCM: " + resp);
        return resp;

    }

    /**
     * @param apiLink
     * @return
     */
    public static void sendVerifyPhone(String apiLink, String isoCode, String phone, Callback<Response> callback) {

        ApiClient.VerifyUserPhone verifyUserPhone = ApiClient.getVerifyUserPhone(apiLink);
        //verifyUserPhone.verifyUser(phone, callback);

        //Actual api sending
        String sds = "{\"CountryISOCode\":\"" + isoCode + "\",\"MobileNo\":\"" + phone + "\"}";
        try {
            TypedInput in = new TypedByteArray("application/json", sds.getBytes("UTF-8"));
            verifyUserPhone.verifyUser(in, callback);
        } catch (UnsupportedEncodingException e) {
            LOGD(TAG, "error unSupportedEncodingException: " + e.getMessage());
        }
    }

    public static TestBean sendTestApi(String apiLink) {

        ApiClient.TestApi testApi = ApiClient.getTestApi(apiLink);
        TestBean tb = testApi.testUser();

        LOGD(TAG, "testbean:" + tb);
        return tb;
    }

    public static void sendOTPCode(String apiLink, String isoCountryCode, String phone,
                                        String otpCode, Callback<RolesBean> callback) {

        ApiClient.SendOTP sendOTP = ApiClient.getSendOTP(apiLink);
        /*RolesBean resp = sendOTP.otp(isoCountryCode, phone, otpCode);*/

        //Actual api sending
        String json = "{\"CountryISOCode\":\"" + isoCountryCode + "\",\"MobileNo\":\"" + phone + "\"," +
                "\"OTP\":\"" + otpCode + "\"}";
        try {
            TypedInput in = new TypedByteArray("application/json", json.getBytes("UTF-8"));
            sendOTP.otp(in, callback);
        } catch (UnsupportedEncodingException e) {
            LOGE(TAG, "error unSupportedEncodingException: " + e.getMessage());
        } catch (Exception ex) {
            if (ex != null) {
                LOGE(TAG, ex.getMessage());
            }
        }
    }

    /**
     * register user at the server and return userIds
     *
     * @param
     * @return
     */
    public static void registerUser(String apiLink, int userId, String platform, String regId,
                                    String deviceType, String ver, Callback<Response> callback) {
        //UserIdsBean resp = null;
        /*String ftlId = apiLink.substring(apiLink.lastIndexOf("/") + 1);
        String endPt = apiLink.substring(0, apiLink.lastIndexOf("/"));*/

        ApiClient.Registration registration = ApiClient.registerUser(apiLink);
        //UserIdsBean resp = registration.register(ftlId, phone, platform, regId, deviceType, ver);

        //Actual api sending
        String json = "{\"Platform\":\"" + platform + "\"," +
                "\"GCMRegId\":\"" + regId + "\"," +
                "\"DeviceType\":\"" + deviceType + "\"," +
                "\"Version\":\"" + ver + "\"}";
        try {
            TypedInput in = new TypedByteArray("application/json", json.getBytes("UTF-8"));
            registration.register(String.valueOf(userId), in, callback);
        } catch (UnsupportedEncodingException e) {
            LOGE(TAG, "error unSupportedEncodingException: " + e.getMessage());
        } catch (Exception ex) {
            if (ex != null) {
                LOGE(TAG, ex.getMessage());
            }
        }
    }

    /**
     * @param apiLink
     * @return
     */
    public static void apiFTLoad(String apiLink, int userId, Callback<JsonElement> callback) {

        //RolesBean resp = null;
        ApiClient.FirstTimeLoad getFTL = ApiClient.getFirstTimeLoad(apiLink);

        //Actual api sending - No input json
        getFTL.firstLoad(String.valueOf(userId),callback);
        /*String json = "{\"CountryISOCode\":\"" + isoCountryCode + "\",\"MobileNo\":\"" + phone + "\"," +
                "\"OTP\":\"" + otpCode + "\"}";
        try {
            TypedInput in = new TypedByteArray("application/json", json.getBytes("UTF-8"));
            resp = sendOTP.otp(in);
        } catch (UnsupportedEncodingException e) {
            LOGE(TAG, "error unSupportedEncodingException: " + e.getMessage());
        } catch (Exception ex) {
            if (ex != null) {
                LOGE(TAG, ex.getMessage());
            }
        }*/
    }


    /**
     * retrieves list of school names from the server through retrofit api
     *
     * @param
     * @return
     */
    public static SchoolsBean fetchSchools(String apiLink, String dtStamp) {
        String ftlId = apiLink.substring(apiLink.lastIndexOf("/") + 1);
        String endPt = apiLink.substring(0, apiLink.lastIndexOf("/"));
        Log.d(TAG, "id: " + ftlId);
        Log.d(TAG, "endpt: " + endPt);

        ApiClient.FetchSchools fetchSchools = ApiClient.fetchSchools(endPt);
        SchoolsBean resp = fetchSchools.getSchools(ftlId, dtStamp);

        Log.w(TAG, "server response for fetching schools: " + resp);
        return resp;
    }

    /**
     * retrieves list of school names from the server through retrofit api
     *
     * @param
     * @return
     */
    public static void sendCreateClass(String apiLink, String institute, String className, String subject,
                                       String teacherTitle, String teacherFName, String teacherLName,
                                       String classTitle, Callback<Response> callback) {
        String ftlId = apiLink.substring(apiLink.lastIndexOf("/") + 1);
        String endPt = apiLink.substring(0, apiLink.lastIndexOf("/"));
        Log.d(TAG, "id: " + ftlId);
        Log.d(TAG, "endpt: " + endPt);

        ApiClient.SendCreateClass sendCreateClass = ApiClient.sendCreateClass(endPt);
        sendCreateClass.createClass(ftlId, institute, className, subject, teacherTitle, teacherFName, teacherLName, classTitle, callback);
    }


}
