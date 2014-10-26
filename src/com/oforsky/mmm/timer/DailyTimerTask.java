package com.oforsky.mmm.timer;

import com.oforsky.mmm.cache.SvcCfgCacheStore;
import com.oforsky.mmm.ebo.DailyCsvReqEbo;
import com.oforsky.mmm.ebo.MmmEnum;
import com.oforsky.mmm.ebo.WatchStockEbo;
import com.oforsky.mmm.proxy.MmmProxy;
import com.oforsky.mmm.proxy.MmmProxyUtil;
import com.oforsky.mmm.svc.MmmPart;
import com.truetel.jcore.part.JcoreTimerTask;
import com.truetel.jcore.util.TimeUtil;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by kingweng on 2014/8/24.
 */
public class DailyTimerTask implements JcoreTimerTask {

    private static final Logger log = Logger.getLogger(DailyTimerTask.class);

    public static void schedule() throws Exception {
        MmmPart.getDailyTimerTimer()
                .scheduleDaily("DailyTimerTask", new DailyTimerTask(),
                        toMinsOfDay(SvcCfgCacheStore.getDailyImportTime()));
    }

    @Override
    public void timerFiredCb() throws Exception {
        String yesterday = getYesterday();
        List<WatchStockEbo> watchStocks = getProxy()
                .listAll(MmmEnum.WatchStock);
        List<DailyCsvReqEbo> cvsReqEbos = genDailyCsvReqEbos(yesterday,
                watchStocks);
        log.info("To create CsvPullReq list, size == " + cvsReqEbos.size());
        getProxy().batchCreateDailyCsvReq(cvsReqEbos);
    }

    private List<DailyCsvReqEbo> genDailyCsvReqEbos(String yesterday,
            List<WatchStockEbo> watchStocks) {
        List<DailyCsvReqEbo> pullReqEbos = new ArrayList<DailyCsvReqEbo>();
        for (WatchStockEbo watchStock : watchStocks) {
            DailyCsvReqEbo ebo = new DailyCsvReqEbo(watchStock.getCode(),
                    getMonthStr(yesterday),
                    watchStock.getName());
            pullReqEbos.add(ebo);
        }
        return pullReqEbos;
    }

    private String getMonthStr(String yesterday) {
        return yesterday.substring(0, yesterday.length() - 2);
    }

    private MmmProxy getProxy() throws Exception {
        return MmmProxyUtil.getProxy();
    }

    private String getYesterday() throws Exception {
        Calendar yesterday = TimeUtil
                .addDate(TimeUtil.getToday().getTime(), -1);
        return TimeUtil.dateToY6d(yesterday.getTime());
    }

    private static Integer toMinsOfDay(String hhmm) throws Exception {
        Integer hour = Integer.parseInt(hhmm.substring(0, 2));
        Integer min = Integer.parseInt(hhmm.substring(2, 4));
        return hour * 60 + min;
    }
}
