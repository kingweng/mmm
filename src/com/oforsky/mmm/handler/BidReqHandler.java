package com.oforsky.mmm.handler;

import com.oforsky.mmm.bid.BidMachine;
import com.oforsky.mmm.bid.BidMachineSim;
import com.oforsky.mmm.ebo.BidReqEbo;
import com.oforsky.mmm.service.DloService;
import com.oforsky.mmm.service.DloServiceImpl;

public class BidReqHandler {

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

	public BidReqEbo proceed() throws Exception {
		machine.proceed(bid);
		bid.succeed();
		bid.getAction().postProceed(bid);
		dloSvc.updateBidReq(bid);
		return bid;
	}

	public void failBid(Exception e) throws Exception {
		bid.fail(e);
		dloSvc.updateBidReq(bid);
	}
	
}
