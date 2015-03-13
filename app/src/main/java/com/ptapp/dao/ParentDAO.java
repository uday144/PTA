package com.ptapp.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.ptapp.provider.PTAppDatabase;

/**
 * All CRUD(Create, Read, Update, Delete) Operations
 */
public class ParentDAO {
    private static final String TAG = "ParentDAO";

    // Database fields
    private SQLiteDatabase db;
    private PTAppDatabase dbHelper;

    /**
     * To access the database, instantiate the subclass of SQLiteOpenHelper,
     * i.e. PTAppDbHelper
     */
    public ParentDAO(Context context) {
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

            cursor = db.query(Parent.TABLE_NAME,
                    new String[]{Parent.COL_NAME_ID}, null, null, null,
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

    // add Parent object
    public void addParent(ParentBean parent) {
        try {
            // Gets the database in write mode(open the database)
            db = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(Parent.COL_NAME_ID, parent.getId());
            values.put(Parent.COL_NAME_FNAME, parent.getfName());
            values.put(Parent.COL_NAME_LNAME, parent.getlName());
            values.put(Parent.COL_NAME_EMAIL, parent.getEmail());
            values.put(Parent.COL_NAME_PHONE, parent.getPhone());
            values.put(Parent.COL_NAME_GENDER, parent.getGender());
            values.put(Parent.COL_NAME_MSG_COUNT, parent.getMsgCount());
            values.put(Parent.COL_NAME_IS_GROUP, parent.getIsGroup());
            values.put(Parent.COL_NAME_IMAGE_PATH, parent.getImgPath());

            // Inserting Row
            long insertId = db.insert(Parent.TABLE_NAME, null, values);
            Log.i(TAG,
                    "id of new row inserted in Parent tbl: "
                            + String.valueOf(insertId));

        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
        } finally {
            db.close(); // Closing database connection
        }
    }

    *//**
     * Parents
     *//*
    public ArrayList<ParentBean> getParents() {

        ArrayList<ParentBean> lst_Parents = null;
        Cursor c = null;
        try {

            // Gets the database in read mode(open the database)
            db = dbHelper.getReadableDatabase();
            lst_Parents = new ArrayList<ParentBean>();

            c = db.query(Parent.TABLE_NAME, null, null, null, null, null, null);
            if (c.moveToFirst()) {
                do {
                    ParentBean p = setParentBean(c);
                    lst_Parents.add(p);
                } while (c.moveToNext());
            }

        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
        } finally {
            c.close(); // close the cursor
            db.close(); // close the database
        }

        return lst_Parents;
    }


    *//**
     * Getting single row by email
     *//*
    public ParentBean getParentByEmailId(String emailId) {
        ParentBean par = null;
        Cursor c = null;

        try {
            // Gets the database in read mode(open the database)
            db = dbHelper.getReadableDatabase();

            c = db.query(Parent.TABLE_NAME, null, Parent.COL_NAME_EMAIL + "=?",
                    new String[]{emailId}, null, null, null, null);

            if (c.moveToFirst()) {
                par = setParentBean(c);
            }
        } catch (SQLException ex) {
            Log.e(TAG, ex.getMessage());
        } finally {
            c.close();
            db.close();
        }
        return par;
    }

    *//**
     * Getting single row by Id
     *//*
    public ParentBean getParent(String parentId) {
        ParentBean par = null;
        Cursor c = null;

        try {
            // Gets the database in read mode(open the database)
            db = dbHelper.getReadableDatabase();

            c = db.query(Parent.TABLE_NAME, null, Parent.COL_NAME_ID + "=?",
                    new String[]{parentId}, null, null, null, null);
            if (c.moveToFirst()) {
                par = setParentBean(c);
            }
        } catch (SQLException ex) {
            Log.e(TAG, ex.getMessage());
        } finally {
            c.close();
            db.close();
        }
        return par;
    }

    public void updateMsgCount(int count, String chatId) {
        try {
            // Gets the database in write mode(open the database)
            db = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(Parent.COL_NAME_MSG_COUNT, count);

            //Updading Row
            db.update(Parent.TABLE_NAME, values, Parent.COL_NAME_ID + "=?", new String[]{String.valueOf(chatId)});
        } catch (SQLException ex) {
            Log.e(TAG, ex.getMessage());
        } finally {
            db.close();
        }
    }

    private ParentBean setParentBean(Cursor c) {
        ParentBean p = new ParentBean();
        p.setId(c.getString(c.getColumnIndex(Parent.COL_NAME_ID)));
        p.setfName(c.getString(c.getColumnIndex(Parent.COL_NAME_FNAME)));
        p.setlName(c.getString(c.getColumnIndex(Parent.COL_NAME_LNAME)));
        p.setEmail(c.getString(c.getColumnIndex(Parent.COL_NAME_EMAIL)));
        p.setPhone(c.getString(c.getColumnIndex(Parent.COL_NAME_PHONE)));
        p.setGender(c.getString(c.getColumnIndex(Parent.COL_NAME_GENDER)));
        p.setMsgCount(c.getInt(c.getColumnIndex(Parent.COL_NAME_MSG_COUNT)));
        p.setIsGroup(c.getInt(c.getColumnIndex(Parent.COL_NAME_IS_GROUP)));
        p.setImgPath(c.getString(c.getColumnIndex(Parent.COL_NAME_IMAGE_PATH)));
        return p;
    }*/
}
