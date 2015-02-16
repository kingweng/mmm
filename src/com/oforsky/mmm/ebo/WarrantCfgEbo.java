/**
 * WarrantCfgEbo.java
 *
 * Copyright (c) 2007-2013 TrueTel Communications. All rights reserved.
 */
package com.oforsky.mmm.ebo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.apache.log4j.Logger;

@Entity
@Table(name = WarrantCfgCoreEbo.DB_TABLE_NAME)
@NamedQueries({
		@NamedQuery(name = WarrantCfgCoreEbo.QUERY_LISTALL_NAME, query = WarrantCfgCoreEbo.QUERY_LISTALL),
		@NamedQuery(name = WarrantCfgCoreEbo.QUERY_COUNTALL_NAME, query = WarrantCfgCoreEbo.QUERY_COUNTALL) })
public class WarrantCfgEbo extends WarrantCfgCoreEbo {
	private static final long serialVersionUID = 1L;
	private static final String TASVERSION = "TAS-OFF-R5V1P10 2014-08-01 15:49:52";
	private static final Logger log = Logger.getLogger(WarrantCfgEbo.class);

	public WarrantCfgEbo() {
	}

	@Override
	protected void preCreateSkyCb() throws Exception {
		super.preCreateSkyCb();
		setDefaultQualifiedDealers();
	}

	private void setDefaultQualifiedDealers() {
		List<String> dealers = new ArrayList<String>();
		dealers.addAll(Arrays.asList(new String[] { "國泰", "凱基", "元大", "群益" }));
		setQualifiedDealers(dealers);
	}
}
