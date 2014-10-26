/**
 * WatchStockEbo.java
 *
 * Copyright (c) 2007-2013 TrueTel Communications. All rights reserved.
 */
package com.oforsky.mmm.ebo;

import javax.persistence.*;

import org.apache.log4j.Logger;

import com.truetel.jcore.util.AppException;

import java.util.List;

import static com.oforsky.mmm.util.MmmUtil.getCsvStrList;

@Entity
@Table(name = WatchStockCoreEbo.DB_TABLE_NAME)
@NamedQueries({
        @NamedQuery(name = WatchStockCoreEbo.QUERY_LISTALL_NAME,
                query = WatchStockCoreEbo.QUERY_LISTALL)
        , @NamedQuery(name = WatchStockCoreEbo.QUERY_COUNTALL_NAME,
        query = WatchStockCoreEbo.QUERY_COUNTALL)
}
)
public class WatchStockEbo extends WatchStockCoreEbo {
    private static final long serialVersionUID = 1L;

    private static final String TASVERSION = "TAS-OFF-R5V1P4 2014-04-30 16:43:36";

    private static final Logger log = Logger.getLogger(WatchStockEbo.class);

    public WatchStockEbo() {
    }

    public static WatchStockEbo parseStr(String inputStr) throws Exception {
        List<String> values = getCsvStrList(inputStr);
        checkCsvLineLength(inputStr, values);
        return setValues(values);
    }

    private static WatchStockEbo setValues(List<String> values) {
        WatchStockEbo ebo = new WatchStockEbo();
        ebo.setName(values.get(0));
        ebo.setCode(values.get(1));
        return ebo;
    }

    private static void checkCsvLineLength(String inputStr, List<String> values)
            throws AppException {
        if (values == null || values.size() != 2) {
            throw new AppException(5004, inputStr);
        }
    }
}

