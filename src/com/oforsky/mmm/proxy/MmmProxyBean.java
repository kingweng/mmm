/**
 * MmmProxyBean.java
 *
 * Copyright (c) 2007-2013 TrueTel Communications. All rights reserved.
 */
package com.oforsky.mmm.proxy;

import com.oforsky.mmm.dlo.StockDlo;
import com.oforsky.mmm.ebo.MmmConstant;
import com.oforsky.mmm.ebo.StockEbo;
import com.oforsky.mmm.ebo.WatchStockEbo;
import com.oforsky.mmm.handler.DailyCsvReqHandler;
import com.oforsky.mmm.handler.HistoryImporter;
import com.oforsky.mmm.handler.ReportHandler;
import com.oforsky.mmm.handler.TickHandler;
import com.truetel.jcore.proxy.ProxyInterceptor;
import com.truetel.jcore.util.AppException;

import org.apache.log4j.Logger;

import javax.ejb.*;
import javax.interceptor.Interceptors;

import java.util.List;

@Stateless(name = MmmProxyUtil.PROXY_EJB, mappedName = MmmConstant.APP_NAME)
@Remote(MmmProxy.class)
@Local(MmmProxyLocal.class)
@Interceptors(ProxyInterceptor.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class MmmProxyBean extends MmmBaseProxyBean implements MmmProxy {
	private static final long serialVersionUID = 1L;

	private static final Logger log = Logger.getLogger(MmmProxyBean.class);

	private static final String TASVERSION = "TAS-OFF-R5V1P4 2014-04-30 16:43:36";

	@Override
	public void deleteStocksByMonth(String code, String monthStr)
			throws AppException {
		log.info("deleteStocksByMonth()");
		try {
			new StockDlo(getEntityManager()).deleteByCodeMonth(code, monthStr);
		} catch (Exception ee) {
			log.error("deleteStocksByMonth failed!", ee);
			handleException(ee);
		}
	}

	@Override
	public void dailyCsvReqReqFail(Integer reqOid, String errMsg)
			throws AppException {
		log.info("dailyCsvReqReqFail() reqOid=" + reqOid + ", errMsg=" + errMsg);
		try {
			new DailyCsvReqHandler().fail(reqOid, errMsg);
		} catch (Exception e) {
			log.error("dailyCsvReqReqFail failed!", e);
			handleException(e);
		}
	}

	@Override
	public void retrieveTick(String code, String y6d) throws AppException {
		try {
			new TickHandler().retrieve(code, y6d);
		} catch (Exception e) {
			log.error("retrieveTick failed!", e);
			handleException(e);
		}
	}

	@Override
	public void genTickReqs() throws AppException {
		log.info("genTickReqs() ...");
		try {
			new TickHandler().genTickReqs();
		} catch (Exception e) {
			log.error("genTickReqs failed!", e);
			handleException(e);
		}
	}

	@Override
	public void dailyCsvReqFailRetrieval(Integer reqOid, String errMsg)
			throws AppException {
		log.info("dailyCsvReqFailRetrieval() reqOid=" + reqOid + ", errMsg="
				+ errMsg);
		try {
			new DailyCsvReqHandler().failRetrieval(reqOid, errMsg);
		} catch (Exception e) {
			log.error("dailyCsvReqFailRetrieval failed!", e);
			handleException(e);
		}
	}

	@Override
	public void dailyCsvReqRetrieve(Integer reqOid) throws AppException {
		log.info("dailyCsvReqRetrieve() reqOid=" + reqOid);
		try {
			new DailyCsvReqHandler().retrieve(reqOid);
		} catch (Exception e) {
			log.error("dailyCsvReqRetrieve failed!", e);
			handleException(e);
		}
	}

	@Override
	public void dailyCsvReqParse(Integer reqOid) throws AppException {
		log.info("dailyCsvReqParse() reqOid=" + reqOid);
		try {
			new DailyCsvReqHandler().parse(reqOid);
		} catch (Exception e) {
			log.error("dailyCsvReqParse failed!", e);
			handleException(e);
		}
	}

	@Override
	public void deleteAllStocks() throws AppException {
		try {
			new StockDlo().deleteAll();
		} catch (Exception e) {
			log.error("deleteAllStocks failed!", e);
			handleException(e);
		}
	}

	@Override
	public void importStockHistory(WatchStockEbo watchStock)
			throws AppException {
		try {
			List<StockEbo> stocks = new HistoryImporter(watchStock)
					.retrieveStocks();
			new StockDlo().batchCreate(stocks);
		} catch (Exception e) {
			log.error("importStockHistory failed!", e);
			handleException(e);
		}
	}

	@Override
	public List<WatchStockEbo> findoutBigVolumeStocks(Integer dayCount,
			Double times) throws AppException {
		try {
			return new ReportHandler().findoutBigVolumeStocks(dayCount, times);
		} catch (Exception e) {
			log.error("findoutBigVolumeStocks failed!", e);
			handleException(e);
			return null;
		}
	}

}
