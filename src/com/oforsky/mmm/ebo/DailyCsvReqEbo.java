/**
 * CsvPullReqEbo.java
 *
 * Copyright (c) 2007-2013 TrueTel Communications. All rights reserved.
 */
package com.oforsky.mmm.ebo;

import java.io.File;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.apache.log4j.Logger;

import com.oforsky.mmm.part.req.CsvPullReq;
import com.oforsky.mmm.part.zone.CsvPullReqZone;
import com.truetel.jcore.util.DirUtil;

@Entity
@Table(name = DailyCsvReqCoreEbo.DB_TABLE_NAME)
@NamedQueries({
		@NamedQuery(name = DailyCsvReqCoreEbo.QUERY_LISTALL_NAME, query = DailyCsvReqCoreEbo.QUERY_LISTALL),
		@NamedQuery(name = DailyCsvReqCoreEbo.QUERY_COUNTALL_NAME, query = DailyCsvReqCoreEbo.QUERY_COUNTALL) })
public class DailyCsvReqEbo extends DailyCsvReqCoreEbo {
	private static final long serialVersionUID = 1L;

	private static final String TASVERSION = "TAS-OFF-R5V1P4 2014-04-30 16:43:36";

	private static final Logger log = Logger.getLogger(DailyCsvReqEbo.class);

	private static final String TMP_FILE_DIR = "dailyCsv";

	public DailyCsvReqEbo() {
	}

	public DailyCsvReqEbo(String code, String monthStr, String name,
			StockTypeEnum type) {
		setCode(code);
		setMonthStr(monthStr);
		setName(name);
		setStockType(type);
	}

	public DailyCsvReqEbo(StockGroupEbo stock, String monthStr) {
		this(stock.getCode(), monthStr, stock.getName(), stock.getStockType());
	}

	private String composeUrl() throws Exception {
		return getStockType().getDailyUrl(getCode(), getMonthStr());
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
		sb.append(DirUtil.getAppDataHome(MmmConstant.APP_NAME) + "/"
				+ TMP_FILE_DIR + "/" + getMonthStr() + "/");
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
