package com.oforsky.mmm.timer;

import org.apache.log4j.Logger;

import com.oforsky.mmm.handler.ContextHandler;
import com.oforsky.mmm.handler.strategy.TickStrategy;
import com.oforsky.mmm.svc.MmmPart;
import com.truetel.jcore.part.JcoreTimerTask;

public class RevenueBreakTimerTask implements JcoreTimerTask {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger log = Logger.getLogger(DailyTimerTask.class);

	private String hhmm;

	public RevenueBreakTimerTask(String hhmm) {
		this.hhmm = hhmm;
	}

	public void schedule() throws Exception {
		log.info("schedule at [" + hhmm + "]");
		MmmPart.getDailyTimerTimer().scheduleDaily(
				RevenueBreakTimerTask.class.getSimpleName(), this,
				toMinsOfDay(hhmm));

	}

	@Override
	public void timerFiredCb() throws Exception {
		log.info("timerFiredCb enter");
		log.info("enableRevenueBreak for selling");
		for (TickStrategy strategy : ContextHandler.get().getStrategies()) {
			strategy.enableRevenueBreak();
		}
		log.info("timerFiredCb leave");
	}

	private Integer toMinsOfDay(String hhmm) throws Exception {
		Integer hour = Integer.parseInt(hhmm.substring(0, 2));
		Integer min = Integer.parseInt(hhmm.substring(2, 4));
		return hour * 60 + min;
	}
}
