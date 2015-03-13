package com.ptapp.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.ptapp.bean.api.SchoolsBean;
import com.ptapp.app.R;
import com.ptapp.utils.ApiMethodsUtils;
import com.ptapp.utils.SharedPrefUtil;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class CreateFirstClassActivity extends Activity {

    private static final String TAG = "PTApp-CreateFirstClassActivity: ";
    private Spinner title;
    private String selectedTitle;
    private String[] schools;
    private AutoCompleteTextView vwCenter;
    private EditText className, subject, teacherFName, teacherLName, classTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_first_class);

        //find views
        title = (Spinner) findViewById(R.id.spin_title);
        vwCenter = (AutoCompleteTextView) findViewById(R.id.et_center_name);
        className = (EditText) findViewById(R.id.et_class_name);
        subject = (EditText) findViewById(R.id.et_subject_name);
        teacherFName = (EditText) findViewById(R.id.et_teacher_fname);
        teacherLName = (EditText) findViewById(R.id.et_teacher_lname);
        classTitle = (EditText) findViewById(R.id.et_class_title);

        //load titles
        setSpinnerTitle();

        //getSchools from sharedPref
        String jsonSchools = SharedPrefUtil.getPrefSchoolsBeanJsonString(CreateFirstClassActivity.this);
        if (jsonSchools.isEmpty()) {
            new FetchSchoolsTask().execute(""); //getSchools from the server

        } else {
            //De-serializing the String to Object
            SchoolsBean schoolsBean = new Gson().fromJson(jsonSchools, SchoolsBean.class);
            schools = schoolsBean.getSchools();

            if (schools != null && vwCenter != null) {
                setSchoolsAdapter();
            }
        }
    }

    /**
     * Sets the string adapter for AutoCompleteTextView dropdown and builds a popup to show on click of any dropdown item
     */
    private void setSchoolsAdapter() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(CreateFirstClassActivity.this, android.R.layout.simple_list_item_1, schools);
        vwCenter.setAdapter(adapter);

        vwCenter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                Log.i(TAG, "selected item: " + adapterView.getItemAtPosition(pos).toString());

                AlertDialog.Builder builder = new AlertDialog.Builder(CreateFirstClassActivity.this);
                builder.setMessage(R.string.registered_with_school)
                        .setCancelable(false)
                        .setPositiveButton("Correct Number", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                Intent intent = new Intent(CreateFirstClassActivity.this, VerifyPhoneActivity.class);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("Alter School Name", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        })
                        .create().show();
            }
        });
    }

    /**
     * Sets the array adapter to fill the spinner with 'Titles' data and adds a listener on Spinner Item selection
     */
    private void setSpinnerTitle() {

        ArrayAdapter<CharSequence> dataAdapter = ArrayAdapter.createFromResource(CreateFirstClassActivity.this, R.array.titles, android.R.layout.simple_spinner_item);
        dataAdapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);
        title.setAdapter(dataAdapter);

        // Spinner item selection Listener
        addListenerOnSpinnerItemSelection();
    }

    /**
     * stores the selected item of the 'Titles' spinner in a variable
     */
    public void addListenerOnSpinnerItemSelection() {

        title.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                selectedTitle = parent.getItemAtPosition(pos).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void onClickContinue(View v) {

        //TODO: store the info int local db first and then send it to the server
        if (!vwCenter.getText().toString().isEmpty() && !className.getText().toString().isEmpty() && !subject.getText().toString().isEmpty() &&
                !teacherFName.getText().toString().isEmpty() && !teacherLName.getText().toString().isEmpty() &&
                !classTitle.getText().toString().isEmpty()) {

            new CreateClassTask().execute("");
        }
    }

    public void onClickValidateNum(View v) {

        Intent intent = new Intent(CreateFirstClassActivity.this, VerifyPhoneActivity.class);
        startActivity(intent);
    }

    private class CreateClassTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {

            try {
                ApiMethodsUtils.sendCreateClass(getResources().getString(R.string.first_part_api_link) + "/1",
                        vwCenter.getText().toString(), className.getText().toString(), subject.getText().toString(),
                        selectedTitle, teacherFName.getText().toString(), teacherLName.getText().toString(),
                        classTitle.getText().toString(), new Callback<Response>() {
                            @Override
                            public void success(Response response, Response response2) {

                                Intent intent = new Intent(CreateFirstClassActivity.this, IndividualEducatorHome.class);
                                startActivity(intent);
                            }

                            @Override
                            public void failure(RetrofitError retrofitError) {
                                Log.wtf(TAG, "Unknown retrofit error: " + retrofitError.getMessage());
                            }
                        });
            } catch (RetrofitError retrofitError) {
                final String exMsg = retrofitError.getMessage();
                Log.w(TAG, "Retrofit error(CreateClassTask): " + exMsg);

            } catch (Exception ex) {
                Log.w(TAG, "Error(CreateClassTask): " + ex.getMessage());
            }
            return null;
        }
    }


    /**
     * Fetch schools from the server using retrofit api
     */
    private class FetchSchoolsTask extends
            AsyncTask<String, Void, SchoolsBean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected SchoolsBean doInBackground(String... arg0) {

            SchoolsBean ff = null;
            try {
                ff = ApiMethodsUtils.fetchSchools(getResources().getString(R.string.first_part_api_link) + "/1", "");

            } catch (RetrofitError retrofitError) {

                final String exMsg = retrofitError.getMessage();
                Log.w(TAG, "Retrofit error: " + exMsg);
                final Response r = retrofitError.getResponse();

            } catch (Exception ex) {
                Log.w(TAG, "Retrofit error(SendOTPTask): " + ex.getMessage());
            }
            return ff;
        }

        protected void onPostExecute(SchoolsBean result) {

            if (result != null) {

                //Serializing the Object to JSON string
                String schoolsJSON = new Gson().toJson(result);
                //Storing this JSON string into SharedPreferences
                SharedPrefUtil.setPrefSchoolsBeanJsonString(CreateFirstClassActivity.this, schoolsJSON);

                //fill schools array
                schools = result.getSchools();
                if (schools != null && vwCenter != null) {
                    setSchoolsAdapter();
                }

                for (String s : schools) {
                    Log.i(TAG, "school: " + s);
                }
            }
        }
    }
}
