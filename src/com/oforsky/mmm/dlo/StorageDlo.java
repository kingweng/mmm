/**
 * StorageDlo.java
 *
 * Copyright (c) 2007-2013 TrueTel Communications. All rights reserved.
 */
package com.oforsky.mmm.dlo;

import java.util.List;

import javax.persistence.EntityManager;

import org.apache.log4j.Logger;

import com.oforsky.mmm.ebo.MmmConstant;
import com.oforsky.mmm.ebo.StorageEbo;

public class StorageDlo extends StorageCoreDlo {

	private static final Logger log = Logger.getLogger(StorageDlo.class);
	private static final String TASVERSION = "TAS-OFF-R5V1P10 2014-08-01 15:49:52";

	public StorageDlo() {
		super();
	}

	public StorageDlo(EntityManager emgr) {
		super(emgr);
	}

	@Override
	public String totalRevenue() throws Exception {
		log.info("totalRevenue() enter...");
		List<StorageEbo> ebos = listAll();
		log.debug("size == " + ebos.size());
		int sum = 0;
		StringBuilder sb = new StringBuilder();
		for (StorageEbo ebo : ebos) {
			ebo.postGet();
			sum += ebo.getRevenue();
			sb.append(String.format(MmmConstant.REVENUE_OUTPUT_FORMAT,
					ebo.getCode(), ebo.getRevenue())
					+ "\n");
		}
		return "total revenue is " + sum + "\n" + sb.toString();
	}
}
