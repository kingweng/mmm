package com.oforsky.mmm.part.req;

import com.oforsky.mmm.cache.SvcCfgCacheStore;
import com.oforsky.mmm.part.zone.CsvPullReqZone;
import com.oforsky.mmm.part.zone.MmmZone;
import com.oforsky.mmm.timer.RetryTimer;
import com.oforsky.mmm.timer.TimerService;

/**
 * Created by kingweng on 2014/8/20.
 */
public class CsvPullReq extends com.oforsky.mmm.part.req.RetryReqBase {

    private Integer reqOid;

    public CsvPullReq(Integer reqOid) {
        super(new ReqRetryArgs());
        this.reqOid = reqOid;
    }

    @Override
    protected void handleSucc() throws Exception {
        getProxy().dailyCsvReqRetrieve(reqOid);
    }

    @Override
    protected void handleTmpFail(Exception e) throws Exception {
        getProxy().dailyCsvReqFailRetrieval(reqOid, e.getMessage());
    }

    @Override
    protected void handleFinalFail(Exception e) throws Exception {
        getProxy().dailyCsvReqReqFail(reqOid, e.getMessage());
    }

    @Override
    public String getTaskId() {
        return "CsvPullReq-" + reqOid;
    }
}

class ReqRetryArgs implements com.oforsky.mmm.part.req.RetryArgs {

    @Override
    public Integer getRetryIntvl() {
        return SvcCfgCacheStore.getRetryIntvl();
    }

    @Override
    public Integer getRetryLimit() {
        return SvcCfgCacheStore.getRetryLimit();
    }

    @Override
    public MmmZone getRetryZone() {
        return CsvPullReqZone.get();
    }

    @Override
    public TimerService getRetryTimer() {
        return RetryTimer.get();
    }
}
