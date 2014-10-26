package com.oforsky.mmm.part.zone;

import com.oforsky.mmm.ebo.MmmConstant;
import com.truetel.jcore.data.ModuleStateEnum;
import com.truetel.jcore.part.PartFactory;
import com.truetel.jcore.part.Zone;
import com.truetel.jcore.part.ZoneReq;

public class RetryZone implements MmmZone {

    private static RetryZone instance = new RetryZone();

    public static RetryZone get() {
        return instance;
    }

    private Zone zone = null;

    private RetryZone() {
    }

    @Override
    public void putReq(ZoneReq req) throws Exception {
        instance.zone.putReq(req);
    }

    public void postActivate() throws Exception {
        instance.zone = PartFactory
                .newZone(MmmConstant.APP_NAME, "RetryZone");
        instance.zone.attach(ModuleStateEnum.Primary);
    }

    public void postPrimarize() throws Exception {
        instance.zone.setCoreThreadPoolSize(5);
    }
}
