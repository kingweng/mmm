package com.oforsky.mmm.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.oforsky.mmm.cache.DealCfgCacheStore;
import com.oforsky.mmm.data.HistoricalStock;
import com.oforsky.mmm.ebo.TickEbo;
import com.oforsky.mmm.proxy.MmmProxyUtil;

public class StrategyHandler implements TickListener {

	private static StrategyHandler instance;

	private List<Strategy> strategies = new ArrayList<Strategy>();

	private StrategyHandler() {
	}

	public void addStrategy(Strategy strategy) {
		this.strategies.add(strategy);
	}

	public static void init(Map<String, HistoricalStock> historyMap)
			throws Exception {
		instance = new StrategyHandler();
		instance.reset(historyMap);
	}

	public static void init() throws Exception {
		Map<String, HistoricalStock> historyMap = MmmProxyUtil.getProxy()
				.getHistoryMap(DealCfgCacheStore.getDayCount());
		init(historyMap);
	}

	@Override
	public void handleTick(TickEbo ebo) {
		for (Strategy each : strategies) {
			each.handleTick(ebo);
		}
	}

	public void reset(Map<String, HistoricalStock> historyMap) throws Exception {
		for (Strategy each : strategies) {
			each.init(historyMap);
		}
	}

	public static StrategyHandler get() {
		return instance;
	}

	public List<Strategy> getStrategies() {
		return strategies;
	}
}
