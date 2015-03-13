package com.ptapp.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.ptapp.provider.PTAppDatabase;

/** All CRUD(Create, Read, Update, Delete) Operations */
public class MessageDAO {
	private String ERROR_TAG = "ERR_DC_Class";
	private String INFO_TAG = "INFO_DC_Class";

	// Database fields
	private SQLiteDatabase db;
	private PTAppDatabase dbHelper;

	/**
	 * To access the database, instantiate the subclass of SQLiteOpenHelper,
	 * i.e. PTAppDbHelper
	 */
	public MessageDAO(Context context) {
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

			cursor = db.query(Message.TABLE_NAME,
					new String[] { Message.COL_NAME_ID }, null, null, null,
					null, null, null);
			count = cursor.getCount();
		} catch (Exception ex) {
			Log.e(ERROR_TAG, ex.getMessage());
		} finally {
			cursor.close();
			db.close();
		}
		return count;
	}

	public MessageBean getSingleMessage(String studentId, String EducatorId) {
		MessageBean msg = null;
		Cursor c = null;

		try {
			// Gets the database in read mode(open the database)
			db = dbHelper.getReadableDatabase();
			c = db.query(Message.TABLE_NAME, null, Message.COL_NAME_STUDENT_ID
					+ "=? AND " + Message.COL_NAME_EDUCATOR_ID + "=?",
					new String[] { studentId, EducatorId }, null, null, null);

			if (c.moveToFirst()) {
				msg = new MessageBean();
				msg.setId(c.getLong(c.getColumnIndex(Message.COL_NAME_ID)));
				msg.setParentId(c.getString(c
						.getColumnIndex(Message.COL_NAME_PARENT_ID)));
				msg.setEducatorId(c.getString(c
						.getColumnIndex(Message.COL_NAME_EDUCATOR_ID)));
				msg.setStudentId(c.getString(c
						.getColumnIndex(Message.COL_NAME_STUDENT_ID)));
				msg.setFolderId(c.getString(c
						.getColumnIndex(Message.COL_NAME_FOLDER_ID)));
				msg.setNeedPush(c.getString(c
						.getColumnIndex(Message.COL_NAME_NEED_PUSH)));
				msg.setMsgTxt(c.getString(c
						.getColumnIndex(Message.COL_NAME_MSG_TXT)));
				msg.setMediaUrl(c.getString(c
						.getColumnIndex(Message.COL_NAME_MEDIA_URL)));
				msg.setMediaLocalPath(c.getString(c
						.getColumnIndex(Message.COL_NAME_MEDIA_LOCAL_PATH)));
				msg.setMediaMimeType(c.getString(c
						.getColumnIndex(Message.COL_NAME_MEDIA_MIME_TYPE)));
				msg.setMediaPTAppType(c.getString(c
						.getColumnIndex(Message.COL_NAME_MEDIA_PTAPP_TYPE)));
				msg.setMediaSize(c.getString(c
						.getColumnIndex(Message.COL_NAME_MEDIA_SIZE)));
				msg.setMediaName(c.getString(c
						.getColumnIndex(Message.COL_NAME_MEDIA_NAME)));
				msg.setThumbImage(c.getString(c
						.getColumnIndex(Message.COL_NAME_THUMB_IMAGE)));
				msg.setThumbServerPath(c.getString(c
						.getColumnIndex(Message.COL_NAME_THUMB_SERVER_PATH)));
				msg.setThumbLocalPath(c.getString(c
						.getColumnIndex(Message.COL_NAME_MEDIA_LOCAL_PATH)));
				msg.setReceivedTimestamp(c.getString(c
						.getColumnIndex(Message.COL_NAME_RECEIVED_TIMESTAMP)));
				msg.setSendTimestamp(c.getString(c
						.getColumnIndex(Message.COL_NAME_SEND_TIMESTAMP)));
				msg.setReciptServerTimestamp(c.getString(c
						.getColumnIndex(Message.COL_NAME_RECEIPT_SERVER_TIMESTAMP)));
				msg.setReceiptDeviceTimestamp(c.getString(c
						.getColumnIndex(Message.COL_NAME_RECEIPT_DEVICE_TIMESTAMP)));
			} else {
				Log.v(INFO_TAG,
						"getChatMessage() method - Query returned no data.");
			}
		} catch (Exception ex) {
			Log.e(ERROR_TAG, ex.getMessage());
		} finally {
			c.close();
			db.close();
		}
		return msg;
	}

	public void saveSingleMessage(MessageBean msg) {
		try {
			// Gets the database in write mode(open the database)
			db = dbHelper.getWritableDatabase();

			ContentValues values = new ContentValues();
			// values.put(Message.COL_NAME_ID, msg.getId());
			values.put(Message.COL_NAME_PARENT_ID, msg.getParentId());
			values.put(Message.COL_NAME_ID, msg.getEducatorId());
			values.put(Message.COL_NAME_STUDENT_ID, msg.getStudentId());
			values.put(Message.COL_NAME_FOLDER_ID, msg.getFolderId());
			values.put(Message.COL_NAME_NEED_PUSH, msg.getNeedPush());
			values.put(Message.COL_NAME_MSG_TXT, msg.getMsgTxt());
			values.put(Message.COL_NAME_MEDIA_URL, msg.getMediaUrl());
			values.put(Message.COL_NAME_MEDIA_MIME_TYPE, msg.getMediaMimeType());
			values.put(Message.COL_NAME_MEDIA_PTAPP_TYPE,
					msg.getMediaPTAppType());
			values.put(Message.COL_NAME_MEDIA_SIZE, msg.getMediaSize());
			values.put(Message.COL_NAME_MEDIA_NAME, msg.getMediaName());
			values.put(Message.COL_NAME_THUMB_IMAGE, msg.getThumbImage());
			values.put(Message.COL_NAME_THUMB_SERVER_PATH,
					msg.getThumbServerPath());
			values.put(Message.COL_NAME_MEDIA_LOCAL_PATH,
					msg.getThumbLocalPath());
			values.put(Message.COL_NAME_RECEIVED_TIMESTAMP,
					msg.getReceivedTimestamp());
			values.put(Message.COL_NAME_SEND_TIMESTAMP, msg.getSendTimestamp());
			values.put(Message.COL_NAME_RECEIPT_SERVER_TIMESTAMP,
					msg.getReciptServerTimestamp());
			values.put(Message.COL_NAME_RECEIPT_DEVICE_TIMESTAMP,
					msg.getReceiptDeviceTimestamp());

			// Inserting Row
			long insertId = db.insert(Message.TABLE_NAME, null, values);
			Log.i(INFO_TAG,
					"id of new row inserted in Message tbl: "
							+ String.valueOf(insertId));

		} catch (Exception ex) {
			Log.e(ERROR_TAG, ex.getMessage());
		} finally {
			db.close();
		}
	}

	public ArrayList<MessageBean> getStudentEducatorChat(String studentId,
			String EducatorId) {
		ArrayList<MessageBean> lst_boMsgs = null;
		Cursor c = null;
		try {
			// Gets the database in read mode(open the database)
			db = dbHelper.getReadableDatabase();
			c = db.query(Message.TABLE_NAME, null, Message.COL_NAME_STUDENT_ID
					+ "=? AND " + Message.COL_NAME_EDUCATOR_ID + "=?",
					new String[] { studentId, EducatorId }, null, null, null);
			if (c.moveToFirst()) {
				lst_boMsgs = new ArrayList<MessageBean>();
				do {
					MessageBean msg = new MessageBean();
					msg.setId(c.getLong(c.getColumnIndex(Message.COL_NAME_ID)));
					msg.setParentId(c.getString(c
							.getColumnIndex(Message.COL_NAME_PARENT_ID)));
					msg.setEducatorId(c.getString(c
							.getColumnIndex(Message.COL_NAME_EDUCATOR_ID)));
					msg.setStudentId(c.getString(c
							.getColumnIndex(Message.COL_NAME_STUDENT_ID)));
					msg.setFolderId(c.getString(c
							.getColumnIndex(Message.COL_NAME_FOLDER_ID)));
					msg.setNeedPush(c.getString(c
							.getColumnIndex(Message.COL_NAME_NEED_PUSH)));
					msg.setMsgTxt(c.getString(c
							.getColumnIndex(Message.COL_NAME_MSG_TXT)));
					msg.setMediaUrl(c.getString(c
							.getColumnIndex(Message.COL_NAME_MEDIA_URL)));
					msg.setMediaMimeType(c.getString(c
							.getColumnIndex(Message.COL_NAME_MEDIA_MIME_TYPE)));
					msg.setMediaPTAppType(c.getString(c
							.getColumnIndex(Message.COL_NAME_MEDIA_PTAPP_TYPE)));
					msg.setMediaSize(c.getString(c
							.getColumnIndex(Message.COL_NAME_MEDIA_SIZE)));
					msg.setMediaName(c.getString(c
							.getColumnIndex(Message.COL_NAME_MEDIA_NAME)));
					msg.setThumbImage(c.getString(c
							.getColumnIndex(Message.COL_NAME_THUMB_IMAGE)));
					msg.setThumbServerPath(c.getString(c
							.getColumnIndex(Message.COL_NAME_THUMB_SERVER_PATH)));
					msg.setThumbLocalPath(c.getString(c
							.getColumnIndex(Message.COL_NAME_MEDIA_LOCAL_PATH)));
					msg.setReceivedTimestamp(c.getString(c
							.getColumnIndex(Message.COL_NAME_RECEIVED_TIMESTAMP)));
					msg.setSendTimestamp(c.getString(c
							.getColumnIndex(Message.COL_NAME_SEND_TIMESTAMP)));
					msg.setReciptServerTimestamp(c.getString(c
							.getColumnIndex(Message.COL_NAME_RECEIPT_SERVER_TIMESTAMP)));
					msg.setReceiptDeviceTimestamp(c.getString(c
							.getColumnIndex(Message.COL_NAME_RECEIPT_DEVICE_TIMESTAMP)));

					// add CourseBean to list
					lst_boMsgs.add(msg);
				} while (c.moveToNext());
			} else {
				Log.v(INFO_TAG,
						"getStudentEducatorChat() method - Query returned no data.");
			}

		} catch (Exception ex) {
			Log.e(ERROR_TAG, ex.getMessage());
		} finally {
			c.close();
			db.close();
		}
		return lst_boMsgs;
	}
*/
}
