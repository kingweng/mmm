/**
 * BalanceCfgDlo.java
 *
 * Copyright (c) 2007-2013 TrueTel Communications. All rights reserved.
 */
package com.oforsky.mmm.dlo;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import com.oforsky.mmm.ebo.BalanceCfgEbo;

public class BalanceCfgDlo extends BalanceCfgCoreDlo {

	private static final Logger log = Logger.getLogger(BalanceCfgDlo.class);
	private static final String TASVERSION = "TAS-OFF-R5V1P10 2014-08-01 15:49:52";

	public BalanceCfgDlo() {
		super();
	}

	public BalanceCfgDlo(EntityManager emgr) {
		super(emgr);
	}

	public int getAndLockBalance() throws Exception {
		String sql = "select " + BalanceCfgEbo.ATTR_Balance + " from "
				+ BalanceCfgEbo.DB_TABLE_NAME + " for update";
		Query query = getEmgr().createNativeQuery(sql);
		return ((Integer) query.getSingleResult()).intValue();
	}

	public int updateBalance(int value) throws Exception {
		String sql = "update " + BalanceCfgEbo.DB_TABLE_NAME + " set "
				+ BalanceCfgEbo.ATTR_Balance + " = " + value;
		Query query = getEmgr().createNativeQuery(sql);
		query.executeUpdate();
		return get().getBalance();
	}
}
