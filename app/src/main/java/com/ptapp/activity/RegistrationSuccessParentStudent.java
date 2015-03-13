package com.ptapp.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Spinner;

import com.ptapp.app.R;

/**
 * When parent or student is invited by an unregistered individual teacher or an institute
 */
public class RegistrationSuccessParentStudent extends Activity {

    private static final String TAG = "PTApp-RegistrationSuccessParentStudent: ";
    private Spinner title;
    private String selectedTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_success_parent_student);

        title = (Spinner) findViewById(R.id.spin_title);

        //load countries and set the default country
        setSpinnerTitle();
        getActionBar().setTitle("<class> @ <Teacher name>");
    }

    private void setSpinnerTitle() {

    }

    public void onClickRadGrpParStu(View v) {

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.registration_success_parent_student, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
