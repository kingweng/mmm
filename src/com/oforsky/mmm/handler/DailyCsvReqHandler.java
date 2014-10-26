package com.oforsky.mmm.handler;

import com.oforsky.mmm.ebo.DailyCsvReqEbo;
import com.oforsky.mmm.part.req.CsvParseReq;
import com.oforsky.mmm.part.req.CsvPullReq;
import com.oforsky.mmm.part.zone.CsvParseReqZone;
import com.oforsky.mmm.part.zone.CsvPullReqZone;
import com.oforsky.mmm.part.zone.MmmZone;
import com.oforsky.mmm.service.DloService;
import com.oforsky.mmm.service.DloServiceImpl;
import com.oforsky.mmm.service.FileService;
import com.oforsky.mmm.service.FileServiceImpl;
import org.apache.log4j.Logger;

import java.io.File;

/**
 * Created by kingweng on 2014/10/20.
 */
public class DailyCsvReqHandler {

    private static final Logger log = Logger
            .getLogger(DailyCsvReqHandler.class);

    private final MmmZone parseZone;

    private FileService fileSvc;

    private DloService dloSvc;

    private MmmZone pullZone;

    public DailyCsvReqHandler(FileService fileService, DloService dloService,
            MmmZone pullZone, MmmZone parseZone) {
        this.fileSvc = fileService;
        this.dloSvc = dloService;
        this.pullZone = pullZone;
        this.parseZone = parseZone;
    }

    public DailyCsvReqHandler() {
        this(new FileServiceImpl(), new DloServiceImpl(), CsvPullReqZone.get(),
                CsvParseReqZone.get());
    }

    public DailyCsvReqEbo retrieve(Integer reqOid) throws Exception {
        log.info("retrieve() reqOid=" + reqOid);
        throw new Exception("test err");
//        DailyCsvReqEbo ebo = dloSvc.getDailyCsvReq(reqOid);
//        fileSvc.download(ebo.getCsvUrl(),
//                new File(ebo.getCsvFilePath()));
//        ebo.retrieve();
//        dloSvc.updateDailyCsvReq(ebo);
//        parseZone.putReq(new CsvParseReq(reqOid));
//        return ebo;
    }

    public DailyCsvReqEbo parse(Integer reqOid) throws Exception {
        log.info("retrieve() reqOid=" + reqOid);
        DailyCsvReqEbo ebo = dloSvc.getDailyCsvReq(reqOid);
        ebo.parse();
        DailyStockImporter importer = DailyStockImporter
                .build(new File(ebo.getCsvFilePath()));
        log.debug("getStock size=" + importer.getStocks().size());
        dloSvc.deleteStock(ebo.getCode(), ebo.getMonthStr());
        dloSvc.batchCreateStock(importer.getStocks());
        dloSvc.updateDailyCsvReq(ebo);
        return ebo;
    }

    public DailyCsvReqEbo failRetrieval(Integer reqOid, String msg)
            throws Exception {
        log.info("failRetrieval() reqOid=" + reqOid);
        DailyCsvReqEbo ebo = dloSvc.getDailyCsvReq(reqOid);
        ebo.failRetrieval(msg);
        dloSvc.updateDailyCsvReq(ebo);
        return ebo;
    }

    public DailyCsvReqEbo fail(Integer reqOid, String errMsg) throws Exception {
        log.info("fail() reqOid=" + reqOid + ", errMsg=" + errMsg);
        DailyCsvReqEbo ebo = dloSvc.getDailyCsvReq(reqOid);
        ebo.fail(errMsg);
        dloSvc.updateDailyCsvReq(ebo);
        return ebo;
    }
}
