package com.oforsky.mmm.service;

import java.util.Collection;
import java.util.List;

import com.oforsky.mmm.dlo.DailyCsvReqDlo;
import com.oforsky.mmm.dlo.StockDlo;
import com.oforsky.mmm.dlo.TickDlo;
import com.oforsky.mmm.dlo.WatchStockDlo;
import com.oforsky.mmm.ebo.DailyCsvReqEbo;
import com.oforsky.mmm.ebo.StockEbo;
import com.oforsky.mmm.ebo.TickEbo;
import com.oforsky.mmm.ebo.WatchStockEbo;

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
	public List<WatchStockEbo> listWatchStock() throws Exception {
		return new WatchStockDlo().listAll();
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
}
