package com.oforsky.mmm.handler;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import com.oforsky.mmm.cache.StockGroupCacheStore;
import com.oforsky.mmm.data.BigVolume;
import com.oforsky.mmm.data.HistoricalStock;
import com.oforsky.mmm.ebo.DealEbo;
import com.oforsky.mmm.ebo.DealStatsEbo;
import com.oforsky.mmm.ebo.StockEbo;
import com.oforsky.mmm.ebo.StockGroupEbo;
import com.oforsky.mmm.service.DloService;
import com.oforsky.mmm.service.DloServiceImpl;
import com.truetel.jcore.util.AppException;

public class ReportHandler {

	private static final Logger log = Logger.getLogger(ReportHandler.class);

	private DloService dloSvc;

	public ReportHandler() {
		this(new DloServiceImpl());
	}

	public ReportHandler(DloService dloSvc) {
		this.dloSvc = dloSvc;
	}

	public List<DealEbo> findPastDeals(String startDate, int days, int times,
			int breakK, double revenueRate) throws Exception {
		log.info("findPastDeals() startDate=" + startDate + ", days=" + days
				+ ", times=" + times + ", breakK=" + breakK + ", revenueRate="
				+ revenueRate);
		List<DealEbo> result = new LinkedList<DealEbo>();
		for (String code : StockGroupCacheStore.getStore().getKeySet()) {
			findDealToResult(startDate, days, times, breakK, revenueRate,
					result, code);
		}
		return result;
	}

	private void findDealToResult(String startDate, int days, int times,
			int breakK, double revenueRate, List<DealEbo> result, String code)
			throws Exception {
		HistoricalStock history = new HistoricalStock(code,
				dloSvc.listStocksByCodeFromDate(code, startDate));
		DealHandler handler = new DealHandler(history);
		result.addAll(handler.findPastDeals(startDate, days, times, breakK,
				revenueRate));
	}

	private boolean isBigVolumeIgnoreErr(HistoricalStock history, String date,
			int days, int times) {
		try {
			if (history.getStock(date) != null) {
				return history.isBigVolume(date, days, times);
			}
		} catch (AppException e) {
			// ignored, history doesn't contain the data on date
		}
		return false;
	}

	public List<StockGroupEbo> findoutKClosedStocks(double ratio, String date,
			Integer... ks) throws Exception {
		List<StockGroupEbo> result = new LinkedList<StockGroupEbo>();
		Arrays.sort(ks);

		for (StockGroupEbo stock : StockGroupCacheStore.getStore().getValues()) {
			List<StockEbo> stocks = dloSvc.listStocksByCodeDateSize(
					stock.getCode(), date, ks[ks.length - 1]);
			Collections.reverse(stocks);
			if (StockEbo.isMultiKClosed(ratio, stocks, ks)) {
				result.add(stock);
			}
		}
		return result;
	}

	public void analyzeDeals() throws Exception {
		List<DealStatsEbo> result = new LinkedList<DealStatsEbo>();

		for (StockGroupEbo stock : StockGroupCacheStore.getStore().getValues()) {
			List<DealEbo> stocks = dloSvc.listDealByCode(stock.getCode());
			result.add(new DealStatsEbo(stock.getCode(), stocks));
		}
		dloSvc.batchCreateDealStats(result);
	}

	public List<BigVolume> findBigVolumes(String date, Integer days,
			Integer times) throws Exception {
		List<BigVolume> result = new LinkedList<BigVolume>();
		for (String code : StockGroupCacheStore.getStore().getKeySet()) {
			HistoricalStock history = new HistoricalStock(code,
					dloSvc.listStocksByCodeDateSize(code, date, days));
			if (isBigVolumeIgnoreErr(history, date, days, times)) {
				result.add(new BigVolume(code, history.recordHigh(date),
						history.getBigVolumeTimes(date, days)));
			}
		}
		return result;
	}

}
