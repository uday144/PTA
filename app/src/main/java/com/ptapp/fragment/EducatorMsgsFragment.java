package com.ptapp.fragment;

import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.ptapp.activity.EducatorMsgsActivity;
import com.ptapp.adapter.EducatorCourseMsgsLvAdapter;
import com.ptapp.bean.ParentBean;
import com.ptapp.io.model.Student;
import com.ptapp.dao.StudentDAO;
//import com.ptapp.provider.PTAppContract.Students;
import com.ptapp.app.R;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import static com.ptapp.utils.LogUtils.makeLogTag;

/**
 * Loads the list of students
 */
public class EducatorMsgsFragment extends Fragment {

    private static final String TAG = makeLogTag(EducatorMsgsFragment.class);

    /*Msgs of which class-course*/
    //private String classId = "";
    private String studentId = "";
    ListView lv1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);    //necessary to show action menu from fragment
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_educator_msgs1, container, false);
        lv1 = (ListView) rootView.findViewById(R.id.list_students);
        TextView tv_Empty = (TextView) rootView.findViewById(R.id.tv_empty);

        studentId = ((EducatorMsgsActivity) getActivity()).studentId;
        if (studentId != null) {

            ArrayList<Student> students = new ArrayList<Student>();
            Cursor c = null;
            try {

                /*Uri studentUri = Students.buildStudentUri(studentId);
                Log.d(TAG, "studentUri: " + studentUri);
                c = getActivity().getContentResolver().query(studentUri, null, null, null, null);
                if (c.moveToFirst()) {
                    do {
                        Student student = setStudent(c);
                        students.add(student);
                    }while (c.moveToNext());
                }*/
            } catch (Exception ex) {
                Log.e(TAG, ex.getMessage());
            } finally {
                c.close(); // Closing the cursor
            }

            if(students.size()>0){
                lv1.setAdapter(new EducatorCourseMsgsLvAdapter(getActivity(), students));
                if (tv_Empty != null) {
                    tv_Empty.setText(null); // Set "no students found" text null.
                    tv_Empty.setVisibility(View.GONE);
                }
            }else {
                lv1.setAdapter(null);
                if (tv_Empty != null) {
                    tv_Empty.setText("nothing_found");
                    tv_Empty.setVisibility(View.VISIBLE);
                }
                Log.d(TAG, "No students found for this course.");
            }
            // Get class's students along with their parents
            //ArrayList<ParentBean> lstPar = new ArrayList<ParentBean>();
            //LinkeedHashMap retains the iteration order(in which items are put into it).
            LinkedHashMap<ParentBean, Student> hashPar = new LinkedHashMap<ParentBean, Student>();
            StudentDAO dcStu = new StudentDAO(getActivity());
            /*ArrayList<StudentBean> lstStu = dcStu.getStudentsOfClass(classId);

            if (lstStu.size() > 0) {
                for (StudentBean stu : lstStu) {
                    //find it's parents

                    ParentDAO dcPar = new ParentDAO((getActivity()));
                    ParentBean par1 = dcPar.getParent(stu.getParent1Id());
                    if (par1 != null) {
                        hashPar.put(par1, stu);
                        //lstPar.add(par1);
                    }
                    ParentBean par2 = dcPar.getParent(stu.getParent2Id());
                    if (par2 != null) {
                        hashPar.put(par2, stu);
                        //lstPar.add(par2);
                    }
                    //ParentBean gau = dcPar.getParent(stu.getGuardian_Id());


                }

                lv1.setAdapter(new EducatorCourseMsgsLvAdapter(getActivity(), hashPar));
                if (tv_Empty != null) {
                    tv_Empty.setText(null); // Set "no events found" text null.
                    tv_Empty.setVisibility(View.GONE);
                }
            } else {
                lv1.setAdapter(null);
                if (tv_Empty != null) {
                    tv_Empty.setText("nothing_found");
                    tv_Empty.setVisibility(View.VISIBLE);
                }
                Log.d(TAG, "Nothing found. students list= " + lstStu);
            }*/
        }

        return rootView;
    }

    private Student setStudent(Cursor c) {
        Student stu;
        stu = new Student();
        /*stu.setId(c.getString(c.getColumnIndex(Students.STUDENT_ID)));
        stu.setfName(c.getString(c.getColumnIndex(Students.STUDENT_FNAME)));
        stu.setlName(c.getString(c.getColumnIndex(Students.STUDENT_LNAME)));
        stu.setDoB(c.getString(c.getColumnIndex(Students.STUDENT_DOB)));
        stu.setEmail(c.getString(c.getColumnIndex(Students.STUDENT_EMAIL)));
        stu.setGender(c.getString(c.getColumnIndex(Students.STUDENT_GENDER)));
        stu.setAddress(c.getString(c.getColumnIndex(Students.STUDENT_ADDRESS)));
        stu.setBloodGroup(c.getString(c.getColumnIndex(Students.STUDENT_BLOOD_GROUP)));
        stu.setMedicalProblems(c.getString(c.getColumnIndex(Students.STUDENT_MEDICAL_PROBLEMS)));
        stu.setMedicationNeeds(c.getString(c.getColumnIndex(Students.STUDENT_MEDICATION_NEEDS)));
        stu.setMedicationAllergies(c.getString(c.getColumnIndex(Students.STUDENT_MEDICATION_ALLERGIES)));
        stu.setFoodAllergies(c.getString(c.getColumnIndex(Students.STUDENT_FOOD_ALLERGIES)));
        stu.setOtherAllergies(c.getString(c.getColumnIndex(Students.STUDENT_OTHER_ALLERGIES)));
        stu.setHobbies(c.getString(c.getColumnIndex(Students.STUDENT_HOBBIES)));
        stu.setPhysicianName(c.getString(c.getColumnIndex(Students.STUDENT_PHYSICIAN_NAME)));
        stu.setPhysicianPhone(c.getString(c.getColumnIndex(Students.STUDENT_PHYSICIAN_PHONE)));
        stu.setSpecialDietaryNeeds(c.getString(c.getColumnIndex(Students.STUDENT_SPECIAL_DIETARY_NEEDS)));
        stu.setAnnualFamilyIncome(c.getLong(c.getColumnIndex(Students.STUDENT_ANNUAL_FAMILY_INCOME)));
        stu.setSpecialInstructions(c.getString(c.getColumnIndex(Students.STUDENT_SPECIAL_INSTRUCTIONS)));
        stu.setClassId(c.getString(c.getColumnIndex(Students.STUDENT_CLASS_ID)));
        stu.setParent1Id(c.getString(c.getColumnIndex(Students.STUDENT_PARENT1_ID)));
        stu.setParent2Id(c.getString(c.getColumnIndex(Students.STUDENT_PARENT2_ID)));
        stu.setGuardian_Id(c.getString(c.getColumnIndex(Students.STUDENT_GUARDIAN_ID)));
        stu.setRollNumber(c.getString(c.getColumnIndex(Students.STUDENT_ROLL_NUMBER)));
        stu.setAge(c.getInt(c.getColumnIndex(Students.STUDENT_AGE)));
        stu.setHeight(c.getString(c.getColumnIndex(Students.STUDENT_HEIGHT)));
        stu.setWeight(c.getString(c.getColumnIndex(Students.STUDENT_WEIGHT)));
        stu.setImgPath(c.getString(c.getColumnIndex(Students.STUDENT_IMAGE_PATH)));*/
        return stu;
    }

    public EducatorMsgsFragment() {
    }
}
