package com.ptapp.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.ptapp.provider.PTAppDatabase;

/** All CRUD(Create, Read, Update, Delete) Operations */
public class TransportDAO {
	private String TAG = "Schoolo - TransportDAO";

	// Database fields
	private SQLiteDatabase db;
	private PTAppDatabase dbHelper;

	/**
	 * To access the database, instantiate the subclass of SQLiteOpenHelper,
	 * i.e. PTAppDbHelper
	 */
	public TransportDAO(Context context) {
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

			cursor = db.query(Transport.TABLE_NAME,
					new String[] { Transport.COL_NAME_ID }, null, null, null,
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

	// add Transport object
	public void addTransport(TransportBean transport) {
		try {
			// Gets the database in write mode(open the database)
			db = dbHelper.getWritableDatabase();

			ContentValues values = new ContentValues();
			values.put(Transport.COL_NAME_ID, transport.getId());
			values.put(Transport.COL_NAME_ROUTE_NAME, transport.getRouteName());
			values.put(Transport.COL_NAME_STOP_NAME, transport.getStopName());
			values.put(Transport.COL_NAME_ZONE_NAME, transport.getZoneName());
			values.put(Transport.COL_NAME_WAY, transport.getWay());
			values.put(Transport.COL_NAME_SPOC_PHONE_NUMBER,
					transport.getSPOCPhoneNumber());
			values.put(Transport.COL_NAME_VEHICLE_NUMBER,
					transport.getVehicleNumber());
			values.put(Transport.COL_NAME_DRIVER_FNAME,
					transport.getDriverFName());
			values.put(Transport.COL_NAME_DRIVER_LNAME,
					transport.getDriverLName());
			values.put(Transport.COL_NAME_DRIVER_PHONE_NUMBER,
					transport.getDriverPhoneNumber());
			values.put(Transport.COL_NAME_CONDUCTOR_FNAME,
					transport.getConductorFName());
			values.put(Transport.COL_NAME_CONDUCTOR_LNAME,
					transport.getConductorLName());
			values.put(Transport.COL_NAME_CONDUCTOR_PHONE_NUMBER,
					transport.getConductorPhoneNumber());

			// Inserting Row
			long insertId = db.insert(Transport.TABLE_NAME, null, values);
			Log.i(TAG,
					"id of new row inserted in Transport tbl: "
							+ String.valueOf(insertId));

		} catch (Exception ex) {
			Log.e(TAG, ex.getMessage());
		} finally {
			db.close();
		}
	}

	public TransportBean getTransport(String TransportId) {
		TransportBean trs = null;
		Cursor c = null;

		try {
			// Gets the database in read mode(open the database)
			db = dbHelper.getReadableDatabase();
			c = db.query(Transport.TABLE_NAME, null, Transport.COL_NAME_ID
					+ "=?", new String[] { TransportId }, null, null, null);

			if (c.moveToFirst()) {
				trs = setTransportBean(c);
			} else {
				Log.v(TAG, "getTransport() method - Query returned no data.");
			}
		} catch (Exception ex) {
			Log.e(TAG, ex.getMessage());
		} finally {
			c.close();
			db.close();
		}
		return trs;
	}

	*//**
	 * @param c
	 * @return
	 *//*
	private TransportBean setTransportBean(Cursor c) {
		TransportBean trs;
		trs = new TransportBean();
		trs.setRouteName(c.getString(c
				.getColumnIndex(Transport.COL_NAME_ROUTE_NAME)));
		trs.setStopName(c.getString(c
				.getColumnIndex(Transport.COL_NAME_STOP_NAME)));
		trs.setZoneName(c.getString(c
				.getColumnIndex(Transport.COL_NAME_ZONE_NAME)));
		trs.setWay(c.getString(c.getColumnIndex(Transport.COL_NAME_WAY)));
		trs.setSPOCPhoneNumber(c.getString(c
				.getColumnIndex(Transport.COL_NAME_SPOC_PHONE_NUMBER)));
		trs.setVehicleNumber(c.getString(c
				.getColumnIndex(Transport.COL_NAME_VEHICLE_NUMBER)));
		trs.setDriverFName(c.getString(c
				.getColumnIndex(Transport.COL_NAME_DRIVER_FNAME)));
		trs.setDriverLName(c.getString(c
				.getColumnIndex(Transport.COL_NAME_DRIVER_LNAME)));
		trs.setDriverPhoneNumber(c.getString(c
				.getColumnIndex(Transport.COL_NAME_DRIVER_PHONE_NUMBER)));
		trs.setConductorFName(c.getString(c
				.getColumnIndex(Transport.COL_NAME_CONDUCTOR_FNAME)));
		trs.setConductorLName(c.getString(c
				.getColumnIndex(Transport.COL_NAME_CONDUCTOR_LNAME)));
		trs.setConductorPhoneNumber(c.getString(c
				.getColumnIndex(Transport.COL_NAME_CONDUCTOR_PHONE_NUMBER)));

		return trs;
	}
*/
}
