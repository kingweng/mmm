/**
 * ActionTypeEnum.java
 *
 * Copyright (c) 2007-2013 TrueTel Communications. All rights reserved.
 */
package com.oforsky.mmm.ebo;

import com.oforsky.mmm.proxy.MmmProxyUtil;
import com.truetel.jcore.util.AppException;

public enum ActionTypeEnum {
			Unused_0(0) {
				@Override
				public void postProceed(BidReqEbo bid) throws Exception {
					throw new AppException(5000, "cannot be here!");
				}
			}
			, Buy(1) {
				@Override
				public void postProceed(BidReqEbo bid) throws Exception {
					MmmProxyUtil.getProxy().createStorage(new StorageEbo(bid));
				}
			}
			, Sell(2) {
				@Override
				public void postProceed(BidReqEbo bid) throws Exception {
					MmmProxyUtil.getProxy().createStorageLog(new StorageLogEbo(bid));
				}
			}
	;

	private static ActionTypeEnum[] allEnums = {
		Buy
		, Sell
	};

	private ActionTypeEnum(int value) {
	}

	public static ActionTypeEnum[] getAllEnums() {
		return allEnums;
	}

	public int value() {
		return ordinal();
	}

	public static ActionTypeEnum getEnum(int value) {
		switch (value) {
		case 1:
			return Buy;
		case 2:
			return Sell;
		default:
			return null;
		}
	}

	public static ActionTypeEnum getEnum(String value) {
		return ActionTypeEnum.valueOf(value);
	}

	/**
     * Checks whether the enum's value is greater than the input enum's value.
     */
    public boolean above(ActionTypeEnum input) {
        return compareTo(input) > 0;
    }

    /**
     * Checks whether the enum's value is less than the input enum's value.
     */
    public boolean below(ActionTypeEnum input) {
        return compareTo(input) < 0;
    }

	public abstract void postProceed(BidReqEbo bid) throws Exception;
	
}
