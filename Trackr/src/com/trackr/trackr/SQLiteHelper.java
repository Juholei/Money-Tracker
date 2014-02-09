package com.trackr.trackr;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLiteHelper extends SQLiteOpenHelper {

//	Setting the SQL related stuff as static final Strings.
	
	public static final String TABLE_EXPENSES = "expenses";
	public static final String TABLE_INCOMES = "incomes";

	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_TITLE = "title";
	public static final String COLUMN_AMOUNT = "amount";
	public static final String COLUMN_TIMESTAMP = "timestamp";
	public static final String COLUMN_YEAR = "year";
	public static final String COLUMN_MONTH = "month";
	public static final String COLUMN_DAY = "day";

	private static final String DATABASE_NAME = "trackr_db.db";
	private static final int DATABASE_VERSION = 1;

//	SQL create statements based on those variables set above
	private static final String EXPENSES_CREATE = 
			"create table " + TABLE_EXPENSES + "(" + 
				COLUMN_ID + " integer primary key autoincrement, " + 
				COLUMN_TITLE + " text not null, " + 
				COLUMN_AMOUNT + " integer not null, " + 
				COLUMN_YEAR + " integer not null, " + 
				COLUMN_MONTH + " integer not null, " + 
				COLUMN_DAY + " integer not null, " + 
				COLUMN_TIMESTAMP + " text not null);";
	
	private static final String INCOMES_CREATE = 
			"create table " + TABLE_INCOMES + "(" + 
					COLUMN_ID + " integer primary key autoincrement, " + 
					COLUMN_TITLE + " text not null, " + 
					COLUMN_AMOUNT + " integer not null, " + 
					COLUMN_YEAR + " integer not null, " + 
					COLUMN_MONTH + " integer not null, " + 
					COLUMN_DAY + " integer not null, " + 
					COLUMN_TIMESTAMP + " text not null);";
	
	public SQLiteHelper(Context context){
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
//	Creating the tables in the database
	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(EXPENSES_CREATE);
		database.execSQL(INCOMES_CREATE);
	}

	
//	Drops the existing tables if the table structure has been changed. This must be overridden for the class to work.
	  @Override
	  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	    Log.w(SQLiteHelper.class.getName(),
	        "Upgrading database from version " + oldVersion + " to "
	            + newVersion + ", which will destroy all old data");
	    db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPENSES);
	    db.execSQL("DROP TABLE IF EXISTS " + TABLE_INCOMES);
	    onCreate(db);
	  }

}
