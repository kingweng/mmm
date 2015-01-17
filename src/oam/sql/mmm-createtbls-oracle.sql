

create table mmm_SvcCfg (
	svcName varchar2(20) not null,
	simMode number(1),
	retryIntvl number(10),
	retryLimit number(10),
	dailyImportTime varchar2(20),
	tickInterval number(10),
	tickTimeRange varchar2(20),
	remainingDays number(10),
	warrantDiffPrice float(126),
	qualifiedDealers varchar2(20),
	closedRateOfK number(10),
	kList varchar2(20),
        constraint mmm_SvcCfg_PK
		primary key (svcName) using index tablespace tasind_svc
) tablespace  tas_svc  ;

create table mmm_ReportParams (
	svcName varchar2(20) not null,
	dayCount number(10),
	overTimes number(10),
        constraint mmm_ReportParams_PK
		primary key (svcName) using index tablespace tasind_svc
) tablespace  tas_svc  ;

create table mmm_Stock (
	stockOid number(10) not null,
	name varchar2(20 char),
	code varchar2(4),
	stockType number(3),
	price float(126),
	startPrice float(126),
	lowestPrice float(126),
	highestPrice float(126),
	changePrice float(126),
	dateStr varchar2(8),
	monthStr varchar2(8),
	volume number(20),
	createTime date,
    	constraint mmm_Stock_CodeDate
		unique (code, dateStr)  using index tablespace tasind_svc,
        constraint mmm_Stock_PK
		primary key (stockOid) using index tablespace tasind_svc
) tablespace  tas_svc  ;
                	
create sequence mmm_Stock_SEQ  increment by 1 cache 100 start with 1 maxvalue 2147483647 nocycle;
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
	buyPrices varchar2(20),
	buyVolumes varchar2(20),
	seldPrices varchar2(20),
	seldVolumes varchar2(20),
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
	storageOid number(10) not null,
	warrantOid number(10),
	name varchar2(20 char),
	price float(126),
	unit varchar2(20),
	totalPrice float(126),
	createTime date,
        constraint mmm_Storage_PK
		primary key (storageOid) using index tablespace tasind_svc
) tablespace  tas_svc  ;
                	
create sequence mmm_Storage_SEQ  increment by 1 cache 100 start with 1 maxvalue 2147483647 nocycle;

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

create table mmm_Deal (
	realOid number(10) not null,
	code varchar2(4),
	buyStockOid number(10),
	recordHigh number(10),
	sellStockOid number(10),
	sellType number(3),
	diffPrice float(126),
	keepDays number(10),
	revenueRate float(126),
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

create table mmm_Bid (
	bidOid number(10) not null,
	warrantOid number(10),
	name varchar2(20 char),
	price float(126),
	unit varchar2(20),
	bidState number(3),
	totalPrice float(126),
	createTime date,
	updateTime date,
        constraint mmm_Bid_PK
		primary key (bidOid) using index tablespace tasind_svc
) tablespace  tas_svc  ;
                	
create sequence mmm_Bid_SEQ  increment by 1 cache 100 start with 1 maxvalue 2147483647 nocycle;

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
	
        
alter table mmm_Deal add constraint
    mmm_Deal_BuyStock foreign key (buyStockOid)
    references mmm_Stock;
    
create index
	mmm_Deal_BuyStock on mmm_Deal (buyStockOid) tablespace tasind_svc;
	
        
alter table mmm_Deal add constraint
    mmm_Deal_SellStock foreign key (sellStockOid)
    references mmm_Stock;
    
create index
	mmm_Deal_SellStock on mmm_Deal (sellStockOid) tablespace tasind_svc;
	
        
alter table mmm_Bid add constraint
    mmm_Bid_Warrant foreign key (warrantOid)
    references mmm_Warrant;
    
create index
	mmm_Bid_Warrant on mmm_Bid (warrantOid) tablespace tasind_svc;
	
