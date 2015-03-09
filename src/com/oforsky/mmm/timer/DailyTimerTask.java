package com.oforsky.mmm.timer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;

import com.oforsky.mmm.cache.StockGroupCacheStore;
import com.oforsky.mmm.ebo.DailyCsvReqEbo;
import com.oforsky.mmm.ebo.StockGroupEbo;
import com.oforsky.mmm.proxy.MmmProxy;
import com.oforsky.mmm.proxy.MmmProxyUtil;
import com.oforsky.mmm.svc.MmmPart;
import com.truetel.jcore.part.JcoreTimerTask;
import com.truetel.jcore.util.AppException;
import com.truetel.jcore.util.TimeUtil;

/**
 * Created by kingweng on 2014/8/24.
 */
public class DailyTimerTask implements JcoreTimerTask {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger log = Logger.getLogger(DailyTimerTask.class);

	private String hhmm;

	public DailyTimerTask(String hhmm) {
		this.hhmm = hhmm;
	}

	public void schedule() throws Exception {
		log.info("schedule at [" + hhmm + "]");
		MmmPart.getDailyTimerTimer().scheduleDaily(
				DailyTimerTask.class.getSimpleName(), this, toMinsOfDay(hhmm));
		
	}

	@Override
	public void timerFiredCb() throws Exception {
		log.info("timerFiredCb enter");
		String yesterday = getYesterday();
		createCsvReqs(getMonthStr(yesterday));
		log.info("timerFiredCb leave");
	}

	public void createCsvReqs(String pullMonth) throws AppException, Exception {
		List<DailyCsvReqEbo> cvsReqEbos = prepareCsvReqs(pullMonth);
		log.info("To create CsvPullReq list, size == " + cvsReqEbos.size());
		getProxy().batchCreateDailyCsvReq(cvsReqEbos);
	}

	private List<DailyCsvReqEbo> prepareCsvReqs(String pullMonth) {
		List<DailyCsvReqEbo> reqs = new ArrayList<DailyCsvReqEbo>();
		for (StockGroupEbo stock : StockGroupCacheStore.getStore().getValues()) {
			DailyCsvReqEbo ebo = new DailyCsvReqEbo(stock, pullMonth);
			reqs.add(ebo);
		}
		return reqs;
	}

	private String getMonthStr(String yyyymmdd) {
		return yyyymmdd.substring(0, yyyymmdd.length() - 2);
	}

	private MmmProxy getProxy() throws Exception {
		return MmmProxyUtil.getProxy();
	}

	private String getYesterday() throws Exception {
		Calendar yesterday = TimeUtil
				.addDate(TimeUtil.getToday().getTime(), -1);
		return TimeUtil.dateToY6d(yesterday.getTime());
	}

	private Integer toMinsOfDay(String hhmm) throws Exception {
		Integer hour = Integer.parseInt(hhmm.substring(0, 2));
		Integer min = Integer.parseInt(hhmm.substring(2, 4));
		return hour * 60 + min;
	}
}
