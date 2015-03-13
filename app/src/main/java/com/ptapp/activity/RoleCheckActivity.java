package com.ptapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ptapp.bean.api.RolesBean;
import com.ptapp.app.R;
import com.ptapp.utils.ApiMethodsUtils;
import com.ptapp.utils.CommonMethods;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

//TODO: Problem in RoleCheckActivity:
/*If we enter something in I'm a Parent/student, a any number, then that number gets stored in the sharedPref,
that it overwrites the number used in the VerifyPhone screen. So, in this case the OTP 1233 doesn't match on the server.

 Need: to consider when to save the phone number in the SharedPref./ save the verifyphone number in a variable,
 incase user opt to go through I'm a teacher flow.*/
public class RoleCheckActivity extends Activity {

    private static final String TAG = "PTApp-RoleCheckActivity: ";

    private Spinner country;
    private String selectedCountry, enteredPhoneNumber, selectedISOCode = "";
    private EditText countryCode, phoneNumber;
    private TextView errorMsg;
    private ProgressBar loadingP, loadingT;
    LinearLayout teacherSection, parentSection;

    private EditText otpChar1, otpChar2, otpChar3, otpChar4;
    private String enteredOTP = "";
    private LinearLayout otpCode;
    private TextView showMsg, otpPhoneText;


    ArrayList<String> codes = new ArrayList<String>();
    ArrayList<String> isoCodes = new ArrayList<String>();
    ArrayList<String> countries = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_role_check);

        findViews();

        //set the text, with the phone number used to register with the app
        //otpPhoneText.setText(getResources().getString(R.string.rc_text1a) + " " + SharedPrefUtil.getPrefPhoneUsedToRegisterWithApp(RoleCheckActivity.this) + " " + getResources().getString(R.string.rc_text1b));

        addTextChangedListenerOnOtpChar1();
        addTextChangedListenerOnOtpChar2();
        addTextChangedListenerOnOtpChar3();

        //load countries and set the default country
        setCountrySpinner();
    }


    public void onClickTeacher(View v) {
        teacherSection.setVisibility(View.VISIBLE);
        parentSection.setVisibility(View.GONE);

        //set focus of cursor
        otpChar1.requestFocus();
    }

    public void onClickParent(View v) {
        parentSection.setVisibility(View.VISIBLE);
        teacherSection.setVisibility(View.GONE);

        //set the focus of cursor
        phoneNumber.requestFocus();
    }

    /**
     * Load countries with their ISD codes from the strings.xml into spinner
     */
    private void setCountrySpinner() {
        TypedArray countriesCodes = getResources().obtainTypedArray(R.array.countries);

        for (int i = 0; i < countriesCodes.length(); ++i) {
            int id = countriesCodes.getResourceId(i, 0);
            codes.add(getResources().getStringArray(id)[0]);
            isoCodes.add(getResources().getStringArray(id)[1]);
            countries.add(getResources().getStringArray(id)[2]);
        }
        countriesCodes.recycle(); //Important!


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item, countries);
        dataAdapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);
        country.setAdapter(dataAdapter);

        // Spinner item selection Listener
        addListenerOnSpinnerItemSelection();

        //set the default country
        //TODO: getting the default country from the device
        country.setSelection(dataAdapter.getPosition("India"));
    }

    /**
     * Loads the ISD code associated with the country, on it's selection
     */
    public void addListenerOnSpinnerItemSelection() {

        final ArrayList<String> fCodes = codes;
        final ArrayList<String> fISOCodes = isoCodes;
        final ArrayList<String> fCountries = countries;

        final EditText fCountryCode = countryCode;

        country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                selectedCountry = parent.getItemAtPosition(pos).toString();
                int selectedPos = fCountries.indexOf(selectedCountry);
                String associatedCode = fCodes.get(selectedPos);
                selectedISOCode = fISOCodes.get(selectedPos);

                fCountryCode.setText(associatedCode);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void onClickOKPar(View v) {

        if (errorMsg.getVisibility() == View.VISIBLE) {
            errorMsg.setVisibility(View.GONE);
        }

        if (!phoneNumber.getText().toString().isEmpty()) {
            Log.i(TAG, "OK clicked, sending phone number to server ...");


            //close the soft keyboard
            CommonMethods.closeKeyboard(phoneNumber, RoleCheckActivity.this);

            enteredPhoneNumber = phoneNumber.getText().toString();
            //SharedPrefUtil.setPrefPhoneUsedToRegisterWithApp(RoleCheckActivity.this, enteredPhoneNumber);

            try {
                if (CommonMethods.isInternetConnected(RoleCheckActivity.this, TAG)) {
                    new SendVerifyPhoneTask().execute("");
                } else {
                    Toast.makeText(RoleCheckActivity.this, "No internet connection available", Toast.LENGTH_LONG).show();
                }
            } catch (Exception ex) {
                if (ex.getMessage() != null) {
                    Log.e(TAG, ex.getMessage());
                } else {
                    Log.e(TAG,
                            "onClickOKPar - No exception message for this error.");
                }
            }
        }
    }

    private class SendVerifyPhoneTask extends
            AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if (loadingP != null) {
                loadingP.setVisibility(View.VISIBLE);
            }
        }

        @Override
        protected Void doInBackground(String... arg0) {
            try {
                ApiMethodsUtils.sendVerifyPhone(getResources().getString(
                                R.string.first_part_api_link)
                                + "/1",
                        selectedISOCode,
                        enteredPhoneNumber,
                        new Callback<Response>() {

                            @Override
                            public void success(Response response, Response response2) { //Registered flow
                                if (loadingP != null) {
                                    loadingP.setVisibility(View.INVISIBLE);
                                }
                                Intent intent = new Intent(RoleCheckActivity.this, OTPActivity.class);
                                startActivity(intent);
                            }

                            @Override
                            public void failure(RetrofitError retrofitError) {

                                if (loadingP != null) {
                                    loadingP.setVisibility(View.INVISIBLE);
                                }
                                Response r = retrofitError.getResponse();
                                if (r != null) {
                                    if (r.getStatus() == 401) { //Not Registered flow
                                        Log.e(TAG, "Error code: 401");

                                        /*Intent intent = new Intent(RoleCheckActivity.this, RoleCheckActivity.class);
                                        startActivity(intent);*/
                                    }
                                    if (r.getStatus() == 404) { //Api link not found.
                                        Log.e(TAG, "Error code: 404, Api link not found.");
                                        errorMsg.setText("Oops! :( Error encountered: 404.");
                                        errorMsg.setVisibility(View.VISIBLE);
                                    }
                                } else {
                                    Log.wtf(TAG, "Unknown retrofit error: " + retrofitError.getMessage());
                                    errorMsg.setText("Oops! :( Error encountered.");
                                    errorMsg.setVisibility(View.VISIBLE);
                                }
                            }
                        });
            } catch (Exception ex) {
                Log.w(TAG, "Error(SendVerifyPhoneTask): " + ex.getMessage());

                errorMsg.setText("Oops! :( Error encountered.");
                errorMsg.setVisibility(View.VISIBLE);
            }
            return null;
        }
    }


    //teacher section code

    /**
     * On text changed,  move cursor's focus to the next EditText
     */
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

    /**
     * On text changed,  move cursor to the next EditText
     */
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

    /**
     * On text changed,  move cursor to the next EditText
     */
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

    public void onClickOtpOK(View v) {

        Log.i(TAG, "OK clicked, sending otp to server ...");

        if (!otpChar1.getText().toString().isEmpty()
                && !otpChar2.getText().toString().isEmpty()
                && !otpChar3.getText().toString().isEmpty()
                && !otpChar4.getText().toString().isEmpty()) {
            Log.i(TAG, "OK clicked, sending otp code to server ...");


            //close the soft keyboard
            EditText focusedView = (EditText) otpCode.getFocusedChild();
            CommonMethods.closeKeyboard(focusedView, RoleCheckActivity.this);

            enteredOTP = otpChar1.getText().toString()
                    + otpChar2.getText().toString()
                    + otpChar3.getText().toString()
                    + otpChar4.getText().toString();


            try {
                if (CommonMethods.isInternetConnected(RoleCheckActivity.this, TAG)) {
                    new SendOTPTask().execute("");
                } else {
                    Toast.makeText(RoleCheckActivity.this, "No internet connection available", Toast.LENGTH_LONG).show();
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

    private class SendOTPTask extends
            AsyncTask<String, Void, RolesBean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if (loadingT != null) {
                loadingT.setVisibility(View.VISIBLE);
            }
        }

        @Override
        protected RolesBean doInBackground(String... arg0) {

            RolesBean ff = null;
            try {
                /*ff = ApiMethodsUtils.sendOTPCode(getResources().getString(
                                R.string.first_part_api_link)
                                + "/1",
                        SharedPrefUtil.getPrefIsoCountryCodeToRegisterWithApp(RoleCheckActivity.this),
                        "",//SharedPrefUtil.getPrefPhoneUsedToRegisterWithApp(RoleCheckActivity.this),
                        enteredOTP);*/
            } catch (RetrofitError retrofitError) {
                final String exMsg = retrofitError.getMessage();
                Log.w(TAG, "Retrofit error: " + exMsg);
                final Response r = retrofitError.getResponse();

                //We cannot touch UI views from any other thread than which originally created its views.
                //"CalledFromWrongThreadException" - Only the original thread that created a view hierarchy can touch its views.
                RoleCheckActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (loadingT != null) {
                            loadingT.setVisibility(View.INVISIBLE);
                        }
                        if (r != null) {
                            if (r.getStatus() == 401) { //OTP didn't match
                                Log.e(TAG, "Error code: 401");
                                showMsg.setText(R.string.otp_not_matched);
                                showMsg.setVisibility(View.VISIBLE);
                            }
                            if (r.getStatus() == 404) { //Api link not found.
                                Log.e(TAG, "Error code: 404, Api link not found.");
                                showMsg.setText("Oops! :( Error encountered: 404.");
                                showMsg.setVisibility(View.VISIBLE);
                            }
                        } else {
                            Log.wtf(TAG, "Unknown retrofit error: " + exMsg);
                            showMsg.setText("Oops! :( Error encountered.");
                            showMsg.setVisibility(View.VISIBLE);
                        }
                    }
                });
            } catch (Exception ex) {
                Log.w(TAG, "Retrofit error(SendOTPTask): " + ex.getMessage());
            }
            return ff;
        }

        protected void onPostExecute(RolesBean result) {

            if (loadingT != null) {
                loadingT.setVisibility(View.INVISIBLE);
            }

            if (result != null) {

                Intent intent = new Intent(RoleCheckActivity.this, CreateFirstClassActivity.class);
                startActivity(intent);
                /*showMsg.setText(result.getMsgSuccess());
                showMsg.setVisibility(View.VISIBLE);*/

            } else {
                Log.wtf(TAG, "SendOTPTask, 'onPostExecute' returned null.");
            }

        }
    }

    private void findViews() {
        teacherSection = (LinearLayout) findViewById(R.id.teacher_section);
        parentSection = (LinearLayout) findViewById(R.id.parent_section);
        phoneNumber = (EditText) findViewById(R.id.phone_number);
        countryCode = (EditText) findViewById(R.id.country_code);
        country = (Spinner) findViewById(R.id.spin_country);
        errorMsg = (TextView) findViewById(R.id.error_message);
        loadingP = (ProgressBar) findViewById(R.id.progress_bar);

        //otp views
        otpChar1 = (EditText) findViewById(R.id.otp_char1);
        otpChar2 = (EditText) findViewById(R.id.otp_char2);
        otpChar3 = (EditText) findViewById(R.id.otp_char3);
        otpChar4 = (EditText) findViewById(R.id.otp_char4);
        otpCode = (LinearLayout) findViewById(R.id.otp_code);
        loadingT = (ProgressBar) findViewById(R.id.progress_bar_t);
        showMsg = (TextView) findViewById(R.id.show_msg);
        otpPhoneText = (TextView) findViewById(R.id.otp_phone_text);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.role_check, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        /*if (id == R.id.action_settings) {
            return true;
        }*/
        return super.onOptionsItemSelected(item);
    }
}
