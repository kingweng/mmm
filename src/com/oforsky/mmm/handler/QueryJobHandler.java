package com.oforsky.mmm.handler;

import org.apache.log4j.Logger;

import com.oforsky.mmm.capture.TickRetriever;
import com.oforsky.mmm.ebo.BidReqEbo;
import com.oforsky.mmm.ebo.QueryJobEbo;
import com.oforsky.mmm.ebo.StorageEbo;
import com.oforsky.mmm.ebo.TickEbo;
import com.oforsky.mmm.ebo.WarrantEbo;
import com.oforsky.mmm.ebo.WarrantTickEbo;
import com.oforsky.mmm.service.DloService;
import com.oforsky.mmm.service.DloServiceImpl;
import com.truetel.jcore.util.AppException;

public class QueryJobHandler {

	private static final Logger log = Logger.getLogger(QueryJobEbo.class);

	private DloService dloSvc;

	private QueryJobEbo job;

	public QueryJobHandler(DloService dloSvc, QueryJobEbo job) {
		this.dloSvc = dloSvc;
		this.job = job;
	}

	public QueryJobHandler(QueryJobEbo job) {
		this(new DloServiceImpl(), job);
	}

	public BidReqEbo createBidReq(StorageEbo ebo, TickRetriever retriever)
			throws Exception {
		TickEbo tick = job.retrieve(ebo, retriever);
		if (!tick.isQualifyToSell()) {
			createTickLog(tick);
			throw new AppException(5011, ebo.getUnit(), ebo.getCode());
		}
		BidReqEbo req = BidReqEbo.sell(ebo, tick);
		dloSvc.createBidReq(req);
		dloSvc.updateQueryJob(job);
		return req;
	}

	private void createTickLog(TickEbo tick) {
		try {
			WarrantTickEbo logEb = WarrantTickEbo.valueOf(tick);
			dloSvc.createWarrantTick(logEb);
		} catch (Exception e) {
			log.error("createTickLog failed!", e);
		}

	}

	public BidReqEbo createBidReq(WarrantHandler handler) throws Exception {
		handler.retrieve(job.getCode());
		for (WarrantEbo each : handler.getWarrants()) {
			if (each.avaliableSell() >= job.getAmount()) {
				dloSvc.createWarrant(each);
				BidReqEbo req = BidReqEbo.buy(each, job);
				pay(req);
				dloSvc.createBidReq(req);
				job.retrieve();
				dloSvc.updateQueryJob(job);
				return req;
			}
		}
		throw new AppException(5010, job.getCode(), job.getAmount());
	}

	private void pay(BidReqEbo bid) throws Exception {
		int balance = dloSvc.getAndLockBalance();
		int pay = (bid.getFee() + bid.getAmount());
		log.info("prepare to pay, balance = " + balance + ", pay = " + pay);
		if (balance < pay) {
			throw new AppException(5013, balance, pay);
		}
		balance = dloSvc.updateBalance(balance - pay);
		log.info("after update, balance == " + balance);
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
