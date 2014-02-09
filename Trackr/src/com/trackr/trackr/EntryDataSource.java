package com.trackr.trackr;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class EntryDataSource {

	private SQLiteDatabase database;
	private SQLiteHelper dbHelper;

	public EntryDataSource(Context context) {
		dbHelper = new SQLiteHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	// Adds the event to database based on the parameters given. Type determines
	// whether the entry goes to expenses or incomes table. Timestamp is added here.
	// Returns true if event is successfully added to database, false if not.
	// Reasons failing are that database call returns with error code -1 or
	// the event type given is wrong.
	public boolean addEntry(Entry entry, String type) {

		// Getting timestamp
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US)
				.format(Calendar.getInstance().getTime());

		// Creating an object where we put the values and which we then pass to
		// the database.insert() function.
		ContentValues values = new ContentValues();

		values.put(SQLiteHelper.COLUMN_TITLE, entry.getTitle());
		values.put(SQLiteHelper.COLUMN_AMOUNT, entry.getAmount().toString());
		values.put(SQLiteHelper.COLUMN_TIMESTAMP, timeStamp);
		values.put(SQLiteHelper.COLUMN_DAY, entry.getDay());
		values.put(SQLiteHelper.COLUMN_MONTH, entry.getMonth());
		values.put(SQLiteHelper.COLUMN_YEAR, entry.getYear());

		if (type.equals(SQLiteHelper.TABLE_EXPENSES)
				|| type.equals(SQLiteHelper.TABLE_INCOMES)) {
			open();
			long insertID = database.insert(type, null, values);
			close();

			if (insertID != -1) {
				return true;
			}
		}
		return false;

	}

	// Deletes the entry with the id from the table that matches the type
	public void deleteEntry(int id, String type) {
		open();
		database.delete(type, SQLiteHelper.COLUMN_ID + " = " + id, null);
		close();
	}

	// Returns an ArrayList filled with the entries that match year, month and
	// type given as a parameter
	public ArrayList<Entry> getEntriesForMonth(int year, int month, String type) {
		ArrayList<Entry> entries = new ArrayList<Entry>();
		open();

		Cursor cursor = database.query(type, null, SQLiteHelper.COLUMN_YEAR
				+ " = " + year + " and " + SQLiteHelper.COLUMN_MONTH + " = "
				+ month, null, null, null, SQLiteHelper.COLUMN_DAY
				+ " desc");
		cursor.moveToFirst();

		while (!cursor.isAfterLast()) {
			Entry entry = cursorToEntry(cursor);
			entries.add(entry);
			cursor.moveToNext();
		}
		cursor.close();
		close();
		return entries;
	}
//	Returns the sum of all Entries of the month as a BigDecimal object. 
	public BigDecimal getSumForMonth(int year, int month, String type) {
		ArrayList<Entry> entries = getEntriesForMonth(year, month, type);
		
		return Entry.getSum(entries);
	}

	// Converts Cursor object returned by the database to an Entry object
	private Entry cursorToEntry(Cursor cursor) {
		Entry entry = new Entry();

		entry.setId(cursor.getInt(cursor.getColumnIndex(SQLiteHelper.COLUMN_ID)));
		entry.setAmount(cursor.getString(cursor
				.getColumnIndex(SQLiteHelper.COLUMN_AMOUNT)));
		entry.setTitle(cursor.getString(cursor
				.getColumnIndex(SQLiteHelper.COLUMN_TITLE)));
		entry.setYear(cursor.getInt(cursor
				.getColumnIndex(SQLiteHelper.COLUMN_YEAR)));
		entry.setMonth(cursor.getInt(cursor
				.getColumnIndex(SQLiteHelper.COLUMN_MONTH)));
		entry.setDay(cursor.getInt(cursor
				.getColumnIndex(SQLiteHelper.COLUMN_DAY)));

		return entry;
	}
}