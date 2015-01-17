package com.oforsky.mmm.data;

import java.util.Collection;
import java.util.List;
import java.util.NavigableMap;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import com.oforsky.mmm.ebo.StockEbo;
import com.truetel.jcore.util.AppException;

public class HistoricalStock {
	private static final Logger log = Logger.getLogger(HistoricalStock.class);

	private final TreeMap<String, StockEbo> stocks = new TreeMap<String, StockEbo>();

	private final String code;

	public HistoricalStock(String code, List<StockEbo> ebos) {
		this.code = code;
		initMap(ebos);
	}

	private void initMap(List<StockEbo> ebos) {
		for (StockEbo ebo : ebos) {
			stocks.put(ebo.getDateStr(), ebo);
		}
	}

	public double computeK(String date, int days) throws Exception {
		checkDate(date);
		checkBoundary(date, days);
		return calculateK(date, days);
	}

	private double calculateK(String date, int days) {
		double total = 0;
		for (StockEbo stock : subLessMap(date, days).values()) {
			total += stock.getPrice();
		}
		return total / days;
	}

	private SortedMap<String, StockEbo> subLessMap(String date, int days) {
		NavigableMap<String, StockEbo> subMap = stocks.headMap(date, true);
		SortedMap<String, StockEbo> result = subMap;
		int count = 0;
		for (String tmpDate : subMap.descendingKeySet()) {
			count++;
			if (count == days) {
				log.trace("tmpDate=" + tmpDate + ", date=" + date);
				result = stocks.subMap(tmpDate, true, date, true);
				break;
			}
		}
		log.trace("subLessMap[date=" + date + ",days=" + days + "]=" + result);
		return result;
	}

	private void checkBoundary(String date, int days) throws AppException {
		if (stocks.headMap(date, true).size() - days < 0) {
			throw new AppException(5008, days);
		}
	}

	private void checkDate(String date) throws AppException {
		if (stocks.get(date) == null) {
			throw new AppException(5007, date);
		}
	}

	public boolean isBigVolume(String date, int days, int times)
			throws AppException {
		checkDate(date);
		checkBoundary(date, days);
		return timesGreater(date, days, times);
	}

	private boolean timesGreater(String date, int days, int times) {
		double total = 0;
		for (StockEbo stock : subLessMap(date, days).values()) {
			total += stock.getVolume();
		}
		return stocks.get(date).getVolume() > ((total / days) * times);
	}

	public StockEbo getStock(String date) throws AppException {
		checkDate(date);
		return stocks.get(date);
	}

	public Collection<StockEbo> getStocks() {
		return stocks.values();
	}

	public boolean isBelowK(String date, int days) throws Exception {
		double k = computeK(date, days);
		return stocks.get(date).getPrice() < k;
	}

	public int recordHigh(String date) throws AppException {
		checkDate(date);
		int recordHigh = 0;
		double price = stocks.get(date).getPrice();
		Collection<String> keys = stocks.headMap(date, false)
				.descendingKeySet();
		log.trace("keys=" + keys);
		for (String index : keys) {
			if (price > stocks.get(index).getPrice()) {
				recordHigh++;
				continue;
			}
			break;
		}
		return recordHigh;
	}

	public Collection<StockEbo> getStocks(String startDate) {
		return stocks.tailMap(startDate).values();
	}

	public Collection<StockEbo> getStocks(String startDate, String endDate) {
		return stocks.subMap(startDate, true, endDate, true).values();
	}

	public String getCode() {
		return code;
	}

	public double getBigVolumeTimes(String date, int days) throws AppException {
		checkDate(date);
		checkBoundary(date, days);
		return bigVolumeTimes(date, days);
	}

	private double bigVolumeTimes(String date, int days) {
		double total = 0;
		for (StockEbo stock : subLessMap(date, days).values()) {
			total += stock.getVolume();
		}
		return stocks.get(date).getVolume() / ((total / days));
	}
}
