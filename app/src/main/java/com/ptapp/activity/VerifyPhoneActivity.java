package com.ptapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ptapp.bean.api.ResponseMessage;
import com.ptapp.app.R;
import com.ptapp.utils.ApiMethodsUtils;
import com.ptapp.utils.CommonMethods;
import com.ptapp.utils.SharedPrefUtil;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.ptapp.utils.LogUtils.LOGE;
import static com.ptapp.utils.LogUtils.LOGI;
import static com.ptapp.utils.LogUtils.LOGW;

public class VerifyPhoneActivity extends Activity {

    private static final String TAG = "VerifyPhoneActivity: ";
    private Spinner country;
    private String selectedCountry, enteredPhoneNumber, selectedISOCode = "";
    private EditText countryCode, phoneNumber;
    private TextView errorMsg;
    private ProgressBar loading;

    ArrayList<String> codes = new ArrayList<String>();
    ArrayList<String> isoCodes = new ArrayList<String>();
    ArrayList<String> countries = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone);

        phoneNumber = (EditText) findViewById(R.id.phone_number);
        countryCode = (EditText) findViewById(R.id.country_code);
        country = (Spinner) findViewById(R.id.spin_country);
        errorMsg = (TextView) findViewById(R.id.error_message);
        loading = (ProgressBar) findViewById(R.id.progress_bar);

        //set the focus of cursor
        phoneNumber.requestFocus();

        //load countries and set the default country
        setCountrySpinner();

        //show soft keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

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

    // Add spinner data
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


    public void onClickOK(View v) {

        if (errorMsg.getVisibility() == View.VISIBLE) {
            errorMsg.setVisibility(View.GONE);
        }

        if (!phoneNumber.getText().toString().isEmpty()) {

            //close the soft keyboard
            CommonMethods.closeKeyboard(phoneNumber, VerifyPhoneActivity.this);
            enteredPhoneNumber = phoneNumber.getText().toString();

            if (enteredPhoneNumber.length() != 10) {
                errorMsg.setText("Phone number should be of 10 digits.");
                errorMsg.setVisibility(View.VISIBLE);
            } else {
                try {
                    if (CommonMethods.isInternetConnected(VerifyPhoneActivity.this, TAG)) {
                        Log.i(TAG, "Sending phone number to server for verification ...");
                        new SendVerifyPhoneTask().execute("");
                    } else {
                        Toast.makeText(VerifyPhoneActivity.this, "No internet connection available", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception ex) {
                    if (ex.getMessage() != null) {
                        Log.e(TAG, ex.getMessage());
                        errorMsg.setText(ex.getMessage());
                    } else {
                        Log.e(TAG,
                                "onClickOK - No exception message for this error.");
                    }
                }
            }
        }
    }


    private class SendVerifyPhoneTask extends
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
                ApiMethodsUtils.sendVerifyPhone(getResources().getString(R.string.first_part_api_link),
                        selectedISOCode,
                        enteredPhoneNumber,
                        new Callback<Response>() {
                            @Override
                            public void success(Response response, Response response2) { //Registered flow
                                if (loading != null) {
                                    loading.setVisibility(View.INVISIBLE);
                                }
                                SharedPrefUtil.setPrefRegistrationPhoneNumber(VerifyPhoneActivity.this, enteredPhoneNumber);
                                SharedPrefUtil.setPrefIsoCountryCodeToRegisterWithApp(VerifyPhoneActivity.this, selectedISOCode);

                                Intent intent = new Intent(VerifyPhoneActivity.this, OTPActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                startActivity(intent);
                            }

                            @Override
                            public void failure(RetrofitError retrofitError) {
                                //TODO:Showing user understandable messages
                                if (loading != null) {
                                    loading.setVisibility(View.INVISIBLE);
                                }

                                try {
                                    ResponseMessage msg = (ResponseMessage) retrofitError.getBodyAs(ResponseMessage.class);
                                    if (msg != null) {
                                        if (msg.getMessage() != null) {
                                            errorMsg.setText(msg.getMessage());
                                            Log.w(TAG, "Error: " + msg.getMessage());
                                        } else {
                                            Log.w(TAG, "An error response(body) with no error message.");
                                            errorMsg.setText("Ops! An unknown error has come up.");
                                        }
                                    } else {
                                        Log.w(TAG, "There is no response body for the error.");
                                        errorMsg.setText("Ops! Unexpected error has come up.");
                                    }
                                } catch (RuntimeException rex) {
                                    Log.wtf(TAG, "Unable to convert the response body to the specified type/object.");
                                    errorMsg.setText("Ops! Unexpected error has come up.");
                                } catch (Exception ex) {
                                    if (ex.getMessage() != null) {
                                        Log.wtf(TAG, "Unknown general exception has come up - " + ex.getMessage());
                                    } else {
                                        Log.wtf(TAG, "Unknown general exception has come up.");
                                    }
                                    errorMsg.setText("Ops! Unexpected error has come up.");
                                }
                                errorMsg.setVisibility(View.VISIBLE);

                                //Another way of getting response into a string.
                                /*String errJson = new String(((TypedByteArray) retrofitError.getResponse().getBody()).getBytes());*/

                                /*if (!errJson.isEmpty()) {
                                    Log.w(TAG, errJson);
                                    errorMsg.setText(errJson);
                                } else {
                                    if (!retrofitError.getMessage().isEmpty()) {
                                        Log.w(TAG, "error: " + retrofitError.getMessage());
                                    }
                                    if (retrofitError.getCause() != null) {
                                        Log.w(TAG, "error: " + retrofitError.getCause().getMessage());
                                    }
                                    errorMsg.setText("Ops! Unknown error has occurred.");
                                }*/

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

//    @Override
//    public void onBackPressed() {
//        //Intent intent = new Intent(VerifyPhoneActivity.this, AgreeTosActivity.class);
//        //startActivity(intent);
//    }
}
