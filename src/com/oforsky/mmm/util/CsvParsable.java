package com.oforsky.mmm.util;

public interface CsvParsable<T> {
	T from(String[] columns) throws Exception;

	T from(String[] columns, String header) throws Exception;
}
