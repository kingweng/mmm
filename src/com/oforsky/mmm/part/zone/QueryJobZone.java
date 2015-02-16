package com.oforsky.mmm.part.zone;

import com.oforsky.mmm.svc.MmmPart;
import com.truetel.jcore.part.ZoneReq;

/**
 * Created by kingweng on 2014/8/20.
 */
public class QueryJobZone implements MmmZone {
    private static QueryJobZone instance = new QueryJobZone();

    public static QueryJobZone get() {
        return instance;
    }

    private QueryJobZone() {

    }

    @Override
    public void putReq(ZoneReq req) throws Exception {
        MmmPart.getQueryJobReqZone().putReq(req);
    }

}
