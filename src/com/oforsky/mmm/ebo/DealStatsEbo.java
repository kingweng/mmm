/**
 * DealStatsEbo.java
 *
 * Copyright (c) 2007-2013 TrueTel Communications. All rights reserved.
 */
package com.oforsky.mmm.ebo;

import java.util.List;

import javax.persistence.*;

import org.apache.log4j.Logger;

import com.truetel.jcore.util.AppException;

@Entity
@Table(name = DealStatsCoreEbo.DB_TABLE_NAME)
@NamedQueries({
		@NamedQuery(name = DealStatsCoreEbo.QUERY_LISTALL_NAME, query = DealStatsCoreEbo.QUERY_LISTALL),
		@NamedQuery(name = DealStatsCoreEbo.QUERY_COUNTALL_NAME, query = DealStatsCoreEbo.QUERY_COUNTALL) })
public class DealStatsEbo extends DealStatsCoreEbo {
	private static final long serialVersionUID = 1L;
	private static final String TASVERSION = "TAS-OFF-R5V1P10 2014-08-01 15:49:52";
	private static final Logger log = Logger.getLogger(DealStatsEbo.class);

	public DealStatsEbo() {
	}

	public DealStatsEbo(String code, List<DealEbo> deals) {
		int succCnt = calculateSuccCnt(deals);
		setCode(code);
		setSuccCnt(succCnt);
		setFailCnt(deals.size() - succCnt);
		setWinChance(calculateWinChance(deals, succCnt));
		setRevenueRate(calculateRevenueRate(deals));
		setAvgKeepDays(calculateAvgKeepDays(deals));
	}

	private Double calculateAvgKeepDays(List<DealEbo> deals) {
		if (deals.size() == 0) {
			return 0.0;
		}
		int totalKeepDays = 0;
		for (DealEbo ebo : deals) {
			totalKeepDays += ebo.getKeepDays();
		}
		return (double) (totalKeepDays / deals.size());
	}

	private Double calculateRevenueRate(List<DealEbo> deals) {
		double totalRate = 0.0;
		for (DealEbo ebo : deals) {
			totalRate += ebo.getRevenueRate();
		}
		return totalRate;
	}

	private int calculateSuccCnt(List<DealEbo> deals) {
		int succCnt = 0;
		for (DealEbo ebo : deals) {
			if (ebo.getDiffPrice() > 0.0) {
				succCnt++;
			}
		}
		return succCnt;
	}

	private int calculateWinChance(List<DealEbo> deals, int succCnt) {
		if (deals.size() == 0) {
			return 0;
		}
		return (int) (((double) succCnt / (double) deals.size()) * 100.0);
	}

	@Override
	protected void postCreateSkyCb() throws Exception {
		super.postCreateSkyCb();
//		if(getWinChance() >= 40){
//			BuyingStockEbo data = new BuyingStockEbo();
//			data.setCode(getCode());
//			getProxy().createBuyingStock(data );
//		}
		
	}
}
