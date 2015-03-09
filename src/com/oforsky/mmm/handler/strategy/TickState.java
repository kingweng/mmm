package com.oforsky.mmm.handler.strategy;

import com.oforsky.mmm.ebo.TickEbo;

public interface TickState {
	void handle(TickContext context, TickEbo tick) throws Exception;
}
