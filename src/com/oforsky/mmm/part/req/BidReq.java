package com.oforsky.mmm.part.req;

import org.apache.log4j.Logger;

import com.oforsky.mmm.proxy.MmmProxyUtil;
import com.truetel.jcore.part.TransactionPolicy;
import com.truetel.jcore.part.TransactionPolicyEnum;
import com.truetel.jcore.part.ZoneReq;

@TransactionPolicy(TransactionPolicyEnum.None)
public class BidReq implements ZoneReq {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger log = Logger.getLogger(BidReq.class);

	private Integer bidOid;

	public BidReq(Integer bidOid) {
		this.bidOid = bidOid;
	}

	@Override
	public void handleReq() throws Exception {
		log.info("handleReq() bidOid=" + bidOid);
		try {
			MmmProxyUtil.getProxy().proceedBidReq(bidOid);
		} catch (Exception e) {
			log.error("handleReq failed! bidOid=" + bidOid, e);
			MmmProxyUtil.getProxy().failBidReq(bidOid, e);
		}
	}

}
