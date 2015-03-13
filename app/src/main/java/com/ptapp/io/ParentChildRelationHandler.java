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
import com.ptapp.io.model.ParentChildRelation;
import com.ptapp.provider.PTAppContract;

import java.util.ArrayList;
import java.util.HashMap;

import static com.ptapp.utils.LogUtils.LOGD;
import static com.ptapp.utils.LogUtils.makeLogTag;

public class ParentChildRelationHandler extends JSONHandler {
    private static final String TAG = makeLogTag(ParentChildRelationHandler.class);

    private HashMap<String, ParentChildRelation> mParentChildRelations = new HashMap<String, ParentChildRelation>();

    public ParentChildRelationHandler(Context context) {
        super(context);
    }

    @Override
    public void process(JsonElement element) {
        for (ParentChildRelation parentChildRelation : new Gson().fromJson(element, ParentChildRelation[].class)) {
            mParentChildRelations.put(String.valueOf(parentChildRelation.ParentChildRelationId), parentChildRelation);
        }
    }

    @Override
    public void makeContentProviderOperations(ArrayList<ContentProviderOperation> list) {
        Uri uri = PTAppContract.addCallerIsSyncAdapterParameter(
                PTAppContract.ParentChildRelation.CONTENT_URI);

        LOGD(TAG, "Doing full (non-incremental) update for ParentChildRelations.");
        list.add(ContentProviderOperation.newDelete(uri).build());

        for (ParentChildRelation parentChildRelation : mParentChildRelations.values()) {

            boolean isNew = true;
            buildSession(isNew, parentChildRelation, list);
        }

        LOGD(TAG, "ParentChildRelations: FULL. New total: " + mParentChildRelations.size());
    }

    private void buildSession(boolean isInsert,
                              ParentChildRelation parentChildRelation, ArrayList<ContentProviderOperation> list) {
        ContentProviderOperation.Builder builder;
        Uri allParentChildRelationsUri = PTAppContract
                .addCallerIsSyncAdapterParameter(PTAppContract.ParentChildRelation.CONTENT_URI);
        Uri thisParentChildRelationUri = PTAppContract
                .addCallerIsSyncAdapterParameter(PTAppContract.ParentChildRelation.buildParentChildRelationUri(
                        String.valueOf(parentChildRelation.ParentChildRelationId)));

        if (isInsert) {
            builder = ContentProviderOperation.newInsert(allParentChildRelationsUri);
        } else {
            builder = ContentProviderOperation.newUpdate(thisParentChildRelationUri);
        }

        builder.withValue(PTAppContract.ParentChildRelation._ID, parentChildRelation.ParentChildRelationId)
                .withValue(PTAppContract.ParentChildRelation.PARENT_TYPE, parentChildRelation.ParentType)
                .withValue(PTAppContract.ParentChildRelation.STUDENT_ID, parentChildRelation.StudentId)
                .withValue(PTAppContract.ParentChildRelation.PARENT_ID, parentChildRelation.ParentId);

        list.add(builder.build());
    }
}
