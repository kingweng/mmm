/**
 * StockEbo.java
 *
 * Copyright (c) 2007-2013 TrueTel Communications. All rights reserved.
 */
package com.oforsky.mmm.ebo;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.apache.log4j.Logger;

import com.oforsky.mmm.util.CsvParsable;
import com.oforsky.mmm.util.Statistics;
import com.truetel.jcore.util.AppException;
import com.truetel.jcore.util.TimeUtil;

@Entity
@Table(name = StockCoreEbo.DB_TABLE_NAME)
@NamedQueries({
		@NamedQuery(name = StockCoreEbo.QUERY_LISTALL_NAME, query = StockCoreEbo.QUERY_LISTALL),
		@NamedQuery(name = StockCoreEbo.QUERY_COUNTALL_NAME, query = StockCoreEbo.QUERY_COUNTALL) })
public class StockEbo extends StockCoreEbo implements CsvParsable<StockEbo> {
	private static final long serialVersionUID = 1L;

	private static final String TASVERSION = "TAS-OFF-R5V1P4 2014-04-30 16:43:36";

	private static final Logger log = Logger.getLogger(StockEbo.class);

	public StockEbo() {
	}

	private Double parseDouble(String input) {
		if (input.equals("--")) {
			return 0.0;
		}
		return Double.parseDouble(input.replaceAll(",", ""));
	}

	private Long getVolume(String volumeStr) {
		if (volumeStr.equals("--")) {
			return 0l;
		}
		long value = Long.parseLong(volumeStr.replace(",", ""));
		return StockTypeEnum.Listed == getStockType() ? value / 1000L : value;
	}

	private String convertDateStr(String input) {
		String[] tokens = input.trim().split("/");
		Integer year = Integer.parseInt(tokens[0]) + 1911;
		return year + tokens[1] + tokens[2];
	}

	@Override
	public String toString() {
		return dbgstr();
	}

	@Deprecated
	public static double computeK(List<StockEbo> stocks) {
		double total = 0;
		for (StockEbo stock : stocks) {
			total += stock.getPrice();
		}
		return total / (double) stocks.size();
	}

	public boolean isEmpty() {
		if (getVolume() == null || getVolume() == 0) {
			return true;
		}
		return false;
	}

	private static void checkKsLength(Integer... ks) throws AppException {
		if (ks == null || ks.length <= 1) {
			throw new AppException(5000, "size of k must be bigger than 1");
		}
	}

	public static boolean isPointsClosed(double ratio, double[] kValues) {
		Statistics statis = new Statistics(kValues);
		double result = statis.getStdDev() / statis.getMean();
		if (result < ratio) {
			return true;
		}
		return false;
	}

	public static boolean isMultiKClosed(double ratio, List<StockEbo> stocks,
			Integer... ks) throws Exception {
		checkKsLength(ks);
		checkStockLength(stocks, ks);
		double[] kValues = computeKValues(stocks, ks);
		return isPointsClosed(ratio, kValues);
	}

	private static double[] computeKValues(List<StockEbo> stocks, Integer... ks) {
		double[] kValues = new double[ks.length];
		for (int i = 0; i < ks.length; i++) {
			List<StockEbo> ebos = stocks.subList(stocks.size() - ks[i],
					stocks.size() - 1);
			kValues[i] = computeK(ebos);
		}
		return kValues;
	}

	private static void checkStockLength(List<StockEbo> stocks, Integer... ks)
			throws AppException {
		Arrays.sort(ks);
		if (ks[ks.length - 1] > stocks.size()) {
			throw new AppException(5000,
					"size of stocks must be bigger than max of k");
		}
	}

	@Override
	public int compareTo(StockEbo obj) {
		return getDateStr().compareTo(obj.getDateStr());
	}

	@Override
	public StockEbo from(String[] columns) throws Exception {
		checkColumnLen(columns);
		setValueWithErr(columns);
		return this;
	}

	private void setValueWithErr(String[] columns) throws AppException {
		try {
			setDateStr(convertDateStr(columns[0]));
			setMonthStr(getDateStr().substring(0, 6));
			setVolume(getVolume(columns[1]));
			setStartPrice(parseDouble(columns[3]));
			setHighestPrice(parseDouble(columns[4]));
			setLowestPrice(parseDouble(columns[5]));
			setPrice(parseDouble(columns[6]));
			setChangePrice(parseDouble(columns[7]));
		} catch (Exception e) {
			log.error("parseStr failed!", e);
			throw new AppException(5002, Arrays.asList(columns));
		}
	}

	private void checkColumnLen(String[] columns) throws AppException {
		if (columns.length != 9) {
			throw new AppException(5002, Arrays.asList(columns));
		}
	}

	@Override
	public StockEbo from(String[] columns, String header) throws Exception {
		return from(columns);
	}

	public Date getDate() throws Exception {
		return TimeUtil.string2Date(getDateStr(), TimeUtil.FORMAT_YYYYMMDD);
	}
}
