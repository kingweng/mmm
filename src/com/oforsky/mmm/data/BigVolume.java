package com.oforsky.mmm.data;

import java.io.Serializable;

public class BigVolume implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String code;
	private int recordHigh;
	private double times;

	public BigVolume(String code, int recordHigh, double times) {
		super();
		this.code = code;
		this.recordHigh = recordHigh;
		this.times = times;
	}

	public String getCode() {
		return code;
	}

	public int getRecordHigh() {
		return recordHigh;
	}

	public double getTimes() {
		return times;
	}

}
