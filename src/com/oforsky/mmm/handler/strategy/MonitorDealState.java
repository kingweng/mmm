package com.oforsky.mmm.handler.strategy;

import com.oforsky.mmm.ebo.StockEbo;

public class MonitorDealState implements DealState {
	private DealStrategy strategy;

	public MonitorDealState(DealStrategy strategy) {
		this.strategy = strategy;
	}

	@Override
	public void handle(DealContext context, StockEbo stock) {
		if (strategy.canBuy(stock)) {
			context.setState(nextState(stock));
		}
	}

	private HoldingDealState nextState(StockEbo stock) {
		return new HoldingDealState(strategy, stock.getCode(), stock.getDateStr(),
				strategy.getBuyPrice(stock));
	}
}
