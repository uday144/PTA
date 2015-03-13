package com.ptapp.bean;

/**
 * This class is our model and contains the data we will save in the database
 * and show in the user interface.
 */
public class FeeBean {
	// private variables
	private String id; // PK
	private String ReceiptNumber;
	private String FeeCycle;
	private String PaidOn;
	private String Amount;

	// Empty constructor
	public FeeBean() {

	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getReceiptNumber() {
		return ReceiptNumber;
	}

	public void setReceiptNumber(String receiptNumber) {
		ReceiptNumber = receiptNumber;
	}

	public String getFeeCycle() {
		return FeeCycle;
	}

	public void setFeeCycle(String feeCycle) {
		FeeCycle = feeCycle;
	}

	public String getPaidOn() {
		return PaidOn;
	}

	public void setPaidOn(String paidOn) {
		PaidOn = paidOn;
	}

	public String getAmount() {
		return Amount;
	}

	public void setAmount(String amount) {
		Amount = amount;
	}

}
