package com.oforsky.mmm.service;

import java.io.File;

/**
 * Created by kingweng on 2014/10/19.
 */
public interface HttpService {
	void download(String url, File file, String encoding) throws Exception;

	String download(String url, String encoding) throws Exception;

	String getSessionId(String url) throws Exception;

	String downloadBySession(String url, String sessionId, String encoding)
			throws Exception;

}
