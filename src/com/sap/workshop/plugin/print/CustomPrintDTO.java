package com.sap.workshop.plugin.print;

/**
 * Custom DTO which will hol the data for the print
 * @author D060523
 *
 */
public class CustomPrintDTO {

	private String someText;
	
	private int someNumber;

	public String getSomeText() {
		return someText;
	}

	public void setSomeText(String someText) {
		this.someText = someText;
	}

	public int getSomeNumber() {
		return someNumber;
	}

	public void setSomeNumber(int someNumber) {
		this.someNumber = someNumber;
	}
}
