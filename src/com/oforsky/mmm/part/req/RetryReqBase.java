package com.oforsky.mmm.part.req;

import com.oforsky.mmm.proxy.MmmProxy;
import com.oforsky.mmm.proxy.MmmProxyUtil;
import com.truetel.jcore.util.AppException;
import org.apache.log4j.Logger;

abstract class RetryReqBase implements RetryReq {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private static final Logger log = Logger.getLogger(RetryReqBase.class);

    private com.oforsky.mmm.part.req.RetryArgs args;

    private com.oforsky.mmm.part.req.Retry retry;

    protected RetryReqBase(com.oforsky.mmm.part.req.RetryArgs args) {
        this.args = args;
        this.retry = new com.oforsky.mmm.part.req.Retry(args);
    }

    protected RetryReqBase() {
        //this.args = RetryArgs.IMPL;
        this.retry = new com.oforsky.mmm.part.req.Retry(args);
    }

    @Override
    public final void handleReq() throws Exception {
        try {
            handleSucc();
        } catch (Exception e) {
            log.error("handleReq failed!", e);
            handleFail(e);
        }
    }

    private void handleFail(Exception e) throws Exception {
        if (!retry.reachMaxRetries()) {
            handleTmpFail(e);
            retry.setTimer(this);
        } else {
            handleFinalFail(e);
        }
    }

    @Override
    public final void timerFiredCb() throws Exception {
        log.debug("put RetryReq into zone");
        args.getRetryZone().putReq(this);
    }

    protected Integer getErrCode(Exception e) {
        Integer errCode = null;
        if (e instanceof AppException) {
            AppException ae = (AppException) e;
            errCode = ae.getErrorCode();
        }
        return errCode;
    }

    protected String getErrMsg(Exception e) {
        String errMsg = e.getMessage();
        if (e.getCause() != null) {
            errMsg = e.getCause().getMessage();
        }
        return errMsg;
    }

    protected MmmProxy getProxy() throws Exception {
        return MmmProxyUtil.getProxy();
    }

    protected abstract void handleSucc() throws Exception;

    protected abstract void handleTmpFail(Exception e) throws Exception;

    protected abstract void handleFinalFail(Exception e) throws Exception;
}
