package com.oforsky.mmm.bid;

import org.apache.log4j.Logger;

import com.oforsky.mmm.ebo.BidReqEbo;

public class BidInitState extends BidState {

	private static final Logger log = Logger.getLogger(BidInitState.class);

	public BidInitState(BidContext context) {
		super(context);
	}

	@Override
	public void handle(BidEvent event) throws Exception {
		BidState state = null;
		BidReqEbo bid = context.getBidReq();
		bid.getAction().handleInit(bid);
		state = new BidSuccessState(context);
		context.setState(state);
	}

}
