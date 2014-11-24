package com.oforsky.mmm.handler;

import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.oforsky.mmm.capture.data.Warrant;
import com.oforsky.mmm.capture.data.stock.OriWarrant;
import com.oforsky.mmm.service.HttpService;

public class WarrantHandler {

	private static final String WARRANT_BASE_URL = "http://iwarrant.capital.com.tw/warrants/wScreenerPull.aspx";

	private static final String WARRANT_QUERY_URL = "http://iwarrant.capital.com.tw/data/warrants/Get_wScreenerResultScodes.aspx?ul=";

	private String code;

	private List<String> warrantCodes;

	private List<Warrant> warrants;

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

	private List<Warrant> retrieveWarrants() throws Exception {
		String content = httpSvc.downloadBySession(WARRANT_QUERY_URL + code,
				sessionId);
		List<OriWarrant> warrants = new Gson().fromJson(content,
				new TypeToken<List<OriWarrant>>() {
				}.getType());
		return null;
	}

	private List<String> retrieveCodes() throws Exception {
		String codesContent = httpSvc.downloadBySession(WARRANT_QUERY_URL
				+ code, sessionId);
		codesContent = codesContent.split("\\|")[0];
		return Arrays.asList(codesContent.split(","));
	}

	public List<String> getWarrantCodes() {
		return warrantCodes;
	}

	public List<Warrant> getWarrants() {
		// TODO Auto-generated method stub
		return null;
	}
}
