package com.oforsky.mmm.service;

import com.truetel.jcore.util.FileUtil;
import com.truetel.jcore.util.StringUtil;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by kingweng on 2014/10/19.
 */
public class HttpServiceImpl implements HttpService {

	private static final Logger log = Logger.getLogger(HttpServiceImpl.class);

	private static final int TIMEOUT = 2000;

	@Override
	public void download(String url, File file) throws Exception {
		log.trace("retrieveFile url[" + url + "] to file[" + file + "]");
		file.getParentFile().mkdirs();
		URLConnection conn = new URL(url).openConnection();
		conn.setConnectTimeout(3000);
		conn.setReadTimeout(3000);
		String content = IOUtils.toString(conn.getInputStream(), "Big5");
		FileUtil.writeFile(content, file);
	}

	@Override
	public String download(String url) throws Exception {
		return downloadBySession(url, null);
	}

	@Override
	public String getSessionId(String url) throws Exception {
		URLConnection urlConn = new URL(url).openConnection();
		setTimeout(urlConn);
		urlConn.connect();
		String cookieStr = urlConn.getHeaderField("Set-Cookie");
		// ASP.NET_SessionId=pbfwsb55h0obbs45cdm1xmv0; warrantCodeCookie=08843P
		String sessionId = cookieStr.substring(18, cookieStr.indexOf(";"));
		return sessionId;
	}

	private void setTimeout(URLConnection urlConn) {
		urlConn.setConnectTimeout(TIMEOUT);
		urlConn.setReadTimeout(TIMEOUT);
	}

	@Override
	public String downloadBySession(String url, String sessionId)
			throws Exception {
		URLConnection urlConn = new URL(url).openConnection();
		setTimeout(urlConn);
		setSessionId(sessionId, urlConn);
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

	private void setSessionId(String sessionId, URLConnection urlConn) {
		if (StringUtil.isNonEmpty(sessionId)) {
			urlConn.setRequestProperty("Cookie", "ASP.NET_SessionId="
					+ sessionId + ";");
		}
	}
}
