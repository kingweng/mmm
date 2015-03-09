package com.oforsky.mmm.handler.strategy;

import org.apache.log4j.Logger;

import com.oforsky.mmm.ebo.TickEbo;

public class MonitorTickState implements TickState {

	private static final Logger log = Logger.getLogger(MonitorTickState.class);

	private TickStrategy strategy;

	public MonitorTickState(TickStrategy strategy) {
		this.strategy = strategy;
	}

	@Override
	public synchronized void handle(TickContext context, TickEbo tick)
			throws Exception {
		if (context.isAlive() && strategy.canBuy(tick)) {
			log.info("can buy, code=" + tick.getCode());
			context.setState(nextState(tick));
			context.prepareBuy(tick.getCode(), tick.getPrice());
		}
	}

	private TickState nextState(TickEbo tick) {
		return new HoldingTickState(strategy, tick.getCode(), tick.getPrice());
	}
}
