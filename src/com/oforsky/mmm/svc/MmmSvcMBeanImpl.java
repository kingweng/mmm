/**
 * MmmSvcMBeanImpl.java
 *
 * Copyright (c) 2007-2013 TrueTel Communications. All rights reserved.
 */
package com.oforsky.mmm.svc;

import org.apache.log4j.Logger;

import com.oforsky.mmm.cache.SvcCfgCacheStore;
import com.oforsky.mmm.timer.DailyTimerTask;
import com.oforsky.mmm.timer.KBreakTimerTask;
import com.oforsky.mmm.timer.RevenueBreakTimerTask;
import com.oforsky.mmm.timer.SchTimerTask;

public class MmmSvcMBeanImpl extends MmmBaseSvcMBeanImpl implements MmmSvcMBean {

	private static final Logger log = Logger.getLogger(MmmSvcMBeanImpl.class);

	private static final String TASVERSION = "TAS-OFF-R5V1P4 2014-04-30 16:43:36";

	public MmmSvcMBeanImpl() throws Exception {
	}

	@Override
	protected void postActivateSvcCb() throws Exception {
		super.postActivateSvcCb();
	}

	@Override
	protected void postPrimarizeSvcCb() throws Exception {
		super.postPrimarizeSvcCb();
		log.info("schedule all timers...");
		new DailyTimerTask(SvcCfgCacheStore.getDailyImportTime()).schedule();
		new RevenueBreakTimerTask(SvcCfgCacheStore.getRevenueSellTime())
				.schedule();
		new KBreakTimerTask(SvcCfgCacheStore.getKBreakSellTime()).schedule();
		SchTimerTask.schedule();
	}
}
