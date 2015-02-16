package com.oforsky.mmm.handler;

import java.util.Date;

import com.oforsky.mmm.capture.TickRetriever;
import com.oforsky.mmm.ebo.BidReqEbo;
import com.oforsky.mmm.ebo.StorageEbo;
import com.oforsky.mmm.ebo.TickEbo;
import com.oforsky.mmm.ebo.WarrantEbo;
import com.oforsky.mmm.service.DloService;
import com.oforsky.mmm.util.MmmUtil;
import com.truetel.jcore.util.TimeUtil;

public class StorageHandler {

	private DloService dloSvc;

	private TickRetriever retriever;

	public StorageHandler(DloService dloSvc, TickRetriever retriever) {
		this.retriever = retriever;
		this.dloSvc = dloSvc;
	}

	public void sellStorage(StorageEbo ebo) throws Exception {
		WarrantEbo warrant = ebo.getWarrantEboForced();
		TickEbo tick = TickEbo.valueOf(retriever.getWarrantTick(
				MmmUtil.getTodayStr(), ebo.getCode(), warrant.getCode()));
		BidReqEbo req = BidReqEbo.sell(ebo, tick);
		dloSvc.createBidReq(req);
	}

}
