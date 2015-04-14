package com.oforsky.mmm.service;

import java.util.List;

import com.oforsky.mmm.ebo.TickEbo;
import com.oforsky.mmm.ebo.WarrantEbo;

public interface WarrantService {

	List<WarrantEbo> listWarrants(String code) throws Exception;

	TickEbo getTick(String warrantCode) throws Exception;

}
