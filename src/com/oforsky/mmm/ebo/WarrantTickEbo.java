/**
 * WarrantTickEbo.java
 *
 * Copyright (c) 2007-2013 TrueTel Communications. All rights reserved.
 */
package com.oforsky.mmm.ebo;

import javax.persistence.*;

import org.apache.log4j.Logger;

import com.truetel.jcore.util.AppException;

@Entity
@Table(name = WarrantTickCoreEbo.DB_TABLE_NAME)
@NamedQueries({
		@NamedQuery(name = WarrantTickCoreEbo.QUERY_LISTALL_NAME, query = WarrantTickCoreEbo.QUERY_LISTALL),
		@NamedQuery(name = WarrantTickCoreEbo.QUERY_COUNTALL_NAME, query = WarrantTickCoreEbo.QUERY_COUNTALL) })
public class WarrantTickEbo extends WarrantTickCoreEbo {
	private static final long serialVersionUID = 1L;
	private static final String TASVERSION = "TAS-OFF-R5V1P10 2014-08-01 15:49:52";
	private static final Logger log = Logger.getLogger(WarrantTickEbo.class);

	public WarrantTickEbo() {
	}

	public static WarrantTickEbo valueOf(TickEbo tick) throws Exception {
		WarrantTickEbo ebo = new WarrantTickEbo();
		ebo.setCode(tick.getCode());
		ebo.setPrice(tick.getPrice());
		ebo.setTickVolume(tick.getTickVolume());
		ebo.setTotalVolume(tick.getTotalVolume());
		ebo.setTimestamp(tick.getTimestamp());
		ebo.setBuyPrices(tick.getBuyPrices());
		ebo.setBuyVolumes(tick.getBuyVolumes());
		ebo.setSeldPrices(tick.getSeldPrices());
		ebo.setSeldVolumes(tick.getSeldVolumes());
		return ebo;
	}
}
