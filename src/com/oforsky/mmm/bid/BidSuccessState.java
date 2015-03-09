package com.oforsky.mmm.bid;

import com.truetel.jcore.util.AppException;

public class BidSuccessState extends BidState {

	public BidSuccessState(BidContext context) {
		super(context);
	}

	@Override
	public void handle(BidEvent event) throws Exception {
		throw new AppException(5000, "Bid is done!");
	}

}
