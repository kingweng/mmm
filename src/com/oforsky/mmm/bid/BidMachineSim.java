package com.oforsky.mmm.bid;

import org.apache.log4j.Logger;

import com.oforsky.mmm.ebo.BidReqEbo;
import com.oforsky.mmm.ebo.StorageEbo;

public class BidMachineSim implements BidMachine {

	private static final Logger log = Logger.getLogger(BidMachineSim.class);

	@Override
	public void proceed(BidReqEbo bid) throws Exception {
		log.info("proceed ...");
		log.info("bid == " + bid.dbgstr());
		Thread.sleep(1000);
	}

	@Override
	public void sell(StorageEbo ebo) throws Exception {
		log.info("sell ...");
		log.info("bid == " + ebo.dbgstr());
		Thread.sleep(1000);
	}

}
