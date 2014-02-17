package com.trackr.trackr;

import android.os.Build;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.TabActivity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

@SuppressWarnings("deprecation")
public class MainActivity extends TabActivity {

	// Tab Names for TabSpecs
	private static final String EXPENSES = "Expenses";
	private static final String INCOMES = "Incomes";

	private TabHost tabHost;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			ActionBar ab = getActionBar();
			ab.setDisplayShowHomeEnabled(false);
		}

		tabHost = getTabHost();
		tabHost.setup();

		// Creating Expenses tab and adding it to tabHost
		TabSpec expensesSpec = tabHost.newTabSpec(EXPENSES);
		expensesSpec.setIndicator(EXPENSES);
		Intent expensesIntent = new Intent(this, EntryViewActivity.class);
		expensesIntent.putExtra("com.trackr.trackr.type", "expenses");
		expensesSpec.setContent(expensesIntent);
		tabHost.addTab(expensesSpec);

		// Creating Incomes tab
		TabSpec incomesSpec = tabHost.newTabSpec(INCOMES);
		incomesSpec.setIndicator(INCOMES);
		Intent incomesIntent = new Intent(this, EntryViewActivity.class);
		incomesIntent.putExtra("com.trackr.trackr.type", "incomes");
		incomesSpec.setContent(incomesIntent);
		tabHost.addTab(incomesSpec);

	}

	// Creating options menu that becomes visible by pressing menu or on Android
	// 4.x displays items in the actionbar
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	// Determines the action when option menu item is selected. Pressing
	// menu_add_entry_ opens AddEntry screen and based on the active tab, the
	// tab is for adding an expense or an income.
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent;

		switch (item.getItemId()) {
		case R.id.menu_add_entry:
			intent = new Intent(this, AddEntry.class);

			if (tabHost.getCurrentTabTag().equals(EXPENSES)) {
				intent.putExtra("type", SQLiteHelper.TABLE_EXPENSES);
				startActivityForResult(intent, 1);
			} else if (tabHost.getCurrentTabTag().equals(INCOMES)) {
				intent.putExtra("type", SQLiteHelper.TABLE_INCOMES);
				startActivity(intent);
			}
			break;
		}
		return false;
	}

}
