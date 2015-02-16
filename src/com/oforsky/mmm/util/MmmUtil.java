package com.oforsky.mmm.util;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import com.truetel.jcore.util.TimeUtil;

/**
 * Created by kingweng on 2014/8/24.
 */
public class MmmUtil {

	public static List<String> toList(Iterator<String> iterator) {
		List<String> list = new ArrayList<String>();
		while (iterator.hasNext()) {
			list.add(iterator.next());
		}
		return list;
	}

	public static List<String> getCsvStrList(String csvLine) throws IOException {
		CSVRecord record = CSVFormat.DEFAULT.parse(new StringReader(csvLine))
				.getRecords().get(0);
		List<String> values = toList(record.iterator());
		return values;
	}

	public static Calendar hhmmToCalendar(String hhmmStr) throws Exception {
		Calendar now = Calendar.getInstance();
		Calendar hhmm = Calendar.getInstance();
		hhmm.setTime(TimeUtil.string2Date(hhmmStr, "HHmm"));
		now.set(Calendar.HOUR_OF_DAY, hhmm.get(Calendar.HOUR_OF_DAY));
		now.set(Calendar.MINUTE, hhmm.get(Calendar.MINUTE));
		return now;
	}

	public static String getTodayStr() {
		return TimeUtil.date2String(new Date(), TimeUtil.FORMAT_YYYYMMDD);
	}
}
