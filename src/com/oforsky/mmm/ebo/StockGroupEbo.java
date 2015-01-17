/**
 * StockGroupEbo.java
 *
 * Copyright (c) 2007-2013 TrueTel Communications. All rights reserved.
 */
package com.oforsky.mmm.ebo;

import java.util.Arrays;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.apache.log4j.Logger;

import com.oforsky.mmm.util.CsvParsable;
import com.truetel.jcore.util.AppException;

@Entity
@Table(name = StockGroupCoreEbo.DB_TABLE_NAME)
@NamedQueries({
		@NamedQuery(name = StockGroupCoreEbo.QUERY_LISTALL_NAME, query = StockGroupCoreEbo.QUERY_LISTALL),
		@NamedQuery(name = StockGroupCoreEbo.QUERY_COUNTALL_NAME, query = StockGroupCoreEbo.QUERY_COUNTALL) })
public class StockGroupEbo extends StockGroupCoreEbo implements
		CsvParsable<StockGroupEbo> {
	private static final long serialVersionUID = 1L;
	private static final String TASVERSION = "TAS-OFF-R5V1P10 2014-08-01 15:49:52";
	private static final Logger log = Logger.getLogger(StockGroupEbo.class);

	public StockGroupEbo() {
	}

	@Override
	public StockGroupEbo from(String[] columns) throws Exception {
		checkParsing(columns);
		setName(columns[0]);
		setCode(columns[1]);
		setStockType(StockTypeEnum.getEnum(columns[2]));
		return this;
	}

	private void checkParsing(String[] values) throws AppException {
		List<String> types = Arrays
				.asList(new String[] { "Listed", "Cabinet" });
		if (values == null || values.length != 3 || !types.contains(values[2])) {
			throw new AppException(5004, Arrays.asList(values));
		}
	}

	@Override
	public StockGroupEbo from(String[] columns, String header) throws Exception {
		return from(columns);
	}
}
