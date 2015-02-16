package com.oforsky.mmm.capture;

import java.net.MalformedURLException;

import org.apache.log4j.Logger;

import com.oforsky.mmm.cache.StockGroupCacheStore;
import com.oforsky.mmm.capture.data.Tick;
import com.oforsky.mmm.ebo.StockGroupEbo;
import com.oforsky.mmm.service.HttpService;
import com.oforsky.mmm.service.HttpServiceImpl;

/**
 * Created by kingweng on 2014/8/4.
 */
public class TickRetrieverImpl implements TickRetriever {

	private static final Logger log = Logger.getLogger(TickRetrieverImpl.class);

	private HttpService fileSvc;

	public TickRetrieverImpl() {
		this(new HttpServiceImpl());
	}

	public TickRetrieverImpl(HttpService fileSvc) {
		this.fileSvc = fileSvc;
	}

	@Override
	public Tick getTick(String yyyymmdd, String code) throws Exception {
		String content = fileSvc.download(getTickUrl(yyyymmdd, code), "utf-8");
		log.trace("download tick content == " + content);
		return Tick.valueOf(content);
	}

	private String getTickUrl(String yyyymmdd, String code)
			throws MalformedURLException {
		StockGroupEbo stock = StockGroupCacheStore.getStore().get(code);
		return stock.getStockType().getTickUrl(code, yyyymmdd);
	}

	@Override
	public Tick getWarrantTick(String yyyymmdd, String targetCode,
			String warrantCode) throws Exception {
		String content = fileSvc.download(
				getWarrantTickUrl(yyyymmdd, targetCode, warrantCode), "utf-8");
		log.trace("download tick content == " + content);
		return Tick.valueOf(content);
	}

	private String getWarrantTickUrl(String yyyymmdd, String targetCode,
			String warrantCode) {
		StockGroupEbo stock = StockGroupCacheStore.getStore().get(targetCode);
		return stock.getStockType().getTickUrl(warrantCode, yyyymmdd);
	}
}
