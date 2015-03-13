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
import com.ptapp.io.model.Subject;
import com.ptapp.provider.PTAppContract;

import java.util.ArrayList;
import java.util.HashMap;

import static com.ptapp.utils.LogUtils.LOGD;
import static com.ptapp.utils.LogUtils.makeLogTag;

public class SubjectHandler extends JSONHandler {
    private static final String TAG = makeLogTag(SubjectHandler.class);

    private HashMap<String, Subject> mSubjects = new HashMap<String, Subject>();

    public SubjectHandler(Context context) {
        super(context);
    }

    @Override
    public void process(JsonElement element) {
        for (Subject subject : new Gson().fromJson(element, Subject[].class)) {
            mSubjects.put(String.valueOf(subject.SubjectId), subject);
        }
    }

    @Override
    public void makeContentProviderOperations(ArrayList<ContentProviderOperation> list) {
        Uri uri = PTAppContract.addCallerIsSyncAdapterParameter(
                PTAppContract.Subject.CONTENT_URI);

        LOGD(TAG, "Doing full (non-incremental) update for subject.");
        list.add(ContentProviderOperation.newDelete(uri).build());

        for (Subject subject : mSubjects.values()) {

            boolean isNew = true;
            buildSession(isNew, subject, list);
        }

        LOGD(TAG, "Subjects: FULL. New total: " + mSubjects.size());
    }

    private void buildSession(boolean isInsert,
                              Subject subject, ArrayList<ContentProviderOperation> list) {
        ContentProviderOperation.Builder builder;
        Uri allSubjectsUri = PTAppContract
                .addCallerIsSyncAdapterParameter(PTAppContract.Subject.CONTENT_URI);
        Uri thisSubjectUri = PTAppContract
                .addCallerIsSyncAdapterParameter(PTAppContract.Subject.buildSubjectUri(
                        String.valueOf(subject.SubjectId)));

        if (isInsert) {
            builder = ContentProviderOperation.newInsert(allSubjectsUri);
        } else {
            builder = ContentProviderOperation.newUpdate(thisSubjectUri);
        }

        builder.withValue(PTAppContract.Subject._ID, subject.SubjectId)
                .withValue(PTAppContract.Subject.DESCRIPTION, subject.Description)
                .withValue(PTAppContract.Subject.SUBJECT_CODE, subject.SubjectCode);

        list.add(builder.build());
    }
}
