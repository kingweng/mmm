package com.oforsky.mmm.part.req;

import com.oforsky.mmm.proxy.MmmProxyUtil;
import com.truetel.jcore.part.ZoneReq;

/**
 * Created by kingweng on 2014/11/6.
 */
public class TickReq implements ZoneReq {

    private final String code;

    private final String y6d;

    public TickReq(String code, String y6d) {
        this.code = code;
        this.y6d = y6d;
    }

    @Override
    public void handleReq() throws Exception {
        MmmProxyUtil.getProxy().retrieveTick(code, y6d);
    }
}
