/**
 * MmmProxyBean.java
 *
 * Copyright (c) 2007-2013 TrueTel Communications. All rights reserved.
 */
package com.oforsky.mmm.proxy;

import java.util.List;
import java.util.Map;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;

import org.apache.log4j.Logger;

import com.oforsky.mmm.cache.DealCfgCacheStore;
import com.oforsky.mmm.cache.SvcCfgCacheStore;
import com.oforsky.mmm.capture.TickRetrieverImpl;
import com.oforsky.mmm.data.BigVolume;
import com.oforsky.mmm.data.HistoricalStock;
import com.oforsky.mmm.dlo.BidReqDlo;
import com.oforsky.mmm.dlo.DealDlo;
import com.oforsky.mmm.dlo.DealStatsDlo;
import com.oforsky.mmm.dlo.QueryJobDlo;
import com.oforsky.mmm.dlo.StockDlo;
import com.oforsky.mmm.ebo.BidReqEbo;
import com.oforsky.mmm.ebo.DealEbo;
import com.oforsky.mmm.ebo.MmmConstant;
import com.oforsky.mmm.ebo.QueryJobEbo;
import com.oforsky.mmm.ebo.StockGroupEbo;
import com.oforsky.mmm.ebo.StorageEbo;
import com.oforsky.mmm.ebo.TickEbo;
import com.oforsky.mmm.ebo.WarrantEbo;
import com.oforsky.mmm.handler.BidReqHandler;
import com.oforsky.mmm.handler.DailyCsvReqHandler;
import com.oforsky.mmm.handler.QueryJobHandler;
import com.oforsky.mmm.handler.ReportHandler;
import com.oforsky.mmm.handler.StorageHandler;
import com.oforsky.mmm.handler.Strategy;
import com.oforsky.mmm.handler.StrategyHandler;
import com.oforsky.mmm.handler.TickHandler;
import com.oforsky.mmm.handler.WarrantHandler;
import com.oforsky.mmm.service.DloServiceImpl;
import com.oforsky.mmm.timer.TickTimerTask;
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
			TickHandler handler = new TickHandler();
			handler.addListener(StrategyHandler.get());
			handler.retrieve(code, y6d);
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

	@Override
	public Map<String, HistoricalStock> getHistoryMap(Integer dayCount)
			throws AppException {
		try {
			return new ReportHandler().getHistoryMap(dayCount);
		} catch (Exception e) {
			log.error("getHistoryMap failed!", e);
			handleException(e);
		}
		return null;
	}

	@Override
	public void invokeTickTimerManully() throws AppException {
		try {
			StrategyHandler.get().reset(
					getHistoryMap(DealCfgCacheStore.getDayCount()));
			genTickReqs();
			TickTimerTask.schedule();
		} catch (Exception e) {
			log.error("invokeTickTimerManully failed!", e);
			handleException(e);
		}
	}

	@Override
	public List<WarrantEbo> selectWarrants(String code) throws AppException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void createBidReq(Integer jobOid) throws AppException {
		try {
			QueryJobEbo job = new QueryJobDlo().get(jobOid);
			new QueryJobHandler(job).createBidReq(new WarrantHandler());
		} catch (Exception e) {
			log.error("createBidReq failed!", e);
			handleException(e);
		}
	}

	@Override
	public void queryJobFailRetrieval(Integer jobOid, Exception e)
			throws AppException {
		try {
			QueryJobEbo job = new QueryJobDlo().get(jobOid);
			new QueryJobHandler(job).failRetrieval(e);
		} catch (Exception ee) {
			log.error("bidJobFailRetrieval failed!", ee);
			handleException(ee);
		}
	}

	@Override
	public void queryJobFail(Integer jobOid, Exception e) throws AppException {
		try {
			QueryJobEbo job = new QueryJobDlo().get(jobOid);
			new QueryJobHandler(job).fail(e);
		} catch (Exception ee) {
			log.error("bidJobFailRetrieval failed!", ee);
			handleException(ee);
		}
	}

	@Override
	public void sellStorage(StorageEbo ebo) throws AppException {
		try {
			new StorageHandler(new DloServiceImpl(), new TickRetrieverImpl())
					.sellStorage(ebo);
		} catch (Exception ee) {
			log.error("sellStorage failed!", ee);
			handleException(ee);
		}
	}

	@Override
	public void proceedBidReq(Integer bidOid) throws AppException {
		try {
			getBidReqHandler(bidOid).proceed();
		} catch (Exception ee) {
			log.error("proceedBidReq failed!", ee);
			handleException(ee);
		}
	}

	private BidReqHandler getBidReqHandler(Integer bidOid) throws Exception {
		BidReqEbo req = new BidReqDlo().get(bidOid);
		return new BidReqHandler(req);
	}

	@Override
	public void failBidReq(Integer bidOid, Exception e) throws AppException {
		try {
			getBidReqHandler(bidOid).failBid(e);
		} catch (Exception ee) {
			log.error("failBidReq failed!", ee);
			handleException(ee);
		}
	}

	@Override
	public void genFakeTick(TickEbo ebo) throws AppException {
		try {
			StrategyHandler.get().reset(
					getHistoryMap(DealCfgCacheStore.getDayCount()));
			for (Strategy each : StrategyHandler.get().getStrategies()) {
				each.handleTick(ebo);
			}
		} catch (Exception ee) {
			log.error("genFakeTick failed!", ee);
			handleException(ee);
		}
	}
}
