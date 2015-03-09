package com.oforsky.mmm.handler.strategy;

import com.oforsky.mmm.ebo.DealEbo;
import com.oforsky.mmm.ebo.StockEbo;
import com.truetel.jcore.util.AppException;

public class HoldingDealState implements DealState {

	private DealStrategy strategy;

	private String code;

	private Double buyPrice;

	private String buyDate;

	public HoldingDealState(DealStrategy strategy, String code, String dateStr,
			Double buyPrice) {
		this.strategy = strategy;
		this.code = code;
		this.buyPrice = buyPrice;
		this.buyDate = dateStr;
	}

	@Override
	public void handle(DealContext context, StockEbo stock) throws Exception {
		checkCode(stock);
		if (strategy.canSell(buyPrice, stock)) {
			context.setState(new MonitorDealState(strategy));
			context.addDeal(aDeal(stock));
		}
	}

	private void checkCode(StockEbo stock) throws AppException {
		if (!code.equals(stock.getCode())) {
			throw new AppException(5000,
					"different codes cannot in a single deal.");
		}
	}

	private DealEbo aDeal(StockEbo stock) {
		DealEbo deal = new DealEbo();
		deal.setCode(code);
		deal.setBuyDateStr(buyDate);
		deal.setBuyPrice(buyPrice);
		deal.setSellDateStr(stock.getDateStr());
		deal.setSellPrice(strategy.getSellPrice());
		deal.setSellType(strategy.getSellType());
		return deal;
	}
}
