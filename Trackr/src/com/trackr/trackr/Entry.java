package com.trackr.trackr;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;

public class Entry {
	private String title;
	private BigDecimal amount;
	private int id;
	private int year;
	private int month;
	private int day;

	public Entry(String title, BigDecimal amount, int year, int month, int day) {
		this.title = title;
		this.amount = amount;
		this.year = year;
		this.month = month;
		this.day = day;
	}

	public Entry() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public BigDecimal getAmount() {
		return amount;
	}

//	Parameter is a string, which is used to create a new BigDecimal object
	public void setAmount(String amount) {
		this.amount = new BigDecimal(amount);
	}

//	 Returns the amount of the entry as a string. The amount has been divided
//	 by 1000 and is rounded to 2 decimals.
//	 It is also formatted to be always displayed with two decimals. € sign is
//	 also added to the end.
	public String getFormattedAmount() {
		return formatValue(amount) + "€";
	}

//	Returns date in the European format, dd.mm.yyyy
	public String getDate() {
		String date = this.getDay() + "." + this.getMonth() + "."
				+ this.getYear();

		return date;
	}

//	Takes an ArrayList of Entries as a parameter. Iterates over the list and
//	counts the sum of the amounts.
	static public BigDecimal getSum(ArrayList<Entry> entries) {
		Iterator<Entry> iter = entries.iterator();
		Entry entry;
		BigDecimal sum = new BigDecimal(0);

		while (iter.hasNext()) {
			entry = iter.next();
			sum = sum.add(entry.getAmount());

		}
		return sum;
	}
//	Calculates the sum with getSum() and formats it to a string with € sign.
//	Converts the sum to the proper format and to a string where the sum is
//	displayed with two decimals and € sign.
	static public String getFormattedSum(ArrayList<Entry> entries) {
		BigDecimal sum = getSum(entries);

		return formatValue(sum) + "€";
	}

	// Converts the given value to the proper display form by rounding it to 2 decimals.
	static public String formatValue(BigDecimal value) {
		BigDecimal rounded = value.setScale(2, BigDecimal.ROUND_HALF_EVEN);

		return rounded.toString();
	}
}