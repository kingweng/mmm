package com.oforsky.mmm.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.log4j.Logger;

import com.google.common.collect.Iterables;
import com.truetel.jcore.util.AppException;

public abstract class CsvParser<T> {

	private static final Logger log = Logger.getLogger(CsvParser.class);

	private final boolean headerExisted;

	private final BufferedReader reader;

	private String header;

	private String nextLine;

	public CsvParser(boolean headerExisted, Reader reader) throws IOException {
		this(headerExisted, 1, reader);
	}

	public CsvParser(boolean headerExisted, int headerLines, Reader reader)
			throws IOException {
		this.headerExisted = headerExisted;
		this.reader = new BufferedReader(reader);
		retrieveHeader(headerExisted, headerLines);
	}

	private void retrieveHeader(boolean headerExisted, int headerLines)
			throws IOException {
		StringBuilder tmp = new StringBuilder();
		if (headerExisted) {
			for (int i = 0; i < headerLines; i++) {
				tmp.append(this.reader.readLine() + "\n");
			}
			header = tmp.toString();
			log.info("retrieveHeader, header=" + header);
		}
	}

	public boolean hasNaxt() throws IOException {
		if ((nextLine = reader.readLine()) != null) {
			return true;
		}
		closeReader();
		return false;
	}

	private void closeReader() {
		try {
			reader.close();
		} catch (Exception e) {
			log.warn("close reader failed!", e);
		}
	}

	public T next(CsvParsable<T> obj) throws Exception {
		try {
			CSVRecord record = lineToCsvRecord();
			String[] columns = Iterables.toArray(record, String.class);
			if (headerExisted) {
				return obj.from(columns, header);
			}
			return obj.from(columns);
		} catch (Throwable t) {
			log.error("parse nextLine failed! nextLine[" + nextLine + "]", t);
			throw new AppException(5009, t.getMessage(), nextLine, t);
		}
	}

	private CSVRecord lineToCsvRecord() throws IOException {
		return CSVFormat.DEFAULT.parse(new StringReader(nextLine)).getRecords()
				.get(0);
	}

	protected String getHeader() {
		return header;
	}

	public List<T> list() throws Exception {
		List<T> list = new LinkedList<T>();
		while (hasNaxt()) {
			list.add(next(aCsvParsable()));
		}
		return list;
	}

	protected abstract CsvParsable<T> aCsvParsable() throws Exception;
}
