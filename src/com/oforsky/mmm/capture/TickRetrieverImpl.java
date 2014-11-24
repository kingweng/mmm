package com.oforsky.mmm.capture;

import java.net.MalformedURLException;

import org.apache.log4j.Logger;

import com.oforsky.mmm.capture.data.Tick;
import com.oforsky.mmm.service.HttpService;
import com.oforsky.mmm.service.HttpServiceImpl;

/**
 * Created by kingweng on 2014/8/4.
 */
public class TickRetrieverImpl implements TickRetriever {

	private static final Logger log = Logger.getLogger(TickRetrieverImpl.class);

	private static final String TWSE_GET_TICK_URL = "http://mis.twse.com.tw/stock/api/getStockInfo.jsp?ex_ch=tse_%s.tw_%s";

	private HttpService fileSvc;

	public TickRetrieverImpl() {
		this(new HttpServiceImpl());
	}

	public TickRetrieverImpl(HttpService fileSvc) {
		this.fileSvc = fileSvc;
	}

	@Override
	public Tick getTick(String yyyymmdd, String stockCode) throws Exception {
		String content = fileSvc.download(getTickUrl(yyyymmdd, stockCode));
		log.trace("download tick content == " + content);
		return Tick.valueOf(content);
	}

	private String getTickUrl(String yyyymmdd, String code)
			throws MalformedURLException {
		return String.format(TWSE_GET_TICK_URL, yyyymmdd, code);
	}
}
