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
import com.ptapp.io.model.StaffEngagement;
import com.ptapp.provider.PTAppContract;

import java.util.ArrayList;
import java.util.HashMap;

import static com.ptapp.utils.LogUtils.LOGD;
import static com.ptapp.utils.LogUtils.makeLogTag;

public class StaffEngagementHandler extends JSONHandler {
    private static final String TAG = makeLogTag(StaffEngagementHandler.class);

    private HashMap<String, StaffEngagement> mStaffEngagements = new HashMap<String, StaffEngagement>();

    public StaffEngagementHandler(Context context) {
        super(context);
    }

    @Override
    public void process(JsonElement element) {
        for (StaffEngagement staffEngagement : new Gson().fromJson(element, StaffEngagement[].class)) {
            mStaffEngagements.put(String.valueOf(staffEngagement.StaffEngagementId), staffEngagement);
        }
    }

    @Override
    public void makeContentProviderOperations(ArrayList<ContentProviderOperation> list) {
        Uri uri = PTAppContract.addCallerIsSyncAdapterParameter(
                PTAppContract.StaffEngagement.CONTENT_URI);

        LOGD(TAG, "Doing full (non-incremental) update for StaffEngagement.");
        list.add(ContentProviderOperation.newDelete(uri).build());

        for (StaffEngagement staffEngagement : mStaffEngagements.values()) {

            boolean isNew = true;
            buildSession(isNew, staffEngagement, list);
        }

        LOGD(TAG, "StaffEngagements: FULL. New total: " + mStaffEngagements.size());
    }

    private void buildSession(boolean isInsert,
                              StaffEngagement staffEngagement, ArrayList<ContentProviderOperation> list) {
        ContentProviderOperation.Builder builder;
        Uri allStaffEngagementsUri = PTAppContract
                .addCallerIsSyncAdapterParameter(PTAppContract.StaffEngagement.CONTENT_URI);
        Uri thisStaffEngagementUri = PTAppContract
                .addCallerIsSyncAdapterParameter(PTAppContract.StaffEngagement.buildStaffEngagementUri(
                        String.valueOf(staffEngagement.StaffEngagementId)));

        if (isInsert) {
            builder = ContentProviderOperation.newInsert(allStaffEngagementsUri);
        } else {
            builder = ContentProviderOperation.newUpdate(thisStaffEngagementUri);
        }

        builder.withValue(PTAppContract.StaffEngagement._ID, staffEngagement.StaffEngagementId)
                .withValue(PTAppContract.StaffEngagement.ACTIVE, staffEngagement.Active)
                .withValue(PTAppContract.StaffEngagement.END_DATE, staffEngagement.EndDate)
                .withValue(PTAppContract.StaffEngagement.START_DATE, staffEngagement.StartDate)
                .withValue(PTAppContract.StaffEngagement.STAFF_ID, staffEngagement.StaffId)
                .withValue(PTAppContract.StaffEngagement.CLASS_SUBJECT_ID, staffEngagement.ClassSubjectId);

        list.add(builder.build());
    }
}
