/**
 * QueryJobEbo.java
 *
 * Copyright (c) 2007-2013 TrueTel Communications. All rights reserved.
 */
package com.oforsky.mmm.ebo;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.apache.log4j.Logger;

import com.oforsky.mmm.capture.TickRetriever;
import com.oforsky.mmm.handler.WarrantHandler;
import com.oforsky.mmm.part.req.QueryJobReq;
import com.oforsky.mmm.svc.MmmPart;
import com.oforsky.mmm.util.MmmUtil;

@Entity
@Table(name = QueryJobCoreEbo.DB_TABLE_NAME)
@NamedQueries({
		@NamedQuery(name = QueryJobCoreEbo.QUERY_LISTALL_NAME, query = QueryJobCoreEbo.QUERY_LISTALL),
		@NamedQuery(name = QueryJobCoreEbo.QUERY_COUNTALL_NAME, query = QueryJobCoreEbo.QUERY_COUNTALL) })
public class QueryJobEbo extends QueryJobCoreEbo {
	private static final long serialVersionUID = 1L;
	private static final String TASVERSION = "TAS-OFF-R5V1P10 2014-08-01 15:49:52";
	private static final Logger log = Logger.getLogger(QueryJobEbo.class);

	public QueryJobEbo() {
	}

	public void failRetrieval(Exception e) throws Exception {
		setJobState(JobStateFsm.Action.FailRetrieval);
		setFailMsg(e.getMessage());
		setRetryCnt(getRetryCnt() + 1);
	}

	public void fail(Exception e) throws Exception {
		setJobState(JobStateFsm.Action.Fail);
		setFailMsg(e.getMessage());
		setRetryCnt(getRetryCnt() + 1);
	}

	@Override
	protected void postCreateSkyCb() throws Exception {
		super.postCreateSkyCb();
		log.info("put QueryJobReq[" + getJobOid() + "] into zone");
		MmmPart.getQueryJobReqZone().putReq(
				new QueryJobReq(getJobOid(), getAction()));
	}

	public TickEbo retrieve(StorageEbo ebo, TickRetriever retriever)
			throws Exception {
		setJobState(JobStateFsm.Action.Retrieve);
		WarrantEbo warrant = ebo.getWarrantEboForced();
		TickEbo tick = TickEbo.valueOf(retriever.getWarrantTick(
				MmmUtil.getTodayStr(), ebo.getCode(), warrant.getCode()));
		return tick;
	}

	public void retrieve() throws Exception {
		setJobState(JobStateFsm.Action.Retrieve);
	}
}
