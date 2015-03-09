package com.oforsky.mmm.bid;

import com.oforsky.mmm.ebo.BidReqEbo;

public class BidContext extends BidState {

	private BidState state;

	private BidReqEbo bidReq;

	public BidContext(BidReqEbo bidReq) {
		super(null);
		this.state = new BidInitState(this);
		this.bidReq = bidReq;
	}

	public BidReqEbo getBidReq() {
		return bidReq;
	}

	public void setState(BidState state) throws Exception {
		this.state = state;
	}

	@Override
	public void handle(BidEvent event) throws Exception {
		state.handle(event);
	}

}
