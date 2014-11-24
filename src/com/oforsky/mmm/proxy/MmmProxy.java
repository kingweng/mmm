/**
 * MmmProxy.java
 *
 * Copyright (c) 2007-2013 TrueTel Communications. All rights reserved.
 */
package com.oforsky.mmm.proxy;

import com.oforsky.mmm.ebo.StockEbo;
import com.oforsky.mmm.ebo.WatchStockEbo;
import com.truetel.jcore.util.AppException;

import javax.ejb.Remote;

import java.util.List;

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

	public void importStockHistory(WatchStockEbo watchStock)
			throws AppException;

	public List<WatchStockEbo> findoutBigVolumeStocks(Integer dayCount,
			Double times) throws AppException;

}
