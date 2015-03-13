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
import com.ptapp.io.model.StudentAssociation;
import com.ptapp.provider.PTAppContract;

import java.util.ArrayList;
import java.util.HashMap;

import static com.ptapp.utils.LogUtils.LOGD;
import static com.ptapp.utils.LogUtils.makeLogTag;

public class StudentAssociationHandler extends JSONHandler {
    private static final String TAG = makeLogTag(StudentAssociationHandler.class);

    private HashMap<String, StudentAssociation> mStudentAssociations = new HashMap<String, StudentAssociation>();

    public StudentAssociationHandler(Context context) {
        super(context);
    }

    @Override
    public void process(JsonElement element) {
        for (StudentAssociation studentAssociation : new Gson().fromJson(element, StudentAssociation[].class)) {
            mStudentAssociations.put(String.valueOf(studentAssociation.StudentAssociationId), studentAssociation);
        }
    }

    @Override
    public void makeContentProviderOperations(ArrayList<ContentProviderOperation> list) {
        Uri uri = PTAppContract.addCallerIsSyncAdapterParameter(
                PTAppContract.StudentAssociation.CONTENT_URI);

        LOGD(TAG, "Doing full (non-incremental) update for StudentAssociation.");
        list.add(ContentProviderOperation.newDelete(uri).build());

        for (StudentAssociation studentAssociation : mStudentAssociations.values()) {

            boolean isNew = true;
            buildSession(isNew, studentAssociation, list);
        }

        LOGD(TAG, "StudentAssociations: FULL. New total: " + mStudentAssociations.size());
    }

    private void buildSession(boolean isInsert,
                              StudentAssociation studentAssociation, ArrayList<ContentProviderOperation> list) {
        ContentProviderOperation.Builder builder;
        Uri allStudentAssociationsUri = PTAppContract
                .addCallerIsSyncAdapterParameter(PTAppContract.StudentAssociation.CONTENT_URI);
        Uri thisStudentAssociationUri = PTAppContract
                .addCallerIsSyncAdapterParameter(PTAppContract.StudentAssociation.buildStudentAssociationUri(
                        String.valueOf(studentAssociation.StudentAssociationId)));

        if (isInsert) {
            builder = ContentProviderOperation.newInsert(allStudentAssociationsUri);
        } else {
            builder = ContentProviderOperation.newUpdate(thisStudentAssociationUri);
        }

        builder.withValue(PTAppContract.StudentAssociation._ID, studentAssociation.StudentAssociationId)
                .withValue(PTAppContract.StudentAssociation.YEARLY_RESULT, studentAssociation.YearlyResult)
                .withValue(PTAppContract.StudentAssociation.STUDENT_ID, studentAssociation.StudentId)
                .withValue(PTAppContract.StudentAssociation.CLASS_SUBJECT_ID, studentAssociation.ClassSubjectId);

        list.add(builder.build());
    }
}
