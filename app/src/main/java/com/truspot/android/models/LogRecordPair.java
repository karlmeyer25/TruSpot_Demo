package com.truspot.android.models;


/**
 * */
public class LogRecordPair {
	private String name;
	private String strValue;
	private boolean putQuotesAroundValue;

	public LogRecordPair(String name, String strValue, boolean putQuotesAroundValue) {
		this.name = name;
		this.strValue = strValue;
		this.putQuotesAroundValue = putQuotesAroundValue;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStrValue() {
		return strValue;
	}

	public void setStrValue(String strValue) {
		this.strValue = strValue;
	}

	public boolean isPutQuotesAroundValue() {
		return putQuotesAroundValue;
	}

	public void setPutQuotesAroundValue(boolean putQuotesAroundValue) {
		this.putQuotesAroundValue = putQuotesAroundValue;
	}
}
