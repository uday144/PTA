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
import com.ptapp.io.model.Institute;
import com.ptapp.provider.PTAppContract;

import java.util.ArrayList;
import java.util.HashMap;

import static com.ptapp.utils.LogUtils.LOGD;
import static com.ptapp.utils.LogUtils.makeLogTag;

public class InstituteHandler extends JSONHandler {
    private static final String TAG = makeLogTag(InstituteHandler.class);

    private HashMap<String, Institute> mInstitutes = new HashMap<String, Institute>();

    public InstituteHandler(Context context) {
        super(context);
    }

    @Override
    public void process(JsonElement element) {
        for (Institute institute : new Gson().fromJson(element, Institute[].class)) {
            mInstitutes.put(String.valueOf(institute.InstituteId), institute);
        }
    }

    @Override
    public void makeContentProviderOperations(ArrayList<ContentProviderOperation> list) {
        Uri uri = PTAppContract.addCallerIsSyncAdapterParameter(
                PTAppContract.Institute.CONTENT_URI);

        LOGD(TAG, "Doing full (non-incremental) update for institute.");
        list.add(ContentProviderOperation.newDelete(uri).build());

        for (Institute institute : mInstitutes.values()) {

            boolean isNew = true;
            buildSession(isNew, institute, list);
        }

        LOGD(TAG, "Institutes: FULL. New total: " + mInstitutes.size());
    }

    private void buildSession(boolean isInsert,
                              Institute institute, ArrayList<ContentProviderOperation> list) {
        ContentProviderOperation.Builder builder;
        Uri allInstitutesUri = PTAppContract
                .addCallerIsSyncAdapterParameter(PTAppContract.Institute.CONTENT_URI);
        Uri thisInstituteUri = PTAppContract
                .addCallerIsSyncAdapterParameter(PTAppContract.Institute.buildInstituteUri(
                        String.valueOf(institute.InstituteId)));

        if (isInsert) {
            builder = ContentProviderOperation.newInsert(allInstitutesUri);
        } else {
            builder = ContentProviderOperation.newUpdate(thisInstituteUri);
        }

        builder.withValue(PTAppContract.Institute._ID, institute.InstituteId)
                .withValue(PTAppContract.Institute.ACTIVE, institute.Active)
                .withValue(PTAppContract.Institute.EMAIL, institute.Email)
                .withValue(PTAppContract.Institute.INSTITUTE_NAME, institute.InstituteName)
                .withValue(PTAppContract.Institute.IS_REGISTERED, institute.IsRegistered)
                .withValue(PTAppContract.Institute.PHONE, institute.Phone)
                .withValue(PTAppContract.Institute.COUNTRY_ISO_CODE, institute.CountryISOCode)
                .withValue(PTAppContract.Institute.WEBSITE, institute.Website)
                .withValue(PTAppContract.Institute.ADDRESS_ID, institute.AddressId)
                .withValue(PTAppContract.Institute.INSTITUTE_TYPE_DESCRIPTION, institute.InstituteTypeDescription)
                .withValue(PTAppContract.Institute.INSTITUTE_TYPE_CODE, institute.InstituteTypeCode);

        list.add(builder.build());
    }
}
