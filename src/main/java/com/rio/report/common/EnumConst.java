package com.rio.report.common;



public enum EnumConst {

	OK("OK"), FAIL("FAIL"), SUCCESS("success"),

	// Block Option
	ALLOW("allow"), Deny("deny"),

	// Role
	ROLE("role");


	private String value;

	EnumConst(String value) {
		this.value = value;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}


}
