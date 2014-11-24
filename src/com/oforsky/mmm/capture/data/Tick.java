package com.oforsky.mmm.capture.data;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.Gson;
import com.oforsky.mmm.capture.data.stock.MsgArray;
import com.oforsky.mmm.capture.data.stock.Stock;
import com.truetel.jcore.util.AppException;

/**
 * Created by kingweng on 2014/8/4.
 */
public class Tick {

	private String code;

	private Double price;

	private Long timestamp;

	private Integer tickVolume;

	private Integer totalVolume;

	private List<Double> seldPrices;

	private List<Integer> seldVolumes;

	private List<Double> buyPrices;

	private List<Integer> buyVolumes;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public Integer getTickVolume() {
		return tickVolume;
	}

	public void setTickVolume(Integer tickVolume) {
		this.tickVolume = tickVolume;
	}

	public Integer getTotalVolume() {
		return totalVolume;
	}

	public void setTotalVolume(Integer totalVolume) {
		this.totalVolume = totalVolume;
	}

	public List<Double> getSeldPrices() {
		return seldPrices;
	}

	public void setSeldPrices(List<Double> seldPrices) {
		this.seldPrices = seldPrices;
	}

	public List<Integer> getSeldVolumes() {
		return seldVolumes;
	}

	public void setSeldVolumes(List<Integer> seldVolumes) {
		this.seldVolumes = seldVolumes;
	}

	public List<Double> getBuyPrices() {
		return buyPrices;
	}

	public void setBuyPrices(List<Double> buyPrices) {
		this.buyPrices = buyPrices;
	}

	public List<Integer> getBuyVolumes() {
		return buyVolumes;
	}

	public void setBuyVolumes(List<Integer> buyVolumes) {
		this.buyVolumes = buyVolumes;
	}

	public static Tick valueOf(String content) throws Exception {
		Stock stockInfo = new Gson().fromJson(content, Stock.class);
		checkContent(content, stockInfo);
		MsgArray array = stockInfo.getMsgArray().get(0);
		return asTick(array);
	}

	private static void checkContent(String content, Stock stockInfo)
			throws AppException {
		if (stockInfo.getMsgArray().isEmpty()) {
			throw new AppException(5005, content);
		}
	}

	private static Tick asTick(MsgArray array) {
		Tick tick = new Tick();
		tick.setCode(array.getC());
		tick.setPrice(Double.parseDouble(array.getZ()));
		tick.setTimestamp(Long.parseLong(array.getTlong()));
		tick.setTickVolume(Integer.parseInt(array.getTv()));
		tick.setTotalVolume(Integer.parseInt(array.getV()));
		tick.setBuyPrices(getPricesList(array.getB()));
		tick.setBuyVolumes(getVolumesList(array.getG()));
		tick.setSeldPrices(getPricesList(array.getA()));
		tick.setSeldVolumes(getVolumesList(array.getF()));
		return tick;
	}

	private static List<Double> getPricesList(String input) {
		if ("-".equals(input)) { // limit up or down
			return null;
		}
		List<Double> prices = new ArrayList<Double>();
		for (String token : input.split("_")) {
			prices.add(Double.parseDouble(token));
		}
		return prices;
	}

	private static List<Integer> getVolumesList(String input) {
		if ("-".equals(input)) { // limit up or down
			return null;
		}
		List<Integer> volumes = new ArrayList<Integer>();
		for (String token : input.split("_")) {
			volumes.add(Integer.parseInt(token));
		}
		return volumes;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}
}
