/**
 * StorageLogEbo.java
 *
 * Copyright (c) 2007-2013 TrueTel Communications. All rights reserved.
 */
package com.oforsky.mmm.ebo;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.apache.log4j.Logger;

import com.oforsky.mmm.cache.StorageCacheStore;
import com.truetel.jcore.util.TimeUtil;

@Entity
@Table(name = StorageLogCoreEbo.DB_TABLE_NAME)
@NamedQueries({
		@NamedQuery(name = StorageLogCoreEbo.QUERY_LISTALL_NAME, query = StorageLogCoreEbo.QUERY_LISTALL),
		@NamedQuery(name = StorageLogCoreEbo.QUERY_COUNTALL_NAME, query = StorageLogCoreEbo.QUERY_COUNTALL) })
public class StorageLogEbo extends StorageLogCoreEbo {
	private static final long serialVersionUID = 1L;
	private static final String TASVERSION = "TAS-OFF-R5V1P10 2014-08-01 15:49:52";
	private static final Logger log = Logger.getLogger(StorageLogEbo.class);

	public StorageLogEbo() {
	}

	public StorageLogEbo(BidReqEbo bid) throws Exception {
		WarrantEbo warrant = bid.getWarrantEboForced();
		setName(warrant.getName());
		setCode(warrant.getCode());
		setBuyPrice(warrant.getSellPrice());
		setSellPrice(bid.getPrice());
		setTargetCode(warrant.getTargetCode());
		StorageEbo storage = StorageCacheStore.getStore().get(getTargetCode());
		setTargetPrice(storage.getTargetPrice());
		setUnit(bid.getApplyUnit());
		setAmount(bid.getAmount());
		setBuyTime(warrant.getCreateTime());
		setWarrantOid(warrant.getWarrantOid());
	}

	@Override
	protected void preCreateSkyCb() throws Exception {
		super.preCreateSkyCb();
		setKeepDays();
		setRevenue();
	}

	private void setKeepDays() throws Exception {
		List<StockEbo> ebos = getProxy()
				.listStockByCodeFromDate(getTargetCode(),
						TimeUtil.date2String(getBuyTime(), "yyyyMMdd"));
		setKeepDays(ebos.size() + 1);
	}

	private void setRevenue() {
		int result = (int) Math.ceil(getSellPrice() * getUnit() * 1000
				- getBuyPrice() * getUnit() * 1000);
		setRevenue(result);
	}

	@Override
	protected void postCreateSkyCb() throws Exception {
		super.postCreateSkyCb();
		getProxy().deleteStorage(getTargetCode());
	}
}
