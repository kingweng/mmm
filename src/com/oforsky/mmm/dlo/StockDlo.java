/**
 * StockDlo.java
 *
 * Copyright (c) 2007-2013 TrueTel Communications. All rights reserved.
 */
package com.oforsky.mmm.dlo;

import java.util.*;

import javax.naming.NamingException;
import javax.persistence.*;

import org.apache.log4j.Logger;

import com.truetel.jcore.data.PageReq;
import com.truetel.jcore.data.QueryReq;
import com.truetel.jcore.util.AppException;
import com.truetel.jcore.util.QueryUtil;
import com.oforsky.mmm.ebo.*;

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
}
