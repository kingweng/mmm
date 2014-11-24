package com.oforsky.mmm.part.zone;

import com.oforsky.mmm.svc.MmmPart;
import com.truetel.jcore.part.ZoneReq;

/**
 * Created by kingweng on 2014/11/6.
 */
public class TickReqZone implements MmmZone {
    private static TickReqZone instance = new TickReqZone();

    public static TickReqZone get() {
        return instance;
    }

    private TickReqZone() {

    }

    @Override
    public void putReq(ZoneReq req) throws Exception {
        MmmPart.getTickReqZone().putReq(req);
    }
}
