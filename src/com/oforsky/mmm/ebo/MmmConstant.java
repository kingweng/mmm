/**
 * MmmConstant.java
 *
 * Copyright (c) 2007-2013 TrueTel Communications. All rights reserved.
 */
package com.oforsky.mmm.ebo;

public class MmmConstant extends MmmBaseConstant {
	private static final String TASVERSION = "TAS-OFF-R5V1P10 2014-08-01 15:49:52";

	// Service can add more constants here
	public static final String YAHOO_FINANCE_URL = "http://real-chart.finance.yahoo.com/table.csv?s=%s.TW%s&%s&g=d&a=0&b=4&c=2000&ignore=.csv";

	public static final String LISTED_DAILY_URL = "http://www.twse.com.tw/ch/trading/exchange/STOCK_DAY/STOCK_DAY_print.php?"
			+ "genpage=genpage/Report%s/%s_F3_1_8_%s.php&type=csv";

	public static final String TICK_URL = "http://mis.twse.com.tw/stock/api/getStockInfo.jsp?ex_ch=%1$s_%2$s.tw_%3$s";

	public static final String CABINET_DAILY_URL = "http://www.otc.org.tw/web/stock/aftertrading/daily_trading_info/st43_result.php?l=zh-tw&d=%s&stkno=%s";

	public static final String[] QUALIFIED_DEALERS = { "凱基", "元大", "國泰", "永豐",
			"群益" };

	public static final String REVENUE_OUTPUT_FORMAT = "Storage[%1$s] current revenue is %2$d";
}
