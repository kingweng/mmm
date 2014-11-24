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
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import com.oforsky.mmm.ebo.DriveEbo;
import com.oforsky.mmm.ebo.LicenseEbo;
import com.oforsky.mmm.ebo.MmmEnum;
import com.oforsky.mmm.ebo.WatchStockEbo;
import com.oforsky.mmm.proxy.MmmProxy;
import com.oforsky.mmm.proxy.MmmProxyUtil;
import com.truetel.jcore.proxy.SvcTest;

public class MmmTest extends MmmBaseTest {
	private static final String TASVERSION = "TAS-OFF-R5V1P10 2014-08-01 15:49:52";

	public static void main(String args[]) {
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

	@SvcTest(value = 201, paramNames = { "filepath" })
	public void importWatchStock(String filepath) throws Exception {
		proxy.importCsvWatchStock(filepath);
	}

	@SvcTest(value = 202)
	public void importStockHistory() throws Exception {
		MmmProxy proxy = MmmProxyUtil.getProxy();
		cui.output("delete all stocks");
		proxy.deleteAllStocks();
		@SuppressWarnings("unchecked")
		List<WatchStockEbo> watchStocks = proxy.listAll(MmmEnum.WatchStock);
		for (WatchStockEbo watchStock : watchStocks) {
			proxy.importStockHistory(watchStock);
			cui.output("watchStock[" + watchStock.getName()
					+ "] import successfully.");
		}
	}

	@SvcTest(value = 203, paramNames = { "dayCount", "times" })
	public void findoutBigVolumeStocks(Integer dayCount, String times)
			throws Exception {
		List<WatchStockEbo> stocks = proxy.findoutBigVolumeStocks(dayCount,
				Double.parseDouble(times));
		for (WatchStockEbo ebo : stocks) {
			cui.output(ebo.dbgstr());
		}
	}
}
