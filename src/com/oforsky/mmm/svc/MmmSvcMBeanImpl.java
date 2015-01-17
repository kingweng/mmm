/**
 * MmmSvcMBeanImpl.java
 *
 * Copyright (c) 2007-2013 TrueTel Communications. All rights reserved.
 */
package com.oforsky.mmm.svc;

import com.oforsky.mmm.cache.SvcCfgCacheStore;
import com.oforsky.mmm.timer.DailyTimerTask;
import com.oforsky.mmm.timer.SchTimerTask;

import org.apache.log4j.Logger;

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
		new DailyTimerTask(SvcCfgCacheStore.getDailyImportTime()).schedule();
		SchTimerTask.schedule();
	}
}
