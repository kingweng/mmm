package com.oforsky.mmm.part.req;

import org.apache.log4j.Logger;

public class Retry {

	private Logger log = Logger.getLogger(Retry.class);

	private int retryTimes;

	private final RetryArgs args;

	public Retry(RetryArgs args) {
		this.args = args;
	}

	public boolean reachMaxRetries() {
		return retryTimes >= args.getRetryLimit();
	}

	public void setTimer(RetryReq retryReq) throws Exception {
		retryTimes++;
		log.debug("setTimer: retryTimes=[" + retryTimes + "], delaySec=["
				+ args.getRetryIntvl() + "]");
		args.getRetryTimer().schedule(retryReq, args.getRetryIntvl());
	}

	public void reset() {
		retryTimes = 0;
	}
}
