package com.ptapp.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.ptapp.app.R;

import static com.ptapp.utils.LogUtils.makeLogTag;

public class EducatorComposeMessage extends BaseActivity {

    private static final String TAG = makeLogTag(EducatorComposeMessage.class);
    private String classId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_educator_compose_message);

        if (getIntent() != null) {
            classId = getIntent().getStringExtra("classId");
            Log.i(TAG, "classId: " + classId);

            Spinner spinner = (Spinner) findViewById(R.id.groups_spinner);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                    EducatorComposeMessage.this,
                    R.array.groups_array,
                    android.R.layout.simple_spinner_item
            );
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.educator_compose_message, menu);
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
