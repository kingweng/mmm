package com.oforsky.mmm.handler.strategy;

import org.apache.log4j.Logger;

import com.oforsky.mmm.cache.BalanceCfgCacheStore;
import com.oforsky.mmm.cache.StorageCacheStore;
import com.oforsky.mmm.ebo.ActionTypeEnum;
import com.oforsky.mmm.ebo.QueryJobEbo;
import com.oforsky.mmm.ebo.SellTypeEnum;
import com.oforsky.mmm.ebo.StorageEbo;
import com.oforsky.mmm.ebo.TickEbo;
import com.oforsky.mmm.proxy.MmmProxy;

public class TickContext implements TickState {

	private static final Logger log = Logger.getLogger(TickContext.class);

	private boolean isAlive = true;

	private TickState state;

	private MmmProxy proxy;

	private TickStrategy strategy;

	public TickContext(MmmProxy proxy, TickStrategy strategy) {
		this.strategy = strategy;
		this.state = new MonitorTickState(strategy);
		this.proxy = proxy;
	}

	public TickContext(MmmProxy proxy, TickStrategy strategy, StorageEbo storage) {
		this(proxy, strategy);
		if (storage != null) {
			this.state = new HoldingTickState(strategy, storage.getCode(),
					storage.getTargetPrice());
		}
	}

	public synchronized void setState(TickState state) {
		this.state = state;
		isAlive = false;
	}

	@Override
	public void handle(TickContext context, TickEbo tick) throws Exception {
		this.state.handle(this, tick);
	}

	public boolean isAlive() {
		return isAlive;
	}

	public void prepareSell(String code, Double sellPrice, SellTypeEnum sellType)
			throws Exception {
		StorageEbo storage = StorageCacheStore.getStore().get(code);
		if (storage == null) {
			log.warn("cannot find code[" + code + "] in storages");
			return;
		}
		createQueryJob(code, sellPrice, storage.getAmount(),
				ActionTypeEnum.Sell);
	}

	private void createQueryJob(String code, Double price, Integer amount,
			ActionTypeEnum action) throws Exception {
		QueryJobEbo job = new QueryJobEbo();
		job.setCode(code);
		job.setPrice(price);
		job.setAmount(amount);
		job.setAction(action);
		proxy.createQueryJob(job);
	}

	public void prepareBuy(String code, Double buyPrice) throws Exception {
		log.info("prepareBuy enter, code = " + code + ", buyPrice = "
				+ buyPrice);
		createQueryJob(code, buyPrice, BalanceCfgCacheStore.getSingleBid(),
				ActionTypeEnum.Buy);
	}

	public TickStrategy getStrategy() {
		return strategy;
	}
}
