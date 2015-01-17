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

public class WarrantHandler {

	private static final Logger log = Logger.getLogger(WarrantHandler.class);

	private static final String WARRANT_BASE_URL = "http://iwarrant.capital.com.tw/warrants/wScreenerPull.aspx";

	private static final String WARRANT_QUERY_URL = "http://iwarrant.capital.com.tw/data/warrants/Get_wScreenerResultScodes.aspx?ul=";

	private static final String WARRANT_DETAIL_URL = "http://iwarrant.capital.com.tw/data/warrants/Get_wScreenerResultCont.aspx?wcodelist=";

	private String code;

	private List<String> warrantCodes;

	private List<WarrantEbo> warrants;

	private HttpService httpSvc;

	private String sessionId;

	public WarrantHandler(String code, HttpService httpSvc) {
		this.code = code;
		this.httpSvc = httpSvc;
	}

	public void retrieve() throws Exception {
		sessionId = httpSvc.getSessionId(WARRANT_BASE_URL);
		warrantCodes = retrieveCodes();
		warrants = retrieveWarrants();
	}

	private List<WarrantEbo> retrieveWarrants() throws Exception {
		String content = httpSvc.downloadBySession(WARRANT_DETAIL_URL
				+ asCodes(warrantCodes) + "&ndays_sv=60", sessionId);
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

	private List<String> retrieveCodes() throws Exception {
		String codesContent = httpSvc.downloadBySession(WARRANT_QUERY_URL
				+ code, sessionId);
		codesContent = codesContent.split("\\|")[0];
		return Arrays.asList(codesContent.split(","));
	}

	public List<WarrantEbo> getWarrants() {
		return warrants;
	}
}
