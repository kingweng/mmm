/**
 * BidReqEbo.java
 *
 * Copyright (c) 2007-2013 TrueTel Communications. All rights reserved.
 */
package com.oforsky.mmm.ebo;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.apache.log4j.Logger;

import com.oforsky.mmm.part.req.BidReq;
import com.oforsky.mmm.part.zone.BidReqZone;

@Entity
@Table(name = BidReqCoreEbo.DB_TABLE_NAME)
@NamedQueries({
		@NamedQuery(name = BidReqCoreEbo.QUERY_LISTALL_NAME, query = BidReqCoreEbo.QUERY_LISTALL),
		@NamedQuery(name = BidReqCoreEbo.QUERY_COUNTALL_NAME, query = BidReqCoreEbo.QUERY_COUNTALL) })
public class BidReqEbo extends BidReqCoreEbo {
	private static final long serialVersionUID = 1L;
	private static final String TASVERSION = "TAS-OFF-R5V1P10 2014-08-01 15:49:52";
	private static final Logger log = Logger.getLogger(BidReqEbo.class);

	public static final Integer BID_UNIT = 1000;

	public static final Double FEE_RATIO = 0.001425;

	public static final Double TAX_RATIO = 0.001;

	public static final int MIN_FEE = 20;

	public BidReqEbo() {
	}

	public static BidReqEbo buy(WarrantEbo each, QueryJobEbo job) {
		BidReqEbo ebo = new BidReqEbo();
		ebo.setJobOid(job.getJobOid());
		ebo.setAction(ActionTypeEnum.Buy);
		ebo.setWarrantOid(each.getWarrantOid());
		ebo.setPrice(each.getSellPrice());
		ebo.setName(each.getName());
		ebo.setApplyUnit(getBuyUnit(job.getAmount(), each));
		ebo.setAmount((int) (ebo.getApplyUnit() * ebo.getPrice() * BID_UNIT));
		return ebo;
	}

	private static int getBuyUnit(int amount, WarrantEbo each) {
		int unit = (int) (amount / (each.getSellPrice() * BID_UNIT));
		return unit;
	}

	@Override
	public String toString() {
		return dbgstr();
	}

	public void succeed() throws Exception {
		setBidState(BidStateFsm.Action.Succeed);
		setRemainUnit(0);
	}

	public void fail(Exception e) throws Exception {
		setBidState(BidStateFsm.Action.Fail);
		setFailMsg(e.getMessage());
	}

	@Override
	protected void postCreateSkyCb() throws Exception {
		super.postCreateSkyCb();
		log.info("put BidReq to zone, bidOid=" + getBidOid());
		BidReqZone.get().putReq(new BidReq(getBidOid()));
	}

	public static BidReqEbo sell(StorageEbo storage, TickEbo tick,
			Integer warrantTickOid) throws Exception {
		BidReqEbo ebo = new BidReqEbo();
		ebo.setAction(ActionTypeEnum.Sell);
		ebo.setWarrantOid(storage.getWarrantEboForced().getWarrantOid());
		ebo.setApplyUnit(storage.getUnit());
		ebo.setName(storage.getWarrantEboForced().getName());
		ebo.setPrice(tick.getBuyPrice(storage.getUnit()));
		ebo.setAmount((int) (ebo.getPrice() * ebo.getApplyUnit() * BID_UNIT));
		ebo.setWarrantTickOid(warrantTickOid);
		return ebo;
	}

	public int getFee() {
		int fee = (int) Math.round(getAmount() * FEE_RATIO);
		if (fee < MIN_FEE) {
			fee = MIN_FEE;
		}
		return fee;
	}

	public int getSellTax() {
		return (int) Math.round(getAmount() * TAX_RATIO);
	}

	@Override
	protected void preDeleteSkyCb() throws Exception {
		super.preDeleteSkyCb();
		setRemainUnit(getApplyUnit());
	}
}
