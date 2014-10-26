package com.oforsky.mmm.part.zone;

import com.oforsky.mmm.ebo.MmmConstant;
import com.oforsky.mmm.proxy.MmmProxy;
import com.oforsky.mmm.svc.MmmPart;
import com.truetel.jcore.data.ModuleStateEnum;
import com.truetel.jcore.part.PartFactory;
import com.truetel.jcore.part.Zone;
import com.truetel.jcore.part.ZoneReq;

/**
 * Created by kingweng on 2014/8/20.
 */
public class CsvPullReqZone implements MmmZone {
    private static CsvPullReqZone instance = new CsvPullReqZone();

    public static CsvPullReqZone get() {
        return instance;
    }

    private CsvPullReqZone() {

    }

    @Override
    public void putReq(ZoneReq req) throws Exception {
        MmmPart.getCsvPullReqZone().putReq(req);
    }

}
