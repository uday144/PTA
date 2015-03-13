package com.ptapp.service;

//import com.wdonahue.twitchtvclient.model.JustinTvStreamData;

import com.google.gson.JsonElement;
import com.ptapp.bean.api.jsonString;
import com.ptapp.bean.api.GcmUserBean;
import com.ptapp.bean.api.RolesBean;
import com.ptapp.bean.api.SchoolsBean;
import com.ptapp.bean.api.TestBean;
import com.ptapp.bo.SeverCalendarBO;
import com.ptapp.bo.StudentsBO;
import com.ptapp.utils.MockClient;
import com.ptapp.utils.RetrofitErrorHandler;

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RestAdapter.LogLevel;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;
import retrofit.mime.TypedInput;

public class ApiClient {
    private static TestApi testApi;
    private static StudentInfo studentInfo;
    private static FTEventFeed ftEventFeed;
    private static EventFeed eventFeed;
    private static RegIdToServer regIdToServer;
    private static SendChatMsg sendChatMsg;
    private static VerifyUserPhone verifyUserPhone;
    private static SendOTP sendOTP;
    private static FetchSchools schools;
    private static SendCreateClass sendCreateClass;
    private static Registration registration;
    private static FirstTimeLoad firstTimeLoad;


    //Headers that need to be added to many request
    //often for sending JSON as request body
    private static RequestInterceptor getRequestInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade requestFacade) {
                requestFacade.addHeader("Content-Type", "application/json");
            }
        };
    }

    public static StudentInfo getStudentInfo(String endPointURL) {
        if (studentInfo == null) {
            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint(endPointURL)
                    .setErrorHandler(new RetrofitErrorHandler())
                    .setLogLevel(LogLevel.FULL).build();

            studentInfo = restAdapter.create(StudentInfo.class);
        }

        return studentInfo;
    }

    // public interface TwitchTvApiInterface {
    // @GET("/stream/list.json")
    // void getStreams(@Query("limit") int limit, @Query("offset") int offset,
    // Callback<List<JustinTvStreamData>> callback);
    // }

    public interface StudentInfo {
        @GET("/students/{user}")
        StudentsBO getStu(@Path("user") String user);
    }

    /**
     * ********* User and Device Registration **********
     */
    public static RegIdToServer getRegIdToServer(String endPointURL) {
        if (regIdToServer == null) {
            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint(endPointURL)
                    .setErrorHandler(new RetrofitErrorHandler())
                    .setLogLevel(LogLevel.FULL).build();

            regIdToServer = restAdapter.create(RegIdToServer.class);
        }

        return regIdToServer;
    }

    public interface RegIdToServer {
        @GET("/gcm/{user}")
        GcmUserBean getRegis(@Path("user") String user,
                             @Query("role") String role,
                             @Query("deviceRegId") String deviceRegId,
                             @Query("devicePlatform") String devicePlatform,
                             @Query("deviceModel") String deviceModel,
                             @Query("osVersion") String osVersion);
    }


    /**
     * ********* Send Chat Msg **********
     */
    public static SendChatMsg getSendChatMsg(String endPointURL) {
        if (sendChatMsg == null) {
            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint(endPointURL)
                    .setErrorHandler(new RetrofitErrorHandler())
                    .setLogLevel(LogLevel.FULL).build();

            sendChatMsg = restAdapter.create(SendChatMsg.class);
        }

        return sendChatMsg;
    }

    public interface SendChatMsg {
        @GET("/messages/{msgid}")
        String getSendMsg(@Path("msgid") String msgid,
                          @Query("to") String to,
                          @Query("msgTxt") String msgTxt,
                          @Query("from") String from,
                          @Query("is_group") String is_group,
                          @Query("status") String status);
    }


    /**
     * ********* First Time Load (User(kid's) data) **********
     */
    public static FTEventFeed getFTEventFeed(String endPointURL) {

        if (ftEventFeed == null) {
            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint(endPointURL)
                    .setErrorHandler(new RetrofitErrorHandler())
                    .setLogLevel(LogLevel.FULL).build();

            ftEventFeed = restAdapter.create(FTEventFeed.class);

        }

        return ftEventFeed;
    }

    public interface FTEventFeed {
        @GET("/{event}")
        SeverCalendarBO getEvent(@Path("event") String event);
    }

    /**
     * ********* Events load - testing **********
     */
    public static EventFeed getEventFeed(String endPointURL) {

        if (eventFeed == null) {
            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint(endPointURL)
                    .setErrorHandler(new RetrofitErrorHandler())
                    .setLogLevel(LogLevel.FULL).build();

            eventFeed = restAdapter.create(EventFeed.class);

        }

        return eventFeed;
    }

    public interface EventFeed {
        @GET("/{event}")
        jsonString getEvent(@Path("event") String event);
    }

    /**
     * ********* Verify User Phone **********
     */
    public static VerifyUserPhone getVerifyUserPhone(String endPointURL) {
        if (verifyUserPhone == null) {
            //Don't set error handler when using a callback method(for void return), otherwise onFailure inside callback method won't run
            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint(endPointURL)
                            //add this when sending to actual api
                            //.setRequestInterceptor(getRequestInterceptor())  //to add header with contentType:json
                            //.setErrorHandler(new RetrofitErrorHandler())
                    .setLogLevel(LogLevel.FULL)
                            //.setClient(new MockClient())    //remove this when actual api is ready
                    .build();

            verifyUserPhone = restAdapter.create(VerifyUserPhone.class);
        }

        return verifyUserPhone;
    }

    public interface VerifyUserPhone {
        @POST("/auth/user")
        /*void verifyUser(@Query("MobileNo") String MobileNo,
                        Callback<Response> callback*/
        void verifyUser(@Body TypedInput in,
                        Callback<Response> callback

        );
    }


    //Testapi

    /**
     * ********* Verify User Phone **********
     */
    public static TestApi getTestApi(String endPointURL) {
        if (testApi == null) {
            //Don't set error handler when using a callback method(for void return), otherwise onFailure inside callback method won't run
            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint(endPointURL)
                            //add this when sending to actual api
                            //.setRequestInterceptor(getRequestInterceptor())  //to add header with contentType:json
                            //.setErrorHandler(new RetrofitErrorHandler())
                    .setLogLevel(LogLevel.FULL)
                            //.setClient(new MockClient())    //remove this when actual api is ready
                    .build();

            testApi = restAdapter.create(TestApi.class);
        }

        return testApi;
    }

    public interface TestApi {
        @GET("/")
        /*void verifyUser(@Query("MobileNo") String MobileNo,
                        Callback<Response> callback*/
        TestBean testUser();
    }


    /**
     * ********* Send OTP Code **********
     */
    public static SendOTP getSendOTP(String endPointURL) {
        if (sendOTP == null) {
            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint(endPointURL)
                    .setErrorHandler(new RetrofitErrorHandler())
                            //.setClient(new MockClient())    //remove this when actual api is ready
                    .setLogLevel(LogLevel.FULL)
                    .build();

            sendOTP = restAdapter.create(SendOTP.class);
        }

        return sendOTP;
    }

    public interface SendOTP {
        @POST("/auth/validateotp")
        /*RolesBean otp(
                @Query("CountryISOCode") String ISOCountryCOde,
                @Query("MobileNo") String phone,
                @Query("OTP") String otp
        );*/

        void otp(@Body TypedInput in,Callback<RolesBean> callback);
    }

    /**
     * ********* Registration **********
     */
    public static Registration registerUser(String endPointURL) {
        if (registration == null) {
            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint(endPointURL)
                            //.setErrorHandler(new RetrofitErrorHandler())
                            //.setClient(new MockClient())    //remove this when actual api is ready
                    .setLogLevel(LogLevel.FULL)
                    .build();

            registration = restAdapter.create(Registration.class);
        }

        return registration;
    }

    public interface Registration {

        @POST("/register/device/{user_id}")
        /*UserIdsBean register(@Path("userid") String userid,
                             @Field("phone") String phone,
                             @Field("platform") String platform,
                             @Field("regId") String regId,
                             @Field("deviceType") String deviceType,
                             @Field("ver") String ver
        );*/

        void register(@Path("user_id") String user_id,
                      @Body TypedInput in,
                      Callback<Response> callback
        );
    }

    /**
     * ********* First-time Load **********
     */
    public static FirstTimeLoad getFirstTimeLoad(String endPointURL) {
        if (firstTimeLoad == null) {
            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint(endPointURL)
                    .setErrorHandler(new RetrofitErrorHandler())
                            //.setClient(new MockClient())    //remove this when actual api is ready
                    .setLogLevel(LogLevel.FULL)
                    .build();

            firstTimeLoad = restAdapter.create(FirstTimeLoad.class);
        }

        return firstTimeLoad;
    }

    public interface FirstTimeLoad {
        @GET("/loaddata/{id}")
        void firstLoad(@Path("id") String id,
                       Callback<JsonElement> callback);
    }


    /**
     * ********* Fetch Schools name **********
     */
    public static FetchSchools fetchSchools(String endPointURL) {
        if (schools == null) {
            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint(endPointURL)
                            //.setErrorHandler(new RetrofitErrorHandler())
                    .setClient(new MockClient())    //remove this when actual api is ready
                    .setLogLevel(LogLevel.FULL)
                    .build();

            schools = restAdapter.create(FetchSchools.class);
        }

        return schools;
    }

    public interface FetchSchools {
        @GET("/schools/{msgid}")
        SchoolsBean getSchools(@Path("msgid") String msgid,
                               @Query("phone") String dtTimestamp
        );
    }

    /**
     * ********* Send create class fields **********
     */
    public static SendCreateClass sendCreateClass(String endPointURL) {
        if (sendCreateClass == null) {
            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint(endPointURL)
                            //.setErrorHandler(new RetrofitErrorHandler())
                    .setClient(new MockClient())    //remove this when actual api is ready
                    .setLogLevel(LogLevel.FULL)
                    .build();

            sendCreateClass = restAdapter.create(SendCreateClass.class);
        }

        return sendCreateClass;
    }

    public interface SendCreateClass {
        @FormUrlEncoded
        @POST("/createclass/{msgid}")
        void createClass(@Path("msgid") String msgid,
                         @Field("institute") String institute,
                         @Field("class") String className,
                         @Field("subject") String subject,
                         @Field("teacherTitle") String teacherTitle,
                         @Field("teacherFName") String teacherFName,
                         @Field("teacherLName") String teacherLName,
                         @Field("classTitle") String classTitle,
                         Callback<Response> callback

        );
    }
}



