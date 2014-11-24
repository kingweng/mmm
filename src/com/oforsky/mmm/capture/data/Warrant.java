package com.oforsky.mmm.capture.data;

import java.util.Arrays;
import java.util.List;

public class Warrant {

	private List<String> codes;

	public Warrant(List<String> codes) {
		this.codes = codes;
	}

	public static Warrant valueOf(String content) {
		content = content.split("\\|")[0];
		return new Warrant(Arrays.asList(content.split(",")));
	}

	public List<String> getCodes() {
		return codes;
	}

}
