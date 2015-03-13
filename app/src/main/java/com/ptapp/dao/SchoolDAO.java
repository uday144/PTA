package com.ptapp.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.ptapp.provider.PTAppDatabase;

/** All CRUD(Create, Read, Update, Delete) Operations */
public class SchoolDAO {
    private String TAG = "DC_School";

    // Database fields
    private SQLiteDatabase db;
    private PTAppDatabase dbHelper;

    /**
     * To access the database, instantiate the subclass of SQLiteOpenHelper,
     * i.e. PTAppDbHelper
     */
    public SchoolDAO(Context context) {
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

            cursor = db.query(School.TABLE_NAME,
                    new String[]{School.COL_NAME_ID}, null, null, null,
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

    // add school object.
    public void addSchool(SchoolBean school) {
        try {
            // Gets the database in write mode(open the database)
            db = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(School.COL_NAME_ID, school.getId());
            values.put(School.COL_NAME_SCHOOL_NAME, school.getSchoolName());
            values.put(School.COL_NAME_SCHOOL_ADDRESS,
                    school.getSchoolAddress());

            // Inserting Row
            long insertId = db.insert(School.TABLE_NAME, null, values);
            Log.i(TAG,
                    "id of new row inserted in School tbl: "
                            + String.valueOf(insertId));

        } catch (Exception ex) {
            Log.e(null, ex.getMessage());
        } finally {
            db.close();
        }
    }

    public SchoolBean getSchool(String schoolId) {
        SchoolBean sch = null;
        Cursor c = null;

        try {
            // Gets the database in read mode(open the database)
            db = dbHelper.getReadableDatabase();
            c = db.query(School.TABLE_NAME, null, School.COL_NAME_ID
                    + "=?", new String[] { schoolId }, null, null, null);

            if (c.moveToFirst()) {
                sch = setSchoolBean(c);
            } else {
                Log.v(TAG,
                        "getSchool() method - Query returned nil data.");
            }
        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
        } finally {
            c.close();
            db.close();
        }
        return sch;
    }

    private SchoolBean setSchoolBean(Cursor c) {
        SchoolBean sch = new SchoolBean();
        sch.setId(c.getString(c.getColumnIndex(School.COL_NAME_ID)));
        sch.setSchoolAddress(c.getString(c.getColumnIndex(School.COL_NAME_SCHOOL_ADDRESS)));
        sch.setSchoolName(c.getString(c.getColumnIndex(School.COL_NAME_SCHOOL_NAME)));

        return sch;
    }*/
}
