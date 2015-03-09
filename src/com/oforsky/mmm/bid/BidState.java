package com.oforsky.mmm.bid;

public abstract class BidState {

	protected BidContext context;

	public BidState(BidContext context) {
		this.context = context;
	}

	public abstract void handle(BidEvent event) throws Exception;
}
