package com.oforsky.mmm.part.req;

import com.oforsky.mmm.cache.SvcCfgCacheStore;
import com.oforsky.mmm.part.zone.MmmZone;
import com.oforsky.mmm.part.zone.QueryJobZone;
import com.oforsky.mmm.proxy.MmmProxyUtil;
import com.oforsky.mmm.timer.RetryTimer;
import com.oforsky.mmm.timer.TimerService;
import com.truetel.jcore.part.TransactionPolicy;
import com.truetel.jcore.part.TransactionPolicyEnum;

@TransactionPolicy(TransactionPolicyEnum.None)
public class QueryJobReq extends RetryReqBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer jobOid;

	public QueryJobReq(Integer jobOid) {
		super(new ReqRetryArgs());
		this.jobOid = jobOid;
	}

	@Override
	public String getTaskId() {
		return "BidJobReq-" + jobOid;
	}

	@Override
	protected void handleSucc() throws Exception {
		MmmProxyUtil.getProxy().createBidReq(jobOid);
	}

	@Override
	protected void handleTmpFail(Exception e) throws Exception {
		MmmProxyUtil.getProxy().queryJobFailRetrieval(jobOid, e);
	}

	@Override
	protected void handleFinalFail(Exception e) throws Exception {
		MmmProxyUtil.getProxy().queryJobFail(jobOid, e);
	}

	static class ReqRetryArgs implements com.oforsky.mmm.part.req.RetryArgs {

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
			return QueryJobZone.get();
		}

		@Override
		public TimerService getRetryTimer() {
			return RetryTimer.get();
		}
	}
}
