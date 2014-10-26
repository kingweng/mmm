package com.oforsky.mmm.service;

import com.oforsky.mmm.dlo.DailyCsvReqDlo;
import com.oforsky.mmm.dlo.StockDlo;
import com.oforsky.mmm.ebo.DailyCsvReqEbo;
import com.oforsky.mmm.ebo.StockEbo;

import java.util.Collection;

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
}
