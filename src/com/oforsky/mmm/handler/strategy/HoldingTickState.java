package com.oforsky.mmm.handler.strategy;

import org.apache.log4j.Logger;

import com.oforsky.mmm.ebo.TickEbo;
import com.truetel.jcore.util.AppException;

public class HoldingTickState implements TickState {

	private static final Logger log = Logger.getLogger(HoldingTickState.class);

	private TickStrategy strategy;

	private Double buyPrice;

	private String code;

	public HoldingTickState(TickStrategy strategy, String code, Double price) {
		this.strategy = strategy;
		this.code = code;
		this.buyPrice = price;
	}

	@Override
	public synchronized void handle(TickContext context, TickEbo tick)
			throws Exception {
		checkCode(tick);
		if (context.isAlive() && strategy.canSell(buyPrice, tick)) {
			log.info("can sell, code=" + tick.getCode());
			context.setState(new MonitorTickState(strategy));
			context.prepareSell(code, tick.getPrice(), strategy.getSellType());
		}
	}

	private void checkCode(TickEbo tick) throws AppException {
		if (!code.equals(tick.getCode())) {
			throw new AppException(5000,
					"different codes cannot in a single deal.");
		}
	}

}
