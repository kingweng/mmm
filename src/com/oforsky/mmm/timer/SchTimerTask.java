package com.oforsky.mmm.timer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.oforsky.mmm.cache.StorageCacheStore;
import com.oforsky.mmm.cache.SvcCfgCacheStore;
import com.oforsky.mmm.data.HistoricalStock;
import com.oforsky.mmm.ebo.StorageEbo;
import com.oforsky.mmm.handler.ContextHandler;
import com.oforsky.mmm.handler.strategy.TickContext;
import com.oforsky.mmm.handler.strategy.TickStrategy;
import com.oforsky.mmm.handler.strategy.TickStrategyImpl;
import com.oforsky.mmm.proxy.MmmProxy;
import com.oforsky.mmm.proxy.MmmProxyUtil;
import com.oforsky.mmm.svc.MmmPart;
import com.oforsky.mmm.util.MmmUtil;
import com.truetel.jcore.data.DataFactory;
import com.truetel.jcore.data.RecurringTime;
import com.truetel.jcore.data.RecurringTypeEnum;
import com.truetel.jcore.data.Schedule;
import com.truetel.jcore.part.ScheduleTimerTask;
import com.truetel.jcore.util.TimeUtil;

/**
 * Created by kingweng on 2014/11/7.
 */
public class SchTimerTask implements ScheduleTimerTask {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger log = Logger.getLogger(SchTimerTask.class);

	public static void schedule() throws Exception {
		Schedule sch = getWorkingDaysSchedule();
		MmmPart.getSchTimerTimer().schedule(sch, new SchTimerTask());
	}

	private static Schedule getWorkingDaysSchedule() throws Exception {
		Schedule sch = DataFactory.newSchedule();
		sch.setStartTime(TimeUtil.addMinute(new Date(), 1).getTime());
		sch.setScheduleId("SchTimerTask");
		sch.setRecurringType(RecurringTypeEnum.Weekly);
		List<RecurringTime> rtimeList = getRecurringTimes();
		sch.setRecurringInfo(rtimeList.toArray(new RecurringTime[0]));
		return sch;
	}

	private static List<RecurringTime> getRecurringTimes() throws Exception {
		Calendar startTime = MmmUtil.hhmmToCalendar(getStartTime());
		// Sunday = 1, Monday = 2, Tuesday = 3, Wednesday = 4, Thursday = 5,
		// Friday = 6,
		// Saturday = 7
		List<RecurringTime> rtimeList = new ArrayList<RecurringTime>();
		for (int i = 2; i <= 6; i++) {
			RecurringTime rtime = DataFactory.newRecurringTime();
			rtime.setDayOfWeek(i);
			rtime.setHourOfDay(startTime.get(Calendar.HOUR_OF_DAY));
			rtime.setMinute(startTime.get(Calendar.MINUTE));
			rtimeList.add(rtime);
		}
		return rtimeList;
	}

	private static String getStartTime() {
		return SvcCfgCacheStore.getTickTimeRange().split("-")[0];
	}

	@Override
	public void durationEndedCb() throws Exception {
		// ignored
	}

	@Override
	public void timerEndedCb() throws Exception {
		// ignored
	}

	@Override
	public void timerFiredCb() throws Exception {
		log.info("SchTimerTask() fire...");
		MmmProxy proxy = MmmProxyUtil.getProxy();
		Map<String, HistoricalStock> histories = proxy.getHistoryMap(120);
		for (String code : histories.keySet()) {
			setupContext(proxy, histories, code);
		}
		proxy.genTickReqs();
		TickTimerTask.schedule();
	}

	private void setupContext(MmmProxy proxy,
			Map<String, HistoricalStock> histories, String code)
			throws Exception {
		TickStrategy strategy = new TickStrategyImpl(histories.get(code));
		StorageEbo storage = StorageCacheStore.getStore().get(code);
		TickContext context = new TickContext(proxy, strategy, storage);
		ContextHandler.get().putContext(code, context);
	}
}
