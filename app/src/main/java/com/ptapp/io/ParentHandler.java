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
import com.ptapp.io.model.Parent;
import com.ptapp.provider.PTAppContract;

import java.util.ArrayList;
import java.util.HashMap;

import static com.ptapp.utils.LogUtils.LOGD;
import static com.ptapp.utils.LogUtils.makeLogTag;

public class ParentHandler extends JSONHandler {
    private static final String TAG = makeLogTag(ParentHandler.class);

    private HashMap<String, Parent> mParents = new HashMap<String, Parent>();

    public ParentHandler(Context context) {
        super(context);
    }

    @Override
    public void process(JsonElement element) {
        for (Parent parent : new Gson().fromJson(element, Parent[].class)) {
            mParents.put(String.valueOf(parent.ParentId), parent);
        }
    }

    @Override
    public void makeContentProviderOperations(ArrayList<ContentProviderOperation> list) {
        Uri uri = PTAppContract.addCallerIsSyncAdapterParameter(
                PTAppContract.Parent.CONTENT_URI);

        LOGD(TAG, "Doing full (non-incremental) update for Parents.");
        list.add(ContentProviderOperation.newDelete(uri).build());

        for (Parent parent : mParents.values()) {

            boolean isNew = true;
            buildSession(isNew, parent, list);
        }

        LOGD(TAG, "Parents: FULL. New total: " + mParents.size());
    }

    private void buildSession(boolean isInsert,
                              Parent parent, ArrayList<ContentProviderOperation> list) {
        ContentProviderOperation.Builder builder;
        Uri allParentsUri = PTAppContract
                .addCallerIsSyncAdapterParameter(PTAppContract.Parent.CONTENT_URI);
        Uri thisParentUri = PTAppContract
                .addCallerIsSyncAdapterParameter(PTAppContract.Parent.buildParentUri(
                        String.valueOf(parent.ParentId)));

        if (isInsert) {
            builder = ContentProviderOperation.newInsert(allParentsUri);
        } else {
            builder = ContentProviderOperation.newUpdate(thisParentUri);
        }

        builder.withValue(PTAppContract.Parent._ID, parent.ParentId)
                .withValue(PTAppContract.Parent.FIRST_NAME, parent.FirstName)
                .withValue(PTAppContract.Parent.LAST_NAME, parent.LastName)
                .withValue(PTAppContract.Parent.EMAIL, parent.Email)
                .withValue(PTAppContract.Parent.GENDER, parent.Gender)
                .withValue(PTAppContract.Parent.MOBILE, parent.Mobile)
                .withValue(PTAppContract.Parent.COUNTRY_ISO_CODE, parent.CountryISOCode)
                .withValue(PTAppContract.Parent.QUALIFICATION, parent.Qualification)
                .withValue(PTAppContract.Parent.ADDRESS_ID, parent.AddressId)
                .withValue(PTAppContract.Parent.IMAGE_URL, parent.ImageURL)
                .withValue(PTAppContract.Parent.USER_ID, parent.UserId)
                .withValue(PTAppContract.Parent.JID, parent.Jid);

        //TODO: Need to remove this logic once server starts feeding this value. Currently its absent.
        /*if (parent.Jid == null) {
            String tmpJid = "x" + parent.UserId + ".p@xmpp.jp";
            builder.withValue(PTAppContract.Parent.JID, tmpJid);
        }*/

        list.add(builder.build());
    }
}
