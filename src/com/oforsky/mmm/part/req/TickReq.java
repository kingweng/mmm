package com.oforsky.mmm.part.req;

import org.apache.log4j.Logger;

import com.oforsky.mmm.proxy.MmmProxyUtil;
import com.truetel.jcore.part.TransactionPolicy;
import com.truetel.jcore.part.TransactionPolicyEnum;
import com.truetel.jcore.part.ZoneReq;

/**
 * Created by kingweng on 2014/11/6.
 */
@TransactionPolicy(TransactionPolicyEnum.None)
public class TickReq implements ZoneReq {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger log = Logger.getLogger(TickReq.class);

	private final String code;

	private final String y6d;

	public TickReq(String code, String y6d) {
		this.code = code;
		this.y6d = y6d;
	}

	@Override
	public void handleReq() throws Exception {
		try {
			MmmProxyUtil.getProxy().retrieveTick(code, y6d);
		} catch (Exception e) {
			log.error("retrieveTick failed!", e);
			// ignore
		}
	}
}
