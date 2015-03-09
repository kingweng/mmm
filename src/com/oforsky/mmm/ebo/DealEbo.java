/**
 * DealEbo.java
 *
 * Copyright (c) 2007-2013 TrueTel Communications. All rights reserved.
 */
package com.oforsky.mmm.ebo;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.apache.log4j.Logger;

import com.truetel.jcore.util.AppException;

@Entity
@Table(name = DealCoreEbo.DB_TABLE_NAME)
@NamedQueries({
		@NamedQuery(name = DealCoreEbo.QUERY_LISTALL_NAME, query = DealCoreEbo.QUERY_LISTALL),
		@NamedQuery(name = DealCoreEbo.QUERY_COUNTALL_NAME, query = DealCoreEbo.QUERY_COUNTALL) })
public class DealEbo extends DealCoreEbo {
	private static final long serialVersionUID = 1L;
	private static final String TASVERSION = "TAS-OFF-R5V1P10 2014-08-01 15:49:52";
	private static final Logger log = Logger.getLogger(DealEbo.class);

	public DealEbo() {
	}

	@Override
	protected void preCreateSkyCb() throws Exception {
		super.preCreateSkyCb();
		setRecordHigh();
		setKeepDays();
		setDiffPrice();
		setRevenueRate();
	}

	private void setRevenueRate() {
		setRevenueRate(getDiffPrice() / getBuyPrice());
	}

	private void setDiffPrice() {
		setDiffPrice(getSellPrice() - getBuyPrice());
	}

	private void setKeepDays() throws AppException, Exception {
		int keepDays = getProxy().getKeepDays(getCode(), getBuyDateStr(),
				getSellDateStr());
		setKeepDays(keepDays);
	}

	private void setRecordHigh() throws AppException, Exception {
		int recordHigh = getProxy().getRecordHigh(getCode(), getBuyDateStr());
		setRecordHigh(recordHigh);
	}
}
