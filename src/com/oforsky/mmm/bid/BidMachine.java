package com.oforsky.mmm.bid;

import com.oforsky.mmm.ebo.BidReqEbo;
import com.oforsky.mmm.ebo.StorageEbo;

public interface BidMachine {

	void proceed(BidReqEbo bid) throws Exception;

	void sell(StorageEbo ebo) throws Exception;

}
