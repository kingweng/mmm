/**
 * StockTypeEnum.java
 *
 * Copyright (c) 2007-2013 TrueTel Communications. All rights reserved.
 */
package com.oforsky.mmm.ebo;

import java.util.Calendar;

public enum StockTypeEnum {
			Unused_0(0) {
				@Override
				public String getHistoryUrl(String code) throws Exception {
					new UnsupportedOperationException("Cannot be here!");
					return code;
				}

				@Override
				public String getDailyUrl(String code, String yyyymm)
						throws Exception {
					new UnsupportedOperationException("Cannot be here!");
					return code;
				}

				@Override
				public String getTickUrl(String code, String yyyymm) {
					new UnsupportedOperationException("Cannot be here!");
					return null;
				}

			}
			, Listed(1) {
				@Override
				public String getHistoryUrl(String code) throws Exception {
					return String.format(MmmConstant.YAHOO_FINANCE_URL, code,
							"", getTodayFormat());
				}

				@Override
				public String getDailyUrl(String code, String yyyymm) throws Exception {
					return String.format(MmmConstant.LISTED_DAILY_URL, yyyymm, yyyymm,
							code);
				}

				@Override
				public String getTickUrl(String code, String yyyymmdd) {
					return String.format(MmmConstant.TICK_URL, "tse", code, yyyymmdd);
				}

			}
			, Cabinet(2) {
				@Override
				public String getHistoryUrl(String code) throws Exception {
					return String.format(MmmConstant.YAHOO_FINANCE_URL, code,
							"O", getTodayFormat());
				}

				@Override
				public String getDailyUrl(String code, String yyyymm) throws Exception {
					return String.format(MmmConstant.CABINET_DAILY_URL,
							getReMonthStr(yyyymm), code);
				}

				@Override
				public String getTickUrl(String code, String yyyymmdd) {
					return String.format(MmmConstant.TICK_URL, "otc", code, yyyymmdd);
				}

			}
	;

	private static StockTypeEnum[] allEnums = {
		Listed
		, Cabinet
	};

	private StockTypeEnum(int value) {
	}

	public static StockTypeEnum[] getAllEnums() {
		return allEnums;
	}

	public int value() {
		return ordinal();
	}

	public static StockTypeEnum getEnum(int value) {
		switch (value) {
		case 1:
			return Listed;
		case 2:
			return Cabinet;
		default:
			return null;
		}
	}

	public static StockTypeEnum getEnum(String value) {
		return StockTypeEnum.valueOf(value);
	}

	/**
     * Checks whether the enum's value is greater than the input enum's value.
     */
    public boolean above(StockTypeEnum input) {
        return compareTo(input) > 0;
    }

    /**
     * Checks whether the enum's value is less than the input enum's value.
     */
    public boolean below(StockTypeEnum input) {
        return compareTo(input) < 0;
    }
	
    public abstract String getHistoryUrl(String code) throws Exception;
    
    private static String getTodayFormat() {
		// format as d=10&e=21&f=2014
		Calendar today = Calendar.getInstance();
		return "d=" + today.get(Calendar.MONTH) + "&e="
				+ today.get(Calendar.DAY_OF_MONTH) + "&f="
				+ today.get(Calendar.YEAR);
	}
    
	private static String getReMonthStr(String yyyymm) throws Exception {
		Integer year = Integer.parseInt(yyyymm.substring(0, 4));
		return String.valueOf(year - 1911) + "/" + yyyymm.substring(4);
	}
    
    public abstract String getDailyUrl(String code, String yyyymm) throws Exception;
    
    public abstract String getTickUrl(String code, String yyyymmdd);
}
