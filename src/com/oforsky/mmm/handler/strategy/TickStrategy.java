package com.oforsky.mmm.handler.strategy;

import com.oforsky.mmm.ebo.SellTypeEnum;
import com.oforsky.mmm.ebo.TickEbo;

public interface TickStrategy {

	boolean canBuy(TickEbo tick);

	boolean canSell(Double buyPrice, TickEbo tick);

	SellTypeEnum getSellType();

	void enableKBreak();
	
	void enableRevenueBreak();

}
