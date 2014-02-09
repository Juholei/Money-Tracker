package com.trackr.trackr;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class EntryAdapter extends ArrayAdapter<Entry> {

	private ArrayList<Entry> entries;
	private Activity activity;

	public EntryAdapter(Activity a, int textViewResourceId,
			ArrayList<Entry> entries) {
		super(a, textViewResourceId, entries);
		this.entries = entries;
		this.activity = a;
	}

	// Container class for the TextViews
	public static class ViewHolder {
		public TextView title;
		public TextView amount;
		public TextView date;
	}

	// Creating a View based on our custom layout. TextViews are populated with
	// the variables of the Entry objects.
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		ViewHolder holder;

		// If the view is null, it is created with our custom layout list_item.
		// Then the TextViews are created in the holder object based on the
		// fields declared in the layout and the holder is passed
		// to the View object. If the View is not null, the holder is extracted
		// from the View.
		if (v == null) {
			LayoutInflater li = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = li.inflate(R.layout.list_item, null);

			holder = new ViewHolder();
			holder.title = (TextView) v.findViewById(R.id.title);
			holder.amount = (TextView) v.findViewById(R.id.amount);
			holder.date = (TextView) v.findViewById(R.id.date);
			v.setTag(holder);
		} else {
			holder = (ViewHolder) v.getTag();
		}

		// Position is used to get the right Entry from the ArrayList. The
		// TextViews are then set based on the entry's attributes.
		final Entry entry = entries.get(position);

		if (entry != null) {
			holder.title.setText(entry.getTitle());
			holder.amount.setText(entry.getFormattedAmount());
			holder.date.setText(entry.getDate());
		}
		return v;
	}

//	Overridden to return the Id of the Entry object that corresponds with the position given as a parameter.
	@Override
	public long getItemId(int position) {
		Entry entry = entries.get(position);
		return entry.getId();
	}
}
