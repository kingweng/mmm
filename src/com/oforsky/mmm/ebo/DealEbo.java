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
import javax.persistence.Transient;

import org.apache.log4j.Logger;

import com.oforsky.mmm.data.HistoricalStock;
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

	@Transient
	private transient HistoricalStock history;

	public DealEbo() {
	}

	public DealEbo(StockEbo buyStock, StockEbo sellStock, SellTypeEnum type,
			HistoricalStock stocks) throws Exception {
		history = stocks;
		setSellType(type);
		setCode(buyStock.getCode());
		setBuyDateStr(buyStock.getDateStr());
		setSellDateStr(sellStock.getDateStr());
		recordHigh();
		if (type == SellTypeEnum.KBreak) {
			setDiffPrice(sellStock.getLowestPrice() - buyStock.getPrice());
		} else {
			setDiffPrice(sellStock.getHighestPrice() - buyStock.getPrice());
		}
		setKeepDays(diffStocksSize());
		setRevenueRate(getDiffPrice() / buyStock.getPrice());
	}

	private int diffStocksSize() throws Exception {
		return history.getStocks(getBuyDateStr(), getSellDateStr()).size();
	}

	private void recordHigh() throws Exception, AppException {
		log.debug("compute stock[" + getCode() + "] recordHigh on ["
				+ getBuyDateStr() + "]");
		setRecordHigh(history.recordHigh(getBuyDateStr()));
	}
}
