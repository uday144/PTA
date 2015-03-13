package com.ptapp.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.ptapp.provider.PTAppDatabase;

/**
 * All CRUD(Create, Read, Update, Delete) Operations
 */
public class MessagesDAO {
    private String TAG = "PTAppUI - Messages DAO";


    // Database fields
    private SQLiteDatabase db;
    private PTAppDatabase dbHelper;

    /**
     * To access the database, instantiate the subclass of SQLiteOpenHelper,
     * i.e. PTAppDbHelper
     */
    public MessagesDAO(Context context) {
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

            cursor = db.query(Messages.TABLE_NAME,
                    new String[]{Messages.COL_NAME_ID}, null, null, null,
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

    *//**
     * Inserts  a message in the table
     *
     * @long - Id of newly inserted message
     *//*
    public long addMessage(MessagesBean msg) {
        long insertId = 0;
        try {
            // Gets the database in write mode(open the database)
            db = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();

            values.put(Messages.COL_NAME_MSG_TEXT, msg.getMsgText());
            values.put(Messages.COL_NAME_FROM_CID, msg.getFromCID());
            values.put(Messages.COL_NAME_TO_CID, msg.getToCID());
            //values.put(Messages.COL_NAME_AT, msg.getAt());

            // Inserting Row
            insertId = db.insert(Messages.TABLE_NAME, null, values);
            Log.i(TAG,
                    "id of new row inserted in Messages tbl: "
                            + String.valueOf(insertId));

        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
        } finally {
            db.close();
        }
        return insertId;
    }

    public void updateMsgStatus(String msgId, String msgStatus) {
        try {//msgId is auto-generated long value

            // Gets the database in write mode(open the database)
            db = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(Messages.COL_NAME_STATUS, msgStatus);

            //Update Row
            db.update(Messages.TABLE_NAME, values, Messages.COL_NAME_ID + "=?", new String[]{msgId});
        } catch (SQLException ex) {
            Log.e(TAG, ex.getMessage());
        } finally {
            db.close();
        }
    }

    //int - number of rows deleted.
    public int deleteMessages(String chatId, Context context) {
        int count = 0;

        try {
            // Gets the database in write mode(open the database)
            db = dbHelper.getWritableDatabase();

            count = db.delete(Messages.TABLE_NAME,
                    Messages.COL_NAME_TO_CID + "=? OR ( " + Messages.COL_NAME_FROM_CID + "=? AND " + Messages.COL_NAME_TO_CID + "=?)",
                    new String[]{chatId, chatId, SharedPrefUtil.getPrefAppUserId(context)});   //args


        } catch (SQLException ex) {
            Log.e(TAG, "" + ex.getMessage());
        } finally {
            db.close(); // Closing database connection
        }

        return count;
    }

    // get count of messages for me from(parentId), which has been received but not yet read
    public int getCountOfMsgsForMeFrom(String myUserId, String fromUserId) {

        //ArrayList<MessagesBean> lstMsgs = null;
        int count = 0;
        Cursor c = null;
        try {
            // Gets the database in read mode(open the database)
            db = dbHelper.getReadableDatabase();
            // lstMsgs = new ArrayList<MessagesBean>();

            c = db.query(Messages.TABLE_NAME,
                    null,       //columns
                    Messages.COL_NAME_TO_CID + "=? AND " + Messages.COL_NAME_FROM_CID + "=?",
                    new String[]{myUserId, fromUserId},       //args
                    null,       //group by
                    null,       //having
                    Messages.COL_NAME_AT + " DESC",     //order by
                    null);      //limit
            count = c.getCount();
            *//*if (c.moveToFirst()) {
                do {
                    MessagesBean msg = setMessagesBean(c);
                    lstMsgs.add(msg);

                } while (c.moveToNext());
            }*//*
        } catch (SQLException ex) {
            Log.e(TAG, "" + ex.getMessage());
        } finally {
            c.close(); // Closing the cursor
            db.close(); // Closing database connection
        }

        return count;
    }


    // get messages.
    public ArrayList<MessagesBean> getMessages(String chatId, Context context) {

        ArrayList<MessagesBean> lstMsgs = new ArrayList<MessagesBean>();
        Cursor c = null;
        try {
            // Gets the database in read mode(open the database)
            db = dbHelper.getReadableDatabase();
            c = db.query(Messages.TABLE_NAME,
                    null,       //columns
                    Messages.COL_NAME_TO_CID + "=? OR ( " + Messages.COL_NAME_FROM_CID + "=? AND " + Messages.COL_NAME_TO_CID + "=?)",
                    new String[]{chatId, chatId, SharedPrefUtil.getPrefAppUserId(context)},       //args
                    null,       //group by
                    null,       //having
                    Messages.COL_NAME_AT + " DESC",     //order by
                    null);      //limit

            if (c.moveToFirst()) {
                do {
                    MessagesBean msg = setMessagesBean(c);
                    lstMsgs.add(msg);

                } while (c.moveToNext());
            }
        } catch (SQLException ex) {
            Log.e(TAG, "" + ex.getMessage());
        } finally {
            c.close(); // Closing the cursor
            db.close(); // Closing database connection
        }

        return lstMsgs;
    }

    *//**
     * Maps the values from cursor to MessagesBean columns, then
     * return the table object
     *
     * @param c - Cursor
     * @return MessagesBean
     *//*
    private MessagesBean setMessagesBean(Cursor c) {
        MessagesBean msg = new MessagesBean();
        msg.setId(c.getLong(c.getColumnIndex(Messages.COL_NAME_ID)));
        msg.setFromCID(c.getString(c.getColumnIndex(Messages.COL_NAME_FROM_CID)));
        msg.setMsgText(c.getString(c.getColumnIndex(Messages.COL_NAME_MSG_TEXT)));
        msg.setToCID(c.getString(c.getColumnIndex(Messages.COL_NAME_TO_CID)));
        msg.setAt(c.getString(c.getColumnIndex(Messages.COL_NAME_AT)));
        msg.setStatus(c.getString(c.getColumnIndex(Messages.COL_NAME_STATUS)));

        return msg;
    }
*/
}
