package com.oforsky.mmm.service;

import java.util.Collection;
import java.util.List;

import com.oforsky.mmm.dlo.BiasDlo;
import com.oforsky.mmm.dlo.BidReqDlo;
import com.oforsky.mmm.dlo.DailyCsvReqDlo;
import com.oforsky.mmm.dlo.DealDlo;
import com.oforsky.mmm.dlo.DealStatsDlo;
import com.oforsky.mmm.dlo.QueryJobDlo;
import com.oforsky.mmm.dlo.StockDlo;
import com.oforsky.mmm.dlo.StorageDlo;
import com.oforsky.mmm.dlo.StorageLogDlo;
import com.oforsky.mmm.dlo.TickDlo;
import com.oforsky.mmm.dlo.WarrantDlo;
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

/**
 * Created by kingweng on 2014/10/25.
 */
public class DloServiceImpl implements DloService {
	@Override
	public DailyCsvReqEbo getDailyCsvReq(Integer reqOid) throws Exception {
		return new DailyCsvReqDlo().get(reqOid);
	}

	@Override
	public void batchCreateStock(Collection<StockEbo> ebos) throws Exception {
		new StockDlo().batchCreate(ebos);
	}

	@Override
	public void deleteStock(String code, String monthStr) throws Exception {
		new StockDlo().deleteByCodeMonth(code, monthStr);
	}

	@Override
	public void updateDailyCsvReq(DailyCsvReqEbo ebo) throws Exception {
		new DailyCsvReqDlo().update(ebo);
	}

	@Override
	public void createTick(TickEbo ebo) throws Exception {
		new TickDlo().create(ebo);
	}

	@Override
	public List<StockEbo> listStocksByCodeSize(String code, int size)
			throws Exception {
		return new StockDlo().listByCodeSize(code, size);
	}

	@Override
	public List<StockEbo> listStocksByCodeDateSize(String code,
			String baseDate, int size) throws Exception {
		return new StockDlo().listByCodeDateSize(code, baseDate, size);
	}

	@Override
	public List<StockEbo> listStocksByCodeFromDate(String code, String startDate)
			throws Exception {
		return new StockDlo().listByCodeFromDate(code, startDate);
	}

	@Override
	public List<DealEbo> listDealByCode(String code) throws Exception {
		return new DealDlo().listByCode(code);
	}

	@Override
	public void batchCreateDealStats(List<DealStatsEbo> ebos) throws Exception {
		new DealStatsDlo().batchCreate(ebos);
	}

	@Override
	public void createStorage(StorageEbo ebo) throws Exception {
		new StorageDlo().create(ebo);
	}

	@Override
	public void createStorageLog(StorageLogEbo ebo) throws Exception {
		new StorageLogDlo().create(ebo);
	}

	@Override
	public void createBidReq(BidReqEbo ebo) throws Exception {
		new BidReqDlo().create(ebo);
	}

	@Override
	public void updateQueryJob(QueryJobEbo job) throws Exception {
		new QueryJobDlo().update(job);
	}

	@Override
	public void updateBidReq(BidReqEbo bid) throws Exception {
		new BidReqDlo().update(bid);
	}

	@Override
	public WarrantEbo createWarrant(WarrantEbo ebo) throws Exception {
		new WarrantDlo().create(ebo);
		return ebo;
	}

	@Override
	public void createBias(BiasEbo bias) throws Exception {
		new BiasDlo().forceUpdate(bias);
	}
}
