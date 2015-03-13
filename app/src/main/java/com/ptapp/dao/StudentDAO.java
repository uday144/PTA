package com.ptapp.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.ptapp.provider.PTAppDatabase;

/**
 * All CRUD(Create, Read, Update, Delete) Operations
 */
public class StudentDAO {
    private String TAG = "Schoolo - StudentDAO";

    // Database fields
    private SQLiteDatabase db;
    private PTAppDatabase dbHelper;

    /**
     * To access the database, instantiate the subclass of SQLiteOpenHelper,
     * i.e. PTAppDbHelper
     */
    public StudentDAO(Context context) {
        dbHelper = new PTAppDatabase(context);
    }

    public void close() {
        dbHelper.close();
    }

    /*public int getRowsCount() {
        Cursor cursor = null;
        int count = 0;
        try {
            // Gets the database in read mode(open the database)
            db = dbHelper.getReadableDatabase();

            cursor = db.query(Student.TABLE_NAME,
                    new String[]{Student.COL_NAME_ID}, null, null, null,
                    null, null, null);
            count = cursor.getCount();
        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
        } finally {
            cursor.close();
            db.close();
        }
        return count;
    }

    // add Student object
    public void addStudent(StudentBean student) {
        try {
            // Gets the database in write mode(open the database)
            db = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(Student.COL_NAME_ID, student.getId());
            values.put(Student.COL_NAME_FNAME, student.getfName());
            values.put(Student.COL_NAME_LNAME, student.getlName());
            values.put(Student.COL_NAME_DOB, student.getDoB());
            values.put(Student.COL_NAME_EMAIL, student.getEmail());
            values.put(Student.COL_NAME_GENDER, student.getGender());
            values.put(Student.COL_NAME_ADDRESS, student.getAddress());
            values.put(Student.COL_NAME_BLOOD_GROUP, student.getBloodGroup());
            values.put(Student.COL_NAME_MEDICAL_PROBLEMS,
                    student.getMedicalProblems());
            values.put(Student.COL_NAME_MEDICATION_NEEDS,
                    student.getMedicationNeeds());
            values.put(Student.COL_NAME_MEDICATION_ALLERGIES,
                    student.getMedicationAllergies());
            values.put(Student.COL_NAME_FOOD_ALLERGIES,
                    student.getFoodAllergies());
            values.put(Student.COL_NAME_OTHER_ALLERGIES,
                    student.getOtherAllergies());
            values.put(Student.COL_NAME_HOBBIES, student.getHobbies());
            values.put(Student.COL_NAME_PHYSICIAN_NAME,
                    student.getPhysicianName());
            values.put(Student.COL_NAME_PHYSICIAN_PHONE,
                    student.getPhysicianPhone());
            values.put(Student.COL_NAME_SPECIAL_DIETARY_NEEDS,
                    student.getSpecialDietaryNeeds());
            values.put(Student.COL_NAME_ANNUAL_FAMILY_INCOME,
                    student.getAnnualFamilyIncome());
            values.put(Student.COL_NAME_SPECIAL_INSTRUCTIONS,
                    student.getSpecialInstructions());
            values.put(Student.COL_NAME_CLASS_ID, student.getClassId());
            values.put(Student.COL_NAME_PARENT1_ID, student.getParent1Id());
            values.put(Student.COL_NAME_PARENT2_ID, student.getParent2Id());
            values.put(Student.COL_NAME_GUARDIAN_ID, student.getGuardian_Id());
            values.put(Student.COL_NAME_ROLL_NUMBER, student.getRollNumber());
            values.put(Student.COL_NAME_AGE, student.getAge());
            values.put(Student.COL_NAME_HEIGHT, student.getHeight());
            values.put(Student.COL_NAME_WEIGHT, student.getWeight());
            values.put(Student.COL_NAME_IMAGE_PATH, student.getImgPath());

            // Inserting Row
            long insertId = db.insert(Student.TABLE_NAME, null, values);
            Log.i(TAG,
                    "id of new row inserted in Student tbl: "
                            + String.valueOf(insertId));

        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
        } finally {
            db.close();
        }
    }

    public String getStudentId(String prevStudentId) {
        String studentIdCntxt = "";
        Cursor c = null;
        try {
            // Gets the database in read mode(open the database)
            db = dbHelper.getReadableDatabase();
            c = db.query(Student.TABLE_NAME,
                    new String[]{Student.COL_NAME_ID}, null, null, null,
                    null, Student.COL_NAME_ID + " ASC");
            if (c.moveToFirst()) {
                do {
                    studentIdCntxt = c.getString(c
                            .getColumnIndex(Student.COL_NAME_ID));
                    if (studentIdCntxt.equals(prevStudentId)) {
                        if (c.moveToNext()) {

                        } else {
                            c.moveToFirst();
                        }
                        studentIdCntxt = c.getString(c
                                .getColumnIndex(Student.COL_NAME_ID));
                        break;
                    } else {

                    }
                } while (c.moveToNext());
            } else {
                Log.v(TAG, "getStudentId() method - Query returned 0 data.");
            }

        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
        } finally {
            c.close();
            db.close();
        }
        return studentIdCntxt;
    }

    public ArrayList<Long> getAllStudentIds() {
        ArrayList<Long> stuIds = new ArrayList<Long>();
        String[] cols_Student = new String[]{Student.COL_NAME_ID};
        Cursor c = null;
        try {
            // Gets the database in read mode(open the database)
            db = dbHelper.getReadableDatabase();
            c = db.query(Student.TABLE_NAME, cols_Student, null, null, null,
                    null, null);

            if (c.moveToFirst()) {
                do {
                    stuIds.add(c.getLong(c.getColumnIndex(Student.COL_NAME_ID)));
                } while (c.moveToNext());
            } else {
                Log.v(TAG,
                        "getClassIdOfStudent() method - Query returned 0 data.");
            }
        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
        } finally {
            c.close();
            db.close();
        }
        return stuIds;
    }

    *//**
     * @return returns ClassId of a Student
     *//*
    public String getClassIdOfStudent(long StudentId) {
        String classId = "";
        String[] cols_Student = new String[]{Student.COL_NAME_CLASS_ID};
        Cursor c = null;
        try {
            // Gets the database in read mode(open the database)
            db = dbHelper.getReadableDatabase();
            c = db.query(Student.TABLE_NAME, cols_Student, Student.COL_NAME_ID
                            + "=?", new String[]{String.valueOf(StudentId)}, null,
                    null, null);

            if (c.moveToFirst()) {
                classId = c.getString(c
                        .getColumnIndex(Student.COL_NAME_CLASS_ID));
            } else {
                Log.v(TAG,
                        "getClassIdOfStudent() method - Query returned 0 data.");
            }
        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
        } finally {
            c.close();
            db.close();
        }
        return classId;
    }

    *//**
     * @return returns RollNo. of a Student
     *//*
    public String getRollNumberOfStudent(String StudentId) {
        String rollNum = "";
        String[] cols_Student = new String[]{Student.COL_NAME_ROLL_NUMBER};
        Cursor c = null;
        try {
            // Gets the database in read mode(open the database)
            db = dbHelper.getReadableDatabase();
            c = db.query(Student.TABLE_NAME, cols_Student, Student.COL_NAME_ID
                    + "=?", new String[]{StudentId}, null, null, null);

            if (c.moveToFirst()) {
                rollNum = c.getString(c
                        .getColumnIndex(Student.COL_NAME_ROLL_NUMBER));
            } else {
                Log.v(TAG,
                        "getRollNumberOfStudent() method - Query returned nil data.");
            }
        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
        } finally {
            c.close();
            db.close();
        }
        return rollNum;
    }

    public StudentBean getStudent(String StudentId) {
        StudentBean stu = null;
        Cursor c = null;

        try {
            // Gets the database in read mode(open the database)
            db = dbHelper.getReadableDatabase();
            c = db.query(Student.TABLE_NAME, null, Student.COL_NAME_ID + "=?",
                    new String[]{StudentId}, null, null, null);

            if (c.moveToFirst()) {
                stu = setStudentBean(c);
            } else {
                Log.v(TAG,
                        "getRollNumberOfStudent() method - Query returned nil data.");
            }
        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
        } finally {
            c.close();
            db.close();
        }
        return stu;
    }

    public ArrayList<StudentBean> getStudentsOfClass(String classId) {
        ArrayList<StudentBean> lstStu = null;
        Cursor c = null;
        try {
            lstStu = new ArrayList<StudentBean>();

            // Gets the database in read mode(open the database)
            db = dbHelper.getReadableDatabase();

            c = db.query(Student.TABLE_NAME,
                    null,
                    Student.COL_NAME_CLASS_ID + "=?",
                    new String[]{classId},
                    null, null,
                    Student.COL_NAME_AGE + " DESC");

            if (c.moveToFirst()) {
                do {
                    StudentBean stu = setStudentBean(c);
                    lstStu.add(stu);
                } while (c.moveToNext());
            } else {
                Log.v(TAG,
                        "getStudentsBasicInfoAgeWise method - returned no data.");
            }
        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
        } finally {
            c.close();
            db.close();
        }

        return lstStu;
    }

    // get Eldest child
    public ArrayList<StudentBean> getStudentsBasicInfoAgeWise(String parentId) {
        ArrayList<StudentBean> lstStu = null;
        Cursor c = null;
        try {
            lstStu = new ArrayList<StudentBean>();
            // Gets the database in read mode(open the database)
            db = dbHelper.getReadableDatabase();
            c = db.query(Student.TABLE_NAME,
                    null,
                    Student.COL_NAME_PARENT1_ID + "=? OR " + Student.COL_NAME_PARENT2_ID + "=?",
                    new String[]{parentId, parentId},
                    null,
                    null,
                    Student.COL_NAME_AGE + " DESC");

            if (c.moveToFirst()) {
                do {
                    StudentBean stu = new StudentBean();
                    stu.setId(c.getString(c.getColumnIndex(Student.COL_NAME_ID)));
                    stu.setfName(c.getString(c
                            .getColumnIndex(Student.COL_NAME_FNAME)));
                    stu.setlName(c.getString(c
                            .getColumnIndex(Student.COL_NAME_LNAME)));
                    stu.setClassId(c.getString(c
                            .getColumnIndex(Student.COL_NAME_CLASS_ID)));
                    stu.setAge(c.getInt(c.getColumnIndex(Student.COL_NAME_AGE)));
                    stu.setRollNumber(c.getString(c
                            .getColumnIndex(Student.COL_NAME_ROLL_NUMBER)));
                    stu.setImgPath(c.getString(c
                            .getColumnIndex(Student.COL_NAME_IMAGE_PATH)));

                    lstStu.add(stu);
                } while (c.moveToNext());
            } else {
                Log.v(TAG,
                        "getStudentsBasicInfoAgeWise method - returned no data.");
            }
        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
        } finally {
            c.close();
            db.close();
        }

        return lstStu;
    }

    *//**
     * @param c
     * @return
     *//*
    private StudentBean setStudentBean(Cursor c) {
        StudentBean stu;
        stu = new StudentBean();
        stu.setId(c.getString(c.getColumnIndex(Student.COL_NAME_ID)));
        stu.setfName(c.getString(c.getColumnIndex(Student.COL_NAME_FNAME)));
        stu.setlName(c.getString(c.getColumnIndex(Student.COL_NAME_LNAME)));
        stu.setDoB(c.getString(c.getColumnIndex(Student.COL_NAME_DOB)));
        stu.setEmail(c.getString(c.getColumnIndex(Student.COL_NAME_EMAIL)));
        stu.setGender(c.getString(c.getColumnIndex(Student.COL_NAME_GENDER)));
        stu.setAddress(c.getString(c.getColumnIndex(Student.COL_NAME_ADDRESS)));
        stu.setBloodGroup(c.getString(c
                .getColumnIndex(Student.COL_NAME_BLOOD_GROUP)));
        stu.setMedicalProblems(c.getString(c
                .getColumnIndex(Student.COL_NAME_MEDICAL_PROBLEMS)));
        stu.setMedicationNeeds(c.getString(c
                .getColumnIndex(Student.COL_NAME_MEDICATION_NEEDS)));
        stu.setMedicationAllergies(c.getString(c
                .getColumnIndex(Student.COL_NAME_MEDICATION_ALLERGIES)));
        stu.setFoodAllergies(c.getString(c
                .getColumnIndex(Student.COL_NAME_FOOD_ALLERGIES)));
        stu.setOtherAllergies(c.getString(c
                .getColumnIndex(Student.COL_NAME_OTHER_ALLERGIES)));
        stu.setHobbies(c.getString(c.getColumnIndex(Student.COL_NAME_HOBBIES)));
        stu.setPhysicianName(c.getString(c
                .getColumnIndex(Student.COL_NAME_PHYSICIAN_NAME)));
        stu.setPhysicianPhone(c.getString(c
                .getColumnIndex(Student.COL_NAME_PHYSICIAN_PHONE)));
        stu.setSpecialDietaryNeeds(c.getString(c
                .getColumnIndex(Student.COL_NAME_SPECIAL_DIETARY_NEEDS)));
        stu.setAnnualFamilyIncome(c.getLong(c
                .getColumnIndex(Student.COL_NAME_ANNUAL_FAMILY_INCOME)));
        stu.setSpecialInstructions(c.getString(c
                .getColumnIndex(Student.COL_NAME_SPECIAL_INSTRUCTIONS)));
        stu.setClassId(c.getString(c.getColumnIndex(Student.COL_NAME_CLASS_ID)));
        stu.setParent1Id(c.getString(c
                .getColumnIndex(Student.COL_NAME_PARENT1_ID)));
        stu.setParent2Id(c.getString(c
                .getColumnIndex(Student.COL_NAME_PARENT2_ID)));
        stu.setGuardian_Id(c.getString(c
                .getColumnIndex(Student.COL_NAME_GUARDIAN_ID)));
        stu.setRollNumber(c.getString(c
                .getColumnIndex(Student.COL_NAME_ROLL_NUMBER)));
        stu.setAge(c.getInt(c.getColumnIndex(Student.COL_NAME_AGE)));
        stu.setHeight(c.getString(c.getColumnIndex(Student.COL_NAME_HEIGHT)));
        stu.setWeight(c.getString(c.getColumnIndex(Student.COL_NAME_WEIGHT)));
        stu.setImgPath(c.getString(c
                .getColumnIndex(Student.COL_NAME_IMAGE_PATH)));
        return stu;
    }
*/
}
