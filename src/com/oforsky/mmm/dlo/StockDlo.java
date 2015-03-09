/**
 * StockDlo.java
 *
 * Copyright (c) 2007-2013 TrueTel Communications. All rights reserved.
 */
package com.oforsky.mmm.dlo;

import java.math.BigInteger;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import com.oforsky.mmm.ebo.StockEbo;
import com.truetel.jcore.data.PageReq;
import com.truetel.jcore.data.QueryReq;

public class StockDlo extends StockCoreDlo {

	private static final Logger log = Logger.getLogger(StockDlo.class);

	private static final String TASVERSION = "TAS-OFF-R5V1P4 2014-04-30 16:43:36";

	public StockDlo() {
		super();
	}

	public StockDlo(EntityManager emgr) {
		super(emgr);
	}

	public List<StockEbo> listByCodeSize(String code, int size)
			throws Exception {
		QueryReq<StockEbo> query = new QueryReq<StockEbo>();
		query.setWhere("e." + StockEbo.ATTR_Code + " = ?1");
		query.setParams(new Object[] { code });
		query.setOrderBy(StockEbo.ATTR_DateStr + " desc");
		PageReq pageReq = new PageReq(0, size);
		return listPage(query, pageReq).getList();
	}

	public List<StockEbo> listByCodeDateSize(String code, String yyyymmdd,
			int size) throws Exception {
		QueryReq<StockEbo> query = new QueryReq<StockEbo>();
		query.setWhere("e." + StockEbo.ATTR_Code + " = ?1 and e."
				+ StockEbo.ATTR_DateStr + " <= ?2");
		query.setParams(new Object[] { code, yyyymmdd });
		query.setOrderBy(StockEbo.ATTR_DateStr + " desc");
		PageReq pageReq = new PageReq(0, size);
		return listPage(query, pageReq).getList();
	}

	public List<StockEbo> listByCodeFromDate(String code, String startDate)
			throws Exception {
		QueryReq<StockEbo> query = new QueryReq<StockEbo>();
		query.setWhere("e." + StockEbo.ATTR_Code + " = ?1 and e."
				+ StockEbo.ATTR_DateStr + " >= ?2");
		query.setParams(new Object[] { code, startDate });
		query.setOrderBy(StockEbo.ATTR_DateStr + " desc");
		return list(query);
	}

	public List<StockEbo> listByCodeFromDateReversed(String code,
			String startDate) throws Exception {
		QueryReq<StockEbo> query = new QueryReq<StockEbo>();
		query.setWhere("e." + StockEbo.ATTR_Code + " = ?1 and e."
				+ StockEbo.ATTR_DateStr + " >= ?2");
		query.setParams(new Object[] { code, startDate });
		query.setOrderBy(StockEbo.ATTR_DateStr);
		return list(query);
	}

	public int getRecordHigh(String code, String date) throws Exception {
		String sql = "select count(*) from mmm_stock as s1 inner join "
				+ "(select s.* from mmm_stock as s inner join "
				+ "(select * from mmm_stock where code = :code"
				+ " and dateStr = :date )"
				+ " as tmp where s.code = :code"
				+ " and s.dateStr < :date "
				+ "and s.price > tmp.price order by s.dateStr desc limit 0,1)"
				+ "as barrier where s1.code = barrier.code and s1.dateStr > barrier.dateStr "
				+ "and s1.dateStr <= :date";
		Query query = getEmgr().createNativeQuery(sql);
		query.setParameter("code", code);
		query.setParameter("date", date);
		log.debug("query==" + sql);
		return ((BigInteger) query.getSingleResult()).intValue();
	}

	public int getKeepDays(String code, String startDate, String endDate)
			throws Exception {
		String sql = "select count(*) from mmm_stock where dateStr >= :startDate and dateStr <= :endDate and code = :code";
		Query query = getEmgr().createNativeQuery(sql);
		query.setParameter("code", code);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		log.debug("query==" + sql);
		return ((BigInteger) query.getSingleResult()).intValue();
	}
}
