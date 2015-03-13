/*
package com.ptapp.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.ptapp.bean.MsgProfileBean;
import com.ptapp.dbhelper.PTAppDbContract.MsgProfile;
import com.ptapp.dbhelper.PTAppDbHelper;

import java.util.ArrayList;

*/
/**
 * All CRUD(Create, Read, Update, Delete) Operations
 *//*

public class MsgProfileDAO {
    private String TAG = "PTAppUI - MsgProfile DAO";

    // Database fields
    private SQLiteDatabase db;
    private PTAppDbHelper dbHelper;

    */
/**
     * To access the database, instantiate the subclass of SQLiteOpenHelper,
     * i.e. PTAppDbHelper
     *//*

    public MsgProfileDAO(Context context) {
        dbHelper = new PTAppDbHelper(context);
    }

    public void close() {
        dbHelper.close();
    }

    public int getRowsCount() {
        Cursor cursor = null;
        int count = 0;
        try {
            // Gets the database in read mode(open the database)
            db = dbHelper.getReadableDatabase();

            cursor = db.query(MsgProfile.TABLE_NAME,
                    new String[]{MsgProfile.COL_NAME_ID}, null, null, null,
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

    public void addMsgProfile(MsgProfileBean prf) {
        try {
            // Gets the database in write mode(open the database)
            db = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();

            values.put(MsgProfile.COL_NAME_NAME, prf.getName());
            values.put(MsgProfile.COL_NAME_CHAT_ID, prf.getChatId());
            values.put(MsgProfile.COL_NAME_COUNT, prf.getCount());
            values.put(MsgProfile.COL_NAME_IS_GROUP, prf.getIsGroup());

            // Inserting Row
            long insertId = db.insert(MsgProfile.TABLE_NAME, null, values);
            Log.i(TAG,
                    "id of new row inserted in MsgProfile tbl: "
                            + String.valueOf(insertId));

        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
        } finally {
            db.close();
        }
    }

    public void updateMsgCount(int count, String chatId) {
        try {
            // Gets the database in write mode(open the database)
            db = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(MsgProfile.COL_NAME_COUNT, count);

            //Updading Row
            db.update(MsgProfile.TABLE_NAME, values, MsgProfile.COL_NAME_CHAT_ID + "=?", new String[]{String.valueOf(chatId)});
        } catch (SQLException ex) {
            Log.e(TAG, ex.getMessage());
        } finally {
            db.close();
        }
    }

    public MsgProfileBean getMsgProfileByChatId(String chatId) {

        MsgProfileBean prf = null;
        Cursor c = null;
        try {
            // Gets the database in read mode(open the database)
            db = dbHelper.getReadableDatabase();
            c = db.query(MsgProfile.TABLE_NAME,
                    null,
                    MsgProfile.COL_NAME_CHAT_ID + "=?",
                    new String[]{chatId},
                    null, null, null, null);

            if (c.moveToFirst()) {
                prf = setMsgProfileBean(c);
            }
        } catch (SQLException ex) {
            Log.e(TAG, "" + ex.getMessage());
        } finally {
            c.close(); // Closing the cursor
            db.close(); // Closing database connection
        }

        return prf;
    }

    // get profile.
    public MsgProfileBean getMsgProfile(String profileId) {

        MsgProfileBean prf = null;
        Cursor c = null;
        try {
            // Gets the database in read mode(open the database)
            db = dbHelper.getReadableDatabase();
            c = db.query(MsgProfile.TABLE_NAME,
                    null,
                    MsgProfile.COL_NAME_ID + "=?",
                    new String[]{profileId},
                    null, null, null, null);

            if (c.moveToFirst()) {

                prf = setMsgProfileBean(c);
            }
        } catch (SQLException ex) {
            Log.e(TAG, "" + ex.getMessage());
        } finally {
            c.close(); // Closing the cursor
            db.close(); // Closing database connection
        }

        return prf;
    }

    // get messages.
    public ArrayList<MsgProfileBean> getMsgProfiles() {

        ArrayList<MsgProfileBean> lstMsgPrfs = new ArrayList<MsgProfileBean>();
        Cursor c = null;
        try {
            // Gets the database in read mode(open the database)
            db = dbHelper.getReadableDatabase();
            c = db.query(MsgProfile.TABLE_NAME,
                    null,
                    null,
                    null,
                    null, null, null, null);

            if (c.moveToFirst()) {
                do {
                    MsgProfileBean prf = setMsgProfileBean(c);
                    lstMsgPrfs.add(prf);

                } while (c.moveToNext());
            }
        } catch (SQLException ex) {
            Log.e(TAG, "" + ex.getMessage());
        } finally {
            c.close(); // Closing the cursor
            db.close(); // Closing database connection
        }

        return lstMsgPrfs;
    }

    */
/**
     * Maps the values from cursor to MessagesBean columns, then
     * return the table object
     *
     * @param c - Cursor
     * @return MessagesBean
     *//*

    private MsgProfileBean setMsgProfileBean(Cursor c) {
        MsgProfileBean prf = new MsgProfileBean();
        prf.setId(c.getLong(c.getColumnIndex(MsgProfile.COL_NAME_ID)));
        prf.setName(c.getString(c.getColumnIndex(MsgProfile.COL_NAME_NAME)));
        prf.setChatId(c.getString(c.getColumnIndex(MsgProfile.COL_NAME_CHAT_ID)));
        prf.setCount(c.getInt(c.getColumnIndex(MsgProfile.COL_NAME_COUNT)));
        prf.setIsGroup(c.getInt(c.getColumnIndex(MsgProfile.COL_NAME_IS_GROUP)));

        return prf;
    }

}
*/
