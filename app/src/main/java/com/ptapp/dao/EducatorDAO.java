package com.ptapp.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/*import com.ptapp.io.model.Course;
import com.ptapp.io.model.Educator;*/
import com.ptapp.provider.PTAppDatabase;

/**
 * All CRUD(Create, Read, Update, Delete) Operations
 */
public class EducatorDAO {
    private String TAG = "EducatorDAO";

    // Database fields
    private SQLiteDatabase db;
    private PTAppDatabase dbHelper;

    /**
     * To access the database, instantiate the subclass of SQLiteOpenHelper,
     * i.e. PTAppDbHelper
     */
    public EducatorDAO(Context context) {
        dbHelper = new PTAppDatabase(context);
    }

    public void close() {
        dbHelper.close();
    }

   /* public int getRowsCount() {
        Cursor cursor = null;
        int count = 0;
        try {
            // Gets the database in read mode(open the database)
            db = dbHelper.getReadableDatabase();

            cursor = db.query(com.ptapp.dbhelper.PTAppContract.Educator.TABLE_NAME,
                    new String[]{com.ptapp.dbhelper.PTAppContract.Educator.COL_NAME_ID}, null, null, null,
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

    public void addEducator(Educator educator) {
        try {
            // Gets the database in write mode(open the database)
            db = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(com.ptapp.dbhelper.PTAppContract.Educator.COL_NAME_ID, educator.getId());
            values.put(com.ptapp.dbhelper.PTAppContract.Educator.COL_NAME_FNAME, educator.getfName());
            values.put(com.ptapp.dbhelper.PTAppContract.Educator.COL_NAME_LNAME, educator.getlName());
            values.put(com.ptapp.dbhelper.PTAppContract.Educator.COL_NAME_EMAIL, educator.getEmail());
            values.put(com.ptapp.dbhelper.PTAppContract.Educator.COL_NAME_PHONE, educator.getPhone());
            values.put(com.ptapp.dbhelper.PTAppContract.Educator.COL_NAME_TYPE, educator.getType());
            values.put(com.ptapp.dbhelper.PTAppContract.Educator.COL_NAME_GENDER, educator.getGender());
            values.put(com.ptapp.dbhelper.PTAppContract.Educator.COL_NAME_IMAGEPATH, educator.getImagePath());

            // Inserting Row
            long insertId = db.insert(com.ptapp.dbhelper.PTAppContract.Educator.TABLE_NAME, null, values);
            Log.i(TAG,
                    "id of new row inserted in Educator tbl: "
                            + String.valueOf(insertId));

        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
        } finally {
            db.close();
        }
    }

    // Get Class Incharge Image path
    public String getClassInchargeImageURL(long clsInchargeEducatorId) {
        String imgURL = "";
        Cursor c = null;
        try {
            // Gets the database in read mode(open the database)
            db = dbHelper.getReadableDatabase();
            String[] cols_Educator = new String[]{com.ptapp.dbhelper.PTAppContract.Educator.COL_NAME_IMAGEPATH};
            c = db.query(com.ptapp.dbhelper.PTAppContract.Educator.TABLE_NAME, cols_Educator,
                    com.ptapp.dbhelper.PTAppContract.Educator.COL_NAME_ID + "=? ",
                    new String[]{String.valueOf(clsInchargeEducatorId)},
                    null, null, null);
            if (c.moveToFirst()) {
                imgURL = c.getString(c
                        .getColumnIndex(com.ptapp.dbhelper.PTAppContract.Educator.COL_NAME_IMAGEPATH));
            }
        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
        } finally {
            c.close();
            db.close();
        }
        return imgURL;
    }

    *//**
     * Getting Student's Educators
     *//*
    public ArrayList<Educator> getStudentEducators(
            ArrayList<Course> lst_Courses) {

        ArrayList<Educator> lst_boEducators = null;
        Cursor c = null;
        try {
            // Gets the database in read mode(open the database)
            db = dbHelper.getReadableDatabase();
            lst_boEducators = new ArrayList<Educator>();

            for (Course item : lst_Courses) {

                c = db.query(com.ptapp.dbhelper.PTAppContract.Educator.TABLE_NAME, null, com.ptapp.dbhelper.PTAppContract.Educator.COL_NAME_ID + "=? ",
                        new String[]{String.valueOf(item.getEducatorId())},
                        null, null, null);


                if (c.moveToFirst()) {
                    Educator boEdu = setEducatorBean(c);
                    lst_boEducators.add(boEdu);
                }
            }
        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
        } finally {
            c.close(); // close the cursor
            db.close(); // close the database
            //dbHelper.close(); // to avoid error- db not closed

        }
        return lst_boEducators;
    }

    public Educator getEducator(String EducatorId) {
        Educator edu = null;
        Cursor c = null;
        try {
            // Gets the database in read mode(open the database)
            db = dbHelper.getReadableDatabase();
            c = db.query(com.ptapp.dbhelper.PTAppContract.Educator.TABLE_NAME, null,
                    com.ptapp.dbhelper.PTAppContract.Educator.COL_NAME_ID + "=?", new String[]{EducatorId},
                    null, null, null);

            if (c.moveToFirst()) {
                edu = setEducatorBean(c);
            } else {
                Log.v(TAG, "getEducator() method - Query returned nil data.");
            }
        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
        } finally {
            c.close();
            db.close();
        }
        return edu;
    }


    *//**
     * Getting single row by email
     *//*
    public Educator getEducatorByEmailId(String emailId) {
        Educator edu = null;
        Cursor c = null;

        try {
            // Gets the database in read mode(open the database)
            db = dbHelper.getReadableDatabase();

            c = db.query(com.ptapp.dbhelper.PTAppContract.Educator.TABLE_NAME, null, com.ptapp.dbhelper.PTAppContract.Educator.COL_NAME_EMAIL + "=?",
                    new String[]{emailId}, null, null, null, null);

            if (c.moveToFirst()) {
                edu = setEducatorBean(c);
            }
        } catch (SQLException ex) {
            Log.e(TAG, ex.getMessage());
        } finally {
            c.close(); // Closing the cursor
            db.close(); // Closing database connection
        }
        return edu;
    }

    public void updateMsgCount(int count, String chatId) {
        try {
            // Gets the database in write mode(open the database)
            db = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(com.ptapp.dbhelper.PTAppContract.Educator.COL_NAME_MSG_COUNT, count);

            //Updading Row
            db.update(com.ptapp.dbhelper.PTAppContract.Educator.TABLE_NAME, values, com.ptapp.dbhelper.PTAppContract.Educator.COL_NAME_ID + "=?", new String[]{String.valueOf(chatId)});
        } catch (SQLException ex) {
            Log.e(TAG, ex.getMessage());
        } finally {
            db.close();
        }
    }


    private Educator setEducatorBean(Cursor c) {
        Educator edu = new Educator();
        edu.setId(c.getString(c.getColumnIndex(com.ptapp.dbhelper.PTAppContract.Educator.COL_NAME_ID)));
        edu.setfName(c.getString(c.getColumnIndex(com.ptapp.dbhelper.PTAppContract.Educator.COL_NAME_FNAME)));
        edu.setlName(c.getString(c.getColumnIndex(com.ptapp.dbhelper.PTAppContract.Educator.COL_NAME_LNAME)));
        edu.setEmail(c.getString(c.getColumnIndex(com.ptapp.dbhelper.PTAppContract.Educator.COL_NAME_EMAIL)));
        edu.setPhone(c.getString(c.getColumnIndex(com.ptapp.dbhelper.PTAppContract.Educator.COL_NAME_PHONE)));
        edu.setType(c.getString(c.getColumnIndex(com.ptapp.dbhelper.PTAppContract.Educator.COL_NAME_TYPE)));
        edu.setGender(c.getString(c.getColumnIndex(com.ptapp.dbhelper.PTAppContract.Educator.COL_NAME_GENDER)));
        edu.setMsgCount(c.getInt(c.getColumnIndex(com.ptapp.dbhelper.PTAppContract.Educator.COL_NAME_MSG_COUNT)));
        edu.setIsGroup(c.getInt(c.getColumnIndex(com.ptapp.dbhelper.PTAppContract.Educator.COL_NAME_IS_GROUP)));
        edu.setImagePath(c.getString(c.getColumnIndex(com.ptapp.dbhelper.PTAppContract.Educator.COL_NAME_IMAGEPATH)));

        return edu;
    }*/
}
