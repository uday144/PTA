package com.ptapp.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.ptapp.provider.PTAppDatabase;

/** All CRUD(Create, Read, Update, Delete) Operations */
public class ClassDAO {
	private String ERROR_TAG = "ERR_DC_Class";
	private String INFO_TAG = "INFO_DC_Class";

	// Database fields
	private SQLiteDatabase db;
	private PTAppDatabase dbHelper;

	/**
	 * To access the database, instantiate the subclass of SQLiteOpenHelper,
	 * i.e. PTAppDbHelper
	 */
	public ClassDAO(Context context) {
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

			cursor = db.query(SchoolClass.TABLE_NAME,
					new String[] { SchoolClass.COL_NAME_ID }, null, null, null,
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

	public ClassBean getSchoolClass(String classId) {
		ClassBean clas = null;
		Cursor c = null;

		try {
			// Gets the database in read mode(open the database)
			db = dbHelper.getReadableDatabase();
			c = db.query(SchoolClass.TABLE_NAME, null, SchoolClass.COL_NAME_ID
					+ "=?", new String[] { classId }, null, null, null);

			if (c.moveToFirst()) {
				clas = new ClassBean();
				clas.setClassName(c.getString(c
						.getColumnIndex(SchoolClass.COL_NAME_CLASS_NAME)));
				clas.setSection(c.getString(c
						.getColumnIndex(SchoolClass.COL_NAME_SECTION)));
				clas.setSchoolId(c.getString(c
						.getColumnIndex(SchoolClass.COL_NAME_SCHOOL_ID)));
				clas.setClsInchargeEducatorId(c.getString(c
						.getColumnIndex(SchoolClass.COL_NAME_CLS_INCHARGE_EDUCATOR_ID)));
				clas.setAttendantId(c.getString(c
						.getColumnIndex(SchoolClass.COL_NAME_ATTENDANT_ID)));
				clas.setFeeId(c.getString(c
						.getColumnIndex(SchoolClass.COL_NAME_FEE_ID)));
				clas.setTransportId(c.getString(c
						.getColumnIndex(SchoolClass.COL_NAME_TRANSPORT_ID)));
				clas.setStudentId(c.getString(c
						.getColumnIndex(SchoolClass.COL_NAME_STUDENT_ID)));
				clas.setSessionYear(c.getString(c
						.getColumnIndex(SchoolClass.COL_NAME_SESSION_YEAR)));
			} else {
				Log.v(INFO_TAG,
						"getSchoolClass() method - Query returned nil data.");
			}
		} catch (Exception ex) {
			Log.e(ERROR_TAG, ex.getMessage());
		} finally {
			c.close();
			db.close();
		}
		return clas;
	}

	*//** Getting Name of the class *//*
	public String getNameOfClass(String classId) {
		String className = "";
		Cursor c = null;
		try {
			// Gets the database in read mode(open the database)
			db = dbHelper.getReadableDatabase();
			c = db.query(SchoolClass.TABLE_NAME,
					new String[] { SchoolClass.COL_NAME_CLASS_NAME },
					SchoolClass.COL_NAME_ID + "=?", new String[] { classId },
					null, null, null);

			if (c.moveToFirst()) {
				className = c.getString(c
						.getColumnIndex(SchoolClass.COL_NAME_CLASS_NAME));
			} else {
				Log.v(INFO_TAG, "getNameOfClass method - returned no data.");
			}
		} catch (Exception ex) {
			Log.e(ERROR_TAG, ex.getMessage());
		} finally {
			c.close();
			db.close();
		}
		return className;
	}

	// get class inchargeId
	public long getClassInchargeEducatorId(long classId) {
		long inchrgId = 0;
		Cursor cursor = null;
		try {
			// Gets the database in read mode(open the database)
			db = dbHelper.getReadableDatabase();

			cursor = db
					.query(SchoolClass.TABLE_NAME,
							new String[] { SchoolClass.COL_NAME_CLS_INCHARGE_EDUCATOR_ID },
							SchoolClass.COL_NAME_ID + "=?",
							new String[] { String.valueOf(classId) }, null,
							null, null, null);

			if (cursor.moveToFirst()) {
				inchrgId = cursor
						.getLong(cursor
								.getColumnIndex(SchoolClass.COL_NAME_CLS_INCHARGE_EDUCATOR_ID));

			}
		} catch (Exception ex) {
			Log.e(ERROR_TAG, ex.getMessage());
		} finally {
			cursor.close(); // Closing the cursor
			db.close(); // Closing database connection

		}
		return inchrgId;
	}

	public void addSchoolClass(ClassBean cls) {
		try {
			// Gets the database in write mode(open the database)
			db = dbHelper.getWritableDatabase();

			ContentValues values = new ContentValues();
			values.put(SchoolClass.COL_NAME_ID, cls.getId());
			values.put(SchoolClass.COL_NAME_CLASS_NAME, cls.getClassName());
			values.put(SchoolClass.COL_NAME_SECTION, cls.getSection());
			values.put(SchoolClass.COL_NAME_SCHOOL_ID, cls.getSchoolId());
			values.put(SchoolClass.COL_NAME_CLS_INCHARGE_EDUCATOR_ID,
					cls.getClsInchargeEducatorId());
			values.put(SchoolClass.COL_NAME_ATTENDANT_ID, cls.getAttendantId());
			values.put(SchoolClass.COL_NAME_FEE_ID, cls.getFeeId());
			values.put(SchoolClass.COL_NAME_TRANSPORT_ID, cls.getTransportId());
			values.put(SchoolClass.COL_NAME_STUDENT_ID, cls.getStudentId());
			values.put(SchoolClass.COL_NAME_SESSION_YEAR, cls.getSessionYear());

			// Inserting Row
			long insertId = db.insert(SchoolClass.TABLE_NAME, null, values);
			Log.i(INFO_TAG,
					"id of new row inserted in Class tbl: "
							+ String.valueOf(insertId));

		} catch (Exception ex) {
			Log.e(ERROR_TAG, ex.getMessage());
		} finally {
			db.close();
		}
	}
*/
	// /**
	// * Adds the row in a table
	// */
	// public void addRowToClass(String className, String section, long
	// schoolId,
	// long clsInchargeEducatorId, long attendantId, long busConductorId,
	// long busDriverId, long studentId, String sessionYear) {
	// try {
	// // Gets the database in write mode(open the database)
	// db = dbHelper.getWritableDatabase();
	//
	// ContentValues values = new ContentValues();
	// values.put(SchoolClass.COL_NAME_CLASS_NAME, className);
	// values.put(SchoolClass.COL_NAME_SECTION, section);
	// values.put(SchoolClass.COL_NAME_SCHOOL_ID, schoolId);
	// values.put(SchoolClass.COL_NAME_CLS_INCHARGE_EDUCATOR_ID,
	// clsInchargeEducatorId);
	// values.put(SchoolClass.COL_NAME_ATTENDANT_ID, attendantId);
	// values.put(SchoolClass.COL_NAME_BUSCONDUCTOR_ID, busConductorId);
	// values.put(SchoolClass.COL_NAME_FEE_ID, busDriverId);
	// values.put(SchoolClass.COL_NAME_STUDENT_ID, studentId);
	// values.put(SchoolClass.COL_NAME_SESSION_YEAR, sessionYear);
	//
	// // Inserting Row
	// long insertId = db.insert(SchoolClass.TABLE_NAME, null, values);
	// Log.i(INFO_TAG,
	// "id of new row inserted in Class tbl: "
	// + String.valueOf(insertId));
	//
	// db.close(); // Closing database connection
	//
	// } catch (SQLException ex) {
	// Log.e(ERROR_TAG, ex.getMessage());
	//
	// }
	// }

}
