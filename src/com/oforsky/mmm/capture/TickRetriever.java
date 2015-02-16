package com.oforsky.mmm.capture;

import com.oforsky.mmm.capture.data.Tick;

/**
 * Created by kingweng on 2014/11/6.
 */
public interface TickRetriever {
	public Tick getTick(String yyyymmdd, String stockCode) throws Exception;

	public Tick getWarrantTick(String yyyymmdd, String targetCode,
			String warrantCode) throws Exception;
}
