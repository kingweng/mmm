/**
 * MmmProxy.java
 *
 * Copyright (c) 2007-2013 TrueTel Communications. All rights reserved.
 */
package com.oforsky.mmm.proxy;

import java.util.List;
import java.util.Map;

import javax.ejb.Remote;

import com.oforsky.mmm.data.BigVolume;
import com.oforsky.mmm.data.HistoricalStock;
import com.oforsky.mmm.ebo.DealEbo;
import com.oforsky.mmm.ebo.StockGroupEbo;
import com.oforsky.mmm.ebo.StorageEbo;
import com.oforsky.mmm.ebo.TickEbo;
import com.oforsky.mmm.ebo.WarrantEbo;
import com.truetel.jcore.util.AppException;

@Remote
public interface MmmProxy extends MmmBaseProxy {

	public void deleteStocksByMonth(String code, String monthStr)
			throws AppException;

	public void dailyCsvReqRetrieve(Integer reqOid) throws AppException;

	public void dailyCsvReqParse(Integer reqOid) throws AppException;

	public void dailyCsvReqFailRetrieval(Integer reqOid, String errMsg)
			throws AppException;

	public void dailyCsvReqReqFail(Integer reqOid, String errMsg)
			throws AppException;

	void retrieveTick(String code, String y6d) throws AppException;

	void genTickReqs() throws AppException;

	public void deleteAllStocks() throws AppException;

	public void importStockHistory(StockGroupEbo ebo) throws AppException;

	public List<StockGroupEbo> findoutKClosedStocks(String date, Double ratio)
			throws AppException;

	public void deleteAllDeals() throws AppException;

	public void deleteDealStats() throws AppException;

	public List<DealEbo> findPastDeals(String startDate, int dayCount,
			int times, int breakK, double revenueRate) throws AppException;

	public void analyzeDeals() throws AppException;

	public List<BigVolume> findBigVolume(String baseDate, Integer dayCount,
			Integer times) throws AppException;

	public List<WarrantEbo> selectWarrants(String code) throws AppException;

	public Map<String, HistoricalStock> getHistoryMap(Integer dayCount)
			throws AppException;

	public void invokeTickTimerManully() throws AppException;

	public void createBidReq(Integer jobOid) throws AppException;

	public void queryJobFailRetrieval(Integer jobOid, Exception e)
			throws AppException;

	public void queryJobFail(Integer jobOid, Exception e) throws AppException;

	public void sellStorage(StorageEbo ebo) throws AppException;

	public void proceedBidReq(Integer bidOid) throws AppException;

	public void failBidReq(Integer bidOid, Exception e) throws AppException;

	public void genFakeTick(TickEbo ebo) throws AppException;

}
