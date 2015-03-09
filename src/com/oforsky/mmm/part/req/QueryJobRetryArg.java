package com.oforsky.mmm.part.req;

import com.oforsky.mmm.cache.WarrantCfgCacheStore;
import com.oforsky.mmm.ebo.ActionTypeEnum;
import com.oforsky.mmm.part.zone.MmmZone;
import com.oforsky.mmm.part.zone.QueryJobZone;
import com.oforsky.mmm.timer.RetryTimer;
import com.oforsky.mmm.timer.TimerService;

public class QueryJobRetryArg implements RetryArgs {

	private ActionTypeEnum jobType;

	public QueryJobRetryArg(ActionTypeEnum jobType) {
		this.jobType = jobType;
	}

	@Override
	public Integer getRetryIntvl() {
		return WarrantCfgCacheStore.getWarrantRetryIntvl();
	}

	@Override
	public Integer getRetryLimit() {
		switch (jobType) {
		case Buy:
			return WarrantCfgCacheStore.getBuyRetryLimit();
		case Sell:
			return WarrantCfgCacheStore.getSellRetryLimit();
		default:
			// impossible to be here!
			return -1;
		}
	}

	@Override
	public MmmZone getRetryZone() {
		return QueryJobZone.get();
	}

	@Override
	public TimerService getRetryTimer() {
		return RetryTimer.get();
	}
}
