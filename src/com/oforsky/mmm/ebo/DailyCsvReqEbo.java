/**
 * CsvPullReqEbo.java
 *
 * Copyright (c) 2007-2013 TrueTel Communications. All rights reserved.
 */
package com.oforsky.mmm.ebo;

import javax.persistence.*;

import com.oforsky.mmm.part.req.CsvPullReq;
import com.oforsky.mmm.part.zone.CsvPullReqZone;
import com.oforsky.mmm.svc.MmmPart;
import com.truetel.jcore.util.DirUtil;
import org.apache.log4j.Logger;

import java.io.File;

@Entity
@Table(name = DailyCsvReqCoreEbo.DB_TABLE_NAME)
@NamedQueries({
        @NamedQuery(name = DailyCsvReqCoreEbo.QUERY_LISTALL_NAME,
                query = DailyCsvReqCoreEbo.QUERY_LISTALL)
        , @NamedQuery(name = DailyCsvReqCoreEbo.QUERY_COUNTALL_NAME,
        query = DailyCsvReqCoreEbo.QUERY_COUNTALL)
}
)
public class DailyCsvReqEbo extends DailyCsvReqCoreEbo {
    private static final long serialVersionUID = 1L;

    private static final String TASVERSION = "TAS-OFF-R5V1P4 2014-04-30 16:43:36";

    private static final Logger log = Logger.getLogger(DailyCsvReqEbo.class);

    private static final String TMP_FILE_DIR = "dailyCsv";

    private static final String URL_TEMPLATE =
            "http://www.twse.com.tw/ch/trading/exchange/STOCK_DAY/STOCK_DAY_print.php?" +
                    "genpage=genpage/Report%s/%s_F3_1_8_%s.php&type=csv";

    public DailyCsvReqEbo() {
    }

    public DailyCsvReqEbo(String code, String monthStr, String name) {
        setCode(code);
        setMonthStr(monthStr);
        setName(name);
    }

    private String composeUrl() {
        return String
                .format(URL_TEMPLATE, getMonthStr(), getMonthStr(), getCode());
    }

    public void retrieve() throws Exception {
        setPullReqState(PullReqStateFsm.Action.Retrieve);
    }

    private String getTmpFileName() {
        return getMonthStr() + "_" + getCode() + ".csv";
    }

    public void parse() throws Exception {
        setPullReqState((PullReqStateFsm.Action.Parse));
    }

    private File getCsvTmpFile() {
        return new File(getCsvFilePath());
    }

    public void retrieveFail(Exception e) throws Exception {
        setPullReqState(PullReqStateFsm.Action.FailRetrieval);
        setErrMsg(e.getMessage());
        setRetryCount(getRetryCount() + 1);
    }

    public void fail(String errMsg) throws Exception {
        setPullReqState(PullReqStateFsm.Action.Fail);
        setErrMsg(errMsg);
    }

    @Override
    public void preCreate() throws Exception {
        super.preCreate();
        setCsvUrl(composeUrl());
        setCsvFilePath(composeFilePath());
    }

    private String composeFilePath() {
        StringBuilder sb = new StringBuilder();
        sb.append(DirUtil.getAppDataHome(MmmConstant.APP_NAME) + "/" +
                TMP_FILE_DIR + "/" + getMonthStr() + "/");
        sb.append(getTmpFileName());
        return sb.toString();
    }

    @Override
    protected void postDeleteSkyCb() throws Exception {
        super.postDeleteSkyCb();
        getCsvTmpFile().delete();
    }

    @Override
    protected void postCreateSkyCb() throws Exception {
        super.postCreateSkyCb();
        CsvPullReqZone.get().putReq(new CsvPullReq(getReqOid()));
    }

    public void failRetrieval(String errMsg) throws Exception {
        setPullReqState(PullReqStateFsm.Action.FailRetrieval);
        setErrMsg(errMsg);
        setRetryCount(getRetryCount() + 1);
    }
}

