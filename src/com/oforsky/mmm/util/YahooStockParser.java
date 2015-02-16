package com.oforsky.mmm.util;

import java.io.StringReader;
import java.util.Arrays;

import org.apache.log4j.Logger;

import com.oforsky.mmm.ebo.StockEbo;
import com.oforsky.mmm.ebo.StockGroupEbo;
import com.oforsky.mmm.service.HttpServiceImpl;
import com.truetel.jcore.util.AppException;

public class YahooStockParser extends CsvParser<StockEbo> {

	private static final Logger log = Logger.getLogger(YahooStockParser.class);

	private StockGroupEbo stock;

	public YahooStockParser(StockGroupEbo ebo, String content) throws Exception {
		super(true, new StringReader(content));
		this.stock = ebo;
	}

	public static YahooStockParser aParser(StockGroupEbo ebo) throws Exception {
		String downloadUrl = ebo.getStockType().getHistoryUrl(ebo.getCode());
		String content = new HttpServiceImpl().download(downloadUrl, "big5");
		return new YahooStockParser(ebo, content);
	}

	@Override
	protected StockEbo aCsvParsable() throws Exception {
		StockEbo ebo = new StockEbo() {

			@Override
			public StockEbo from(String[] columns) throws Exception {
				StockEbo ebo = null;
				try {
					ebo = new StockEbo();
					ebo.setCode(stock.getCode());
					ebo.setName(stock.getName());
					ebo.setStockType(stock.getStockType());
					ebo.setDateStr(columns[0].replaceAll("-", ""));
					ebo.setMonthStr(ebo.getDateStr().substring(0, 6));
					ebo.setVolume(Long.parseLong(columns[5]) / 1000L);
					ebo.setStartPrice(Double.parseDouble(columns[1]));
					ebo.setHighestPrice(Double.parseDouble(columns[2]));
					ebo.setLowestPrice(Double.parseDouble(columns[3]));
					ebo.setPrice(Double.parseDouble(columns[4]));
					ebo.setChangePrice(ebo.getPrice() - ebo.getStartPrice());
				} catch (Exception e) {
					log.error("override from() failed!", e);
					throw new AppException(5006, Arrays.asList(columns));
				}
				return ebo;
			}

		};
		return ebo;
	}

}
