package com.oforsky.mmm.handler.strategy;

import com.oforsky.mmm.ebo.StockEbo;

public interface DealState {

	void handle(DealContext context, StockEbo stock) throws Exception;
}
