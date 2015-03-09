

create table mmm_SvcCfg (
	svcName varchar2(20) not null,
	simMode number(1),
	retryIntvl number(10),
	retryLimit number(10),
	dailyImportTime varchar2(20),
	revenueSellTime varchar2(20),
	kBreakSellTime varchar2(20),
	tickInterval number(10),
	tickTimeout number(10),
	tickTimeRange varchar2(20),
	closedRateOfK number(10),
	kList varchar2(20),
        constraint mmm_SvcCfg_PK
		primary key (svcName) using index tablespace tasind_svc
) tablespace  tas_svc  ;

create table mmm_WarrantCfg (
	svcName varchar2(20) not null,
	remainingDays number(10),
	warrantDiffPrice float(126),
	qualifiedDealers varchar2(256),
	minLeverage number(10),
	warrantRetryIntvl number(10),
	buyRetryLimit number(10),
	sellRetryLimit number(10),
        constraint mmm_WarrantCfg_PK
		primary key (svcName) using index tablespace tasind_svc
) tablespace  tas_svc  ;

create table mmm_DealCfg (
	svcName varchar2(20) not null,
	dayCount number(10),
	overTimes number(10),
	kBreak number(10),
	winChance number(10),
	revenueRate float(126),
        constraint mmm_DealCfg_PK
		primary key (svcName) using index tablespace tasind_svc
) tablespace  tas_svc  ;

create table mmm_BalanceCfg (
	svcName varchar2(20) not null,
	balance number(10),
	singleBid number(10),
        constraint mmm_BalanceCfg_PK
		primary key (svcName) using index tablespace tasind_svc
) tablespace  tas_svc  ;

create table mmm_Stock (
	code varchar2(4) not null,
	name varchar2(20 char),
	dateStr varchar2(8) not null,
	stockType number(3),
	price float(126),
	startPrice float(126),
	lowestPrice float(126),
	highestPrice float(126),
	changePrice float(126),
	monthStr varchar2(8),
	volume number(20),
	createTime date,
        constraint mmm_Stock_PK
		primary key (code, dateStr) using index tablespace tasind_svc
) tablespace  tas_svc  ;
create  index
	mmm_Stock_CodeMonth on mmm_Stock (code, monthStr) tablespace tasind_svc;
create  index
	mmm_Stock_Month on mmm_Stock (monthStr) tablespace tasind_svc;

create table mmm_Tick (
	tickOid number(10) not null,
	code varchar2(20),
	price float(126),
	tickVolume number(10),
	totalVolume number(10),
	timestamp number(20),
	createTime date,
	buyPrices varchar2(100),
	buyVolumes varchar2(100),
	seldPrices varchar2(100),
	seldVolumes varchar2(100),
        constraint mmm_Tick_PK
		primary key (tickOid) using index tablespace tasind_svc
) tablespace  tas_svc  ;
                	
create sequence mmm_Tick_SEQ  increment by 1 cache 100 start with 1 maxvalue 2147483647 nocycle;
create  index
	mmm_Tick_Code on mmm_Tick (code) tablespace tasind_svc;
create  index
	mmm_Tick_Time on mmm_Tick (createTime) tablespace tasind_svc;

create table mmm_Warrant (
	warrantOid number(10) not null,
	name varchar2(20 char),
	code varchar2(20),
	targetCode varchar2(20),
	price float(126),
	buyPrice float(126),
	buyVolume number(10),
	sellPrice float(126),
	sellVolume number(10),
	diffPrices float(126),
	idealDiffPrices float(126),
	remainingDays number(10),
	leverage float(126),
	createTime date,
        constraint mmm_Warrant_PK
		primary key (warrantOid) using index tablespace tasind_svc
) tablespace  tas_svc  ;
                	
create sequence mmm_Warrant_SEQ  increment by 1 cache 100 start with 1 maxvalue 2147483647 nocycle;
create  index
	mmm_Warrant_Code on mmm_Warrant (code) tablespace tasind_svc;
create  index
	mmm_Warrant_Target on mmm_Warrant (targetCode) tablespace tasind_svc;

create table mmm_Storage (
	code varchar2(20) not null,
	warrantOid number(10),
	name varchar2(20 char),
	price float(126),
	targetPrice float(126),
	unit number(10),
	amount number(10),
	createTime date,
        constraint mmm_Storage_PK
		primary key (code) using index tablespace tasind_svc
) tablespace  tas_svc  ;

create table mmm_StorageLog (
	logOid number(10) not null,
	warrantOid number(10),
	name varchar2(20 char),
	code varchar2(20),
	buyPrice float(126),
	sellPrice float(126),
	targetCode varchar2(20),
	targetPrice float(126),
	unit number(10),
	amount number(10),
	revenue number(10),
	logTime date,
	buyTime date,
	keepDays number(10),
        constraint mmm_StorageLog_PK
		primary key (logOid) using index tablespace tasind_svc
) tablespace  tas_svc  ;
                	
create sequence mmm_StorageLog_SEQ  increment by 1 cache 100 start with 1 maxvalue 2147483647 nocycle;
create  index
	mmm_StorageLog_Code on mmm_StorageLog (targetCode) tablespace tasind_svc;
create  index
	mmm_StorageLog_Time on mmm_StorageLog (logTime) tablespace tasind_svc;
create  index
	mmm_StorageLog_Warrant on mmm_StorageLog (warrantOid) tablespace tasind_svc;

create table mmm_DailyCsvReq (
	reqOid number(10) not null,
	code varchar2(6),
	name varchar2(20 char),
	stockType number(3),
	pullReqState number(3),
	csvUrl varchar2(256),
	csvFilePath varchar2(256),
	monthStr varchar2(6),
	retryCount number(10),
	errMsg varchar2(512),
	createTime date,
	updateTime date,
        constraint mmm_DailyCsvReq_PK
		primary key (reqOid) using index tablespace tasind_svc
) tablespace  tas_svc  ;
                	
create sequence mmm_DailyCsvReq_SEQ  increment by 1 cache 100 start with 1 maxvalue 2147483647 nocycle;

create table mmm_StockGroup (
	code varchar2(4) not null,
	name varchar2(20 char),
	stockType number(3),
	createTime date,
        constraint mmm_StockGroup_PK
		primary key (code) using index tablespace tasind_svc
) tablespace  tas_svc  ;

create table mmm_BuyingStock (
	code varchar2(4) not null,
        constraint mmm_BuyingStock_PK
		primary key (code) using index tablespace tasind_svc
) tablespace  tas_svc  ;

create table mmm_Deal (
	realOid number(10) not null,
	code varchar2(4),
	buyDateStr varchar2(20),
	buyPrice float(126),
	recordHigh number(10),
	sellDateStr varchar2(20),
	sellPrice float(126),
	sellType number(3),
	diffPrice float(126),
	keepDays number(10),
	revenueRate float(126),
    	constraint mmm_Deal_BuyStock
		unique (code, buyDateStr)  using index tablespace tasind_svc,
    	constraint mmm_Deal_SellStock
		unique (code, sellDateStr)  using index tablespace tasind_svc,
        constraint mmm_Deal_PK
		primary key (realOid) using index tablespace tasind_svc
) tablespace  tas_svc  ;
                	
create sequence mmm_Deal_SEQ  increment by 1 cache 100 start with 1 maxvalue 2147483647 nocycle;
create  index
	mmm_Deal_Code on mmm_Deal (code) tablespace tasind_svc;

create table mmm_DealStats (
	statsOid number(10) not null,
	code varchar2(20),
	succCnt number(10),
	failCnt number(10),
	winChance number(10),
	revenueRate float(126),
	avgKeepDays float(126),
    	constraint mmm_DealStats_Code
		unique (code)  using index tablespace tasind_svc,
        constraint mmm_DealStats_PK
		primary key (statsOid) using index tablespace tasind_svc
) tablespace  tas_svc  ;
                	
create sequence mmm_DealStats_SEQ  increment by 1 cache 100 start with 1 maxvalue 2147483647 nocycle;

create table mmm_QueryJob (
	jobOid number(10) not null,
	action number(3),
	code varchar2(20),
	price float(126),
	amount number(10),
	retryCnt number(10),
	jobState number(3),
	failMsg varchar2(512),
	createTime date,
	updateTime date,
        constraint mmm_QueryJob_PK
		primary key (jobOid) using index tablespace tasind_svc
) tablespace  tas_svc  ;
                	
create sequence mmm_QueryJob_SEQ  increment by 1 cache 100 start with 1 maxvalue 2147483647 nocycle;
create  index
	mmm_QueryJob_Code on mmm_QueryJob (code) tablespace tasind_svc;

create table mmm_BidReq (
	bidOid number(10) not null,
	warrantOid number(10),
	jobOid number(10),
	name varchar2(20 char),
	price float(126),
	applyUnit number(10),
	remainUnit number(10),
	action number(3),
	bidState number(3),
	amount number(10),
	failMsg varchar2(512),
	createTime date,
	updateTime date,
        constraint mmm_BidReq_PK
		primary key (bidOid) using index tablespace tasind_svc
) tablespace  tas_svc  ;
                	
create sequence mmm_BidReq_SEQ  increment by 1 cache 100 start with 1 maxvalue 2147483647 nocycle;
create  index
	mmm_BidReq_State on mmm_BidReq (bidState) tablespace tasind_svc;

create table mmm_Bias (
	biasOid number(10) not null,
	code varchar2(20),
	dateStr varchar2(20),
	value float(126),
    	constraint mmm_Bias_CodeDate
		unique (code, dateStr)  using index tablespace tasind_svc,
        constraint mmm_Bias_PK
		primary key (biasOid) using index tablespace tasind_svc
) tablespace  tas_svc  ;
                	
create sequence mmm_Bias_SEQ  increment by 1 cache 100 start with 1 maxvalue 2147483647 nocycle;
create  index
	mmm_Bias_Code on mmm_Bias (code) tablespace tasind_svc;

create table mmm_WarrantTick (
	tickOid number(10) not null,
	code varchar2(20),
	price float(126),
	tickVolume number(10),
	totalVolume number(10),
	timestamp number(20),
	createTime date,
	buyPrices varchar2(100),
	buyVolumes varchar2(100),
	seldPrices varchar2(100),
	seldVolumes varchar2(100),
        constraint mmm_WarrantTick_PK
		primary key (tickOid) using index tablespace tasind_svc
) tablespace  tas_svc  ;
                	
create sequence mmm_WarrantTick_SEQ  increment by 1 cache 100 start with 1 maxvalue 2147483647 nocycle;
create  index
	mmm_WarrantTick_Code on mmm_WarrantTick (code) tablespace tasind_svc;
create  index
	mmm_WarrantTick_Time on mmm_WarrantTick (createTime) tablespace tasind_svc;

create table mmm_Drive (
	driveOid number(10) not null,
	license varchar2(20),
	driveTime date,
	poison varchar2(50),
	weight float(126),
	unit varchar2(20),
        constraint mmm_Drive_PK
		primary key (driveOid) using index tablespace tasind_svc
) tablespace  tas_svc  ;
                	
create sequence mmm_Drive_SEQ  increment by 1 cache 100 start with 1 maxvalue 2147483647 nocycle;
create  index
	mmm_Drive_License on mmm_Drive (license) tablespace tasind_svc;

create table mmm_License (
	licenseOid number(10) not null,
	license varchar2(20),
	startTime date,
	endTime date,
	poison varchar2(256),
        constraint mmm_License_PK
		primary key (licenseOid) using index tablespace tasind_svc
) tablespace  tas_svc  ;
                	
create sequence mmm_License_SEQ  increment by 1 cache 100 start with 1 maxvalue 2147483647 nocycle;
create  index
	mmm_License_License on mmm_License (license) tablespace tasind_svc;

create table mmm_Training (
	trainingOid number(10) not null,
	trainingNum varchar2(20),
	pid varchar2(20),
	category varchar2(20),
	startTime date,
	endTime date,
        constraint mmm_Training_PK
		primary key (trainingOid) using index tablespace tasind_svc
) tablespace  tas_svc  ;
                	
create sequence mmm_Training_SEQ  increment by 1 cache 100 start with 1 maxvalue 2147483647 nocycle;
create  index
	mmm_Training_Num on mmm_Training (trainingNum) tablespace tasind_svc;
                	
--  virtual tables
--  virtual tables 

        
alter table mmm_Storage add constraint
    mmm_Storage_Warrant foreign key (warrantOid)
    references mmm_Warrant;
    
create index
	mmm_Storage_Warrant on mmm_Storage (warrantOid) tablespace tasind_svc;
	
create index
	mmm_BidReq_Warrant on mmm_BidReq (warrantOid) tablespace tasind_svc;
	
        
alter table mmm_BidReq add constraint
    mmm_BidReq_Job foreign key (jobOid)
    references mmm_QueryJob;
    
create index
	mmm_BidReq_Job on mmm_BidReq (jobOid) tablespace tasind_svc;
	
