package com.oforsky.mmm.handler;

import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import com.oforsky.mmm.cache.BuyingStockCacheStore;
import com.oforsky.mmm.data.HistoricalStock;
import com.oforsky.mmm.ebo.DealEbo;
import com.oforsky.mmm.ebo.SellTypeEnum;
import com.oforsky.mmm.ebo.StockEbo;

public class DealHandler {

	private static final Logger log = Logger.getLogger(DealHandler.class);

	private HistoricalStock history;

	private StockEbo buyStock;

	private SellTypeEnum sellType;

	public DealHandler(HistoricalStock list) {
		this.history = list;
	}

	public List<DealEbo> findPastDeals(String startDate, int k, int times,
			int breakK) throws Exception {
		double alwaysHigherRate = 100;
		return findPastDeals(startDate, k, times, breakK, alwaysHigherRate);
	}

	private void findSellStockAddToResult(int breakK, double revenueRate,
			List<DealEbo> result, StockEbo each) throws Exception {
		if (buyStock != null && !buyStock.equals(each)
				&& canSell(breakK, revenueRate, each)) {
			log.debug("sell on " + each.getDateStr());
			result.add(new DealEbo(buyStock, each, sellType, history));
			buyStock = null;
		}
	}

	private boolean canSell(int breakK, double revenueRate, StockEbo each)
			throws Exception {
		if (history.isBelowK(each.getDateStr(), breakK)) {
			sellType = SellTypeEnum.KBreak;
			return true;
		}
		if (higherRevenueRate(each.getHighestPrice(), revenueRate)) {
			sellType = SellTypeEnum.RevenueBreak;
			return true;
		}
		return false;
	}

	private boolean higherRevenueRate(double sellPrice, double revenueRate) {
		return revenueRate(buyStock.getPrice(), sellPrice) > revenueRate;
	}

	private double revenueRate(double buyPrice, double sellPrice) {
		return (sellPrice - buyPrice) / buyPrice;
	}

	private void findBuyStock(int k, int times, StockEbo each) {
		try {
			// if (buyStock == null
			// && history.isBigVolume(each.getDateStr(), k, times)
			// && each.getPrice() > priceThreshold(each)) {
			// log.debug("is BigVolume on " + each.getDateStr());
			// buyStock = each;
			// }
			if (buyStock == null
					&& history.isBigVolume(each.getDateStr(), k, times)
					&& each.getChangePrice() > 0
					&& BuyingStockCacheStore.getStore().get(each.getCode()) != null) {
				log.debug("is BigVolume on " + each.getDateStr());
				buyStock = each;
			}
		} catch (Exception e) {
			// ignore error
		}
	}

//	private Double priceThreshold(StockEbo each) throws Exception {
//		double maxPrice = Double.MIN_VALUE;
//		for (int k : DealCfgCacheStore.getAboveKDays()) {
//			double value = history.computeK(k);
//			if (value > maxPrice) {
//				maxPrice = value;
//			}
//		}
//		return maxPrice;
//	}

	public List<DealEbo> findPastDeals(String startDate, int k, int times,
			int breakK, double revenueRate) throws Exception {
		List<DealEbo> result = new LinkedList<DealEbo>();
		for (StockEbo each : history.getStocks(startDate)) {
//			if (each.getDateStr().startsWith("2010")) {
//				break;
//			}
			findBuyStock(k, times, each);
			findSellStockAddToResult(breakK, revenueRate, result, each);
		}
		return result;
	}

}
