package com.oforsky.mmm.timer;

import com.oforsky.mmm.cache.SvcCfgCacheStore;
import com.oforsky.mmm.proxy.MmmProxy;
import com.oforsky.mmm.proxy.MmmProxyUtil;
import com.oforsky.mmm.svc.MmmPart;
import com.oforsky.mmm.util.MmmUtil;
import com.truetel.jcore.part.JcoreTimerTask;

import org.apache.log4j.Logger;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by kingweng on 2014/11/7.
 */
public class TickTimerTask implements JcoreTimerTask {

	private static final Logger log = Logger.getLogger(TickTimerTask.class);

	public static void schedule() throws Exception {
		MmmPart.getDailyTimerTimer().schedule("TickTimerTask",
				new TickTimerTask(), nextFireTime());
	}

	@Override
	public void timerFiredCb() throws Exception {
		log.info("DailyTickTimerTask() fire...");
		MmmProxy proxy = MmmProxyUtil.getProxy();
		proxy.genTickReqs();
		scheduleNext();
	}

	private void scheduleNext() throws Exception {
		Calendar endTime = MmmUtil.hhmmToCalendar(getTickEndTime());
		if (endTime.after(Calendar.getInstance())) {
			schedule();
		}
	}

	private String getTickEndTime() {
		return SvcCfgCacheStore
				.getTickTimeRange().split("-")[1];
	}

	private static Date nextFireTime() {
		Calendar now = Calendar.getInstance();
		now.add(Calendar.SECOND, SvcCfgCacheStore.getTickInterval());
		return now.getTime();
	}
}
