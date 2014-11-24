package com.oforsky.mmm.handler;

import java.util.LinkedList;
import java.util.List;

import com.oforsky.mmm.data.BigVolumeInfo;
import com.oforsky.mmm.ebo.StockEbo;
import com.oforsky.mmm.ebo.WatchStockEbo;
import com.oforsky.mmm.service.DloService;
import com.oforsky.mmm.service.DloServiceImpl;

public class ReportHandler {

	private DloService dloSvc;

	public ReportHandler() {
		this(new DloServiceImpl());
	}

	public ReportHandler(DloService dloSvc) {
		this.dloSvc = dloSvc;
	}

	public boolean overThanMeanVolume(String code, int dayCount, double times)
			throws Exception {
		List<StockEbo> stocks = dloSvc.listStocksByCodeSize(code, dayCount + 1);
		StockEbo base = stocks.remove(0);
		long totalVolume = 0;
		for (StockEbo stock : stocks) {
			totalVolume += stock.getVolume();
		}
		return base.getVolume() >= (totalVolume / (double) dayCount * times);
		// return new BigVolumeInfo(base, stocks, times);
	}

	public List<WatchStockEbo> findoutBigVolumeStocks(int dayCount, double times)
			throws Exception {
		List<WatchStockEbo> watchStocks = dloSvc.listWatchStock();
		List<WatchStockEbo> codes = new LinkedList<WatchStockEbo>();
		for (WatchStockEbo watchStock : watchStocks) {
			if (overThanMeanVolume(watchStock.getCode(), dayCount, times)) {
				codes.add(watchStock);
			}
		}
		return codes;
	}
}
