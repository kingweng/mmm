package com.oforsky.mmm.service;

import java.util.Collection;
import java.util.List;

import com.oforsky.mmm.dlo.DailyCsvReqDlo;
import com.oforsky.mmm.dlo.DealDlo;
import com.oforsky.mmm.dlo.DealStatsDlo;
import com.oforsky.mmm.dlo.StockDlo;
import com.oforsky.mmm.dlo.TickDlo;
import com.oforsky.mmm.ebo.DailyCsvReqEbo;
import com.oforsky.mmm.ebo.DealEbo;
import com.oforsky.mmm.ebo.DealStatsEbo;
import com.oforsky.mmm.ebo.StockEbo;
import com.oforsky.mmm.ebo.TickEbo;

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
}
