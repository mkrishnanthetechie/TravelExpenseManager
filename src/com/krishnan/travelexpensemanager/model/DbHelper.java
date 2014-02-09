package com.krishnan.travelexpensemanager.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import com.krishnan.travelexpensemanager.util.Utility;

/**
 * @author krishm90
 * Creates the database / application tables
 * Concrete class to perform data base operations. 
 *
 */
public class DbHelper extends SQLiteOpenHelper implements ExpenseManagerModel {

	private static final String DB_NAME = "travelexpensemanager.db";
	private static final int dbVersion = 1;

	Context mContext;

	public DbHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, DB_NAME, factory, dbVersion);
	}

	public DbHelper(Context context) {
		super(context, DB_NAME, null, dbVersion);
		this.mContext = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		createTables(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		Utility.out("Upgrading from current version " + oldVersion + " to "
				+ newVersion);
		onCreate(db);
	}

	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		Utility.out("Downgrading from current version " + oldVersion + " to "
				+ newVersion);
		onCreate(db);
	}

	public String getDataPath() {
		return mContext.getDatabasePath(DB_NAME).toString();
	}

	public SQLiteDatabase openDataBase() throws SQLException {

		// Opens the database
		String myPath = getDataPath();
		Utility.out("DB Path :" + myPath);
		createDataBase(myPath);
		return SQLiteDatabase.openDatabase(myPath, null,
				SQLiteDatabase.OPEN_READWRITE);
	}

	/*
	 * private boolean checkDataBase(String path) {
	 * 
	 * SQLiteDatabase checkDB = null;
	 * 
	 * try { checkDB = SQLiteDatabase.openDatabase(path, null,
	 * SQLiteDatabase.OPEN_READONLY);
	 * 
	 * } catch (SQLiteException e) {
	 * 
	 * }
	 * 
	 * finally { if (checkDB != null) { checkDB.close(); } }
	 * 
	 * return checkDB != null ? true : false; }
	 */

	public void createDataBase(String path) throws SQLException {
		/*
		 * boolean dbExist = checkDataBase(path);
		 * 
		 * if (dbExist) { Utility.out("Data base already exists"); } else {
		 * 
		 * // By calling this method and empty database will be created into //
		 * the default system path // of your application so we are gonna be
		 * able to overwrite that // database with our database.
		 * this.getWritableDatabase(); }
		 */
		this.getWritableDatabase();
	}

	public void createTables(SQLiteDatabase db) throws SQLException {
		db.execSQL(TABLE_REPORT_DETAILS_DROP);
		Utility.out(TABLE_REPORT_DETAILS + "dropped. ");

		db.execSQL(TABLE_PERSON_REPORT_DETAILS_DROP);
		Utility.out(TABLE_PERSON_REPORT_DETAILS + "dropped. ");

		db.execSQL(TABLE_EXPENSE_DETAILS_DROP);
		Utility.out(TABLE_EXPENSE_DETAILS + "dropped. ");

		db.execSQL(TABLE_SHARED_EXPENSE_DETAILS_DROP);
		Utility.out(TABLE_SHARED_EXPENSE_DETAILS + " dropped. ");

		db.execSQL(TABLE_REPORT_DETAILS_CREATE);
		Utility.out(TABLE_REPORT_DETAILS + "Created. ");

		db.execSQL(TABLE_PERSON_REPORT_DETAILS_CREATE);
		Utility.out(TABLE_PERSON_REPORT_DETAILS + "Created. ");

		db.execSQL(TABLE_EXPENSE_DETAILS_CREATE);
		Utility.out(TABLE_EXPENSE_DETAILS + " Created. ");

		db.execSQL(TABLE_SHARED_EXPENSE_DETAILS_CREATE);
		Utility.out(TABLE_SHARED_EXPENSE_DETAILS + " Created. ");
	}

	public long insertTableData(SQLiteDatabase db, String table_Name,
			ContentValues cv) throws SQLException {
		return db.insert(table_Name, null, cv);
	}

	public Cursor getCursorFromTableName(SQLiteDatabase db, String table_Name,
			String[] columns) throws SQLException {
		return db.query(table_Name, columns, null, null, null, null, null);
	}

	public Cursor getCursorFromTableNameWithSelection(SQLiteDatabase db,
			String table_Name, String[] columns, String selection,
			String[] selection_Args) throws SQLException {
		return db.query(table_Name, columns, selection, selection_Args, null,
				null, null);
	}

	public Cursor getCursorFromTableNameWithSelectionSorted(SQLiteDatabase db,
			String table_Name, String[] columns, String selection,
			String[] selection_Args, String orderBy) throws SQLException {
		return db.query(table_Name, columns, selection, selection_Args, null,
				null, orderBy);
	}

	public void deleteTableRowsWithSelection(SQLiteDatabase db,
			String table_Name, String selection, String... selection_Args)
			throws Exception {
		db.delete(table_Name, selection, selection_Args);
	}

	public Cursor getCursorFromRawQuery(SQLiteDatabase db, String sql,
			String... args) throws SQLException {
		return db.rawQuery(sql, args);
	}
}
