/**
 * StorageEbo.java
 *
 * Copyright (c) 2007-2013 TrueTel Communications. All rights reserved.
 */
package com.oforsky.mmm.ebo;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.apache.log4j.Logger;

import com.oforsky.mmm.capture.TickRetriever;
import com.oforsky.mmm.capture.TickRetrieverImpl;
import com.oforsky.mmm.util.MmmUtil;

@Entity
@Table(name = StorageCoreEbo.DB_TABLE_NAME)
@NamedQueries({
		@NamedQuery(name = StorageCoreEbo.QUERY_LISTALL_NAME, query = StorageCoreEbo.QUERY_LISTALL),
		@NamedQuery(name = StorageCoreEbo.QUERY_COUNTALL_NAME, query = StorageCoreEbo.QUERY_COUNTALL) })
public class StorageEbo extends StorageCoreEbo {
	private static final long serialVersionUID = 1L;
	private static final String TASVERSION = "TAS-OFF-R5V1P10 2014-08-01 15:49:52";
	private static final Logger log = Logger.getLogger(StorageEbo.class);

	public StorageEbo() {
	}

	public StorageEbo(BidReqEbo bid) throws Exception {
		QueryJobEbo job = bid.getJobEboForced();
		setCode(job.getCode());
		setTargetPrice(job.getPrice());
		setName(bid.getName());
		setPrice(bid.getPrice());
		setWarrantOid(bid.getWarrantOid());
		setUnit(bid.getApplyUnit());
		setAmount(bid.getAmount());
	}

	@Override
	protected void preCreateSkyCb() throws Exception {
		super.preCreateSkyCb();

	}

	@Override
	protected void postGetSkyCb() throws Exception {
		super.postGetSkyCb();
		TickRetriever retriever = new TickRetrieverImpl();
		WarrantEbo warrant = getWarrantEboForced();
		TickEbo tick = TickEbo.valueOf(retriever.getWarrantTick(
				MmmUtil.getTodayStr(), getCode(), warrant.getCode()));
		setCurrentPrice(tick.getPrice());
		setRevenue(computeRevenue());
	}

	@Override
	protected void postDeepGetSkyCb() throws Exception {
		super.postDeepGetSkyCb();

	}

	private int computeRevenue() throws Exception {
		int result = (int) Math.ceil(getCurrentPrice() * getUnit() * 1000
				- getPrice() * getUnit() * 1000);
		return result;
	}
}
