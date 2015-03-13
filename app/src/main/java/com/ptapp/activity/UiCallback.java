package com.ptapp.activity;

import android.app.PendingIntent;

public interface UiCallback<T> {
    public void success(T object);

    public void error(int errorCode, T object);

    public void userInputRequired(PendingIntent pi, T object);
}
