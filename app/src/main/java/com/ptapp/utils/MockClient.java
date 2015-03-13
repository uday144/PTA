package com.ptapp.utils;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.util.Collections;

import retrofit.client.Client;
import retrofit.client.Request;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

/**
 * Created by lifestyle on 09-12-14.
 */
public class MockClient implements Client {

    private static final String TAG = "PTApp-MockClient: ";

    @Override
    public Response execute(Request request) throws IOException {
        Uri uri = Uri.parse(request.getUrl());

        Log.d(TAG, "fetching uri: " + uri.toString());

        String responseString = "";
        int status = 200;

        /**
         * ********* Verify User Phone **********
         */
        if (uri.getPath().equals("/rest/auth/reguser")) {
            if (uri.getQueryParameter("MobileNo").equals("1234567890")) { //it's teacher of a school registered with us
                //if the phone number is registered/found, return status 200

            } else if (uri.getQueryParameter("MobileNo").equals("1234567800")) { //it's parent/student number who is registered with individual teacher
                //I am assuming that server will keep the invited parents/students phone number in its Db corresponding to the teacher who invites
                status = 303;  //see other, means go to Parent/Student screen - unhappy flow
            } else {  //if phone number not found in our db, return status 401, telling that this user is not of any registered schools/institutions

                status = 401; // Not Registered flow - go to RoleCheck screen - unhappy flow, then to create class screen
            }
        }
        /**
         * ********* Send OTP Code **********
         */
        if (uri.getPath().equals("/rest/auth/validateotp")) {

            if (uri.getQueryParameter("MobileNo").equals("1234567890")) { //Phone number is Registered with a school/ found in the app server Db

                if (uri.getQueryParameter("OTP").equals("1234")) {  //check the otp corresponding, for which it was sent

                    //Both role: teacher and Parent
                    responseString = "{\"msgSuccess\":\"Congratulations...! You are now part of the digital network of ABC School both as a parent and a teacher.\"," +
                            "\"roles\":[{\"role\":\"P\"," + "\"kids\":" + "[{\"name\":\"Ram Kapoor\",\"stuClass\":\"3rd\",\"school\":\"ABC School\"}," +
                            "{\"name\":\"Reema Kapoor\",\"stuClass\":\"4th\",\"school\":\"ABC School\"}]}," +
                            "{\"role\":\"E\",\"name\":\"Shoba Devi\",\"school\":\"ABC School\"}]}";

                    //Parent with 2 kids
                    /*responseString = "{\"msgSuccess\":\"Congratulations...! You are now part of the digital network of ABC School as a parent.\"," +
                            "\"roles\":[{\"role\":\"P\"," + "\"kids\":" + "[{\"name\":\"Ali Kuli Mirza\",\"stuClass\":\"3rd\",\"school\":\"ABC School\"}," +
                            "{\"name\":\"Sania Mirza\",\"stuClass\":\"4th\",\"school\":\"ABC School\"}]}" +
                            "]}";*/

                } else { //OTP doesn't match

                    status = 401;
                }
            } else {  //new teacher, not registered with any school/ not found in our server Db.
                //Server needs to add this number into its Db, as a new Teacher registering with the app
                if (uri.getQueryParameter("otp").equals("1233")) {

                    responseString = "{\"msgSuccess\":\"Congratulations...! \"," +
                            "\"roles\":[]}";
                } else { //OTP doesn't match

                    status = 401;
                }
            }
        }

        /**Register user*/
        if (uri.getPath().equals("/rest/register/1")) {
            //Both role
            responseString = "{\"userIds\":[{\"role\":\"T\",\"userId\":\"11\"}," +
                    "{\"role\":\"P\",\"userId\":\"2\"}]}";

            //Parent- either mom or father
            /*responseString = "{\"userIds\":[{\"role\":\"P\",\"userId\":\"1\"}" +
                    "]}";*/
        }

        /**
         * ********* Send list of schools **********
         */
        if (uri.getPath().equals("/rest/schools/1")) {

            responseString = "{\"schools\":[\"Vivek High School\",\"Shishu Niketan School\"]," +
                    "\"timestamp\":\"DT-Stamp\"}";
        }
        /**
         * ********* Receive create class fields **********
         */
        if (uri.getPath().equals("/rest/createclass/1")) {

        }


        return new Response(request.getUrl(), status, "nothing", Collections.EMPTY_LIST, new TypedByteArray("application/json", responseString.getBytes()));
    }
}

/*else {
            status=404; //Api link not found. Please check if api link is correct and running on the server
            responseString = "{\"msgFailure\":\"Retrofit path doesn't matched.\"}";
        }*/
