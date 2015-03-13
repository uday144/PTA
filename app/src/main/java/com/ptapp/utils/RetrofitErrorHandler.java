package com.ptapp.utils;

import android.util.Log;

import retrofit.ErrorHandler;
import retrofit.RetrofitError;
import retrofit.client.Response;

/** To get error details from Retrofit library */

public class RetrofitErrorHandler implements ErrorHandler {

	private static final String TAG = "Retrofit: ";

	@Override
	public Throwable handleError(RetrofitError cause) {
		Response r = cause.getResponse();
		if (r != null) {
			if (r.getStatus() == 401) {
				Log.e(TAG, "Error code: 401. Unauthorised exception.");
				return new Throwable(cause);
			}
			if (r.getStatus() == 404) {
				Log.e(TAG,
						"Error code: 404. Api link not found. Please check if api link is correct and running on the server");
				return new Throwable(cause);
			}
		}
		return cause;
	}

}
