package com.oforsky.mmm.util;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.oforsky.mmm.ebo.StockEbo;
import com.oforsky.mmm.ebo.StockTypeEnum;

public class DailyStockParser extends CsvParser<StockEbo> {

	private String name;

	private String code;

	private StockTypeEnum type;

	public DailyStockParser(String filepath, String name, String code,
			StockTypeEnum type) throws IOException {
		this(new File(filepath), name, code, type);
	}

	public DailyStockParser(File file, String name, String code,
			StockTypeEnum type) throws IOException {
		super(true, 2, new FileReader(file));
		this.name = name;
		this.code = code;
		this.type = type;
	}

	@Override
	protected StockEbo aCsvParsable() throws Exception {
		StockEbo ebo = new StockEbo();
		ebo.setName(name);
		ebo.setCode(code);
		ebo.setStockType(type);
		return ebo;
	}

}
