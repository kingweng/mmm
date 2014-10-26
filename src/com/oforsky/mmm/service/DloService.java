package com.oforsky.mmm.service;

import com.oforsky.mmm.ebo.DailyCsvReqEbo;
import com.oforsky.mmm.ebo.StockEbo;

import java.util.Collection;

/**
 * Created by kingweng on 2014/10/21.
 */
public interface DloService {
    DailyCsvReqEbo getDailyCsvReq(Integer reqOid) throws Exception;

    void batchCreateStock(Collection<StockEbo> ebos) throws Exception;

    void deleteStock(String code, String monthStr) throws Exception;

    void updateDailyCsvReq(DailyCsvReqEbo ebo) throws Exception;
}
