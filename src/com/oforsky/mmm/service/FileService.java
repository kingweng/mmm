package com.oforsky.mmm.service;

import java.io.File;

/**
 * Created by kingweng on 2014/10/19.
 */
public interface FileService {
    void download(String url, File file) throws Exception;

}
