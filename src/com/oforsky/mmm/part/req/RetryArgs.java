package com.oforsky.mmm.part.req;

import com.oforsky.mmm.cache.SvcCfgCacheStore;
import com.oforsky.mmm.part.zone.MmmZone;
import com.oforsky.mmm.proxy.MmmProxy;
import com.oforsky.mmm.proxy.MmmProxyUtil;
import com.oforsky.mmm.timer.TimerService;

public interface RetryArgs {

    Integer getRetryIntvl();

    Integer getRetryLimit();

    MmmZone getRetryZone();

    TimerService getRetryTimer();
}
