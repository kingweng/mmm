package com.oforsky.mmm.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.oforsky.mmm.cache.StorageCacheStore;
import com.oforsky.mmm.data.HistoricalStock;
import com.oforsky.mmm.dlo.StockDlo;
import com.oforsky.mmm.ebo.StorageEbo;
import com.oforsky.mmm.ebo.TickEbo;
import com.oforsky.mmm.handler.strategy.TickContext;
import com.oforsky.mmm.handler.strategy.TickStrategy;
import com.oforsky.mmm.handler.strategy.TickStrategyImpl;
import com.oforsky.mmm.proxy.MmmProxyUtil;

public class ContextHandler implements TickListener {

	private static final Logger log = Logger.getLogger(ContextHandler.class);

	private static ContextHandler instance = new ContextHandler();

	private Map<String, TickContext> contexts = new HashMap<String, TickContext>();

	private ContextHandler() {
	}

	public void putContext(String code, TickContext context) {
		this.contexts.put(code, context);
	}

	@Override
	public void handleTick(TickEbo tick) {
		TickContext context = contexts.get(tick.getCode());
		try {
			if (context != null) {
				context.handle(context, tick);
			}
		} catch (Exception e) {
			log.error("handleTick failed!", e);
		}
	}

	public static ContextHandler get() {
		return instance;
	}

	public List<TickStrategy> getStrategies() {
		List<TickStrategy> result = new ArrayList<TickStrategy>();
		for (TickContext context : contexts.values()) {
			result.add(context.getStrategy());
		}
		return result;
	}

	public void putFakeTick(TickEbo tick) throws Exception {
		log.info("putFakeTick enter, tick=" + tick.dbgstr());
		String code = tick.getCode();
		HistoricalStock history = new HistoricalStock(code,
				new StockDlo().listByCodeSize(code, 120));
		TickStrategy strategy = new TickStrategyImpl(history);
		enableAllBreak(strategy);
		StorageEbo storage = StorageCacheStore.getStore().get(code);
		TickContext context = new TickContext(MmmProxyUtil.getProxy(),
				strategy, storage);
		putContext(code, context);
		handleTick(tick);
	}

	private void enableAllBreak(TickStrategy strategy) {
		strategy.enableKBreak();
		strategy.enableRevenueBreak();
	}
}
