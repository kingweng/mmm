package com.oforsky.mmm.handler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.oforsky.mmm.service.HttpService;
import com.oforsky.mmm.service.HttpServiceImpl;
import com.truetel.jcore.util.AppException;

public class TheoryPriceReceiver {

	private static final Logger log = Logger
			.getLogger(TheoryPriceReceiver.class);

	private static final String THEORY_HOME_URL = "http://warrantinfo.jihsun.com.tw/want/wSimulation.aspx?wcode=";

	private static final String WARRANT_THEORY_URL = "http://cloud01.fortunengine.com.tw/WARNT/GetWcodeTheoryGreek.aspx?wcode=%1$s&paralist=%2$s,%3$s,%4$s&cust_id=JIHSUN4IN1&vc=%5$s";

	private HttpService httpSvc;

	private String vc;

	private String remainDays;

	private String iv;

	public TheoryPriceReceiver(HttpService httpSvc) {
		this.httpSvc = httpSvc;
	}

	public TheoryPriceReceiver() {
		this(new HttpServiceImpl());
	}

	public double computeTheoryPrice(String code, Double targetPrice)
			throws Exception {
		String content = httpSvc.download(THEORY_HOME_URL + code, "big5");
		retrieveVc(content);
		retrieveDays(content);
		retrieveIv(content);
		return getPrice(composeUrl(code, targetPrice));
	}

	private String composeUrl(String code, Double targetPrice) {
		String url = String.format(WARRANT_THEORY_URL, code,
				targetPrice.toString(), iv,
				Integer.parseInt(remainDays) / 360.0, vc);
		log.info("getTheoryPrice url=" + url);
		return url;
	}

	private double getPrice(String url) throws Exception {
		String content = httpSvc.download(url, "big5");
		try {
			log.debug("content=" + content);
			String priceStr = retrievePrice(content);
			return Double.parseDouble(priceStr);
		} catch (Exception e) {
			log.error("retrieveTheoryPrice failed!", e);
			throw new AppException(5014, content);
		}
	}

	private String retrievePrice(String content) {
		String pattern = "stream:\"\\d*\\.?\\d+";
		String priceStr = null;
		Matcher matcher = Pattern.compile(pattern).matcher(content);
		boolean matchFound = matcher.find();
		if (matchFound) {
			String result = matcher.group(0);
			priceStr = result.substring(8, result.length());
		}
		log.debug("priceStr=" + priceStr);
		return priceStr;
	}

	private void retrieveVc(String content) {
		String pattern = "vc='\\d+'";
		Matcher matcher = Pattern.compile(pattern).matcher(content);
		boolean matchFound = matcher.find();
		if (matchFound) {
			String result = matcher.group(0);
			vc = result.substring(4, result.length() - 1);
		}
		log.info("vc=" + vc);
	}

	private void retrieveDays(String content) {
		String pattern = "days=\\d+";
		Matcher matcher = Pattern.compile(pattern).matcher(content);
		boolean matchFound = matcher.find();
		if (matchFound) {
			String result = matcher.group(0);
			remainDays = result.substring(5, result.length());
		}
		log.info("remainDays=" + remainDays);
	}

	private void retrieveIv(String content) {
		String pattern = "currIV=0.\\d+";
		Matcher matcher = Pattern.compile(pattern).matcher(content);
		boolean matchFound = matcher.find();
		if (matchFound) {
			String result = matcher.group(0);
			iv = result.substring(7, result.length());
		}
		log.info("iv=" + iv);
	}
}
