package com.oforsky.mmm.handler.strategy;

import org.apache.log4j.Logger;

import com.oforsky.mmm.data.HistoricalStock;
import com.oforsky.mmm.ebo.DealCfgEbo;
import com.oforsky.mmm.ebo.SellTypeEnum;
import com.oforsky.mmm.ebo.StockEbo;
import com.truetel.jcore.util.AppException;

public class DealStrategyImpl implements DealStrategy {

	private static final Logger log = Logger.getLogger(DealStrategyImpl.class);

	private HistoricalStock history;

	private int times;

	private int days;

	private double revenueRate;

	private int kBreak;

	private SellTypeEnum sellType;

	private Double sellPrice;

	public DealStrategyImpl(DealCfgEbo dealCfg, HistoricalStock history) {
		this.history = history;
		this.days = dealCfg.getDayCount();
		this.times = dealCfg.getOverTimes();
		this.revenueRate = dealCfg.getRevenueRate();
		this.kBreak = dealCfg.getKBreak();
	}

	@Override
	public boolean canBuy(StockEbo stock) {
		try {
			return history.isBigVolume(stock.getDateStr(), days, times)
					&& stock.getChangePrice() > 0;
		} catch (AppException e) {
			log.warn("isBigVolume failed, but ignored", e);
			return false;
		}
	}

	@Override
	public Double getBuyPrice(StockEbo stock) {
		return stock.getPrice();
	}

	@Override
	public Double getSellPrice() {
		return sellPrice;
	}

	@Override
	public SellTypeEnum getSellType() {
		return sellType;
	}

	@Override
	public boolean canSell(Double buyPrice, StockEbo stock) {
		if (isRevenueSell(buyPrice, stock) || isKBreakSell(stock)) {
			log.info("canSell is true, buyPrice==" + buyPrice + ", stock=="
					+ stock.dbgstr());
			return true;
		}
		return false;
	}

	private boolean isRevenueSell(Double buyPrice, StockEbo stock) {
		double result = (stock.getHighestPrice() - buyPrice) / buyPrice;
		if (result > revenueRate) {
			sellPrice = stock.getHighestPrice();
			sellType = SellTypeEnum.RevenueBreak;
			return true;
		}
		return false;
	}

	private boolean isKBreakSell(StockEbo stock) {
		try {
			if (history.isBelowK(stock.getDateStr(), kBreak)) {
				sellPrice = stock.getPrice();
				sellType = SellTypeEnum.KBreak;
				return true;
			}
		} catch (Exception e) {
			log.warn("isBelowK failed, but ignored", e);
		}
		return false;
	}

}
