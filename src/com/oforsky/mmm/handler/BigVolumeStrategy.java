package com.oforsky.mmm.handler;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.oforsky.mmm.cache.DealCfgCacheStore;
import com.oforsky.mmm.cache.DealStatsCacheStore;
import com.oforsky.mmm.cache.StorageCacheStore;
import com.oforsky.mmm.data.HistoricalStock;
import com.oforsky.mmm.ebo.QueryJobEbo;
import com.oforsky.mmm.ebo.StorageEbo;
import com.oforsky.mmm.ebo.TickEbo;
import com.oforsky.mmm.proxy.MmmProxyUtil;

public class BigVolumeStrategy implements Strategy {

	private static final Logger log = Logger.getLogger(BigVolumeStrategy.class);

	private Map<String, Integer> volumeThresholdMap = new HashMap<String, Integer>();

	private Map<String, Double> kBreakPriceMap = new HashMap<String, Double>();

	private Map<String, Double> priceThresholdMap = new HashMap<String, Double>();

	private Map<String, Boolean> readyMap = new HashMap<String, Boolean>();

	private Integer days;

	private Integer times;

	private Integer kBreak;

	public BigVolumeStrategy() {
		this(DealCfgCacheStore.getDayCount(), DealCfgCacheStore.getOverTimes(),
				DealCfgCacheStore.getKBreak());
	}

	public BigVolumeStrategy(Integer days, Integer times, Integer kBreak) {
		this.days = days;
		this.times = times;
		this.kBreak = kBreak;
	}

	@Override
	public void handleTick(TickEbo tick) {
		String code = tick.getCode();
		if (tick.getTotalVolume() == 0) {
			return;
		}
		Integer thresHold = volumeThresholdMap.get(code);
		if (canBuy(tick, code, thresHold)) {
			buying(tick, code);
		}
		if (canSell(tick, code)) {
			selling(tick, code);
		}
	}

	private void selling(TickEbo tick, String code) {
		log.info("prepare to sell stock[" + code + "], price="
				+ tick.getPrice());
		try {
			StorageEbo ebo = StorageCacheStore.getStore().get(code);
			MmmProxyUtil.getProxy().sellStorage(ebo);
			done(code);
		} catch (Exception e) {
			log.error("sellStorage failed!", e);
		}
	}

	private synchronized boolean canSell(TickEbo tick, String code) {
		boolean result = readyMap.get(code)
				&& StorageCacheStore.getStore().get(code) != null
				&& (tick.getPrice() < kBreakPriceMap.get(code) || reachRevenueRate(tick));
		if (result) {
			done(code);
		}
		return result;
	}

	private boolean reachRevenueRate(TickEbo tick) {
		double buyPrice = StorageCacheStore.getStore().get(tick.getCode())
				.getTargetPrice();
		return ((tick.getPrice() - buyPrice) / buyPrice) > DealCfgCacheStore
				.getRevenueRate();
	}

	private void buying(TickEbo tick, String code) {
		log.info("stock[" + code + "]:volume=" + tick.getTotalVolume()
				+ " is BigVolume ");
		try {
			QueryJobEbo job = new QueryJobEbo();
			job.setCode(tick.getCode());
			job.setPrice(tick.getPrice());
			job.setAmount(50000);
			MmmProxyUtil.getProxy().createQueryJob(job);
		} catch (Exception e) {
			log.error("bigVolumeAction failed!", e);
		}
	}

	private void done(String code) {
		readyMap.put(code, false);
	}

	private synchronized boolean canBuy(TickEbo tick, String code,
			Integer thresHold) {
		boolean result = tick.getTotalVolume() > thresHold
				&& tick.getPrice() > priceThresholdMap.get(code)
				&& readyMap.get(code)
				&& StorageCacheStore.getStore().get(code) == null
				&& canBuyWithDealStats(code);
		if (result) {
			done(code);
		}
		return result;
	}

	private boolean canBuyWithDealStats(String code) {
		return DealStatsCacheStore.getStore().get(code) != null
				&& DealStatsCacheStore.getStore().get(code).getWinChance() >= DealCfgCacheStore
						.getWinChance();
	}

	@Override
	public void init(Map<String, HistoricalStock> historyMap) throws Exception {
		log.info("init " + this);
		for (String code : historyMap.keySet()) {
			HistoricalStock history = historyMap.get(code);
			readyMap.put(code, true);
			priceThresholdMap.put(code, priceThreshold(history));
			volumeThresholdMap.put(code, history.predictBigVolume(days, times));
			kBreakPriceMap.put(code, history.computeK(kBreak));
		}
	}

	private Double priceThreshold(HistoricalStock history) throws Exception {
		return history.getLatest().getPrice();
	}

}
