package com.trackr.trackr;

import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

public class AddEntry extends Activity implements OnClickListener,
		OnDateSetListener {

	private Button cancel, save;
	private EditText amount, description, date;
	private Entry entry;
	private EntryDataSource eds;
	private String type;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addentry);

		Bundle extras = getIntent().getExtras();
		type = extras.getString("type");

		if (type.equals("expenses")) {
			setTitle("New Expense");
		} else {
			setTitle("New Income");
		}

		// Setting Buttons and OnClickListeners
		cancel = (Button) findViewById(R.id.bCancel);
		cancel.setOnClickListener(this);

		save = (Button) findViewById(R.id.bSave);
		save.setOnClickListener(this);

		// Setting OnClickListener to date EditText
		date = (EditText) findViewById(R.id.edDate);
		date.setOnClickListener(this);

		description = (EditText) findViewById(R.id.edDescription);
		amount = (EditText) findViewById(R.id.edAmount);

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.bCancel:
			finish();
			break;

		// Creating and opening DatePickerDialog when date EditText is pressed
		case R.id.edDate:
			Calendar cal = Calendar.getInstance();
			int day = cal.get(Calendar.DAY_OF_MONTH);
			int month = cal.get(Calendar.MONTH);
			int year = cal.get(Calendar.YEAR);
			DatePickerDialog datePicker = new DatePickerDialog(this, this,
					year, month, day);
			datePicker.show();
			break;

		// Checking that required inputs are filled and passing data to
		// database. Showing toast notification when adding to database
		// succeeds.
		case R.id.bSave:

			if (description.getText().toString().length() > 0
					&& amount.getText().toString().length() > 0
					&& date.getText().length() > 0) {
				String desc = description.getText().toString();
				String a = amount.getText().toString();
				entry.setTitle(desc);
				entry.setAmount(a);

				eds = new EntryDataSource(this);

				if (eds.addEntry(entry, type)) {
					Toast toast = Toast.makeText(getApplicationContext(),
							entry.getTitle() + " added to " + type,
							Toast.LENGTH_SHORT);
					toast.show();
				}

				setResult(RESULT_OK);
				finish();

			}

		}
	}

	// Handling the date after DatePickerDialog finishes. Creating a new Entry
	// and putting the date data in it.
	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		date.setText(dayOfMonth + "." + (monthOfYear + 1) + "." + year);
		entry = new Entry();
		entry.setYear(year);
		entry.setMonth(monthOfYear + 1);
		entry.setDay(dayOfMonth);

	}

}
