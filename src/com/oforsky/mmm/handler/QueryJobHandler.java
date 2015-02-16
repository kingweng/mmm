package com.oforsky.mmm.handler;

import com.oforsky.mmm.ebo.BidReqEbo;
import com.oforsky.mmm.ebo.QueryJobEbo;
import com.oforsky.mmm.ebo.WarrantEbo;
import com.oforsky.mmm.service.DloService;
import com.oforsky.mmm.service.DloServiceImpl;
import com.truetel.jcore.util.AppException;

public class QueryJobHandler {

	private DloService dloSvc;

	private QueryJobEbo job;

	public QueryJobHandler(DloService dloSvc, QueryJobEbo job) {
		this.dloSvc = dloSvc;
		this.job = job;
	}

	public QueryJobHandler(QueryJobEbo job) {
		this(new DloServiceImpl(), job);
	}

	public BidReqEbo createBidReq(WarrantHandler handler) throws Exception {
		for (WarrantEbo each : job.retrieve(handler)) {
			if (each.avaliableSell() >= job.getAmount()) {
				dloSvc.createWarrant(each);
				BidReqEbo req = BidReqEbo.buy(each, job);
				dloSvc.createBidReq(req);
				dloSvc.updateQueryJob(job);
				return req;
			}
		}
		throw new AppException(5010, job.getCode(), job.getAmount());
	}

	public void failRetrieval(Exception e) throws Exception {
		job.failRetrieval(e);
		dloSvc.updateQueryJob(job);
	}

	public void fail(Exception e) throws Exception {
		job.fail(e);
		dloSvc.updateQueryJob(job);
	}

}
