/**
 * MmmProxyBean.java
 *
 * Copyright (c) 2007-2013 TrueTel Communications. All rights reserved.
 */
package com.oforsky.mmm.proxy;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;

import org.apache.log4j.Logger;

import com.oforsky.mmm.cache.SvcCfgCacheStore;
import com.oforsky.mmm.data.BigVolume;
import com.oforsky.mmm.dlo.DealDlo;
import com.oforsky.mmm.dlo.DealStatsDlo;
import com.oforsky.mmm.dlo.StockDlo;
import com.oforsky.mmm.ebo.DealEbo;
import com.oforsky.mmm.ebo.MmmConstant;
import com.oforsky.mmm.ebo.StockGroupEbo;
import com.oforsky.mmm.handler.DailyCsvReqHandler;
import com.oforsky.mmm.handler.ReportHandler;
import com.oforsky.mmm.handler.TickHandler;
import com.oforsky.mmm.util.YahooStockParser;
import com.truetel.jcore.proxy.ProxyInterceptor;
import com.truetel.jcore.util.AppException;

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
			new DailyCsvReqHandler(reqOid).fail(errMsg);
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
			new DailyCsvReqHandler(reqOid).failRetrieval(errMsg);
		} catch (Exception e) {
			log.error("dailyCsvReqFailRetrieval failed!", e);
			handleException(e);
		}
	}

	@Override
	public void dailyCsvReqRetrieve(Integer reqOid) throws AppException {
		log.info("dailyCsvReqRetrieve() reqOid=" + reqOid);
		try {
			new DailyCsvReqHandler(reqOid).retrieve();
		} catch (Exception e) {
			log.error("dailyCsvReqRetrieve failed!", e);
			handleException(e);
		}
	}

	@Override
	public void dailyCsvReqParse(Integer reqOid) throws AppException {
		log.info("dailyCsvReqParse() reqOid=" + reqOid);
		try {
			new DailyCsvReqHandler(reqOid).parse();
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
	public void importStockHistory(StockGroupEbo ebo) throws AppException {
		try {
			YahooStockParser parser = YahooStockParser.aParser(ebo);
			new StockDlo().batchCreate(parser.list());
		} catch (Exception e) {
			log.error("importStockHistory failed!", e);
			handleException(e);
		}
	}

	@Override
	public List<StockGroupEbo> findoutKClosedStocks(String date, Double ratio)
			throws AppException {
		try {
			Integer[] ks = SvcCfgCacheStore.getKList().toArray(new Integer[0]);
			return new ReportHandler().findoutKClosedStocks(ratio, date, ks);
		} catch (Exception e) {
			log.error("findoutKClosedStocks failed!", e);
			handleException(e);
		}
		return null;
	}

	@Override
	public void deleteAllDeals() throws AppException {
		try {
			int num = new DealDlo().deleteAll();
			log.info("delete " + num + " Deals.");
		} catch (Exception e) {
			log.error("deleteAllDeals failed!", e);
			handleException(e);
		}
	}

	@Override
	public void deleteDealStats() throws AppException {
		try {
			int num = new DealStatsDlo().deleteAll();
			log.info("delete " + num + " DealStats.");
		} catch (Exception e) {
			log.error("deleteDealStats failed!", e);
			handleException(e);
		}
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	@Override
	public List<DealEbo> findPastDeals(String startDate, int days, int times,
			int breakK, double revenueRate) throws AppException {
		try {
			return new ReportHandler().findPastDeals(startDate, days, times,
					breakK, revenueRate);
		} catch (Exception e) {
			log.error("findPastDeal failed!", e);
			handleException(e);
		}
		return null;
	}

	@Override
	public void analyzeDeals() throws AppException {
		try {
			new ReportHandler().analyzeDeals();
		} catch (Exception e) {
			log.error("analyzeDeals failed!", e);
			handleException(e);
		}
	}

	@Override
	public List<BigVolume> findBigVolume(String baseDate, Integer dayCount,
			Integer times) throws AppException {
		try {
			return new ReportHandler()
					.findBigVolumes(baseDate, dayCount, times);
		} catch (Exception e) {
			log.error("analyzeDeals failed!", e);
			handleException(e);
		}
		return null;
	}

}
