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
import com.ptapp.io.model.Student;
import com.ptapp.provider.PTAppContract;

import java.util.ArrayList;
import java.util.HashMap;

import static com.ptapp.utils.LogUtils.LOGD;
import static com.ptapp.utils.LogUtils.makeLogTag;

public class StudentHandler extends JSONHandler {
    private static final String TAG = makeLogTag(StudentHandler.class);

    private HashMap<String, Student> mStudents = new HashMap<String, Student>();

    public StudentHandler(Context context) {
        super(context);
    }

    @Override
    public void process(JsonElement element) {
        for (Student student : new Gson().fromJson(element, Student[].class)) {
            mStudents.put(String.valueOf(student.StudentId), student);
        }
    }

    @Override
    public void makeContentProviderOperations(ArrayList<ContentProviderOperation> list) {
        Uri uri = PTAppContract.addCallerIsSyncAdapterParameter(
                PTAppContract.Student.CONTENT_URI);

        LOGD(TAG, "Doing full (non-incremental) update for student.");
        list.add(ContentProviderOperation.newDelete(uri).build());

        for (Student student : mStudents.values()) {

            boolean isNew = true;
            buildSession(isNew, student, list);
        }

        LOGD(TAG, "Students: FULL. New total: " + mStudents.size());
    }

    private void buildSession(boolean isInsert,
                              Student student, ArrayList<ContentProviderOperation> list) {
        ContentProviderOperation.Builder builder;
        Uri allStudentsUri = PTAppContract
                .addCallerIsSyncAdapterParameter(PTAppContract.Student.CONTENT_URI);
        Uri thisStudentUri = PTAppContract
                .addCallerIsSyncAdapterParameter(PTAppContract.Student.buildStudentUri(
                        String.valueOf(student.StudentId)));

        if (isInsert) {
            builder = ContentProviderOperation.newInsert(allStudentsUri);
        } else {
            builder = ContentProviderOperation.newUpdate(thisStudentUri);
        }

        builder.withValue(PTAppContract.Student._ID, student.StudentId)
                .withValue(PTAppContract.Student.ALLERGIES, student.Allergies)
                .withValue(PTAppContract.Student.DOB, student.dob)
                .withValue(PTAppContract.Student.EMAIL, student.Email)
                .withValue(PTAppContract.Student.FIRST_NAME, student.FirstName)
                .withValue(PTAppContract.Student.GENDER, student.Gender)
                .withValue(PTAppContract.Student.LAST_NAME, student.LastName)
                .withValue(PTAppContract.Student.MOBILE, student.Mobile)
                .withValue(PTAppContract.Student.COUNTRY_ISO_CODE, student.CountryISOCode)
                .withValue(PTAppContract.Student.SPECIAL_INSTRUCTIONS, student.SpecialInstructions)
                .withValue(PTAppContract.Student.ADDRESS_ID, student.AddressId)
                .withValue(PTAppContract.Student.BRANCH_ID, student.BranchId)
                .withValue(PTAppContract.Student.IMAGE_URL, student.ImageURL)
                .withValue(PTAppContract.Student.USER_ID, student.UserId)
                .withValue(PTAppContract.Student.ENROLLMENT_NUM, student.EnrollmentNo)
                .withValue(PTAppContract.Student.JID, student.Jid);

        //TODO: Need to remove this logic once server starts feeding this value. Currently its absent.
        /*if (student.Jid == null) {
            *//*String tmpJid = "x" + student.UserId + ".s@xmpp.jp";*//*
            String tmpJid = "x" + student.UserId + ".s@xmpp.jp";
            builder.withValue(PTAppContract.Student.JID, tmpJid);
        }*/

        list.add(builder.build());
    }
}
