/**
 * DriveEbo.java
 *
 * Copyright (c) 2007-2013 TrueTel Communications. All rights reserved.
 */
package com.oforsky.mmm.ebo;

import com.oforsky.mmm.dlo.SvcCfgDlo;
import com.truetel.jcore.util.TimeUtil;

import org.apache.commons.csv.CSVRecord;
import org.apache.log4j.Logger;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import java.util.List;

@Entity
@Table(name = DriveCoreEbo.DB_TABLE_NAME)
@NamedQueries({
        @NamedQuery(name = DriveCoreEbo.QUERY_LISTALL_NAME,
                query = DriveCoreEbo.QUERY_LISTALL)
        , @NamedQuery(name = DriveCoreEbo.QUERY_COUNTALL_NAME,
        query = DriveCoreEbo.QUERY_COUNTALL)
}
)
public class DriveEbo extends DriveCoreEbo {
    private static final long serialVersionUID = 1L;

    private static final String TASVERSION = "TAS-OFF-R5V1P10 2014-08-01 15:49:52";

    private static final Logger log = Logger.getLogger(DriveEbo.class);

    public DriveEbo() {
    }

    public static DriveEbo valueOf(CSVRecord record) throws Exception {
        DriveEbo ebo = new DriveEbo();
        ebo.setLicense(record.get(0).toString().trim());
        ebo.setDriveTime(TimeUtil.string2Date(record.get(1).trim(),
                "yyyy/MM/dd"));
        ebo.setPoison(record.get(2).trim());
        ebo.setWeight(Double.parseDouble(record.get(3).trim().replace(",", "")));
        ebo.setUnit(record.get(4).trim());
        return ebo;
    }
    
    public void test() throws Exception{
    	SvcCfgDlo dlo = new SvcCfgDlo();
    	System.out.println("dlo="+dlo);
    	System.out.println(dlo.get().dbgstr());
    }
}

