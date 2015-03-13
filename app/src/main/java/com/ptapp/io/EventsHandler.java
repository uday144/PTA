package com.ptapp.io;

import android.content.ContentProviderOperation;
import android.content.Context;
import android.net.Uri;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
//import com.ptapp.io.model.Educator;
import com.ptapp.io.model.Event;
import com.ptapp.provider.PTAppContract;
import com.ptapp.utils.TimeUtils;

import java.util.ArrayList;
import java.util.HashMap;

import static com.ptapp.utils.LogUtils.LOGD;
import static com.ptapp.utils.LogUtils.makeLogTag;


public class EventsHandler extends JSONHandler {
    private static final String TAG = makeLogTag(EventsHandler.class);
    private HashMap<String, Event> mEvents = new HashMap<String, Event>();

    public EventsHandler(Context context) {
        super(context);
    }

    @Override
    public void makeContentProviderOperations(ArrayList<ContentProviderOperation> list) {
        Uri uri = PTAppContract.addCallerIsSyncAdapterParameter(
                PTAppContract.Events.CONTENT_URI);

        LOGD(TAG, "Doing full (non-incremental) update for events.");
        list.add(ContentProviderOperation.newDelete(uri).build());

        for (Event event : mEvents.values()) {

            boolean isNew = true;
            buildSession(isNew, event, list);
        }

        LOGD(TAG, "Events: FULL. New total: " + mEvents.size());
    }

    @Override
    public void process(JsonElement element) {
        for (Event event : new Gson().fromJson(element, Event[].class)) {
            mEvents.put(event.id, event);
        }
    }

    private void buildSession(boolean isInsert,
                              Event event, ArrayList<ContentProviderOperation> list) {
        ContentProviderOperation.Builder builder;
        Uri allEventsUri = PTAppContract
                .addCallerIsSyncAdapterParameter(PTAppContract.Events.CONTENT_URI);
        Uri thisEventUri = PTAppContract
                .addCallerIsSyncAdapterParameter(PTAppContract.Events.buildEventUri(
                        event.id));

        if (isInsert) {
            builder = ContentProviderOperation.newInsert(allEventsUri);
        } else {
            builder = ContentProviderOperation.newUpdate(thisEventUri);
        }

        builder.withValue(PTAppContract.SyncColumns.UPDATED, System.currentTimeMillis())
                .withValue(PTAppContract.Events._ID, event.id)
                .withValue(PTAppContract.Events.ANDROID_EVENT_ID, event.androidEventId)
                .withValue(PTAppContract.Events.EVENT_TYPE, event.evtType)
                .withValue(PTAppContract.Events.EVENT_START, TimeUtils.timestampToMillis(event.startDatetime, 0))
                .withValue(PTAppContract.Events.EVENT_END, TimeUtils.timestampToMillis(event.endDatetime, 0))
                .withValue(PTAppContract.Events.EVENT_TITLE, event.eventTitle)
                .withValue(PTAppContract.Events.EVENT_DESCRIPTION, event.eventDescription)
                .withValue(PTAppContract.Events.EVENT_FOR_CLASSES, event.forClasses);

        list.add(builder.build());
    }
}
