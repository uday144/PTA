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
import com.ptapp.io.model.Class;
import com.ptapp.provider.PTAppContract;

import java.util.ArrayList;
import java.util.HashMap;

import static com.ptapp.utils.LogUtils.LOGD;
import static com.ptapp.utils.LogUtils.makeLogTag;

public class ClassHandler extends JSONHandler {
    private static final String TAG = makeLogTag(ClassHandler.class);

    private HashMap<String, Class> mClasses = new HashMap<String, Class>();

    public ClassHandler(Context context) {
        super(context);
    }

    @Override
    public void process(JsonElement element) {
        for (Class zclass : new Gson().fromJson(element, Class[].class)) {
            mClasses.put(String.valueOf(zclass.ClassId), zclass);
        }
    }

    @Override
    public void makeContentProviderOperations(ArrayList<ContentProviderOperation> list) {
        Uri uri = PTAppContract.addCallerIsSyncAdapterParameter(
                PTAppContract.Classes.CONTENT_URI);

        LOGD(TAG, "Doing full (non-incremental) update for classes.");
        list.add(ContentProviderOperation.newDelete(uri).build());

        for (Class zclass : mClasses.values()) {

            boolean isNew = true;
            buildSession(isNew, zclass, list);
        }

        LOGD(TAG, "Classes: FULL. New total: " + mClasses.size());
    }

    private void buildSession(boolean isInsert,
                              Class zclass, ArrayList<ContentProviderOperation> list) {
        ContentProviderOperation.Builder builder;
        Uri allClassesUri = PTAppContract
                .addCallerIsSyncAdapterParameter(PTAppContract.Classes.CONTENT_URI);
        Uri thisClassUri = PTAppContract
                .addCallerIsSyncAdapterParameter(PTAppContract.Classes.buildClassUri(
                        String.valueOf(zclass.ClassId)));

        if (isInsert) {
            builder = ContentProviderOperation.newInsert(allClassesUri);
        } else {
            builder = ContentProviderOperation.newUpdate(thisClassUri);
        }

        builder.withValue(PTAppContract.Classes._ID, zclass.ClassId)
                .withValue(PTAppContract.Classes.CLASS_TYPE_CODE, zclass.ClassTypeCode)
                .withValue(PTAppContract.Classes.CLASS_TYPE_DESCRIPTION, zclass.ClassTypeDescription)
                .withValue(PTAppContract.Classes.SECTION_TYPE_DESCRIPTION, zclass.SectionTypeDescription)
                .withValue(PTAppContract.Classes.SECTION_TYPE_CODE, zclass.SectionTypeCode)
                .withValue(PTAppContract.Classes.ACADEMIC_SESSION_ID, zclass.AcademicSessionId)
                .withValue(PTAppContract.Classes.ATTENDANCE_TYPE_CODE, zclass.AttendanceTypeCode)
                .withValue(PTAppContract.Classes.ATTENDANCE_TYPE_DESCRIPTION, zclass.AttendanceTypeDescription)
                .withValue(PTAppContract.Classes.BRANCH_ID, zclass.BranchId)
                .withValue(PTAppContract.Classes.CLASS_TEACHER, zclass.ClassTeacher);

        list.add(builder.build());
    }
}
