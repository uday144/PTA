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
import com.ptapp.io.model.User;


import java.util.ArrayList;
import java.util.HashMap;

import static com.ptapp.utils.LogUtils.LOGD;
import static com.ptapp.utils.LogUtils.makeLogTag;

public class UserHandler extends JSONHandler {
    private static final String TAG = makeLogTag(UserHandler.class);

    private HashMap<String, User> mUsers = new HashMap<String, User>();

    public UserHandler(Context context) {
        super(context);
    }

    @Override
    public void process(JsonElement element) {
        for (User user : new Gson().fromJson(element, User[].class)) {
            mUsers.put(String.valueOf(user.UserId), user);
        }
    }

    @Override
    public void makeContentProviderOperations(ArrayList<ContentProviderOperation> list) {
        Uri uri = PTAppContract.addCallerIsSyncAdapterParameter(PTAppContract.User.CONTENT_URI);

        LOGD(TAG, "Doing full (non-incremental) update for user.");
        list.add(ContentProviderOperation.newDelete(uri).build());

        for (User user : mUsers.values()) {

            boolean isNew = true;
            buildSession(isNew, user, list);
        }

        LOGD(TAG, "Users: FULL. New total: " + mUsers.size());
    }

    private void buildSession(boolean isInsert,
                              User user, ArrayList<ContentProviderOperation> list) {
        ContentProviderOperation.Builder builder;
        Uri allUsersUri = PTAppContract
                .addCallerIsSyncAdapterParameter(PTAppContract.User.CONTENT_URI);
        Uri thisUserUri = PTAppContract
                .addCallerIsSyncAdapterParameter(PTAppContract.User.buildUserUri(
                        String.valueOf(user.UserId)));

        if (isInsert) {
            builder = ContentProviderOperation.newInsert(allUsersUri);
        } else {
            builder = ContentProviderOperation.newUpdate(thisUserUri);
        }

        builder.withValue(PTAppContract.User._ID, user.UserId)
                .withValue(PTAppContract.User.MOBILE, user.Mobile)
                .withValue(PTAppContract.User.COUNTRY_ISO_CODE, user.CountryIsoCode)
                .withValue(PTAppContract.User.REGISTERED, user.Registered)
                .withValue(PTAppContract.User.DELETED, user.Deleted);

        list.add(builder.build());
    }
}
