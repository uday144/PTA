/*
 * Copyright 2014 Google Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ptapp.sync;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.content.OperationApplicationException;
import android.net.Uri;
import android.os.RemoteException;
import android.preference.PreferenceManager;

import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import com.ptapp.io.AcademicSessionHandler;
import com.ptapp.io.AddressHandler;
import com.ptapp.io.BranchHandler;
import com.ptapp.io.ClassHandler;
import com.ptapp.io.ClassSubjectHandler;
import com.ptapp.io.DefaultGroupsHandler;
import com.ptapp.io.EventsHandler;
import com.ptapp.io.InstituteHandler;
import com.ptapp.io.JSONHandler;

import com.ptapp.io.ParentChildRelationHandler;
import com.ptapp.io.ParentHandler;
import com.ptapp.io.StaffEngagementHandler;
import com.ptapp.io.StaffHandler;
import com.ptapp.io.StudentAssociationHandler;
import com.ptapp.io.StudentHandler;
import com.ptapp.io.SubjectHandler;
import com.ptapp.io.UserHandler;
import com.ptapp.provider.PTAppContract;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;

import static com.ptapp.utils.LogUtils.LOGD;
import static com.ptapp.utils.LogUtils.LOGE;
import static com.ptapp.utils.LogUtils.LOGW;
import static com.ptapp.utils.LogUtils.makeLogTag;

/**
 * Helper class that parses first time data and imports them into the app's
 * Content Provider.
 */
public class PTAppDataHandler {
    //private static final String TAG = makeLogTag(SyncHelper.class);
    private static final String TAG = makeLogTag(PTAppDataHandler.class);

    // Shared preferences key under which we store the timestamp that corresponds to
    // the data we currently have in our content provider.
    private static final String SP_KEY_DATA_TIMESTAMP = "data_timestamp";

    // symbolic timestamp to use when we are missing timestamp data (which means our data is
    // really old or nonexistent)
    private static final String DEFAULT_TIMESTAMP = "Sat, 1 Jan 2000 00:00:00 GMT";

    //These names must match with the JSON keys("educators", "courses", etc.)
    private static final String DATA_KEY_ADDRESS = "Address";
    private static final String DATA_KEY_INSTITUTE = "Institute";
    private static final String DATA_KEY_BRANCH = "Branch";
    private static final String DATA_KEY_ACADEMIC_SESSION = "AcademicSession";
    private static final String DATA_KEY_SUBJECT = "Subject";
    private static final String DATA_KEY_CLASS = "Class";
    private static final String DATA_KEY_CLASS_SUBJECT = "ClassSubject";
    private static final String DATA_KEY_USER = "User";
    private static final String DATA_KEY_STAFF = "Staff";
    private static final String DATA_KEY_STAFF_ENGAGEMENT = "StaffEngagement";
    private static final String DATA_KEY_STUDENT = "Student";
    private static final String DATA_KEY_STUDENT_ASSOCIATION = "StudentAssociation";
    private static final String DATA_KEY_PARENT = "Parent";
    private static final String DATA_KEY_PARENT_CHILD_RELATION = "ParentChildRelation";
    private static final String DATA_KEY_DEFAULT_GROUPS = "DefaultGroups";
    private static final String DATA_KEY_EVENTS = "events";

    private static final String[] DATA_KEYS_IN_ORDER = {
            DATA_KEY_ADDRESS,
            DATA_KEY_INSTITUTE,
            DATA_KEY_BRANCH,
            DATA_KEY_ACADEMIC_SESSION,
            DATA_KEY_SUBJECT,
            DATA_KEY_CLASS,
            DATA_KEY_CLASS_SUBJECT,
            DATA_KEY_USER,
            DATA_KEY_STAFF,
            DATA_KEY_STAFF_ENGAGEMENT,
            DATA_KEY_STUDENT,
            DATA_KEY_STUDENT_ASSOCIATION,
            DATA_KEY_PARENT,
            DATA_KEY_PARENT_CHILD_RELATION,
            DATA_KEY_DEFAULT_GROUPS,
            DATA_KEY_EVENTS
    };

    Context mContext = null;

    // Handlers for each entity type:
    AddressHandler mAddressHandler = null;
    InstituteHandler mInstituteHandler = null;
    BranchHandler mBranchHandler = null;
    AcademicSessionHandler academicSessionHandler = null;
    SubjectHandler subjectHandler = null;
    ClassHandler classHandler = null;
    ClassSubjectHandler classSubjectHandler = null;
    UserHandler userHandler = null;
    StaffHandler staffHandler = null;
    StaffEngagementHandler staffEngagementHandler = null;
    StudentHandler studentHandler = null;
    StudentAssociationHandler studentAssociationHandler = null;
    ParentHandler parentHandler = null;
    ParentChildRelationHandler parentChildRelationHandler = null;
    DefaultGroupsHandler defaultGroupsHandler = null;
    EventsHandler mEventsHandler = null;

    // Convenience map that maps the key name to its corresponding handler (e.g.
    // "blocks" to mBlocksHandler (to avoid very tedious if-elses)
    HashMap<String, JSONHandler> mHandlerForKey = new HashMap<String, JSONHandler>();

    // Tally of total content provider operations we carried out (for statistical purposes)
    private int mContentProviderOperationsDone = 0;

    public PTAppDataHandler(Context ctx) {
        mContext = ctx;
    }

    /**
     * Parses the conference data in the given objects and imports the data into the
     * content provider. The format of the data is documented at https://code.google.com/p/iosched.
     *
     * @param dataBodies       The collection of JSON objects to parse and import.
     * @param dataTimestamp    The timestamp of the data. This should be in RFC1123 format.
     * @param downloadsAllowed Whether or not we are supposed to download data from the internet if needed.
     * @throws java.io.IOException If there is a problem parsing the data.
     */
    public void applyConferenceData(String[] dataBodies, String dataTimestamp,
                                    boolean downloadsAllowed) throws IOException {
        LOGD(TAG, "Applying data from " + dataBodies.length + " files, timestamp " + dataTimestamp);

        // create handlers for each data type
        mHandlerForKey.put(DATA_KEY_ADDRESS, mAddressHandler = new AddressHandler(mContext));
        mHandlerForKey.put(DATA_KEY_INSTITUTE, mInstituteHandler = new InstituteHandler(mContext));
        mHandlerForKey.put(DATA_KEY_BRANCH, mBranchHandler = new BranchHandler(mContext));
        mHandlerForKey.put(DATA_KEY_ACADEMIC_SESSION, academicSessionHandler = new AcademicSessionHandler(mContext));
        mHandlerForKey.put(DATA_KEY_SUBJECT, subjectHandler = new SubjectHandler(mContext));
        mHandlerForKey.put(DATA_KEY_CLASS, classHandler = new ClassHandler(mContext));
        mHandlerForKey.put(DATA_KEY_CLASS_SUBJECT, classSubjectHandler = new ClassSubjectHandler(mContext));
        mHandlerForKey.put(DATA_KEY_USER, userHandler = new UserHandler(mContext));
        mHandlerForKey.put(DATA_KEY_STAFF, staffHandler = new StaffHandler(mContext));
        mHandlerForKey.put(DATA_KEY_STAFF_ENGAGEMENT, staffEngagementHandler = new StaffEngagementHandler(mContext));
        mHandlerForKey.put(DATA_KEY_STUDENT, studentHandler = new StudentHandler(mContext));
        mHandlerForKey.put(DATA_KEY_STUDENT_ASSOCIATION, studentAssociationHandler = new StudentAssociationHandler(mContext));
        mHandlerForKey.put(DATA_KEY_PARENT, parentHandler = new ParentHandler(mContext));
        mHandlerForKey.put(DATA_KEY_PARENT_CHILD_RELATION, parentChildRelationHandler = new ParentChildRelationHandler(mContext));
        mHandlerForKey.put(DATA_KEY_DEFAULT_GROUPS, defaultGroupsHandler = new DefaultGroupsHandler(mContext));
        mHandlerForKey.put(DATA_KEY_EVENTS, mEventsHandler = new EventsHandler(mContext));

        // process the jsons. This will call each of the handlers when appropriate to deal
        // with the objects we see in the data.
        LOGD(TAG, "Processing " + dataBodies.length + " JSON objects.");
        for (int i = 0; i < dataBodies.length; i++) {
            LOGD(TAG, "Processing json object #" + (i + 1) + " of " + dataBodies.length);
            processDataBody(dataBodies[i]);
        }

        // the sessions handler needs to know the tag and speaker maps to process sessions
        /*mSessionsHandler.setTagMap(mTagsHandler.getTagMap());
        mSessionsHandler.setSpeakerMap(mSpeakersHandler.getSpeakerMap());*/


        // produce the necessary content provider operations
        ArrayList<ContentProviderOperation> batch = new ArrayList<ContentProviderOperation>();
        for (String key : DATA_KEYS_IN_ORDER) {
            LOGD(TAG, "Building content provider operations for: " + key);
            mHandlerForKey.get(key).makeContentProviderOperations(batch);
            LOGD(TAG, "Content provider operations so far: " + batch.size());
        }
        LOGD(TAG, "Total content provider operations: " + batch.size());

        // finally, push the changes into the Content Provider
        LOGD(TAG, "Applying " + batch.size() + " content provider operations.");
        try {
            int operations = batch.size();
            if (operations > 0) {
                mContext.getContentResolver().applyBatch(PTAppContract.CONTENT_AUTHORITY, batch);
            }
            LOGD(TAG, "Successfully applied " + operations + " content provider operations.");
            mContentProviderOperationsDone += operations;
        } catch (RemoteException ex) {
            LOGE(TAG, "RemoteException while applying content provider operations.");
            throw new RuntimeException("Error executing content provider batch operation", ex);
        } catch (OperationApplicationException ex) {
            LOGE(TAG, "OperationApplicationException while applying content provider operations.");
            throw new RuntimeException("Error executing content provider batch operation", ex);
        }

        // notify all top-level paths
        LOGD(TAG, "Notifying changes on all top-level paths on Content Resolver.");
        ContentResolver resolver = mContext.getContentResolver();
        for (String path : PTAppContract.TOP_LEVEL_PATHS) {
            Uri uri = PTAppContract.BASE_CONTENT_URI.buildUpon().appendPath(path).build();
            resolver.notifyChange(uri, null);
        }


        // update our data timestamp
        setDataTimestamp(dataTimestamp);
        LOGD(TAG, "Done applying schoolo data.");
    }

    public int getContentProviderOperationsDone() {
        return mContentProviderOperationsDone;
    }

    /**
     * Processes a conference data body and calls the appropriate data type handlers
     * to process each of the objects represented therein.
     *
     * @param dataBody The body of data to process
     * @throws java.io.IOException If there is an error parsing the data.
     */
    private void processDataBody(String dataBody) throws IOException {
        JsonReader reader = new JsonReader(new StringReader(dataBody));
        JsonParser parser = new JsonParser();
        try {
            reader.setLenient(true); // To err is human

            // the whole file is a single JSON object
            reader.beginObject();

            while (reader.hasNext()) {
                // the key is "educators", "courses", "students", "events" etc.
                String key = reader.nextName();
                if (mHandlerForKey.containsKey(key)) {
                    // pass the value to the corresponding handler
                    mHandlerForKey.get(key).process(parser.parse(reader));
                } else {
                    LOGW(TAG, "Skipping unknown key in ptapp data json: " + key);
                    reader.skipValue();
                }
            }
            reader.endObject();
        } finally {
            reader.close();
        }
    }

    // Returns the timestamp of the data we have in the content provider.
    public String getDataTimestamp() {
        return PreferenceManager.getDefaultSharedPreferences(mContext).getString(
                SP_KEY_DATA_TIMESTAMP, DEFAULT_TIMESTAMP);
    }

    // Sets the timestamp of the data we have in the content provider.
    public void setDataTimestamp(String timestamp) {
        LOGD(TAG, "Setting data timestamp to: " + timestamp);
        PreferenceManager.getDefaultSharedPreferences(mContext).edit().putString(
                SP_KEY_DATA_TIMESTAMP, timestamp).commit();
    }

    // Reset the timestamp of the data we have in the content provider
    public static void resetDataTimestamp(final Context context) {
        LOGD(TAG, "Resetting data timestamp to default (to invalidate our synced data)");
        PreferenceManager.getDefaultSharedPreferences(context).edit().remove(
                SP_KEY_DATA_TIMESTAMP).commit();
    }

    /**
     * A type of ConsoleRequestLogger that does not log requests and responses.
     */
    //TODO: What's use of this here
    /*private RequestLogger mQuietLogger = new ConsoleRequestLogger() {
        @Override
        public void logRequest(HttpURLConnection uc, Object content) throws IOException {
        }

        @Override
        public void logResponse(HttpResponse res) {
        }
    };*/

}
