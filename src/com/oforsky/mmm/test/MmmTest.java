/**
 * MmmTest.java
 *
 * Copyright (c) 2007-2013 TrueTel Communications. All rights reserved.
 */
package com.oforsky.mmm.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import com.oforsky.mmm.data.BigVolume;
import com.oforsky.mmm.ebo.DailyCsvReqEbo;
import com.oforsky.mmm.ebo.DealCfgEbo;
import com.oforsky.mmm.ebo.DealCfgEboBuilder;
import com.oforsky.mmm.ebo.DealEbo;
import com.oforsky.mmm.ebo.DealStatsEbo;
import com.oforsky.mmm.ebo.DriveEbo;
import com.oforsky.mmm.ebo.LicenseEbo;
import com.oforsky.mmm.ebo.MmmEnum;
import com.oforsky.mmm.ebo.StockGroupEbo;
import com.oforsky.mmm.ebo.TickEbo;
import com.oforsky.mmm.ebo.TrainingEbo;
import com.oforsky.mmm.ebo.WarrantEbo;
import com.oforsky.mmm.proxy.MmmProxy;
import com.oforsky.mmm.proxy.MmmProxyUtil;
import com.oforsky.mmm.util.StockGroupParser;
import com.truetel.jcore.proxy.SvcTest;
import com.truetel.jcore.util.AppException;
import com.truetel.jcore.util.FileUtil;
import com.truetel.jcore.util.TimeUtil;
import com.truetel.jcorex.util.ExceptionUtil;

public class MmmTest extends MmmBaseTest {
	private static final String TASVERSION = "TAS-OFF-R5V1P10 2014-08-01 15:49:52";

	public static void main(String args[]) {
		ExceptionUtil.setupMessagePath("mmm", "com.oforsky.mmm",
				"com.oforsky.mmm.properties.AppExceptionMsg");
		MmmTest tester = new MmmTest();
		tester.run(args);
	}

	@SvcTest(value = 101, paramNames = { "filepath" })
	public void importDrive(String filepath) throws Exception {
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				new FileInputStream(new File(filepath)), "Big5"));
		Iterable<CSVRecord> records = CSVFormat.EXCEL.parse(reader);
		List<DriveEbo> ebos = new LinkedList<DriveEbo>();
		for (CSVRecord record : records) {
			DriveEbo ebo = DriveEbo.valueOf(record);
			ebos.add(ebo);
		}
		MmmProxyUtil.getProxy().batchCreateDrive(ebos);
	}

	@SvcTest(value = 102, paramNames = { "filepath" })
	public void importLicense(String filepath) throws Exception {
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				new FileInputStream(new File(filepath)), "Big5"));
		Iterable<CSVRecord> records = CSVFormat.EXCEL.parse(reader);
		List<LicenseEbo> ebos = new LinkedList<LicenseEbo>();
		for (CSVRecord record : records) {
			LicenseEbo ebo = LicenseEbo.valueOf(record);
			ebos.add(ebo);
		}
		MmmProxyUtil.getProxy().batchCreateLicense(ebos);
	}

	@SvcTest(value = 103, paramNames = { "filepath" })
	public void importTraining(String filepath) throws Exception {
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				new FileInputStream(new File(filepath)), "big5"));
		Iterable<CSVRecord> records = CSVFormat.EXCEL.parse(reader);
		List<TrainingEbo> ebos = new LinkedList<TrainingEbo>();
		for (CSVRecord record : records) {
			TrainingEbo ebo = TrainingEbo.valueOf(record);
			// cui.output(ebo.dbgstr());
			ebos.add(ebo);
		}
		MmmProxyUtil.getProxy().batchCreateTraining(ebos);
	}

	@SvcTest(value = 104)
	public void findoutNotContinuousTraining() throws Exception {
		List<TrainingEbo> ebos = proxy.listAll(MmmEnum.Training);
		TrainingEbo lastEbo = ebos.get(0);
		StringBuilder result = new StringBuilder();
		for (TrainingEbo ebo : ebos) {
			if (ebo.getPid().equals(lastEbo.getPid())) {
				if (lastEbo.getEndTime().before(ebo.getStartTime())
						&& less2Weeks(lastEbo.getEndTime(), ebo.getStartTime())) {
					result.append(toString(lastEbo) + ",");
					result.append(toString(ebo) + "\n");
					// result.append("\n");
				}
			}
			lastEbo = ebo;
		}
		FileUtil.writeFile(result.toString(), "/Users/kingweng/result.csv",
				"big5");
		cui.output(result.toString());
	}

	@SvcTest(value = 105)
	public void findoutNoATraining() throws Exception {
		List<TrainingEbo> ebos = proxy.listAll(MmmEnum.Training);
		TrainingEbo lastEbo = ebos.get(0);
		StringBuilder result = new StringBuilder();
		for (TrainingEbo ebo : ebos) {
			if ("B".equals(ebo.getCategory())) {
				result.append(toString(lastEbo) + ",");
				result.append(toString(ebo) + "\n");
			}
			lastEbo = ebo;
		}
		FileUtil.writeFile(result.toString(), "/Users/kingweng/result.csv",
				"big5");
		cui.output(result.toString());
	}

	private boolean less2Weeks(Date date1, Date date2) {
		Calendar c1 = Calendar.getInstance();
		c1.setTime(date1);
		Calendar c2 = Calendar.getInstance();
		c2.setTime(date2);
		return TimeUtil.addWeek(date1, 2).getTime().before(date2);
	}

	private String toString(TrainingEbo ebo) {
		return ebo.getTrainingOid() + "," + ebo.getTrainingNum() + ","
				+ ebo.getPid() + "," + ebo.getCategory() + ","
				+ ebo.getStartTime() + "," + ebo.getEndTime();
	}

	@SvcTest(value = 201, paramNames = { "filepath" })
	public void importStockGroup(String filepath) throws Exception {
		proxy.batchDeleteStockGroup(allStockGroups());
		StockGroupParser parser = new StockGroupParser(filepath);
		proxy.batchCreateStockGroup(parser.list());
	}

	@SvcTest(value = 202)
	public void importStockHistory() throws Exception {
		MmmProxy proxy = MmmProxyUtil.getProxy();
		cui.output("delete all stocks");
		proxy.deleteAllStocks();
		for (StockGroupEbo stock : allStockGroups()) {
			proxy.importStockHistory(stock);
			cui.output("Stock[" + stock.getName() + "] import successfully.");
		}
	}

	@SvcTest(value = 203, paramNames = { "baseDate(yyyymmdd)", "dayCount",
			"times" })
	public void findoutBigVolumeStocks(String baseDate, Integer dayCount,
			Integer times) throws Exception {
		List<BigVolume> result = proxy.findBigVolume(baseDate, dayCount, times);
		for (BigVolume each : result) {
			cui.output(genBigVolumeResult(each));
		}
	}

	private String genBigVolumeResult(BigVolume data) throws Exception {
		DealStatsEbo ebo = proxy.getDealStatsByCode(data.getCode());
		if (ebo == null) {
			return "";
		}
		String format = "Code[%1$s]: recordHigh=%2$3d times=%3$2.2f winChance=%4$d%% "
				+ "succCnt=%5$2d failCnt=%6$2d revenueRate=%7$+2.2f";
		return String.format(format, data.getCode(), data.getRecordHigh(),
				data.getTimes(), ebo.getWinChance(), ebo.getSuccCnt(),
				ebo.getFailCnt(), ebo.getRevenueRate());
	}

	@SuppressWarnings("unchecked")
	private List<StockGroupEbo> allStockGroups() throws Exception {
		return proxy.listAll(MmmEnum.StockGroup);
	}

	@SvcTest(value = 204, paramNames = { "startDate(yyyymmdd)", "dayCount",
			"times", "breakK", "revenueRate" })
	public void analyzeBigVolumeDeals(String startDate, Integer dayCount,
			Integer times, Integer breakK, String revenueRate) throws Exception {
		DealCfgEbo cfg = DealCfgEboBuilder.dealCfgEbo().withDayCount(dayCount)
				.withOverTimes(times).withKBreak(breakK)
				.withRevenueRate(Double.parseDouble(revenueRate)).build();
		cfg.checkRequired();
		cui.output("clean Deal table");
		proxy.deleteAllDeals();
		cui.output("clean DealStats table");
		proxy.deleteDealStats();
		cui.output("start finding Deals ...");
		List<DealEbo> ebos = proxy.findPastDeals(startDate, cfg);
		batchCreateDeals(ebos);
		cui.output("analyze Deals ...");
		proxy.analyzeDeals();
	}

	private void batchCreateDeals(List<DealEbo> ebos) throws AppException {
		cui.output("persisting Deals to Database");
		List<DealEbo> bucket = new ArrayList<DealEbo>();
		while (true) {
			bucket.add(ebos.remove(0));
			if (bucket.size() == 100) {
				proxy.batchCreateDeal(bucket);
				bucket.clear();
			}
			if (ebos.size() == 0) {
				break;
			}
		}
		proxy.batchCreateDeal(bucket);
	}

	@SvcTest(value = 209, paramNames = { "month(yyyymm)" })
	public void importStockByMonth(String month) throws Exception {
		List<DailyCsvReqEbo> ebos = new ArrayList<DailyCsvReqEbo>();
		for (StockGroupEbo stock : allStockGroups()) {
			ebos.add(new DailyCsvReqEbo(stock, month));
		}
		proxy.batchCreateDailyCsvReq(ebos);
	}

	@SvcTest(value = 210, paramNames = { "date(yyyymmdd)", "ratio" })
	public void findoutKClosedStocks(String date, String ratio)
			throws Exception {
		List<StockGroupEbo> result = proxy.findoutKClosedStocks(date,
				Double.parseDouble(ratio));
		for (StockGroupEbo ebo : result) {
			cui.output(ebo.dbgstr());
		}
	}

	@SvcTest(value = 211, paramNames = { "code" })
	public void selectWarrants(String code) throws Exception {
		List<WarrantEbo> ebos = proxy.selectWarrants(code);
		for (WarrantEbo each : ebos) {
			cui.output(each.dbgstr() + "\n");
		}
	}

	@SvcTest(value = 211)
	public void invokeTickTimerManually() throws Exception {
		proxy.invokeTickTimerManully();
	}

	@SvcTest(value = 212, paramNames = { "code", "price", "totalVolume" })
	public void genFakeTick(String code, String price, Integer totalVolume)
			throws Exception {
		TickEbo ebo = new TickEbo();
		ebo.setCode(code);
		ebo.setPrice(Double.parseDouble(price));
		ebo.setTotalVolume(totalVolume);
		proxy.genFakeTick(ebo);
	}

}
