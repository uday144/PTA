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
import com.ptapp.io.model.Address;
import com.ptapp.provider.PTAppContract;

import java.util.ArrayList;
import java.util.HashMap;

import static com.ptapp.utils.LogUtils.LOGD;
import static com.ptapp.utils.LogUtils.makeLogTag;

public class AddressHandler extends JSONHandler {
    private static final String TAG = makeLogTag(AddressHandler.class);

    private HashMap<String, Address> mAddresses = new HashMap<String, Address>();

    public AddressHandler(Context context) {
        super(context);
    }

    @Override
    public void process(JsonElement element) {
        for (Address address : new Gson().fromJson(element, Address[].class)) {
            mAddresses.put(String.valueOf(address.AddressId), address);
        }
    }

    @Override
    public void makeContentProviderOperations(ArrayList<ContentProviderOperation> list) {
        Uri uri = PTAppContract.addCallerIsSyncAdapterParameter(
                PTAppContract.Address.CONTENT_URI);

        LOGD(TAG, "Doing full (non-incremental) update for address.");
        list.add(ContentProviderOperation.newDelete(uri).build());

        for (Address address : mAddresses.values()) {

            boolean isNew = true;
            buildSession(isNew, address, list);
        }

        LOGD(TAG, "Addresses: FULL. New total: " + mAddresses.size());
    }

    private void buildSession(boolean isInsert,
                              Address address, ArrayList<ContentProviderOperation> list) {
        ContentProviderOperation.Builder builder;
        Uri allAddressesUri = PTAppContract
                .addCallerIsSyncAdapterParameter(PTAppContract.Address.CONTENT_URI);
        Uri thisAddressUri = PTAppContract
                .addCallerIsSyncAdapterParameter(PTAppContract.Address.buildAddressUri(
                        String.valueOf(address.AddressId)));

        if (isInsert) {
            builder = ContentProviderOperation.newInsert(allAddressesUri);
        } else {
            builder = ContentProviderOperation.newUpdate(thisAddressUri);
        }

        builder.withValue(PTAppContract.Address._ID, address.AddressId)
                .withValue(PTAppContract.Address.ADDRESS1, address.Address1)
                .withValue(PTAppContract.Address.ADDRESS2, address.Address2)
                .withValue(PTAppContract.Address.ADDRESS3, address.Address3)
                .withValue(PTAppContract.Address.ADDRESS4, address.Address4)
                .withValue(PTAppContract.Address.CITY_CODE, address.CityCode)
                .withValue(PTAppContract.Address.CITY_NAME, address.CityName)
                .withValue(PTAppContract.Address.STATE_CODE, address.StateCode)
                .withValue(PTAppContract.Address.STATE_NAME, address.StateName)
                .withValue(PTAppContract.Address.COUNTRY_CODE, address.CountryCode)
                .withValue(PTAppContract.Address.COUNTRY_NAME, address.CountryName);

        list.add(builder.build());
    }
}
