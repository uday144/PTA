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
import com.ptapp.io.model.ClassSubject;
import com.ptapp.provider.PTAppContract;

import java.util.ArrayList;
import java.util.HashMap;

import static com.ptapp.utils.LogUtils.LOGD;
import static com.ptapp.utils.LogUtils.makeLogTag;

public class ClassSubjectHandler extends JSONHandler {
    private static final String TAG = makeLogTag(ClassSubjectHandler.class);

    private HashMap<String, ClassSubject> mClassSubjects = new HashMap<String, ClassSubject>();

    public ClassSubjectHandler(Context context) {
        super(context);
    }

    @Override
    public void process(JsonElement element) {
        for (ClassSubject classSubject : new Gson().fromJson(element, ClassSubject[].class)) {
            mClassSubjects.put(String.valueOf(classSubject.ClassSubjectId), classSubject);
        }
    }

    @Override
    public void makeContentProviderOperations(ArrayList<ContentProviderOperation> list) {
        Uri uri = PTAppContract.addCallerIsSyncAdapterParameter(
                PTAppContract.ClassSubject.CONTENT_URI);

        LOGD(TAG, "Doing full (non-incremental) update for ClassSubjects.");
        list.add(ContentProviderOperation.newDelete(uri).build());

        for (ClassSubject classSubject : mClassSubjects.values()) {

            boolean isNew = true;
            buildSession(isNew, classSubject, list);
        }

        LOGD(TAG, "ClassSubjects: FULL. New total: " + mClassSubjects.size());
    }

    private void buildSession(boolean isInsert,
                              ClassSubject classSubject, ArrayList<ContentProviderOperation> list) {
        ContentProviderOperation.Builder builder;
        Uri allClassSubjectsUri = PTAppContract
                .addCallerIsSyncAdapterParameter(PTAppContract.ClassSubject.CONTENT_URI);
        Uri thisClassSubjectUri = PTAppContract
                .addCallerIsSyncAdapterParameter(PTAppContract.ClassSubject.buildClassSubjectUri(
                        String.valueOf(classSubject.ClassId)));

        if (isInsert) {
            builder = ContentProviderOperation.newInsert(allClassSubjectsUri);
        } else {
            builder = ContentProviderOperation.newUpdate(thisClassSubjectUri);
        }

        builder.withValue(PTAppContract.ClassSubject._ID, classSubject.ClassSubjectId)
                .withValue(PTAppContract.ClassSubject.CLASS_ID, classSubject.ClassId)
                .withValue(PTAppContract.ClassSubject.SUBJECT_ID, classSubject.SubjectId)
                .withValue(PTAppContract.ClassSubject.GROUP_JID, classSubject.GroupJid);


        /*if (classSubject.GroupJid == null) {
            String tmpJid = "x" + classSubject.ClassSubjectId + ".g@xmpp.jp";
            builder.withValue(PTAppContract.ClassSubject.GROUP_JID, tmpJid);
        }*/

        list.add(builder.build());
    }
}
