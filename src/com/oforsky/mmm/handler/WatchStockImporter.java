package com.oforsky.mmm.handler;

import com.oforsky.mmm.ebo.StockEbo;
import com.oforsky.mmm.ebo.WatchStockEbo;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by kingweng on 2014/8/24.
 */
public class WatchStockImporter {

    private List<WatchStockEbo> watchStocks;

    private WatchStockImporter(List<WatchStockEbo> ebos) {
        this.watchStocks = ebos;
    }

    public static WatchStockImporter build(Reader reader) throws Exception {
        Set<WatchStockEbo> sets = new HashSet<WatchStockEbo>();
        BufferedReader bReader = null;
        try {
            String input = null;
            bReader = new BufferedReader(reader);
            while ((input = bReader.readLine()) != null) {
                sets.add(WatchStockEbo.parseStr(input));
            }
        } finally {
            closeReader(bReader);
        }
        return new WatchStockImporter(new ArrayList<WatchStockEbo>(sets));
    }

    private static void closeReader(BufferedReader bReader) {
        try {
            bReader.close();
        } catch (Exception e) {
            //ignore
        }
    }

    public static WatchStockImporter build(String content) throws Exception {
        return build(new StringReader(content));
    }

    public static WatchStockImporter build(File csvFile) throws Exception {
        return build(new FileReader(csvFile));
    }

    public List<WatchStockEbo> getWatchStocks() {
        return watchStocks;
    }
}
