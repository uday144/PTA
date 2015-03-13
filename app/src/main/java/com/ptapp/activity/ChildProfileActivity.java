package com.ptapp.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ptapp.bean.ClassBean;
import com.ptapp.bean.FeeBean;
import com.ptapp.bean.ParentBean;
import com.ptapp.io.model.Student;
import com.ptapp.bean.TransportBean;
import com.ptapp.bo.StudentContextBO;
import com.ptapp.bo.StudentsBO;
import com.ptapp.app.R;
import com.ptapp.app.SchooloApp;
import com.ptapp.utils.CommonConstants;
import com.ptapp.utils.SharedPrefUtil;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ChildProfileActivity extends BaseActivity {
    private static String TAG = "PTApp-ChildProfile ";

    LinearLayout ll_demographics;
    LinearLayout ll_second;
    LinearLayout ll_third;
    LinearLayout ll_fourth;
    LinearLayout ll_fifth;
    LinearLayout ll_sixth;

    TextView txt_class_value, txt_name_value, txt_rollNo_value,
            txt_gender_value, txt_dob_value, txt_hobbies_value,
            txt_email_value, txt_address_value;
    TextView txt_fName_value, txt_fMobile_value, txt_fEmail_value,
            txt_mName_value, txt_mMobile_value, txt_mEmail_value,
            txt_gName_value, txt_gMobile_value, txt_gPhoto_value,
            txt_gEmail_value;
    TextView txt_bloodGrp_value, txt_height_value, txt_weight_value,
            txt_medical_problems_value, txt_medication_needs_value,
            txt_medication_allergies_value, txt_food_allergies_value,
            txt_other_allergies_value, txt_special_dietary_needs_value,
            txt_physicianName_value, txt_physicianPhone_value;
    TextView txt_date_value, txt_status_value;
    TextView txt_receiptNo_value, txt_fee_cycle_value, txt_paidOn_value,
            txt_amount_value;
    TextView txt_route_name_value, txt_stop_name_value, txt_zone_name_value,
            txt_way_value, txt_spoc_value, txt_vehicleNum_value,
            txt_driverName_value, txt_driverPhone_value,
            txt_conductorName_value, txt_conductorPhone_value;
    ImageView iv_picMother, iv_picFather, iv_picGuardian;

    private StudentsBO boStudents;
    private StudentContextBO stuContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_profile);

        // Find the views and initialize the respective variables.
        findViewsByIdFromXML();

        // hide until its title is clicked
        ll_second.setVisibility(View.GONE);
        ll_third.setVisibility(View.GONE);
        ll_fourth.setVisibility(View.GONE);
        ll_fifth.setVisibility(View.GONE);
        ll_sixth.setVisibility(View.GONE);

        try {
            boStudents = ((SchooloApp) getApplicationContext()).getStudentsBO();
            if (boStudents != null) {

                stuContext = boStudents.getStudent(SharedPrefUtil.getPrefLastViewedKidStudentId(ChildProfileActivity.this)).getStudentContextBO(
                        getApplicationContext());

                if (stuContext != null) {
                    ClassBean cls = stuContext.getSchoolClass();
                    if (cls != null) {
                        txt_class_value.setText(cls.getClassName() + " "
                                + cls.getSection());
                    } else {
                        Log.d(TAG, "No class details found.");
                    }

                    ArrayList<ParentBean> parents = stuContext.getLstParents();

                    if (parents != null) {

                        if (parents.get(0).getGender().equalsIgnoreCase("Male")) {

                            txt_fName_value.setText(parents.get(0).getfName()
                                    + " " + parents.get(0).getlName());

                            txt_fMobile_value
                                    .setText(parents.get(0).getPhone());
                            setParentImg(parents.get(0).getImgPath(),
                                    iv_picFather);
                            txt_fEmail_value.setText(parents.get(0).getEmail());
                        } else {
                            txt_mName_value.setText(parents.get(0).getfName()
                                    + " " + parents.get(0).getlName());
                            txt_mMobile_value
                                    .setText(parents.get(0).getPhone());
                            setParentImg(parents.get(0).getImgPath(),
                                    iv_picMother);
                            txt_mEmail_value.setText(parents.get(0).getEmail());
                        }
                        if (parents.get(1).getGender().equalsIgnoreCase("Male")) {
                            txt_fName_value.setText(parents.get(1).getfName()
                                    + " " + parents.get(1).getlName());
                            txt_fMobile_value
                                    .setText(parents.get(1).getPhone());
                            setParentImg(parents.get(1).getImgPath(),
                                    iv_picFather);
                            txt_fEmail_value.setText(parents.get(1).getEmail());
                        } else {
                            txt_mName_value.setText(parents.get(1).getfName()
                                    + " " + parents.get(1).getlName());
                            txt_mMobile_value
                                    .setText(parents.get(1).getPhone());
                            setParentImg(parents.get(1).getImgPath(),
                                    iv_picMother);
                            txt_mEmail_value.setText(parents.get(1).getEmail());
                        }

                        if (parents.size() > 2) {
                            txt_gName_value.setText(parents.get(2).getfName()
                                    + " " + parents.get(2).getlName());
                            txt_gMobile_value
                                    .setText(parents.get(2).getPhone());
                            setParentImg(parents.get(2).getImgPath(),
                                    iv_picGuardian);
                            txt_gEmail_value.setText(parents.get(2).getEmail());
                        }

                    } else {
                        Log.w(TAG, "parents detail is null.");
                    }

                    Student stu = stuContext.getStudent();
                    if (stu != null) {
                        // fill the text values
                        /*txt_name_value.setText(stu.getfName() + " "
                                + stu.getlName());
						txt_rollNo_value.setText(stu.getRollNumber());
						txt_gender_value.setText(stu.getGender());
						txt_dob_value.setText(stu.getDoB());
						txt_hobbies_value.setText(stu.getHobbies());
						txt_email_value.setText(stu.getEmail());
						txt_address_value.setText(stu.getAddress());

						txt_bloodGrp_value.setText(stu.getBloodGroup());
						txt_height_value.setText(stu.getHeight());
						txt_weight_value.setText(stu.getWeight());
						txt_medical_problems_value.setText(stu
								.getMedicalProblems());
						txt_medication_needs_value.setText(stu
								.getMedicationNeeds());
						txt_medication_allergies_value.setText(stu
								.getMedicationAllergies());
						txt_food_allergies_value
								.setText(stu.getFoodAllergies());
						txt_other_allergies_value.setText(stu
								.getOtherAllergies());
						txt_special_dietary_needs_value.setText(stu
								.getSpecialDietaryNeeds());
						txt_physicianName_value.setText(stu.getPhysicianName());
						txt_physicianPhone_value.setText(stu
								.getPhysicianPhone());*/
                    } else {
                        Log.d(TAG, "No Student details found.");
                    }

                    FeeBean fee = stuContext.getFee();
                    if (fee != null) {
                        txt_receiptNo_value.setText(fee.getReceiptNumber());
                        txt_fee_cycle_value.setText(fee.getFeeCycle());
                        txt_paidOn_value.setText(fee.getPaidOn());
                        txt_amount_value.setText(fee.getAmount());
                    } else {
                        Log.d(TAG, "No fee details found.");
                    }

                    TransportBean trs = stuContext.getTransport();
                    if (trs != null) {
                        txt_route_name_value.setText(trs.getRouteName());
                        txt_stop_name_value.setText(trs.getStopName());
                        txt_zone_name_value.setText(trs.getZoneName());
                        txt_way_value.setText(trs.getWay());
                        txt_spoc_value.setText(trs.getSPOCPhoneNumber());
                        txt_vehicleNum_value.setText(trs.getVehicleNumber());
                        txt_driverName_value.setText(trs.getDriverFName() + " "
                                + trs.getDriverLName());
                        txt_driverPhone_value.setText(trs
                                .getDriverPhoneNumber());
                        txt_conductorName_value.setText(trs.getConductorFName()
                                + " " + trs.getConductorLName());
                        txt_conductorPhone_value.setText(trs
                                .getConductorPhoneNumber());
                    } else {
                        Log.d(TAG, "No transport details found.");
                    }

                    // set kid image.
                    setKidProfileImg();

                } else {
                    Log.w(TAG, "Student context is null.");
                }
            } else {
                Log.w(TAG, "StudentsBo is null.");
            }
        } catch (Exception ex) {
            if (ex.getMessage() != null) {
                Log.e(TAG, ex.getMessage());
            } else {
                Log.e(TAG, "No exception message for this error.");
            }
        }
    }

    public void onClickToggleDemographics(View v) {
        ll_demographics.setVisibility(ll_demographics.isShown() ? View.GONE
                : View.VISIBLE);
    }

    public void onClickToggleSecond(View v) {
        ll_second.setVisibility(ll_second.isShown() ? View.GONE : View.VISIBLE);
    }

    public void onClickToggleThird(View v) {
        ll_third.setVisibility(ll_third.isShown() ? View.GONE : View.VISIBLE);
    }

    public void onClickToggleFourth(View v) {
        ll_fourth.setVisibility(ll_fourth.isShown() ? View.GONE : View.VISIBLE);
    }

    public void onClickToggleFifth(View v) {
        ll_fifth.setVisibility(ll_fifth.isShown() ? View.GONE : View.VISIBLE);
    }

    public void onClickToggleSixth(View v) {
        ll_sixth.setVisibility(ll_sixth.isShown() ? View.GONE : View.VISIBLE);
    }

    public void onClickPhonePic(View v) {
        try {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:"
                    + txt_driverPhone_value.getText().toString()));
            startActivity(callIntent);
        } catch (Exception ex) {
            Log.e(TAG, "Call failed: " + ex.getMessage());
        }
    }

    public void onClickSmsPic(View v) {
        Intent smsIntent = new Intent(Intent.ACTION_VIEW);
        smsIntent.setType("vnd.android-dir/mms-sms");
        smsIntent.putExtra("address", txt_driverPhone_value.getText()
                .toString());
        // smsIntent.putExtra("sms_body", "Body of Message");
        startActivity(smsIntent);
    }

    /**
     * Set the captured image to the kid's profile imageview
     */
    private void setKidProfileImg() {
        try {
            ImageView ivKidPicProfile = (ImageView) findViewById(R.id.kid_picProfile);

			/*File f = CommonMethods.getFileImgKidProfile(stuContext.getStudent()
                    .getRollNumber());
			if (f.exists()) {

				Picasso.with(ChildProfileActivity.this) //
						.load(f) //
						.placeholder(CommonConstants.LOADING) //
						.error(CommonConstants.NO_PHOTO_AVAILABLE) //
						.fit() //
						.into(ivKidPicProfile);

			} else {
				Log.i(TAG,
						"Kid's picture inside schoolo folder on sd-card not found.");

				// get the image from link saved in imgPath column of table.
				Picasso.with(ChildProfileActivity.this) //
						.load(stuContext.getStudent().getImgPath()) //
						.placeholder(CommonConstants.LOADING) //
						.error(CommonConstants.NO_PHOTO_AVAILABLE) //
						.fit() //
						.into(ivKidPicProfile);

			}*/
        } catch (Exception ex) {
            if (ex.getMessage() != null) {
                Log.e(TAG, ex.getMessage());
            } else {
                Log.e(TAG, "No exception message for this error.");
            }
        }

    }

    /**
     * Set the captured image to the kid's profile imageview
     */
    private void setParentImg(String imgPath, ImageView ivPic) {

        try {

            Picasso.with(ChildProfileActivity.this) //
                    .load(imgPath) //
                    .placeholder(CommonConstants.LOADING) //
                    .error(CommonConstants.NO_PHOTO_AVAILABLE) //
                    .fit() //
                    .into(ivPic);
        } catch (Exception ex) {
            if (ex.getMessage() != null) {
                Log.e(TAG, ex.getMessage());
            } else {
                Log.e(TAG,
                        "setParentImg - No exception message for this error.");
            }
        }

    }

    /**
     * Find the views in the xml file and initialize the variables.
     */
    private void findViewsByIdFromXML() {
        ll_demographics = (LinearLayout) findViewById(R.id.ll_demographics);
        ll_second = (LinearLayout) findViewById(R.id.ll_second);
        ll_third = (LinearLayout) findViewById(R.id.ll_third);
        ll_fourth = (LinearLayout) findViewById(R.id.ll_fourth);
        ll_fifth = (LinearLayout) findViewById(R.id.ll_fifth);
        ll_sixth = (LinearLayout) findViewById(R.id.ll_sixth);
        txt_class_value = (TextView) findViewById(R.id.txt_class_value);
        txt_name_value = (TextView) findViewById(R.id.txt_name_value);
        txt_rollNo_value = (TextView) findViewById(R.id.txt_rollNo_value);
        txt_gender_value = (TextView) findViewById(R.id.txt_gender_value);
        txt_dob_value = (TextView) findViewById(R.id.txt_dob_value);
        txt_hobbies_value = (TextView) findViewById(R.id.txt_hobbies_value);
        txt_email_value = (TextView) findViewById(R.id.txt_email_value);
        txt_address_value = (TextView) findViewById(R.id.txt_address_value);

        txt_fName_value = (TextView) findViewById(R.id.txt_fName_value);
        txt_fMobile_value = (TextView) findViewById(R.id.txt_fMobile_value);
        iv_picFather = (ImageView) findViewById(R.id.picFather);
        txt_fEmail_value = (TextView) findViewById(R.id.txt_fEmail_value);
        txt_mName_value = (TextView) findViewById(R.id.txt_mName_value);
        txt_mMobile_value = (TextView) findViewById(R.id.txt_mMobile_value);
        iv_picMother = (ImageView) findViewById(R.id.picMother);
        txt_mEmail_value = (TextView) findViewById(R.id.txt_mEmail_value);
        txt_gName_value = (TextView) findViewById(R.id.txt_gName_value);
        txt_gMobile_value = (TextView) findViewById(R.id.txt_gMobile_value);
        iv_picGuardian = (ImageView) findViewById(R.id.picGuardian);
        txt_gEmail_value = (TextView) findViewById(R.id.txt_gEmail_value);

        txt_bloodGrp_value = (TextView) findViewById(R.id.txt_bloodGrp_value);
        txt_height_value = (TextView) findViewById(R.id.txt_height_value);
        txt_weight_value = (TextView) findViewById(R.id.txt_weight_value);
        txt_medical_problems_value = (TextView) findViewById(R.id.txt_medical_problems_value);
        txt_medication_needs_value = (TextView) findViewById(R.id.txt_medication_needs_value);
        txt_medication_allergies_value = (TextView) findViewById(R.id.txt_medication_allergies_value);
        txt_food_allergies_value = (TextView) findViewById(R.id.txt_food_allergies_value);
        txt_other_allergies_value = (TextView) findViewById(R.id.txt_other_allergies_value);
        txt_special_dietary_needs_value = (TextView) findViewById(R.id.txt_special_dietary_needs_value);
        txt_physicianName_value = (TextView) findViewById(R.id.txt_physicianName_value);
        txt_physicianPhone_value = (TextView) findViewById(R.id.txt_physicianPhone_value);

        txt_date_value = (TextView) findViewById(R.id.txt_date_value);
        txt_status_value = (TextView) findViewById(R.id.txt_status_value);

        txt_receiptNo_value = (TextView) findViewById(R.id.txt_receiptNo_value);
        txt_fee_cycle_value = (TextView) findViewById(R.id.txt_fee_cycle_value);
        txt_paidOn_value = (TextView) findViewById(R.id.txt_paidOn_value);
        txt_amount_value = (TextView) findViewById(R.id.txt_amount_value);

        txt_route_name_value = (TextView) findViewById(R.id.txt_route_name_value);
        txt_stop_name_value = (TextView) findViewById(R.id.txt_stop_name_value);
        txt_zone_name_value = (TextView) findViewById(R.id.txt_zone_name_value);
        txt_way_value = (TextView) findViewById(R.id.txt_way_value);
        txt_spoc_value = (TextView) findViewById(R.id.txt_spoc_value);
        txt_vehicleNum_value = (TextView) findViewById(R.id.txt_vehicleNum_value);
        txt_driverName_value = (TextView) findViewById(R.id.txt_driverName_value);
        txt_driverPhone_value = (TextView) findViewById(R.id.txt_driverPhone_value);
        txt_conductorName_value = (TextView) findViewById(R.id.txt_conductorName_value);
        txt_conductorPhone_value = (TextView) findViewById(R.id.txt_conductorPhone_value);

    }

}
