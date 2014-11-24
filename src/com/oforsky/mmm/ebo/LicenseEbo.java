/**
 * LicenseEbo.java
 *
 * Copyright (c) 2007-2013 TrueTel Communications. All rights reserved.
 */
package com.oforsky.mmm.ebo;

import com.truetel.jcore.util.TimeUtil;
import org.apache.commons.csv.CSVRecord;
import org.apache.log4j.Logger;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = LicenseCoreEbo.DB_TABLE_NAME)
@NamedQueries({
	@NamedQuery(name = LicenseCoreEbo.QUERY_LISTALL_NAME,
        query = LicenseCoreEbo.QUERY_LISTALL)
    ,@NamedQuery(name = LicenseCoreEbo.QUERY_COUNTALL_NAME,
        query = LicenseCoreEbo.QUERY_COUNTALL)
	}
)
public class LicenseEbo extends LicenseCoreEbo {
    private static final long serialVersionUID = 1L;
    private static final String TASVERSION = "TAS-OFF-R5V1P10 2014-08-01 15:49:52";
    private static final Logger log = Logger.getLogger(LicenseEbo.class);
    
    public LicenseEbo() {
    }

    public static LicenseEbo valueOf(CSVRecord record) throws Exception {
        LicenseEbo ebo = new LicenseEbo();
        ebo.setLicense(record.get(1).toString().trim());
        ebo.setStartTime(TimeUtil.string2Date(record.get(4).trim(),
                "yyyy-MM-dd"));
        ebo.setEndTime(TimeUtil.string2Date(record.get(5).trim(),
                "yyyy-MM-dd"));
        ebo.setPoison(record.get(8).toString().trim());
        return ebo;
    }
}

