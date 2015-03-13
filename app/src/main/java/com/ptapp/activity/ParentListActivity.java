package com.ptapp.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.ptapp.dao.ParentDAO;
import com.ptapp.app.R;

public class ParentListActivity extends Activity {
    private String TAG = "PTApp - ParentListActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_list);

        showParents();
    }

    public void showParents() {

        TextView tv_noEvents = (TextView) findViewById(R.id.tv_no_events);

        ParentDAO daoParent = new ParentDAO(ParentListActivity.this);
        /*final ArrayList<ParentBean> lstParents = daoParent.getParents();

        Log.v(TAG, "number of profiles:" + lstParents.size());

        if (lstParents.size() > 0) {

            // Setting list-view adapter.
            final ListView lv1 = (ListView) findViewById(R.id.list_parent);
            lv1.setAdapter(new ParentsLvAdapter(ParentListActivity.this,
                    lstParents));

            tv_noEvents.setText(null); // Set "no events found" text null.
            tv_noEvents.setVisibility(View.GONE);

        } else {
            tv_noEvents.setText(R.string.no_contacts_found);
            tv_noEvents.setVisibility(View.VISIBLE);
        }*/
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.parent_list, menu);
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
