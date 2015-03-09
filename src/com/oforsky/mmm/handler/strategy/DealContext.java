package com.oforsky.mmm.handler.strategy;

import java.util.ArrayList;
import java.util.List;

import com.oforsky.mmm.ebo.DealEbo;
import com.oforsky.mmm.ebo.StockEbo;

public class DealContext implements DealState {

	private DealState state;

	private List<DealEbo> deals = new ArrayList<DealEbo>();

	public DealContext(DealStrategy strategy) {
		state = new MonitorDealState(strategy);
	}

	public void setState(final DealState state) {
		this.state = state;
	}

	public void addDeal(DealEbo deal) {
		this.deals.add(deal);
	}

	public List<DealEbo> getDeals() {
		return new ArrayList<DealEbo>(deals);
	}

	@Override
	public void handle(DealContext context, StockEbo stock) throws Exception {
		state.handle(this, stock);
	}

}
