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

package com.ptapp.io;

import android.content.ContentProviderOperation;
import android.content.Context;
import android.net.Uri;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.ptapp.io.model.AcademicSession;
import com.ptapp.provider.PTAppContract;

import java.util.ArrayList;
import java.util.HashMap;

import static com.ptapp.utils.LogUtils.LOGD;
import static com.ptapp.utils.LogUtils.makeLogTag;

public class AcademicSessionHandler extends JSONHandler {
    private static final String TAG = makeLogTag(AcademicSessionHandler.class);

    private HashMap<String, AcademicSession> mAcademicSessions = new HashMap<String, AcademicSession>();

    public AcademicSessionHandler(Context context) {
        super(context);
    }

    @Override
    public void process(JsonElement element) {
        for (AcademicSession academicSession : new Gson().fromJson(element, AcademicSession[].class)) {
            mAcademicSessions.put(String.valueOf(academicSession.AcademicSessionId), academicSession);
        }
    }

    @Override
    public void makeContentProviderOperations(ArrayList<ContentProviderOperation> list) {
        Uri uri = PTAppContract.addCallerIsSyncAdapterParameter(
                PTAppContract.AcademicSession.CONTENT_URI);

        LOGD(TAG, "Doing full (non-incremental) update for AcademicSession.");
        list.add(ContentProviderOperation.newDelete(uri).build());

        for (AcademicSession academicSession : mAcademicSessions.values()) {

            boolean isNew = true;
            buildSession(isNew, academicSession, list);
        }

        LOGD(TAG, "AcademicSessions: FULL. New total: " + mAcademicSessions.size());
    }

    private void buildSession(boolean isInsert,
                              AcademicSession academicSession, ArrayList<ContentProviderOperation> list) {
        ContentProviderOperation.Builder builder;
        Uri allAcademicSessionsUri = PTAppContract
                .addCallerIsSyncAdapterParameter(PTAppContract.AcademicSession.CONTENT_URI);
        Uri thisAcademicSessionUri = PTAppContract
                .addCallerIsSyncAdapterParameter(PTAppContract.AcademicSession.buildAcademicSessionUri(
                        String.valueOf(academicSession.AcademicSessionId)));

        if (isInsert) {
            builder = ContentProviderOperation.newInsert(allAcademicSessionsUri);
        } else {
            builder = ContentProviderOperation.newUpdate(thisAcademicSessionUri);
        }

        builder.withValue(PTAppContract.AcademicSession._ID, academicSession.AcademicSessionId)
                .withValue(PTAppContract.AcademicSession.START_DATE, academicSession.StartDate)
                .withValue(PTAppContract.AcademicSession.END_DATE, academicSession.EndDate)
                .withValue(PTAppContract.AcademicSession.SESSION_YEAR, academicSession.SessionYear)
                .withValue(PTAppContract.AcademicSession.SESSION_DESCRIPTION, academicSession.SessionDescription)
                .withValue(PTAppContract.AcademicSession.ACADEMIC_SESSION_TYPE_CODE, academicSession.AcademicSessionTypeCode)
                .withValue(PTAppContract.AcademicSession.ACADEMIC_SESSION_TYPE_DESCRIPTION, academicSession.AcademicSessionTypeDescription);

        list.add(builder.build());
    }
}
