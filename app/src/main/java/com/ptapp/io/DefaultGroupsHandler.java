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
import com.ptapp.provider.PTAppContract;
import com.ptapp.io.model.DefaultGroups;

import java.util.ArrayList;
import java.util.HashMap;

import static com.ptapp.utils.LogUtils.LOGD;
import static com.ptapp.utils.LogUtils.makeLogTag;

public class DefaultGroupsHandler extends JSONHandler {
    private static final String TAG = makeLogTag(DefaultGroupsHandler.class);

    private HashMap<String, DefaultGroups> mDefaultGroups = new HashMap<String, DefaultGroups>();

    public DefaultGroupsHandler(Context context) {
        super(context);
    }

    @Override
    public void process(JsonElement element) {
        for (DefaultGroups defaultGroups : new Gson().fromJson(element, DefaultGroups[].class)) {
            mDefaultGroups.put(String.valueOf(defaultGroups.GroupId), defaultGroups);
        }
    }

    @Override
    public void makeContentProviderOperations(ArrayList<ContentProviderOperation> list) {
        Uri uri = PTAppContract.addCallerIsSyncAdapterParameter(PTAppContract.DefaultGroups.CONTENT_URI);

        LOGD(TAG, "Doing full (non-incremental) update for default groups.");
        list.add(ContentProviderOperation.newDelete(uri).build());

        for (DefaultGroups defaultGroups : mDefaultGroups.values()) {

            boolean isNew = true;
            buildSession(isNew, defaultGroups, list);
        }

        LOGD(TAG, "DefaultGroups: FULL. New total: " + mDefaultGroups.size());
    }

    private void buildSession(boolean isInsert,
                              DefaultGroups defaultGroups, ArrayList<ContentProviderOperation> list) {
        ContentProviderOperation.Builder builder;
        Uri allDefaultGroupsUri = PTAppContract.addCallerIsSyncAdapterParameter(PTAppContract.DefaultGroups.CONTENT_URI);
        Uri thisDefaultGroupsUri = PTAppContract.addCallerIsSyncAdapterParameter(
                PTAppContract.DefaultGroups.buildDefaultGroupsUri((String.valueOf(defaultGroups.GroupId))));

        if (isInsert) {
            builder = ContentProviderOperation.newInsert(allDefaultGroupsUri);
        } else {
            builder = ContentProviderOperation.newUpdate(thisDefaultGroupsUri);
        }

        builder.withValue(PTAppContract.DefaultGroups._ID, defaultGroups.GroupId)
                .withValue(PTAppContract.DefaultGroups.GROUP_NAME, defaultGroups.GroupName)
                .withValue(PTAppContract.DefaultGroups.GROUP_DESCRIPTION, defaultGroups.GroupDescription)
                .withValue(PTAppContract.DefaultGroups.GROUP_TYPE, defaultGroups.GroupType)
                .withValue(PTAppContract.DefaultGroups.IMAGE_URL, defaultGroups.ImageURL)
                .withValue(PTAppContract.DefaultGroups.MEMBER_COUNT, defaultGroups.MemberCount)
                .withValue(PTAppContract.DefaultGroups.JID, defaultGroups.JabberId)
                .withValue(PTAppContract.DefaultGroups.CLASS_SUBJECT_ID, defaultGroups.ClassSubjectId)
                .withValue(PTAppContract.DefaultGroups.STAFF_ENGAGEMENT_ID, defaultGroups.StaffEngagementId);

        list.add(builder.build());
    }
}
