/**
 * StockEbo.java
 *
 * Copyright (c) 2007-2013 TrueTel Communications. All rights reserved.
 */
package com.oforsky.mmm.ebo;

import com.truetel.jcore.util.AppException;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.log4j.Logger;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import static com.oforsky.mmm.util.MmmUtil.getCsvStrList;
import static com.oforsky.mmm.util.MmmUtil.toList;

@Entity
@Table(name = StockCoreEbo.DB_TABLE_NAME)
@NamedQueries({
        @NamedQuery(name = StockCoreEbo.QUERY_LISTALL_NAME,
                query = StockCoreEbo.QUERY_LISTALL)
        , @NamedQuery(name = StockCoreEbo.QUERY_COUNTALL_NAME,
        query = StockCoreEbo.QUERY_COUNTALL)
}
)
public class StockEbo extends StockCoreEbo {
    private static final long serialVersionUID = 1L;

    private static final String TASVERSION = "TAS-OFF-R5V1P4 2014-04-30 16:43:36";

    private static final Logger log = Logger.getLogger(StockEbo.class);

    public StockEbo() {
    }

    public static StockEbo parseStr(String input, String code, String name)
            throws Exception {
        List<String> values = getCsvStrList(input);
        checkTokensLength(input, values);
        StockEbo ebo = null;
        try {
            ebo = new StockEbo();
            ebo.setCode(code);
            ebo.setName(name);
            ebo.setDateStr(convertDateStr(values.get(0).toString()));
            ebo.setMonthStr(ebo.getDateStr().substring(0, 6));
            ebo.setVolume(getVolume(values.get(1)));
            ebo.setStartPrice(Double.parseDouble(values.get(3).toString()));
            ebo.setHighestPrice(Double.parseDouble(values.get(4).toString()));
            ebo.setLowestPrice(Double.parseDouble(values.get(5).toString()));
            ebo.setPrice(Double.parseDouble(values.get(6).toString()));
            ebo.setChangePrice(Double.parseDouble(values.get(7).toString()));
        } catch (Exception e) {
            log.error("parseStr failed!", e);
            throw new AppException(5002, input);
        }
        return ebo;
    }

    private static Long getVolume(String volumeStr) {
        return Long.parseLong(volumeStr.replace(",", "")) /
                1000L;
    }

    private static void checkTokensLength(String input, Collection values)
            throws AppException {
        if (values.size() != 9) {
            throw new AppException(5002, input);
        }
    }

    private static String convertDateStr(String input) {
        String[] tokens = input.trim().split("/");
        Integer year = Integer.parseInt(tokens[0]) + 1911;
        return year + tokens[1] + tokens[2];
    }
}

