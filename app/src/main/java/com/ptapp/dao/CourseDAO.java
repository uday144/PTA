package com.ptapp.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;


import com.ptapp.provider.PTAppDatabase;

/**
 * All CRUD(Create, Read, Update, Delete) Operations
 */
public class CourseDAO {
    private String TAG = "PTAppUI - Course DAO";

    // Database fields
    private SQLiteDatabase db;
    private PTAppDatabase dbHelper;

    /**
     * To access the database, instantiate the subclass of SQLiteOpenHelper,
     * i.e. PTAppDbHelper
     */
    public CourseDAO(Context context) {
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

            cursor = db.query(com.ptapp.dbhelper.PTAppContract.Course.TABLE_NAME,
                    new String[]{com.ptapp.dbhelper.PTAppContract.Course.COL_NAME_ID}, null, null, null,
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

    public void addCourse(Course course) {
        try {
            // Gets the database in write mode(open the database)
            db = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(com.ptapp.dbhelper.PTAppContract.Course.COL_NAME_ID, course.getId());
            values.put(com.ptapp.dbhelper.PTAppContract.Course.COL_NAME_COURSE_NAME, course.getCourseName());
            values.put(com.ptapp.dbhelper.PTAppContract.Course.COL_NAME_STUDENT_ID, course.getStudentId());
            values.put(com.ptapp.dbhelper.PTAppContract.Course.COL_NAME_EDUCATOR_ID, course.getEducatorId());
            values.put(com.ptapp.dbhelper.PTAppContract.Course.COL_NAME_CLASS_ID, course.getClassId());
            values.put(com.ptapp.dbhelper.PTAppContract.Course.COL_NAME_YEAR, course.getYear());
            values.put(com.ptapp.dbhelper.PTAppContract.Course.COL_NAME_GRADE, course.getGrade());
            values.put(com.ptapp.dbhelper.PTAppContract.Course.COL_NAME_UNIT1_SCORE, course.getUnit1Score());
            values.put(com.ptapp.dbhelper.PTAppContract.Course.COL_NAME_UNIT2_SCORE, course.getUnit2Score());
            values.put(com.ptapp.dbhelper.PTAppContract.Course.COL_NAME_UNIT3_SCORE, course.getUnit3Score());
            values.put(com.ptapp.dbhelper.PTAppContract.Course.COL_NAME_MSG_COUNT, course.getMsgCount());
            values.put(com.ptapp.dbhelper.PTAppContract.Course.COL_NAME_IS_GROUP, course.getIsGroup());

            // Inserting Row
            long insertId = db.insert(com.ptapp.dbhelper.PTAppContract.Course.TABLE_NAME, null, values);
            Log.i(TAG,
                    "id of new row inserted in Course tbl: "
                            + String.valueOf(insertId));

        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
        } finally {
            db.close();
        }
    }


    public ArrayList<Course> getStudentCourses(String studentId, String classId) {
        ArrayList<Course> lst_boCourses = null;
        Cursor c = null;

        try {
            // Gets the database in read mode(open the database)
            db = dbHelper.getReadableDatabase();
            c = db.query(com.ptapp.dbhelper.PTAppContract.Course.TABLE_NAME, null, com.ptapp.dbhelper.PTAppContract.Course.COL_NAME_STUDENT_ID
                            + "=? AND " + com.ptapp.dbhelper.PTAppContract.Course.COL_NAME_CLASS_ID + "=?",
                    new String[]{studentId, classId}, null, null, null);

            if (c.moveToFirst()) {
                lst_boCourses = new ArrayList<Course>();

                do {
                    Course bo = setCourseBean(c);
                    lst_boCourses.add(bo);
                } while (c.moveToNext());
            } else {
                Log.v(TAG,
                        "getStudentCourses() method - Query returned nil data.");
            }

        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
        } finally {
            c.close();
            db.close();
        }
        return lst_boCourses;
    }

    // get profile.
    public Course getCourse(String courseId) {

        Course crs = null;
        Cursor c = null;
        try {
            // Gets the database in read mode(open the database)
            db = dbHelper.getReadableDatabase();
            c = db.query(com.ptapp.dbhelper.PTAppContract.Course.TABLE_NAME,
                    null,
                    com.ptapp.dbhelper.PTAppContract.Course.COL_NAME_ID + "=?",
                    new String[]{courseId},
                    null, null, null, null);

            if (c.moveToFirst()) {

                crs = setCourseBean(c);
            }
        } catch (SQLException ex) {
            Log.e(TAG, "" + ex.getMessage());
        } finally {
            c.close(); // Closing the cursor
            db.close(); // Closing database connection
        }

        return crs;
    }


    // get course names by student Id and Class Id
    public ArrayList<String> getCourseNames(long studentId, long classId) {
        Cursor cc = null;
        ArrayList<String> lst_names = new ArrayList<String>();

        String[] cols_Course = new String[]{com.ptapp.dbhelper.PTAppContract.Course.COL_NAME_COURSE_NAME};

        // Gets the database in read mode(open the database)
        db = dbHelper.getReadableDatabase();

        try {
            cc = db.query(
                    com.ptapp.dbhelper.PTAppContract.Course.TABLE_NAME,
                    cols_Course,
                    com.ptapp.dbhelper.PTAppContract.Course.COL_NAME_STUDENT_ID + "=? AND "
                            + com.ptapp.dbhelper.PTAppContract.Course.COL_NAME_CLASS_ID + "=?",
                    new String[]{String.valueOf(studentId),
                            String.valueOf(classId)}, null, null, null);

            if (cc.moveToFirst()) {
                do {
                    String course_name = cc.getString(cc
                            .getColumnIndex(com.ptapp.dbhelper.PTAppContract.Course.COL_NAME_COURSE_NAME));
                    lst_names.add(course_name);

                } while (cc.moveToNext());
            }
        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
        } finally {
            cc.close();
            db.close();
        }
        return lst_names;
    }

    *//**
     * Maps the values from cursor to CourseBean columns, then
     * return the table object
     *
     * @param c - Cursor
     * @return CourseBean
     *//*
    private Course setCourseBean(Cursor c) {
        Course crs = new Course();
        crs.setId(c.getString(c.getColumnIndex(com.ptapp.dbhelper.PTAppContract.Course.COL_NAME_ID)));
        crs.setCourseName(c.getString(c.getColumnIndex(com.ptapp.dbhelper.PTAppContract.Course.COL_NAME_COURSE_NAME)));
        crs.setStudentId(c.getString(c.getColumnIndex(com.ptapp.dbhelper.PTAppContract.Course.COL_NAME_STUDENT_ID)));
        crs.setEducatorId(c.getString(c.getColumnIndex(com.ptapp.dbhelper.PTAppContract.Course.COL_NAME_EDUCATOR_ID)));
        crs.setClassId(c.getString(c.getColumnIndex(com.ptapp.dbhelper.PTAppContract.Course.COL_NAME_CLASS_ID)));
        crs.setYear(c.getInt(c.getColumnIndex(com.ptapp.dbhelper.PTAppContract.Course.COL_NAME_YEAR)));
        crs.setGrade(c.getString(c.getColumnIndex(com.ptapp.dbhelper.PTAppContract.Course.COL_NAME_GRADE)));
        crs.setUnit1Score(c.getInt(c.getColumnIndex(com.ptapp.dbhelper.PTAppContract.Course.COL_NAME_UNIT1_SCORE)));
        crs.setUnit2Score(c.getInt(c.getColumnIndex(com.ptapp.dbhelper.PTAppContract.Course.COL_NAME_UNIT2_SCORE)));
        crs.setUnit3Score(c.getInt(c.getColumnIndex(com.ptapp.dbhelper.PTAppContract.Course.COL_NAME_UNIT3_SCORE)));
        crs.setMsgCount(c.getInt(c.getColumnIndex(com.ptapp.dbhelper.PTAppContract.Course.COL_NAME_MSG_COUNT)));
        crs.setIsGroup(c.getInt(c.getColumnIndex(com.ptapp.dbhelper.PTAppContract.Course.COL_NAME_IS_GROUP)));

        return crs;
    }
*/
}
