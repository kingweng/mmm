/**
 * TickEbo.java
 *
 * Copyright (c) 2007-2013 TrueTel Communications. All rights reserved.
 */
package com.oforsky.mmm.ebo;

import com.oforsky.mmm.capture.data.Tick;
import org.apache.log4j.Logger;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = TickCoreEbo.DB_TABLE_NAME)
@NamedQueries({
        @NamedQuery(name = TickCoreEbo.QUERY_LISTALL_NAME,
                query = TickCoreEbo.QUERY_LISTALL)
        , @NamedQuery(name = TickCoreEbo.QUERY_COUNTALL_NAME,
        query = TickCoreEbo.QUERY_COUNTALL)
}
)
public class TickEbo extends TickCoreEbo {
    private static final long serialVersionUID = 1L;

    private static final String TASVERSION = "TAS-OFF-R5V1P10 2014-08-01 15:49:52";

    private static final Logger log = Logger.getLogger(TickEbo.class);

    public TickEbo() {
    }

    public static TickEbo valueOf(Tick data) {
        TickEbo ebo = new TickEbo();
        ebo.setCode(data.getCode());
        ebo.setPrice(data.getPrice());
        ebo.setTimestamp(data.getTimestamp());
        ebo.setTickVolume(data.getTickVolume());
        ebo.setTotalVolume(data.getTotalVolume());
        ebo.setBuyVolumes(data.getBuyVolumes());
        ebo.setBuyPrices(asStringList(data.getBuyPrices()));
        ebo.setSeldVolumes(data.getSeldVolumes());
        ebo.setSeldPrices(asStringList(data.getSeldPrices()));
        return ebo;
    }

    private static List<String> asStringList(List<Double> prices) {
        List<String> list = new ArrayList<String>();
        DecimalFormat dFormat = new DecimalFormat("0.00");
        for (Double d : prices) {
            list.add(dFormat.format(d));
        }
        return list;
    }
}

