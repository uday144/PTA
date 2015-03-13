package com.ptapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebView;

import com.ptapp.app.R;

public class OpenTosActivity extends Activity {

    private static final String TAG = "PTApp-OpenTosActivity: ";

    WebView webView;
    String className = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_tos);

        webView = (WebView) findViewById(R.id.wv_tos);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        if (getIntent() != null) {
            try {
                className = getIntent().getStringExtra("activity");
                webView.loadUrl(getIntent().getStringExtra("linkToS"));
            } catch (Exception ex) {

            }
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                if (className.equals(AgreeTosActivity.class.getName())) {
                    Intent intent = new Intent(OpenTosActivity.this, AgreeTosActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else if (className.equals(RegistrationSuccessActivity.class.getName())) {
                    Intent intent = new Intent(OpenTosActivity.this, RegistrationSuccessActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                //NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
