package com.oforsky.mmm.bid;

public class BidFailureState extends BidState {

	private Exception e;

	public BidFailureState(BidContext context, Exception e) {
		super(context);
		this.e = e;
	}

	@Override
	public void handle(BidEvent event) throws Exception {
		// TODO Auto-generated method stub

	}

	public Exception getException() {
		return e;
	}

}
