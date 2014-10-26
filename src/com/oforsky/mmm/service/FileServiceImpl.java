package com.oforsky.mmm.service;

import com.truetel.jcore.util.FileUtil;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by kingweng on 2014/10/19.
 */
public class FileServiceImpl implements FileService {

    private static final Logger log = Logger.getLogger(FileServiceImpl.class);

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
}
