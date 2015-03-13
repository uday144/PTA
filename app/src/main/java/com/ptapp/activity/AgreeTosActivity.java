package com.ptapp.activity;

import com.ptapp.activity.util.SystemUiHider;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.ptapp.app.R;
import com.ptapp.utils.CommonConstants;
import com.ptapp.utils.SharedPrefUtil;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class AgreeTosActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_agree_tos);
        if (!SharedPrefUtil.getPrefFirstTimeLaunch(AgreeTosActivity.this)) {
            //if it's not first time launch

            String lastAccessedRole = SharedPrefUtil.getPrefUserInRole(AgreeTosActivity.this);
            if (!lastAccessedRole.isEmpty()) {
                switch (lastAccessedRole) {
                    case CommonConstants.ROLE_STAFF:
                        //show teacher screen
                        Intent intent2 = new Intent(AgreeTosActivity.this, EducatorHomeActivity.class);
                        startActivity(intent2);
                        finish();
                        break;
                    case CommonConstants.ROLE_PARENT:
                        //show parent screen
                        Intent intent = new Intent(AgreeTosActivity.this, ParentHomeActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                }
            }
        } else {
            //if roles string is not empty, open registration success screen(for confirmation)
            if (!SharedPrefUtil.getPrefApiRolesJsonString(AgreeTosActivity.this).isEmpty()) {
                Intent intent = new Intent(AgreeTosActivity.this, RegistrationSuccessActivity.class);
                startActivity(intent);
            } else {
                //if registration phone number & country iso code is not empty, open otp screen
                if (!SharedPrefUtil.getPrefRegistrationPhoneNumber(AgreeTosActivity.this).isEmpty()
                        && !SharedPrefUtil.getPrefIsoCountryCodeToRegisterWithApp(AgreeTosActivity.this).isEmpty()) {
                    Intent intent = new Intent(AgreeTosActivity.this, OTPActivity.class);
                    startActivity(intent);
                }
            }
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    public void onClickTosLink(View v) {

        //open the link directly into browser
        String url = CommonConstants.TOS_LINK_APP_INSTALLATION;
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);


        /*Intent intent = new Intent(AgreeTosActivity.this, OpenTosActivity.class);
        intent.putExtra("linkToS", CommonConstants.TOS_LINK_APP_INSTALLATION);
        intent.putExtra("activity", AgreeTosActivity.class.getName());
        startActivity(intent);*/
    }

    public void onClickAgreeAndContinue(View v) {
        Intent intent = new Intent(AgreeTosActivity.this, VerifyPhoneActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
        finish();
    }
}
