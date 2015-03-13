package com.ptapp.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import com.ptapp.fragment.SettingsFragment;

public class SettingsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Display the fragment as the main content.
		FragmentManager fm = getFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		ft.replace(android.R.id.content, new SettingsFragment());
		ft.commit(); // Note that you must call commit() to have any changes you
						// perform in the Editor actually show up in the
						// SharedPreferences.

		// Bundle objBundle = getIntent().getExtras();
		// if (objBundle != null) { // someone is passing an Intent with Bundle
		// // info, like Download
		// // Service
		// displayPickAccountDialog(objBundle);
		// }
	}

}
