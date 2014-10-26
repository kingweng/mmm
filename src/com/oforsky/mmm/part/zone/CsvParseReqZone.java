package com.oforsky.mmm.part.zone;

import com.oforsky.mmm.svc.MmmPart;
import com.truetel.jcore.part.ZoneReq;

/**
 * Created by kingweng on 2014/8/20.
 */
public class CsvParseReqZone implements MmmZone {
    private static CsvParseReqZone instance = new CsvParseReqZone();

    public static CsvParseReqZone get() {
        return instance;
    }

    private CsvParseReqZone() {

    }

    @Override
    public void putReq(ZoneReq req) throws Exception {
        MmmPart.getCsvParseReqZone().putReq(req);
    }

}
