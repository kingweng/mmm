package com.oforsky.mmm.service;

import java.util.Collection;
import java.util.List;

import com.oforsky.mmm.ebo.BiasEbo;
import com.oforsky.mmm.ebo.BidReqEbo;
import com.oforsky.mmm.ebo.DailyCsvReqEbo;
import com.oforsky.mmm.ebo.DealEbo;
import com.oforsky.mmm.ebo.DealStatsEbo;
import com.oforsky.mmm.ebo.QueryJobEbo;
import com.oforsky.mmm.ebo.StockEbo;
import com.oforsky.mmm.ebo.StorageEbo;
import com.oforsky.mmm.ebo.StorageLogEbo;
import com.oforsky.mmm.ebo.TickEbo;
import com.oforsky.mmm.ebo.WarrantEbo;
import com.oforsky.mmm.ebo.WarrantTickEbo;

/**
 * Created by kingweng on 2014/10/21.
 */
public interface DloService {
	DailyCsvReqEbo getDailyCsvReq(Integer reqOid) throws Exception;

	void batchCreateStock(Collection<StockEbo> ebos) throws Exception;

	void deleteStock(String code, String monthStr) throws Exception;

	void updateDailyCsvReq(DailyCsvReqEbo ebo) throws Exception;

	void createTick(TickEbo ebo) throws Exception;

	List<StockEbo> listStocksByCodeSize(String code, int size) throws Exception;

	List<StockEbo> listStocksByCodeDateSize(String code, String baseDate,
			int size) throws Exception;

	List<StockEbo> listStocksByCodeFromDate(String code, String startDate)
			throws Exception;

	List<DealEbo> listDealByCode(String code) throws Exception;

	void batchCreateDealStats(List<DealStatsEbo> result) throws Exception;

	void createStorage(StorageEbo storageEbo) throws Exception;

	void createStorageLog(StorageLogEbo ebo) throws Exception;

	void createBidReq(BidReqEbo result) throws Exception;

	void updateQueryJob(QueryJobEbo job) throws Exception;

	void updateBidReq(BidReqEbo bid) throws Exception;

	WarrantEbo createWarrant(WarrantEbo each) throws Exception;

	void createBias(BiasEbo bias) throws Exception;

	void createWarrantTick(WarrantTickEbo logEbo) throws Exception;

	int getAndLockBalance() throws Exception;

	int updateBalance(int value) throws Exception;

}
