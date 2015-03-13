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
import com.ptapp.io.model.Branch;
import com.ptapp.provider.PTAppContract;

import java.util.ArrayList;
import java.util.HashMap;

import static com.ptapp.utils.LogUtils.LOGD;
import static com.ptapp.utils.LogUtils.makeLogTag;

public class BranchHandler extends JSONHandler {
    private static final String TAG = makeLogTag(BranchHandler.class);

    private HashMap<String, Branch> mBranches = new HashMap<String, Branch>();

    public BranchHandler(Context context) {
        super(context);
    }

    @Override
    public void process(JsonElement element) {
        for (Branch branch : new Gson().fromJson(element, Branch[].class)) {
            mBranches.put(String.valueOf(branch.BranchId), branch);
        }
    }

    @Override
    public void makeContentProviderOperations(ArrayList<ContentProviderOperation> list) {
        Uri uri = PTAppContract.addCallerIsSyncAdapterParameter(
                PTAppContract.Branch.CONTENT_URI);

        LOGD(TAG, "Doing full (non-incremental) update for branches.");
        list.add(ContentProviderOperation.newDelete(uri).build());

        for (Branch branch : mBranches.values()) {

            boolean isNew = true;
            buildSession(isNew, branch, list);
        }

        LOGD(TAG, "Branches: FULL. New total: " + mBranches.size());
    }

    private void buildSession(boolean isInsert,
                              Branch branch, ArrayList<ContentProviderOperation> list) {
        ContentProviderOperation.Builder builder;
        Uri allBranchesUri = PTAppContract
                .addCallerIsSyncAdapterParameter(PTAppContract.Branch.CONTENT_URI);
        Uri thisBranchUri = PTAppContract
                .addCallerIsSyncAdapterParameter(PTAppContract.Branch.buildBranchUri(
                        String.valueOf(branch.BranchId)));

        if (isInsert) {
            builder = ContentProviderOperation.newInsert(allBranchesUri);
        } else {
            builder = ContentProviderOperation.newUpdate(thisBranchUri);
        }

        builder.withValue(PTAppContract.Branch._ID, branch.BranchId)
                .withValue(PTAppContract.Branch.BRANCH_NAME, branch.BranchName)
                .withValue(PTAppContract.Branch.EMAIL, branch.Email)
                .withValue(PTAppContract.Branch.PHONE, branch.Phone)
                .withValue(PTAppContract.Branch.COUNTRY_ISO_CODE, branch.CountryISOCode)
                .withValue(PTAppContract.Branch.ADDRESS_ID, branch.AddressId)
                .withValue(PTAppContract.Branch.INSTITUTE_ID, branch.InstituteId);

        list.add(builder.build());
    }
}
