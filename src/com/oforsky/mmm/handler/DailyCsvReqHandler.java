package com.oforsky.mmm.handler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;

import com.oforsky.mmm.data.HistoricalStock;
import com.oforsky.mmm.ebo.BiasEbo;
import com.oforsky.mmm.ebo.DailyCsvReqEbo;
import com.oforsky.mmm.ebo.StockEbo;
import com.oforsky.mmm.ebo.StockTypeEnum;
import com.oforsky.mmm.part.req.CsvParseReq;
import com.oforsky.mmm.part.zone.CsvParseReqZone;
import com.oforsky.mmm.part.zone.MmmZone;
import com.oforsky.mmm.service.DloService;
import com.oforsky.mmm.service.DloServiceImpl;
import com.oforsky.mmm.service.HttpService;
import com.oforsky.mmm.service.HttpServiceImpl;
import com.oforsky.mmm.util.DailyStockParser;
import com.truetel.jcore.util.FileUtil;

/**
 * Created by kingweng on 2014/10/20.
 */
public class DailyCsvReqHandler {

	private static final Logger log = Logger
			.getLogger(DailyCsvReqHandler.class);

	private final MmmZone parseZone;

	private HttpService fileSvc;

	private DloService dloSvc;

	private DailyCsvReqEbo ebo;

	public DailyCsvReqHandler(Integer reqOid, HttpService fileService,
			DloService dloService, MmmZone parseZone) throws Exception {
		this.fileSvc = fileService;
		this.dloSvc = dloService;
		this.parseZone = parseZone;
		this.ebo = dloSvc.getDailyCsvReq(reqOid);
	}

	public DailyCsvReqHandler(Integer reqOid) throws Exception {
		this(reqOid, new HttpServiceImpl(), new DloServiceImpl(),
				CsvParseReqZone.get());
	}

	public DailyCsvReqEbo retrieve() throws Exception {
		String content = fileSvc.download(ebo.getCsvUrl(), "big5");
		if (StockTypeEnum.Cabinet == ebo.getStockType()) {
			content = jsonToCsv(content);
		}
		FileUtil.writeFile(content, new File(ebo.getCsvFilePath()));
		ebo.retrieve();
		dloSvc.updateDailyCsvReq(ebo);
		parseZone.putReq(new CsvParseReq(ebo.getReqOid()));
		return ebo;
	}

	public String jsonToCsv(String content) {
		int index = content.indexOf("aaData") + 9;
		content = content.substring(index, content.length() - 2);
		content = content.replaceAll("\\],", "\n");
		log.debug("content=" + content);
		content = content.replaceAll("[\\[\\\\]", "");
		log.debug("content=" + content);
		content = content.substring(0, content.length() - 2);
		log.debug("content=" + content);
		String header = "\"103年07月 code name  各日成交資訊(元,股)\"\n日期,成交股數,成交金額,開盤價,最高價,最低價,收盤價,漲跌價差,成交筆數\n";
		content = header + content;
		return content;
	}

	public DailyCsvReqEbo parse() throws Exception {
		ebo.parse();
		List<StockEbo> stocks = parseCsvFile();
		log.debug("getStock size=" + stocks.size());
		dloSvc.deleteStock(ebo.getCode(), ebo.getMonthStr());
		dloSvc.batchCreateStock(stocks);
		dloSvc.updateDailyCsvReq(ebo);
		//createBias();
		return ebo;
	}

	private void createBias() throws Exception {
		List<StockEbo> stocks = dloSvc.listStocksByCodeSize(ebo.getCode(), 20);
		HistoricalStock history = new HistoricalStock(ebo.getCode(), stocks);
		dloSvc.createBias(history.getBias());
	}

	private List<StockEbo> parseCsvFile() throws IOException,
			FileNotFoundException, Exception {
		return new DailyStockParser(ebo.getCsvFilePath(), ebo.getName(),
				ebo.getCode(), ebo.getStockType()).list();
	}

	public DailyCsvReqEbo failRetrieval(String msg) throws Exception {
		ebo.failRetrieval(msg);
		dloSvc.updateDailyCsvReq(ebo);
		return ebo;
	}

	public DailyCsvReqEbo fail(String errMsg) throws Exception {
		ebo.fail(errMsg);
		dloSvc.updateDailyCsvReq(ebo);
		return ebo;
	}

}
