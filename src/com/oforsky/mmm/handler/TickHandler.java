package com.oforsky.mmm.handler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.oforsky.mmm.cache.StockGroupCacheStore;
import com.oforsky.mmm.capture.TickRetriever;
import com.oforsky.mmm.capture.TickRetrieverImpl;
import com.oforsky.mmm.capture.data.Tick;
import com.oforsky.mmm.ebo.StockGroupEbo;
import com.oforsky.mmm.ebo.TickEbo;
import com.oforsky.mmm.part.req.TickReq;
import com.oforsky.mmm.part.zone.MmmZone;
import com.oforsky.mmm.part.zone.TickReqZone;
import com.oforsky.mmm.service.DloService;
import com.oforsky.mmm.service.DloServiceImpl;
import com.truetel.jcore.util.TimeUtil;

/**
 * Created by kingweng on 2014/11/6.
 */
public class TickHandler {

	private static final Logger log = Logger.getLogger(TickHandler.class);

	private MmmZone tickZone;

	private DloService dloSvc;

	private TickRetriever retriever;

	private List<TickListener> listeners = new ArrayList<TickListener>();

	public TickHandler(DloService dloSvc, MmmZone tickZone,
			TickRetriever retriever) {
		this.dloSvc = dloSvc;
		this.tickZone = tickZone;
		this.retriever = retriever;
	}

	public void addListener(TickListener listener) {
		listeners.add(listener);
	}

	public TickHandler() {
		this(new DloServiceImpl(), TickReqZone.get(), new TickRetrieverImpl());
	}

	public void genTickReqs() throws Exception {
		for (StockGroupEbo stock : StockGroupCacheStore.getStore().getValues()) {
			TickReq req = new TickReq(stock.getCode(), today());
			tickZone.putReq(req);
		}
	}

	private String today() {
		return TimeUtil.dateToY6d(new Date());
	}

	public void retrieve(String code, String y6d) throws Exception {
		log.trace("retrieve() code=" + code + ", y6d=" + y6d);
		Tick tick = retriever.getTick(y6d, code);
		TickEbo ebo = TickEbo.valueOf(tick);
		if (ebo.getTotalVolume() != 0) {
			notifyListeners(ebo);
		}
		dloSvc.createTick(ebo);
	}

	private void notifyListeners(TickEbo ebo) {
		log.trace("notifyListeners");
		for (TickListener each : listeners) {
			each.handleTick(ebo);
		}
	}

}
