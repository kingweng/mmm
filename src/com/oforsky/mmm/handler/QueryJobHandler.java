package com.oforsky.mmm.handler;

import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;

import com.oforsky.mmm.capture.TickRetriever;
import com.oforsky.mmm.ebo.BidReqEbo;
import com.oforsky.mmm.ebo.JobStateFsm;
import com.oforsky.mmm.ebo.QueryJobEbo;
import com.oforsky.mmm.ebo.StorageEbo;
import com.oforsky.mmm.ebo.TickEbo;
import com.oforsky.mmm.ebo.WarrantEbo;
import com.oforsky.mmm.ebo.WarrantTickEbo;
import com.oforsky.mmm.service.DloService;
import com.oforsky.mmm.service.DloServiceImpl;
import com.oforsky.mmm.service.WarrantService;
import com.truetel.jcore.util.AppException;

public class QueryJobHandler {

	public static final JobStateFsm[] NOT_FINISHED_STATES = { JobStateFsm.Init,
			JobStateFsm.RetryRetrieval };

	private static final Logger log = Logger.getLogger(QueryJobEbo.class);

	private DloService dloSvc;

	private QueryJobEbo job;

	private WarrantService warrantSvc;

	public QueryJobHandler(DloService dloSvc, QueryJobEbo job) {
		this.dloSvc = dloSvc;
		this.job = job;
	}

	public QueryJobHandler(QueryJobEbo job) {
		this(new DloServiceImpl(), job);
	}

	public QueryJobHandler(DloService dloSvc, WarrantService warrantSvc,
			QueryJobEbo job) {
		this.dloSvc = dloSvc;
		this.warrantSvc = warrantSvc;
		this.job = job;
	}

	private void checkBidInterval() throws Exception {
		Calendar now = Calendar.getInstance();
		if (now.after(dloSvc.getLastBidTime())) {
			throw new AppException(5015, job.getCode());
		}
	}

	public BidReqEbo createBidReq(StorageEbo ebo, TickRetriever retriever)
			throws Exception {
		checkBidInterval();
		TickEbo tick = job.retrieve(ebo, retriever);
		Integer warrantTickOid = checkPrice(ebo, tick);
		BidReqEbo req = BidReqEbo.sell(ebo, tick, warrantTickOid);
		dloSvc.createBidReq(req);
		dloSvc.updateQueryJob(job);
		return req;
	}

	private Integer checkPrice(StorageEbo ebo, TickEbo tick) throws Exception,
			AppException {
		Integer warrantTickOid;
		try {
			WarrantEbo warrant = ebo.getWarrantEboForced();
			double diffPriceRate = diffPriceRate(ebo, tick, warrant);
			log.info("priceDiffRate is " + diffPriceRate);
			if (diffPriceRate > dloSvc.getMaxDiffRate()) {
				throw new AppException(5011, ebo.getUnit(), ebo.getCode());
			}
		} finally {
			warrantTickOid = createTickLog(tick);
		}
		return warrantTickOid;
	}

	private double diffPriceRate(StorageEbo ebo, TickEbo tick,
			WarrantEbo warrant) throws Exception {
		double theoryPrice = warrant.getTheoryPrice(job.getPrice());
		log.info("code[" + ebo.getCode() + "] at price[" + job.getPrice()
				+ "] theory price is " + theoryPrice);
		double priceDiffRate = (theoryPrice - tick.getBuyPrice(ebo.getUnit()))
				/ theoryPrice;
		return Math.abs(priceDiffRate);
	}

	private Integer createTickLog(TickEbo tick) {
		try {
			WarrantTickEbo logEbo = WarrantTickEbo.valueOf(tick);
			dloSvc.createWarrantTick(logEbo);
			return logEbo.getTickOid();
		} catch (Exception e) {
			log.error("createTickLog failed!", e);
		}
		return null;
	}

	public BidReqEbo createBidReq(WarrantHandler handler) throws Exception {
		checkBidInterval();
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

	public QueryJobEbo createBidReq(Integer jobOid) throws Exception {
		checkBidInterval();
		doQuery();
		job.retrieve();
		dloSvc.createBidReq(job);
		dloSvc.updateQueryJob(job);
		return job;
	}

	private void doQuery() throws Exception, AppException {
		switch (job.getAction()) {
		case Buy:
			chooseWarrant();
			break;
		case Sell:
			retrieveWarrantTick();
			break;
		default:
			throw new AppException(5000, "Cannot be here!");
		}
	}

	private void retrieveWarrantTick() throws Exception {
		WarrantEbo warrant = dloSvc.getWarrantByTarget(job.getCode());
		TickEbo tick = warrantSvc.getTick(warrant.getCode());
		job.setTick(tick);
	}

	private void chooseWarrant() throws Exception {
		log.info("choose warrant for Code[" + job.getCode() + "]");
		for (WarrantEbo each : warrantSvc.listWarrants(job.getCode())) {
			if (each.avaliableSell() >= job.getAmount()) {
				job.setWarrant(each);
				return;
			}
		}
		throw new AppException(5010, job.getCode(), job.getAmount());
	}

	public List<QueryJobEbo> recover() throws Exception {
		List<QueryJobEbo> ebos = dloSvc.listQueryJob(NOT_FINISHED_STATES);
		for (QueryJobEbo each : ebos) {
			each.fail(new Exception("after recovery, ignore this query."));
		}
		dloSvc.batchUpdateQueryJob(ebos);
		return ebos;
	}
}
