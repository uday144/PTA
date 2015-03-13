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
import com.ptapp.io.model.Staff;
import com.ptapp.provider.PTAppContract;

import java.util.ArrayList;
import java.util.HashMap;

import static com.ptapp.utils.LogUtils.LOGD;
import static com.ptapp.utils.LogUtils.makeLogTag;

public class StaffHandler extends JSONHandler {
    private static final String TAG = makeLogTag(StaffHandler.class);

    private HashMap<String, Staff> mStaffs = new HashMap<String, Staff>();

    public StaffHandler(Context context) {
        super(context);
    }

    @Override
    public void process(JsonElement element) {
        for (Staff staff : new Gson().fromJson(element, Staff[].class)) {
            mStaffs.put(String.valueOf(staff.StaffId), staff);
        }
    }

    @Override
    public void makeContentProviderOperations(ArrayList<ContentProviderOperation> list) {
        Uri uri = PTAppContract.addCallerIsSyncAdapterParameter(
                PTAppContract.Staff.CONTENT_URI);

        LOGD(TAG, "Doing full (non-incremental) update for staffs.");
        list.add(ContentProviderOperation.newDelete(uri).build());

        for (Staff staff : mStaffs.values()) {

            boolean isNew = true;
            buildSession(isNew, staff, list);
        }

        LOGD(TAG, "Staffs: FULL. New total: " + mStaffs.size());
    }

    private void buildSession(boolean isInsert,
                              Staff staff, ArrayList<ContentProviderOperation> list) {
        ContentProviderOperation.Builder builder;
        Uri allStaffsUri = PTAppContract
                .addCallerIsSyncAdapterParameter(PTAppContract.Staff.CONTENT_URI);
        Uri thisStaffUri = PTAppContract
                .addCallerIsSyncAdapterParameter(PTAppContract.Staff.buildStaffUri(
                        String.valueOf(staff.StaffId)));

        if (isInsert) {
            builder = ContentProviderOperation.newInsert(allStaffsUri);
        } else {
            builder = ContentProviderOperation.newUpdate(thisStaffUri);
        }

        builder.withValue(PTAppContract.Staff._ID, staff.StaffId)
                .withValue(PTAppContract.Staff.DISPLAY_NAME, staff.DisplayName)
                .withValue(PTAppContract.Staff.FIRST_NAME, staff.FirstName)
                .withValue(PTAppContract.Staff.LAST_NAME, staff.LastName)
                .withValue(PTAppContract.Staff.EMAIL, staff.Email)
                .withValue(PTAppContract.Staff.IS_ADMIN, staff.IsAdmin)
                .withValue(PTAppContract.Staff.MOBILE, staff.Mobile)
                .withValue(PTAppContract.Staff.COUNTRY_ISO_CODE, staff.CountryISOCode)
                .withValue(PTAppContract.Staff.PASSWORD, staff.Password)
                .withValue(PTAppContract.Staff.ADDRESS_ID, staff.AddressId)
                .withValue(PTAppContract.Staff.QUALIFICATION_LEVEL, staff.QualificationLevel)
                .withValue(PTAppContract.Staff.QUALIFICATION_NAME, staff.QualificationName)
                .withValue(PTAppContract.Staff.STAFF_TYPE_DESCRIPTION, staff.StaffTypeDescription)
                .withValue(PTAppContract.Staff.STAFF_TYPE_CODE, staff.StaffTypeCode)
                .withValue(PTAppContract.Staff.BRANCH_ID, staff.BranchId)
                .withValue(PTAppContract.Staff.IMAGE_URL, staff.ImageURL)
                .withValue(PTAppContract.Staff.USER_ID, staff.UserId)
                .withValue(PTAppContract.Staff.JID, staff.Jid);

        //TODO: Need to remove this logic once server starts feeding this value. Currently its absent.
        /*if (staff.Jid == null) {
            String tmpJid = "x" + staff.UserId + ".e@xmpp.jp";
            builder.withValue(PTAppContract.Staff.JID, tmpJid);
        }*/

        list.add(builder.build());
    }
}
