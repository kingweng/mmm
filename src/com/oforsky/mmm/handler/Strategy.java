package com.oforsky.mmm.handler;

import java.util.Map;

import com.oforsky.mmm.data.HistoricalStock;
import com.oforsky.mmm.ebo.TickEbo;

public interface Strategy {

	void handleTick(TickEbo ebo);

	void init(Map<String, HistoricalStock> historyMap) throws Exception;
}
