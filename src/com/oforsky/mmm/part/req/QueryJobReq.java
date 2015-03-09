package com.oforsky.mmm.part.req;

import com.oforsky.mmm.ebo.ActionTypeEnum;
import com.oforsky.mmm.proxy.MmmProxyUtil;
import com.truetel.jcore.part.TransactionPolicy;
import com.truetel.jcore.part.TransactionPolicyEnum;

@TransactionPolicy(TransactionPolicyEnum.None)
public class QueryJobReq extends RetryReqBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer jobOid;

	public QueryJobReq(Integer jobOid, ActionTypeEnum type) {
		super(new QueryJobRetryArg(type));
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

}
