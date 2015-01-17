package com.oforsky.mmm.service;

import java.io.File;

/**
 * Created by kingweng on 2014/10/19.
 */
public interface HttpService {
	void download(String url, File file) throws Exception;

	String download(String url) throws Exception;

	String getSessionId(String url) throws Exception;

	String downloadBySession(String url, String sessionId) throws Exception;

}
