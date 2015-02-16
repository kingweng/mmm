package com.oforsky.mmm.part.zone;

import com.oforsky.mmm.svc.MmmPart;
import com.truetel.jcore.part.ZoneReq;

public class BidReqZone implements MmmZone {

	private static BidReqZone instance = new BidReqZone();

	public static BidReqZone get() {
		return instance;
	}

	private BidReqZone() {

	}

	@Override
	public void putReq(ZoneReq req) throws Exception {
		MmmPart.getBidReqZone().putReq(req);
	}

}
