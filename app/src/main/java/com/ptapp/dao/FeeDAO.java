package com.ptapp.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.ptapp.provider.PTAppDatabase;

/** All CRUD(Create, Read, Update, Delete) Operations */
public class FeeDAO {
	private String TAG = "Schoolo - FeeDAO";

	// Database fields
	private SQLiteDatabase db;
	private PTAppDatabase dbHelper;

	/**
	 * To access the database, instantiate the subclass of SQLiteOpenHelper,
	 * i.e. PTAppDbHelper
	 */
	public FeeDAO(Context context) {
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

			cursor = db.query(Fee.TABLE_NAME, new String[] { Fee.COL_NAME_ID },
					null, null, null, null, null, null);
			count = cursor.getCount();
		} catch (Exception ex) {
			Log.e(TAG, ex.getMessage());
		} finally {
			cursor.close();
			db.close();
		}
		return count;
	}

	// add Fee object
	public void addFee(FeeBean fee) {
		try {
			// Gets the database in write mode(open the database)
			db = dbHelper.getWritableDatabase();

			ContentValues values = new ContentValues();
			values.put(Fee.COL_NAME_ID, fee.getId());
			values.put(Fee.COL_NAME_RECEIPT_NUMBER, fee.getReceiptNumber());
			values.put(Fee.COL_NAME_FEE_CYCLE, fee.getFeeCycle());
			values.put(Fee.COL_NAME_PAID_ON, fee.getPaidOn());
			values.put(Fee.COL_NAME_AMOUNT, fee.getAmount());

			// Inserting Row
			long insertId = db.insert(Fee.TABLE_NAME, null, values);
			Log.i(TAG,
					"id of new row inserted in Fee tbl: "
							+ String.valueOf(insertId));

		} catch (Exception ex) {
			Log.e(TAG, ex.getMessage());
		} finally {
			db.close();
		}
	}

	public FeeBean getFee(String FeeId) {
		FeeBean f = null;
		Cursor c = null;

		try {
			// Gets the database in read mode(open the database)
			db = dbHelper.getReadableDatabase();
			c = db.query(Fee.TABLE_NAME, null, Fee.COL_NAME_ID + "=?",
					new String[] { FeeId }, null, null, null);

			if (c.moveToFirst()) {
				f = setFeeBean(c);
			} else {
				Log.v(TAG, "getFee() method - Query returned no data.");
			}
		} catch (Exception ex) {
			Log.e(TAG, ex.getMessage());
		} finally {
			c.close();
			db.close();
		}
		return f;
	}

	*//**
	 * @param c
	 * @return
	 *//*
	private FeeBean setFeeBean(Cursor c) {
		FeeBean f;
		f = new FeeBean();
		f.setReceiptNumber(c.getString(c
				.getColumnIndex(Fee.COL_NAME_RECEIPT_NUMBER)));
		f.setFeeCycle(c.getString(c.getColumnIndex(Fee.COL_NAME_FEE_CYCLE)));
		f.setPaidOn(c.getString(c.getColumnIndex(Fee.COL_NAME_PAID_ON)));
		f.setAmount(c.getString(c.getColumnIndex(Fee.COL_NAME_AMOUNT)));

		return f;
	}
*/
}
