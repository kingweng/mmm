package com.oforsky.mmm.capture;

import com.google.gson.Gson;
import com.truetel.jcore.util.AppException;
import com.oforsky.mmm.capture.data.Tick;
import com.oforsky.mmm.capture.data.stock.MsgArray;
import com.oforsky.mmm.capture.data.stock.Stock;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kingweng on 2014/8/4.
 */
public class WebStockCapture {

    private static final Logger log = Logger.getLogger(WebStockCapture.class);

    private static final int TIMEOUT = 2000;

    private static final String TWSE_SUCCESS_RTCODE = "0000";

    private String webUrl;

    public WebStockCapture(String twseUrl) {
        this.webUrl = twseUrl;
    }

    public Tick getTick(String yyyymmdd, String stockCode)
            throws Exception {
        Tick tick = getTickAndHandleException(yyyymmdd, stockCode);
        return tick;
    }

    private Tick getTickAndHandleException(String yyyymmdd, String stockCode)
            throws Exception {
        try {
            return inquireTick(yyyymmdd, stockCode);
        } catch (Exception e) {
            throw new AppException(5001, e);
        }
    }

    private Tick inquireTick(String yyyymmdd, String stockCode)
            throws Exception {
        String content = getUrlContent(yyyymmdd, stockCode);
        Stock stockInfo = new Gson().fromJson(content, Stock.class);
        checkResultCode(content, stockInfo);
        MsgArray array = stockInfo.getMsgArray().get(0);
        Tick tick = getTick(array);
        return tick;
    }

    private void checkResultCode(String content, Stock stockInfo)
            throws AppException {
        if (!TWSE_SUCCESS_RTCODE.equals(stockInfo.getRtcode())) {
            log.error("inquireTick got wrong rtcode, content=" + content);
            throw new AppException(5001, stockInfo.getRtcode());
        }
    }

    private Tick getTick(MsgArray array) {
        Tick tick = new Tick();
        tick.setCode(array.getC());
        tick.setPrice(Double.parseDouble(array.getZ()));
        tick.setTimestamp(Long.parseLong(array.getTlong()));
        tick.setTickVolume(Integer.parseInt(array.getTv()));
        tick.setTotalVolume(Integer.parseInt(array.getV()));
        tick.setBuyPrices(getPricesList(array.getB()));
        tick.setBuyVolumes(getVolumesList(array.getG()));
        tick.setSeldPrices(getPricesList(array.getA()));
        tick.setSeldVolumes(getVolumesList(array.getF()));
        return tick;
    }

    private List<Double> getPricesList(String input) {
        List<Double> prices = new ArrayList<Double>();
        for (String token : input.split("_")) {
            prices.add(Double.parseDouble(token));
        }
        return prices;
    }

    private List<Integer> getVolumesList(String input) {
        List<Integer> volumes = new ArrayList<Integer>();
        for (String token : input.split("_")) {
            volumes.add(Integer.parseInt(token));
        }
        return volumes;
    }

    private String getUrlContent(String yyyymmdd, String stockCode)
            throws Exception {
        //URL url = new URL(webUrl + "?stock=" + stockCode);
        URL url = new URL(
                "http://mis.twse.com.tw/stock/api/getStockInfo.jsp?ex_ch=tse_" +
                        stockCode + ".tw_" + yyyymmdd + "&json=3&delay=0");
        URLConnection urlConn = url.openConnection();
        urlConn.setConnectTimeout(TIMEOUT);
        urlConn.setReadTimeout(TIMEOUT);
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(urlConn.getInputStream()));
        StringBuilder content = new StringBuilder();
        String line = null;
        // read from the urlconnection via the bufferedreader
        while ((line = bufferedReader.readLine()) != null) {
            content.append(line + "\n");
        }
        bufferedReader.close();
        return content.toString();
    }
}
