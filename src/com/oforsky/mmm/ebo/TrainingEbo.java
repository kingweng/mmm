/**
 * TrainingEbo.java
 *
 * Copyright (c) 2007-2013 TrueTel Communications. All rights reserved.
 */
package com.oforsky.mmm.ebo;

import javax.persistence.*;

import org.apache.commons.csv.CSVRecord;
import org.apache.log4j.Logger;

import com.truetel.jcore.util.AppException;
import com.truetel.jcore.util.TimeUtil;

@Entity
@Table(name = TrainingCoreEbo.DB_TABLE_NAME)
@NamedQueries({
		@NamedQuery(name = TrainingCoreEbo.QUERY_LISTALL_NAME, query = TrainingCoreEbo.QUERY_LISTALL),
		@NamedQuery(name = TrainingCoreEbo.QUERY_COUNTALL_NAME, query = TrainingCoreEbo.QUERY_COUNTALL) })
public class TrainingEbo extends TrainingCoreEbo {
	private static final long serialVersionUID = 1L;
	private static final String TASVERSION = "TAS-OFF-R5V1P10 2014-08-01 15:49:52";
	private static final Logger log = Logger.getLogger(TrainingEbo.class);

	public TrainingEbo() {
	}

	public static TrainingEbo valueOf(CSVRecord record) throws Exception {
		TrainingEbo ebo = new TrainingEbo();
		ebo.setTrainingNum(record.get(0).toString().trim());
		ebo.setPid(record.get(1).toString().trim());
		ebo.setCategory(record.get(2).toString().trim());
		//ebo.setUserName(record.get(3).toString().trim());
		ebo.setStartTime(TimeUtil.string2Date(record.get(3).trim(),
				"yyyy-MM-dd"));
		ebo.setEndTime(TimeUtil.string2Date(record.get(4).trim(), "yyyy-MM-dd"));
		return ebo;
	}
}
