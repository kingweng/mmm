/**
 * WatchStockDlo.java
 *
 * Copyright (c) 2007-2013 TrueTel Communications. All rights reserved.
 */
package com.oforsky.mmm.dlo;

import java.io.File;
import java.util.*;

import javax.persistence.*;

import com.oforsky.mmm.handler.WatchStockImporter;
import org.apache.log4j.Logger;

import com.truetel.jcore.util.AppException;

import com.oforsky.mmm.ebo.*;

public class WatchStockDlo extends WatchStockCoreDlo {

	private static final Logger log = Logger.getLogger(WatchStockDlo.class);
    private static final String TASVERSION = "TAS-OFF-R5V1P4 2014-04-30 16:43:36";
    
	public WatchStockDlo() {
		super();
	}

	public WatchStockDlo(EntityManager emgr) {
		super(emgr);
	}

    @Override
    public void importCsv(String filepath) throws Exception {
        log.info("importCsv filepath == " + filepath);
        WatchStockImporter importer = WatchStockImporter
                .build(new File(filepath));
        List<WatchStockEbo> ebos = importer.getWatchStocks();
        log.debug("import size == " + ebos.size());
        batchCreate(ebos);
    }
}
