package com.oforsky.mmm.service;

import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;

import com.oforsky.mmm.cache.StorageCacheStore;
import com.oforsky.mmm.cache.SvcCfgCacheStore;
import com.oforsky.mmm.cache.WarrantCfgCacheStore;
import com.oforsky.mmm.dlo.BalanceCfgDlo;
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
import com.oforsky.mmm.ebo.JobStateFsm;
import com.oforsky.mmm.ebo.QueryJobEbo;
import com.oforsky.mmm.ebo.StockEbo;
import com.oforsky.mmm.ebo.StorageEbo;
import com.oforsky.mmm.ebo.StorageLogEbo;
import com.oforsky.mmm.ebo.TickEbo;
import com.oforsky.mmm.ebo.WarrantEbo;
import com.oforsky.mmm.ebo.WarrantTickEbo;
import com.oforsky.mmm.proxy.MmmProxyUtil;
import com.oforsky.mmm.util.MmmUtil;

/**
 * Created by kingweng on 2014/10/25.
 */
public class DloServiceImpl implements DloService {

	private static final Logger log = Logger.getLogger(DloServiceImpl.class);

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
		log.info("warrantOid=" + ebo.getWarrantOid());
		return ebo;
	}

	@Override
	public void createBias(BiasEbo bias) throws Exception {
		new BiasDlo().forceUpdate(bias);
	}

	@Override
	public void createWarrantTick(WarrantTickEbo logEbo) throws Exception {
		MmmProxyUtil.getProxy().createWarrantTick(logEbo);
	}

	@Override
	public int getAndLockBalance() throws Exception {
		return new BalanceCfgDlo().getAndLockBalance();
	}

	@Override
	public int updateBalance(int value) throws Exception {
		return new BalanceCfgDlo().updateBalance(value);
	}

	@Override
	public Calendar getLastBidTime() throws Exception {
		return MmmUtil.hhmmToCalendar(SvcCfgCacheStore.getLastBidTime());
	}

	@Override
	public double getMaxDiffRate() throws Exception {
		return WarrantCfgCacheStore.getMaxDiffRate();
	}

	@Override
	public QueryJobEbo getQueryJob(Integer jobOid) throws Exception {
		return new QueryJobDlo().get(jobOid);
	}

	@Override
	public void createBidReq(QueryJobEbo job) throws Exception {
		//TODO
	}

	@Override
	public WarrantEbo getWarrantByTarget(String code) throws Exception {
		StorageEbo storage =  StorageCacheStore.getStore().get(code);
		return storage.getWarrantEboForced();
	}

	@Override
	public List<QueryJobEbo> listQueryJob(JobStateFsm... states)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void batchUpdateQueryJob(List<QueryJobEbo> ebos) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
