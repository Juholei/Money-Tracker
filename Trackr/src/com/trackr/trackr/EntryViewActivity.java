package com.trackr.trackr;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ListView;

public class EntryViewActivity extends Activity implements OnClickListener {

	private String type;
	private ListView listView;
	private TextView totalSum, monthView, balanceView;
	private ArrayList<Entry> entryList;
	private EntryAdapter adapter;
	private EntryDataSource eds;
	private ImageButton lastMonth, nextMonth;
	private int month;
	private int year;

	// Called when the view is created.
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main_view);
		type = getIntent().getExtras().getString("com.trackr.trackr.type");

		// Getting date
		Calendar cal = Calendar.getInstance();
		month = cal.get(Calendar.MONTH) + 1;
		year = cal.get(Calendar.YEAR);

		// Initializing the data source
		eds = new EntryDataSource(this);

		// Initializing the ListView for the entries, then setting the listView
		// and registering the listView for Context Menu
		listView = (ListView) findViewById(R.id.entryList);
		registerForContextMenu(listView);

		// Initializing Buttons and setting onClickListeners
		lastMonth = (ImageButton) findViewById(R.id.bLastMonth);
		lastMonth.setOnClickListener(this);

		nextMonth = (ImageButton) findViewById(R.id.bNextMonth);
		nextMonth.setOnClickListener(this);

		// Initializing TextViews
		totalSum = (TextView) findViewById(R.id.totalSum);

		balanceView = (TextView) findViewById(R.id.tvBalance);

		monthView = (TextView) findViewById(R.id.tvMonth);

		// Setting the information on screen
		updateMonth();
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {

		// Checking if the Context Menu was opened from the Expense list and if
		// so, adding a menu item for Delete.
		if (v.getId() == R.id.entryList) {
			menu.add(Menu.NONE, Menu.NONE, Menu.NONE, "Delete");
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {

		// Getting the info from MenuItem parameter to find out which list
		// item/entry was pressed
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
				.getMenuInfo();
		Entry entryForMenu = entryList.get(info.position);

		// Deleting the entry from the database, removing it from the list
		// adapter,
		// notifying the adapter of the changes and updating the totalSum
		// TextView
		eds.deleteEntry(entryForMenu.getId(), type);
		updateMonth();

		// Informing the user of the deletion with a toast notification
		Toast toast = Toast.makeText(getApplicationContext(),
				entryForMenu.getTitle() + " removed", Toast.LENGTH_SHORT);
		toast.show();

		return true;
	}

	@Override
	public void onResume() {
		super.onResume();
		updateMonth();
	}

	private void updateMonth() {

		// getting entryList filled with the
		// entries of the current month
		// and creating the list item adapter for our entryList.
		entryList = eds.getEntriesForMonth(year, month, type);
		adapter = new EntryAdapter(EntryViewActivity.this, R.id.entryList,
				entryList);
		listView.setAdapter(adapter);

		// TextViews
		BigDecimal balance = eds.getSumForMonth(year, month,
				"incomes").subtract(eds.getSumForMonth(year, month,
						"expenses"));
		String formattedSum = Entry.getFormattedSum(entryList);
		String monthName = this.getResources().getStringArray(
				R.array.month_names)[month - 1];
		monthView.setText(monthName + " " + year);
		balanceView.setText(monthName + "'s Balance: "
				+ Entry.formatValue(balance) + "�");

		if (type == "expenses") {
			totalSum.setText("-" + formattedSum);
			totalSum.setTextColor(Color.RED);

		} else {
			totalSum.setText("+" + formattedSum);
			totalSum.setTextColor(Color.GREEN);
		}
		if (balance.signum() == -1) {
			balanceView.setTextColor(Color.parseColor("#FD543C"));
		} else {
			balanceView.setTextColor(Color.parseColor("#99CC00"));
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		// Change month based on the button pressed
		case R.id.bLastMonth:
			if (month == 1) {
				month = 12;
				year--;
			} else {
				month--;
			}
			break;
		case R.id.bNextMonth:
			if (month == 12) {
				month = 1;
				year++;
			} else {
				month++;
			}
			break;

		}

		// Update the view
		updateMonth();

	}
}
