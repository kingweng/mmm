package com.oforsky.mmm.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import com.oforsky.mmm.cache.SvcCfgCacheStore;
import com.truetel.jcore.util.FileUtil;
import com.truetel.jcore.util.StringUtil;

/**
 * Created by kingweng on 2014/10/19.
 */
public class HttpServiceImpl implements HttpService {

	private static final Logger log = Logger.getLogger(HttpServiceImpl.class);

	@Override
	public void download(String url, File file, String encoding)
			throws Exception {
		log.trace("retrieveFile url[" + url + "] to file[" + file + "]");
		file.getParentFile().mkdirs();
		URLConnection conn = new URL(url).openConnection();
		setTimeout(conn);
		String content = IOUtils.toString(conn.getInputStream(), "Big5");
		FileUtil.writeFile(content, file);
	}

	@Override
	public String download(String url, String encoding) throws Exception {
		return downloadBySession(url, null, encoding);
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
		urlConn.setConnectTimeout(SvcCfgCacheStore.getTickTimeout() * 1000);
		urlConn.setReadTimeout(SvcCfgCacheStore.getTickTimeout() * 1000);
	}

	@Override
	public String downloadBySession(String url, String sessionId,
			String encoding) throws Exception {
		log.debug("downloadBySession() url=" + url);
		URLConnection urlConn = new URL(url).openConnection();
		setTimeout(urlConn);
		setSessionId(sessionId, urlConn);
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(urlConn.getInputStream(), encoding));
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
