package com.oforsky.mmm.handler;

import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import com.oforsky.mmm.data.HistoricalStock;
import com.oforsky.mmm.ebo.DealEbo;
import com.oforsky.mmm.ebo.SellTypeEnum;
import com.oforsky.mmm.ebo.StockEbo;
import com.truetel.jcore.util.AppException;

public class DealHandler {

	private static final Logger log = Logger.getLogger(DealHandler.class);

	private HistoricalStock stocks;

	private StockEbo buyStock;

	private SellTypeEnum sellType;

	public DealHandler(HistoricalStock list) {
		this.stocks = list;
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
			result.add(new DealEbo(buyStock, each, sellType, stocks));
			buyStock = null;
		}
	}

	private boolean canSell(int breakK, double revenueRate, StockEbo each)
			throws Exception {
		if (stocks.isBelowK(each.getDateStr(), breakK)) {
			sellType = SellTypeEnum.KBreak;
			return true;
		}
		if (higherRevenueRate(each.getPrice(), revenueRate)) {
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
			if (buyStock == null
					&& stocks.isBigVolume(each.getDateStr(), k, times)) {
				log.debug("is BigVolume on " + each.getDateStr());
				buyStock = each;
			}
		} catch (AppException e) {
			// ignore error
		}
	}

	public List<DealEbo> findPastDeals(String startDate, int k, int times,
			int breakK, double revenueRate) throws Exception {
		List<DealEbo> result = new LinkedList<DealEbo>();
		for (StockEbo each : stocks.getStocks(startDate)) {
			findBuyStock(k, times, each);
			findSellStockAddToResult(breakK, revenueRate, result, each);
		}
		return result;
	}

}
