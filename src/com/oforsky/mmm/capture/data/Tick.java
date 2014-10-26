package com.oforsky.mmm.capture.data;

import java.util.List;

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
}
