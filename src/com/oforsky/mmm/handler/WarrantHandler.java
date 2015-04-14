package com.oforsky.mmm.handler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.oforsky.mmm.capture.data.stock.OriWarrant;
import com.oforsky.mmm.ebo.WarrantEbo;
import com.oforsky.mmm.service.HttpService;
import com.oforsky.mmm.service.HttpServiceImpl;
import com.truetel.jcore.util.AppException;
import com.truetel.jcore.util.StringUtil;

public class WarrantHandler {

	private static final Logger log = Logger.getLogger(WarrantHandler.class);

	private static final String WARRANT_BASE_URL = "http://iwarrant.capital.com.tw/warrants/wScreenerPull.aspx";

	private static final String WARRANT_QUERY_URL = "http://iwarrant.capital.com.tw/data/warrants/Get_wScreenerResultScodes.aspx?histv=60&oir1=0&oir2=50&ul=";

	private static final String WARRANT_DETAIL_URL = "http://iwarrant.capital.com.tw/data/warrants/Get_wScreenerResultCont.aspx?wcodelist=";

	private List<String> warrantCodes;

	private List<WarrantEbo> warrants;

	private HttpService httpSvc;

	private String sessionId;

	public WarrantHandler(HttpService httpSvc) {
		this.httpSvc = httpSvc;
	}

	public WarrantHandler() {
		this(new HttpServiceImpl());
	}

	public void retrieve(String code) throws Exception {
		sessionId = httpSvc.getSessionId(WARRANT_BASE_URL);
		warrantCodes = retrieveCodes(code);
		warrants = retrieveWarrants();
	}

	public WarrantEbo retrieveWarrant(String warrantCode) throws Exception {
		sessionId = httpSvc.getSessionId(WARRANT_BASE_URL);
		String content = httpSvc.downloadBySession(WARRANT_DETAIL_URL
				+ warrantCode + "&ndays_sv=60", sessionId, "big5");
		List<OriWarrant> oriWarrants = new Gson().fromJson(content,
				new TypeToken<List<OriWarrant>>() {
				}.getType());
		return WarrantEbo.valueOf(oriWarrants.get(0));
	}

	private List<WarrantEbo> retrieveWarrants() throws Exception {
		String content = httpSvc.downloadBySession(WARRANT_DETAIL_URL
				+ asCodes(warrantCodes) + "&ndays_sv=60", sessionId, "big5");
		List<OriWarrant> oriWarrants = new Gson().fromJson(content,
				new TypeToken<List<OriWarrant>>() {
				}.getType());
		SortedSet<WarrantEbo> sortedSet = new TreeSet<WarrantEbo>();
		addToSortedSet(oriWarrants, sortedSet);
		return new ArrayList<WarrantEbo>(sortedSet);
	}

	private String asCodes(List<String> codes) {
		StringBuffer sb = new StringBuffer();
		for (String value : codes) {
			sb.append(value + ",");
		}
		return sb.substring(0, sb.length() - 1);
	}

	private void addToSortedSet(List<OriWarrant> oriWarrants,
			SortedSet<WarrantEbo> sortedSet) throws Exception {
		for (OriWarrant oriWarrant : oriWarrants) {
			WarrantEbo ebo = WarrantEbo.valueOf(oriWarrant);
			if (ebo.isQualified()) {
				sortedSet.add(ebo);
			}
		}
	}

	private List<String> retrieveCodes(String code) throws Exception {
		String codesContent = httpSvc.downloadBySession(WARRANT_QUERY_URL
				+ code, sessionId, "big5");
		codesContent = codesContent.split("\\|")[0];
		checkCodesContent(code, codesContent);
		return Arrays.asList(codesContent.split(","));
	}

	private void checkCodesContent(String code, String codesContent)
			throws AppException {
		if (StringUtil.isEmpty(codesContent)) {
			throw new AppException(5012, code);
		}
	}

	public List<WarrantEbo> getWarrants() {
		return warrants;
	}

}
