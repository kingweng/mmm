package com.oforsky.mmm.util;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.oforsky.mmm.ebo.StockGroupEbo;

public class StockGroupParser extends CsvParser<StockGroupEbo> {

	public StockGroupParser(File file) throws IOException {
		super(false, new FileReader(file));
	}

	public StockGroupParser(String filepath) throws IOException {
		this(new File(filepath));
	}

	@Override
	protected StockGroupEbo aCsvParsable() throws Exception {
		return new StockGroupEbo();
	}

}
