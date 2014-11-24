package com.oforsky.mmm.handler;

import java.io.BufferedReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.oforsky.mmm.ebo.StockEbo;
import com.oforsky.mmm.ebo.WatchStockEbo;
import com.oforsky.mmm.service.HttpServiceImpl;

public class HistoryImporter {

	private static final Logger log = Logger.getLogger(HistoryImporter.class);

	private static final String YAHOO_FINANCE_URL = "http://real-chart.finance.yahoo.com/table.csv?s=%s.TW&%s&g=d&a=0&b=4&c=2000&ignore=.csv";

	private List<StockEbo> stocks;

	private WatchStockEbo watchStock;

	public HistoryImporter(WatchStockEbo watchStock) {
		this.watchStock = watchStock;
	}

	public List<StockEbo> retrieveStocks() throws Exception {
		log.info("download URL = " + getDownloadUrl());
		String content = new HttpServiceImpl().download(getDownloadUrl());
		stocks = parse(new StringReader(content), watchStock.getCode(),
				watchStock.getName());
		log.info("download stocks size == " + stocks.size());
		return stocks;
	}

	private String getDownloadUrl() {
		return String.format(YAHOO_FINANCE_URL, watchStock.getCode(),
				getTodayFormat());
	}

	private String getTodayFormat() {
		// format as d=10&e=21&f=2014
		Calendar today = Calendar.getInstance();
		return "d=" + today.get(Calendar.MONTH) + "&e="
				+ today.get(Calendar.DAY_OF_MONTH) + "&f="
				+ today.get(Calendar.YEAR);
	}

	private List<StockEbo> parse(Reader reader, String code, String name)
			throws Exception {
		Set<StockEbo> sets = new HashSet<StockEbo>();
		BufferedReader bReader = null;
		try {
			String input = null;
			bReader = new BufferedReader(reader);
			bReader.readLine();// ignore header
			while ((input = bReader.readLine()) != null) {
				sets.add(StockEbo.fromHistoryStr(input, code, name));
			}
		} finally {
			closeReader(bReader);
		}
		return new ArrayList<StockEbo>(sets);
	}

	private static void closeReader(BufferedReader bReader) {
		try {
			bReader.close();
		} catch (Exception e) {
			// ignore
		}
	}

	public List<StockEbo> getStocks() {
		return stocks;
	}
}
