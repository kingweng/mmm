package com.oforsky.mmm.util;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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

    public static List<String> getCsvStrList(String csvLine)
            throws IOException {
        CSVRecord record = CSVFormat.DEFAULT.parse(new StringReader(csvLine))
                .getRecords().get(0);
        List<String> values = toList(record.iterator());
        return values;
    }
}

