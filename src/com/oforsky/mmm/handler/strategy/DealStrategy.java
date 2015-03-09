package com.oforsky.mmm.handler.strategy;

import com.oforsky.mmm.ebo.SellTypeEnum;
import com.oforsky.mmm.ebo.StockEbo;

public interface DealStrategy {

	boolean canBuy(StockEbo stock);

	boolean canSell(Double buyPrice, StockEbo stock);

	Double getBuyPrice(StockEbo stock);

	Double getSellPrice();

	SellTypeEnum getSellType();

}
