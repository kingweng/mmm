/**
 * WarrantEbo.java
 *
 * Copyright (c) 2007-2013 TrueTel Communications. All rights reserved.
 */
package com.oforsky.mmm.ebo;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.apache.log4j.Logger;

import com.oforsky.mmm.cache.WarrantCfgCacheStore;
import com.oforsky.mmm.capture.data.stock.Item;
import com.oforsky.mmm.capture.data.stock.OriWarrant;
import com.truetel.jcore.util.StringUtil;

@Entity
@Table(name = WarrantCoreEbo.DB_TABLE_NAME)
@NamedQueries({
		@NamedQuery(name = WarrantCoreEbo.QUERY_LISTALL_NAME, query = WarrantCoreEbo.QUERY_LISTALL),
		@NamedQuery(name = WarrantCoreEbo.QUERY_COUNTALL_NAME, query = WarrantCoreEbo.QUERY_COUNTALL) })
public class WarrantEbo extends WarrantCoreEbo {
	private static final long serialVersionUID = 1L;
	private static final String TASVERSION = "TAS-OFF-R5V1P10 2014-08-01 15:49:52";
	private static final Logger log = Logger.getLogger(WarrantEbo.class);

	public WarrantEbo() {
	}

	public static WarrantEbo valueOf(OriWarrant oriWarrant) {
		WarrantEbo ebo = new WarrantEbo();
		Item item = oriWarrant.getItem();
		ebo.setCode(item.getScode());
		ebo.setName(item.getSname());
		ebo.setTargetCode(item.getUlcode());
		ebo.setPrice(getDouble(item.getCp()));
		ebo.setBuyVolume(getInteger(item.getBid_vol()));
		ebo.setBuyPrice(getDouble(item.getBid()));
		ebo.setSellPrice(getDouble(item.getAsk()));
		ebo.setSellVolume(getInteger(item.getAsk_vol()));
		ebo.setDiffPrices(getDouble(item.getSpread()));
		ebo.setIdealDiffPrices(getDouble(item.getSpread_reas()));
		ebo.setRemainingDays(getInteger(item.getDays()));
		ebo.setLeverage(getDouble(item.getLvr()));
		return ebo;
	}

	private static Integer getInteger(String value) {
		if (StringUtil.isEmpty(value) || value.contains("-")) {
			return 0;
		}
		return Integer.parseInt(value);
	}

	private static Double getDouble(String value) {
		if (StringUtil.isEmpty(value) || "-".equals(value)) {
			return 0.0;
		}
		return Double.parseDouble(value);
	}

	@Override
	public String toString() {
		return dbgstr();
	}

	public boolean isQualified() throws Exception {
		if (getRemainingDays() < WarrantCfgCacheStore.getRemainingDays()) {
			return false;
		}
		if (getDiffPrices() > WarrantCfgCacheStore.getWarrantDiffPrice()) {
			return false;
		}
		if (!qualifiedDealer()) {
			return false;
		}
		if (getBuyVolume() < 100) {
			return false;
		}
		if (getLeverage() < WarrantCfgCacheStore.getMinLeverage()) {
			return false;
		}
		return true;
	}

	private boolean qualifiedDealer() throws Exception {
		for (String dealer : WarrantCfgCacheStore.getQualifiedDealers()) {
			if (getName().contains(dealer)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int compareTo(WarrantEbo obj) {
		return obj.getLeverage().compareTo(getLeverage());// reversed
	}

	public int avaliableSell() {
		return (int) (getSellPrice() * 1000 * getSellVolume());
	}
}
