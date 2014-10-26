/**
 * MmmProxy.java
 *
 * Copyright (c) 2007-2013 TrueTel Communications. All rights reserved.
 */
package com.oforsky.mmm.proxy;

import com.oforsky.mmm.ebo.StockEbo;
import com.truetel.jcore.util.AppException;

import javax.ejb.Remote;
import java.util.List;

@Remote
public interface MmmProxy extends MmmBaseProxy {

    public void deleteStocksByMonth(String code, String monthStr)
            throws AppException;

    public void dailyCsvReqRetrieve(Integer reqOid) throws AppException;

    public void dailyCsvReqParse(Integer reqOid) throws AppException;

    public void dailyCsvReqFailRetrieval(Integer reqOid, String errMsg)
            throws AppException;

    public void dailyCsvReqReqFail(Integer reqOid, String errMsg)
            throws AppException;
}
