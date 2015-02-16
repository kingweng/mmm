/**
 * TickEbo.java
 *
 * Copyright (c) 2007-2013 TrueTel Communications. All rights reserved.
 */
package com.oforsky.mmm.ebo;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.apache.log4j.Logger;

import com.oforsky.mmm.capture.data.Tick;
import com.truetel.jcore.util.AppException;

@Entity
@Table(name = TickCoreEbo.DB_TABLE_NAME)
@NamedQueries({
		@NamedQuery(name = TickCoreEbo.QUERY_LISTALL_NAME, query = TickCoreEbo.QUERY_LISTALL),
		@NamedQuery(name = TickCoreEbo.QUERY_COUNTALL_NAME, query = TickCoreEbo.QUERY_COUNTALL) })
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

	public Double getBuyPrice(int unit) throws Exception {
		int count = 0;
		for (int i = 0; i < getBuyVolumes().size(); i++) {
			int volume = getBuyVolumes().get(i);
			double price = Double.parseDouble(getBuyPrices().get(i));
			count += volume;
			if (count >= unit) {
				return price;
			}
		}
		log.warn("no availble to Buy! " + dbgstr());
		throw new AppException(5011, unit, getCode());
	}
}
