package com.oforsky.mmm.handler;

import com.oforsky.mmm.ebo.StockEbo;
import com.truetel.jcore.util.AppException;
import com.truetel.jcore.util.StringUtil;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Created by kingweng on 2014/8/11.
 */
public class DailyStockImporter {

    private List<StockEbo> stocks;

    private DailyStockImporter(List<StockEbo> stocks) throws Exception {
        this.stocks = stocks;
    }

    public static DailyStockImporter build(Reader reader) throws Exception {
        Set<StockEbo> sets = new HashSet<StockEbo>();
        BufferedReader bReader = null;
        try {
            bReader = new BufferedReader(reader);
            String input = null;
            StockInfo info = new StockInfo(
                    bReader.readLine());
            bReader.readLine();//ignore header
            while ((input = bReader.readLine()) != null) {
                sets.add(StockEbo.fromDailyStr(input, info.getCode(),
                        info.getName()));
            }
        } finally {
            closeReader(bReader);
        }
        return new DailyStockImporter(new ArrayList<StockEbo>(sets));
    }

    private static String[] checkInfoStr(String infoStr) throws Exception {
        String[] tokens = null;
        if (StringUtil.isNonEmpty(infoStr) &&
                (tokens = infoStr.split(" ")).length >= 4) {
            return tokens;
        }
        throw new AppException(5003, infoStr);
    }

    public static DailyStockImporter build(String content) throws Exception {
        return build(new StringReader(content));
    }

    public static DailyStockImporter build(File csvFile) throws Exception {
        return build(new FileReader(csvFile));
    }

    private static void closeReader(BufferedReader reader) {
        try {
            reader.close();
        } catch (Exception e) {
            //ignore
        }
    }

    private static void checkCodeFormat(String codeStr)
            throws AppException {
        Pattern codePattern = Pattern.compile("[A-Z0-9]{4,6}");
        if (!codePattern.matcher(codeStr).matches()) {
            throw new AppException(5003, codeStr);
        }
    }

    public List<StockEbo> getStocks() {
        return stocks;
    }

    private static class StockInfo {

        private String code;

        private String name;

        public StockInfo(String infoStr) throws Exception {
            String[] tokens = checkInfoStr(infoStr);
            checkCodeFormat(tokens[1]);
            code = tokens[1];
            name = tokens[2];
        }

        public String getCode() {
            return code;
        }

        public String getName() {
            return name;
        }
    }
}
