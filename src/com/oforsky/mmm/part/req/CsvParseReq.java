package com.oforsky.mmm.part.req;

import com.oforsky.mmm.proxy.MmmProxy;
import com.oforsky.mmm.proxy.MmmProxyUtil;
import com.truetel.jcore.part.TransactionPolicy;
import com.truetel.jcore.part.TransactionPolicyEnum;
import com.truetel.jcore.part.ZoneReq;

import org.apache.log4j.Logger;

/**
 * Created by kingweng on 2014/10/25.
 */
@TransactionPolicy(TransactionPolicyEnum.None)
public class CsvParseReq implements ZoneReq {

    private static final Logger log = Logger
            .getLogger(CsvParseReq.class);

    private Integer reqOid;

    public CsvParseReq(Integer reqOid) {
        this.reqOid = reqOid;
    }

    @Override
    public void handleReq() throws Exception {
        log.info("handleReq enter, reqOid=" + reqOid);
        MmmProxy proxy = MmmProxyUtil.getProxy();
        try {
            proxy.dailyCsvReqParse(reqOid);
        } catch (Exception e) {
            log.error("CsvParseReq handle failed!", e);
            proxy.dailyCsvReqReqFail(reqOid, e.getMessage());
        }
    }
}
