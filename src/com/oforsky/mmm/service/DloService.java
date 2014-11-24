package com.oforsky.mmm.service;

import com.oforsky.mmm.ebo.DailyCsvReqEbo;
import com.oforsky.mmm.ebo.StockEbo;
import com.oforsky.mmm.ebo.TickEbo;
import com.oforsky.mmm.ebo.WatchStockEbo;

import java.util.Collection;
import java.util.List;

/**
 * Created by kingweng on 2014/10/21.
 */
public interface DloService {
	DailyCsvReqEbo getDailyCsvReq(Integer reqOid) throws Exception;

	void batchCreateStock(Collection<StockEbo> ebos) throws Exception;

	void deleteStock(String code, String monthStr) throws Exception;

	void updateDailyCsvReq(DailyCsvReqEbo ebo) throws Exception;

	List<WatchStockEbo> listWatchStock() throws Exception;

	void createTick(TickEbo ebo) throws Exception;

	List<StockEbo> listStocksByCodeSize(String code, int size) throws Exception;
}
