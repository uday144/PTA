package com.ptapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ptapp.bean.api.RolesBean;
import com.ptapp.bo.Role;
import com.ptapp.app.R;
import com.ptapp.utils.ApiMethodsUtils;
import com.ptapp.utils.CommonConstants;
import com.ptapp.utils.CommonMethods;
import com.ptapp.utils.SharedPrefUtil;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class OTPActivity extends Activity {

    private static final String TAG = "PTApp-OTPActivity: ";
    private EditText otpChar1, otpChar2, otpChar3, otpChar4, otpChar5, otpChar6;
    private String enteredOTP = "";
    private LinearLayout succussSection, otpCode, btnContinue;
    private ProgressBar loading;
    private TextView showMsg, teacherMsg, parentMsg, studentMsg, receiveSmsText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        otpChar1 = (EditText) findViewById(R.id.otp_char1);
        otpChar2 = (EditText) findViewById(R.id.otp_char2);
        otpChar3 = (EditText) findViewById(R.id.otp_char3);
        otpChar4 = (EditText) findViewById(R.id.otp_char4);
        otpChar5 = (EditText) findViewById(R.id.otp_char5);
        otpChar6 = (EditText) findViewById(R.id.otp_char6);
        otpCode = (LinearLayout) findViewById(R.id.otp_code);
        receiveSmsText = (TextView) findViewById(R.id.receive_sms_text);
        succussSection = (LinearLayout) findViewById(R.id.success_section);
        loading = (ProgressBar) findViewById(R.id.progress_bar);
        showMsg = (TextView) findViewById(R.id.show_msg);
        parentMsg = (TextView) findViewById(R.id.p_msg);
        teacherMsg = (TextView) findViewById(R.id.t_msg);
        studentMsg = (TextView) findViewById(R.id.s_msg);
        btnContinue = (LinearLayout) findViewById(R.id.btn_continue);

        //up button
        getActionBar().setDisplayHomeAsUpEnabled(true);

        String strReceive = getString(R.string.receive_sms_line1)
                + " " + SharedPrefUtil.getPrefRegistrationPhoneNumber(OTPActivity.this)
                + " " + getString(R.string.receive_sms_line2);
        if (receiveSmsText != null) {
            receiveSmsText.setText(strReceive);
        }


        addTextChangedListenerOnOtpChar1();
        addTextChangedListenerOnOtpChar2();
        addTextChangedListenerOnOtpChar3();
        addTextChangedListenerOnOtpChar4();
        addTextChangedListenerOnOtpChar5();

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    private void addTextChangedListenerOnOtpChar1() {
        otpChar1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (otpChar1.getText().length() == 1) {
                    otpChar2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void addTextChangedListenerOnOtpChar2() {
        otpChar2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (otpChar2.getText().length() == 1) {
                    otpChar3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void addTextChangedListenerOnOtpChar3() {
        otpChar3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (otpChar3.getText().length() == 1) {
                    otpChar4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void addTextChangedListenerOnOtpChar4() {
        otpChar4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (otpChar4.getText().length() == 1) {
                    otpChar5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void addTextChangedListenerOnOtpChar5() {
        otpChar5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (otpChar5.getText().length() == 1) {
                    otpChar6.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void onClickOtpOK(View v) {

        Log.i(TAG, "OK clicked, sending otp to server ...");

        if (!otpChar1.getText().toString().isEmpty()
                && !otpChar2.getText().toString().isEmpty()
                && !otpChar3.getText().toString().isEmpty()
                && !otpChar4.getText().toString().isEmpty()
                && !otpChar5.getText().toString().isEmpty()
                && !otpChar6.getText().toString().isEmpty()) {
            Log.i(TAG, "OK clicked, sending otp code to server ...");


            //close the soft keyboard
            EditText focusedView = (EditText) otpCode.getFocusedChild();
            CommonMethods.closeKeyboard(focusedView, OTPActivity.this);

            enteredOTP = otpChar1.getText().toString()
                    + otpChar2.getText().toString()
                    + otpChar3.getText().toString()
                    + otpChar4.getText().toString()
                    + otpChar5.getText().toString()
                    + otpChar6.getText().toString();


            try {
                if (CommonMethods.isInternetConnected(OTPActivity.this, TAG)) {
                    new SendOTPTask().execute("");
                } else {
                    Toast.makeText(OTPActivity.this, "No internet connection available", Toast.LENGTH_LONG).show();
                }
            } catch (Exception ex) {
                if (ex.getMessage() != null) {
                    Log.e(TAG, ex.getMessage());
                } else {
                    Log.e(TAG,
                            "onClickOK - No exception message for this error.");
                }
            }
        }
    }

    public void onClickContinue(View v) {

        Intent intent = new Intent(OTPActivity.this, RegistrationSuccessActivity.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }

    private class SendOTPTask extends
            AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if (loading != null) {
                loading.setVisibility(View.VISIBLE);
            }
        }

        @Override
        protected Void doInBackground(String... arg0) {

            try {
                ApiMethodsUtils.sendOTPCode(getResources().getString(R.string.first_part_api_link),
                        SharedPrefUtil.getPrefIsoCountryCodeToRegisterWithApp(OTPActivity.this),
                        SharedPrefUtil.getPrefRegistrationPhoneNumber(OTPActivity.this),
                        enteredOTP,
                        new Callback<RolesBean>() {
                            @Override
                            public void success(RolesBean result, Response response2) {
                                showMsg.setVisibility(View.GONE);
                                if (loading != null) {
                                    loading.setVisibility(View.INVISIBLE);
                                }
                                if (result != null) {
                                    if (succussSection.getVisibility() == View.INVISIBLE) {
                                        succussSection.setVisibility(View.VISIBLE);
                                    }
                                    if (btnContinue.getVisibility() == View.INVISIBLE) {
                                        btnContinue.setVisibility(View.VISIBLE);
                                    }

                                    ArrayList<Role> roles = result.getRoles();
                                    if (roles != null) {
                                        for (Role r : roles) {
                                            if (r.getRole().equals(CommonConstants.ROLE_PARENT)) {
                                                parentMsg.setText(r.getDisplayMsg());
                                                parentMsg.setVisibility(View.VISIBLE);
                                            }
                                            if (r.getRole().equals(CommonConstants.ROLE_STAFF)) {
                                                teacherMsg.setText(r.getDisplayMsg());
                                                teacherMsg.setVisibility(View.VISIBLE);
                                            }
                                            if (r.getRole().equals(CommonConstants.ROLE_STUDENT)) {
                                                studentMsg.setText(r.getDisplayMsg());
                                                studentMsg.setVisibility(View.VISIBLE);
                                            }
                                        }
                                    }

                                    /*showMsg.setText(result.getSuccessMsg());*/
                                    //TODO:set the new flag, IsDataVerified=true, int the two places inside json string
                                    //one is at Role level(for teacher)
                                    //and other is at inside Children.
                                    //This flag will be set to false if user submits the wrong data, depending upon
                                    //if one of the kid's data was wrong or full data wrong.

                                    //Serializing the Object to JSON string
                                    String rolesJSON = new Gson().toJson(result);
                                    //Storing this JSON string into SharedPreferences
                                    SharedPrefUtil.setPrefApiRolesJsonString(OTPActivity.this, rolesJSON);
                                }
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                if (loading != null) {
                                    loading.setVisibility(View.INVISIBLE);
                                }

                                if (succussSection.getVisibility() == View.INVISIBLE) {
                                    succussSection.setVisibility(View.VISIBLE);
                                }
                                if (btnContinue.getVisibility() == View.VISIBLE) {
                                    btnContinue.setVisibility(View.INVISIBLE);
                                }
                                Response r = error.getResponse();
                                String errorMessage = "";
                                if (r != null) {
                                    if (r.getStatus() == 401) { //OTP didn't match
                                        Log.e(TAG, "Error code: 401");
                                        errorMessage = getString(R.string.otp_not_matched);
                                    }
                                    if (r.getStatus() == 404) { //Api link not found.
                                        Log.e(TAG, "Error code: 404");
                                        errorMessage = r.getReason();
                                    }
                                } else {
                                    Log.wtf(TAG, "Unknown error: " + error.getMessage());
                                    errorMessage = error.getMessage();
                                }
                                if (!errorMessage.isEmpty()) {
                                    showMsg.setText(errorMessage);

                                    showMsg.setVisibility(View.VISIBLE);
                                    parentMsg.setVisibility(View.GONE);
                                    teacherMsg.setVisibility(View.GONE);
                                    studentMsg.setVisibility(View.GONE);
                                }
                            }
                        });
            } catch (Exception ex) {
                Log.w(TAG, "Error(SendVerifyPhoneTask): " + ex.getMessage());
            }
            return null;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(OTPActivity.this, VerifyPhoneActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                Intent intent = new Intent(OTPActivity.this, VerifyPhoneActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }
}
