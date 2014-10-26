/**
 * StockDlo.java
 *
 * Copyright (c) 2007-2013 TrueTel Communications. All rights reserved.
 */
package com.oforsky.mmm.dlo;

import java.util.*;

import javax.persistence.*;

import org.apache.log4j.Logger;

import com.truetel.jcore.util.AppException;

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

}
