package com.oforsky.mmm.handler.strategy;

import com.oforsky.mmm.cache.DealCfgCacheStore;
import com.oforsky.mmm.data.HistoricalStock;
import com.oforsky.mmm.ebo.DealCfgEbo;
import com.oforsky.mmm.ebo.SellTypeEnum;
import com.oforsky.mmm.ebo.TickEbo;

public class TickStrategyImpl implements TickStrategy {

	private int upperVolume;

	private double lastPrice;

	private double revenueReate;

	private SellTypeEnum sellType;

	private double lowerPrice;

	private volatile boolean enableKBreak = false;

	private volatile boolean enableRevenueBreak = false;

	public TickStrategyImpl(HistoricalStock history) throws Exception {
		this(history, DealCfgCacheStore.getEbo());
	}

	public TickStrategyImpl(HistoricalStock history, DealCfgEbo cfg)
			throws Exception {
		this.lastPrice = history.getLatest().getPrice();
		this.upperVolume = history.predictBigVolume(cfg.getDayCount(),
				cfg.getOverTimes());
		this.revenueReate = cfg.getRevenueRate();
		this.lowerPrice = history.computeK(cfg.getKBreak());
	}

	@Override
	public boolean canBuy(TickEbo tick) {
		return tick.getPrice() > lastPrice
				&& tick.getTotalVolume() > upperVolume;
	}

	@Override
	public boolean canSell(Double buyPrice, TickEbo tick) {
		if (enableRevenueBreak
				&& ((tick.getPrice() - buyPrice) / buyPrice) > revenueReate) {
			sellType = SellTypeEnum.RevenueBreak;
			return true;
		}
		if (enableKBreak && tick.getPrice() < lowerPrice) {
			sellType = SellTypeEnum.KBreak;
			return true;
		}
		return false;
	}

	@Override
	public SellTypeEnum getSellType() {
		return sellType;
	}

	@Override
	public void enableKBreak() {
		this.enableKBreak = true;
	}

	@Override
	public void enableRevenueBreak() {
		this.enableRevenueBreak = true;
	}

}
