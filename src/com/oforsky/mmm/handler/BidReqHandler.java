package com.oforsky.mmm.handler;

import org.apache.log4j.Logger;

import com.oforsky.mmm.bid.BidMachine;
import com.oforsky.mmm.bid.BidMachineSim;
import com.oforsky.mmm.ebo.ActionTypeEnum;
import com.oforsky.mmm.ebo.BidReqEbo;
import com.oforsky.mmm.ebo.StorageEbo;
import com.oforsky.mmm.ebo.StorageLogEbo;
import com.oforsky.mmm.service.DloService;
import com.oforsky.mmm.service.DloServiceImpl;
import com.truetel.jcore.util.AppException;

public class BidReqHandler {

	private static final Logger log = Logger.getLogger(BidReqHandler.class);

	private BidMachine machine;

	private BidReqEbo bid;

	private DloService dloSvc;

	public BidReqHandler(BidReqEbo bid) {
		this(new BidMachineSim(), bid, new DloServiceImpl());
	}

	public BidReqHandler(BidMachine machine, BidReqEbo bid, DloService dloSvc) {
		super();
		this.machine = machine;
		this.bid = bid;
		this.dloSvc = dloSvc;
	}

	public void failBid(Exception e) throws Exception {
		bid.fail(e);
		dloSvc.updateBidReq(bid);
		restoreAmount();
	}

	private void restoreAmount() throws Exception {
		if (bid.getAction() == ActionTypeEnum.Buy) {
			int amount = bid.getAmount() + bid.getFee();
			log.info("restore the money[" + amount + "]");
			int balance = dloSvc.updateBalance(amount);
			log.info("balance == " + balance);
		}
	}

	public void sellStorage() throws Exception {
		int balance = dloSvc.getAndLockBalance();
		int sellFee = bid.getFee() + bid.getSellTax();
		log.info("prepare to sell, balance = " + balance + ", amount = "
				+ bid.getAmount() + ", sellFee = " + sellFee);
		balance = dloSvc.updateBalance(balance + (bid.getAmount() - sellFee));
		log.info("after update, balance == " + balance);
		dloSvc.createStorageLog(new StorageLogEbo(bid));
		bid.succeed();
		dloSvc.updateBidReq(bid);
	}

	public void createStorage() throws Exception {
		dloSvc.createStorage(new StorageEbo(bid));
		bid.succeed();
		dloSvc.updateBidReq(bid);
	}
}
