package com.oforsky.mmm.handler;

import com.oforsky.mmm.ebo.TickEbo;

public interface TickListener {

	void handleTick(TickEbo ebo);
}
