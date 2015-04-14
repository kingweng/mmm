

create table mmm_SvcCfg (
	svcName varchar(20) not null,
	simMode boolean,
	retryIntvl integer,
	retryLimit integer,
	dailyImportTime varchar(20),
	revenueSellTime varchar(20),
	kBreakSellTime varchar(20),
	lastBidTime varchar(20),
	tickInterval integer,
	tickTimeout integer,
	tickTimeRange varchar(20),
	closedRateOfK integer,
	kList varchar(20),
        constraint mmm_SvcCfg_PK
		primary key (svcName) 
)   ENGINE=InnoDB ;

create table mmm_WarrantCfg (
	svcName varchar(20) not null,
	remainingDays integer,
	warrantDiffPrice double precision,
	qualifiedDealers varchar(256),
	minLeverage integer,
	maxDiffRate double precision,
	warrantRetryIntvl integer,
	buyRetryLimit integer,
	sellRetryLimit integer,
        constraint mmm_WarrantCfg_PK
		primary key (svcName) 
)   ENGINE=InnoDB ;

create table mmm_DealCfg (
	svcName varchar(20) not null,
	dayCount integer,
	overTimes integer,
	kBreak integer,
	winChance integer,
	revenueRate double precision,
        constraint mmm_DealCfg_PK
		primary key (svcName) 
)   ENGINE=InnoDB ;

create table mmm_BalanceCfg (
	svcName varchar(20) not null,
	balance integer,
	singleBid integer,
        constraint mmm_BalanceCfg_PK
		primary key (svcName) 
)   ENGINE=InnoDB ;

create table mmm_Stock (
	code varchar(4) not null,
	name varchar(20),
	dateStr varchar(8) not null,
	stockType integer,
	price double precision,
	startPrice double precision,
	lowestPrice double precision,
	highestPrice double precision,
	changePrice double precision,
	monthStr varchar(8),
	volume bigint,
	createTime timestamp  null,
        constraint mmm_Stock_PK
		primary key (code, dateStr) 
)   ENGINE=InnoDB ;
create  index
	mmm_Stock_CodeMonth on mmm_Stock (code, monthStr) ;
create  index
	mmm_Stock_Month on mmm_Stock (monthStr) ;

create table mmm_Tick (
	tickOid integer not null auto_increment,
	code varchar(20),
	price double precision,
	tickVolume integer,
	totalVolume integer,
	timestamp bigint,
	createTime timestamp  null,
	buyPrices varchar(100),
	buyVolumes varchar(100),
	seldPrices varchar(100),
	seldVolumes varchar(100),
        constraint mmm_Tick_PK
		primary key (tickOid) 
)   ENGINE=InnoDB ;
create  index
	mmm_Tick_Code on mmm_Tick (code) ;
create  index
	mmm_Tick_Time on mmm_Tick (createTime) ;

create table mmm_Warrant (
	warrantOid integer not null auto_increment,
	name varchar(20),
	code varchar(20),
	targetCode varchar(20),
	price double precision,
	buyPrice double precision,
	buyVolume integer,
	sellPrice double precision,
	sellVolume integer,
	diffPrices double precision,
	idealDiffPrices double precision,
	remainingDays integer,
	leverage double precision,
	biv double precision,
	createTime timestamp  null,
        constraint mmm_Warrant_PK
		primary key (warrantOid) 
)   ENGINE=InnoDB ;
create  index
	mmm_Warrant_Code on mmm_Warrant (code) ;
create  index
	mmm_Warrant_Target on mmm_Warrant (targetCode) ;

create table mmm_Storage (
	code varchar(20) not null,
	warrantOid integer,
	name varchar(20),
	price double precision,
	targetPrice double precision,
	unit integer,
	amount integer,
	createTime timestamp  null,
        constraint mmm_Storage_PK
		primary key (code) 
)   ENGINE=InnoDB ;

create table mmm_StorageLog (
	logOid integer not null auto_increment,
	warrantOid integer,
	name varchar(20),
	code varchar(20),
	buyPrice double precision,
	sellPrice double precision,
	targetCode varchar(20),
	targetPrice double precision,
	unit integer,
	amount integer,
	revenue integer,
	logTime timestamp  null,
	buyTime timestamp  null,
	keepDays integer,
        constraint mmm_StorageLog_PK
		primary key (logOid) 
)   ENGINE=InnoDB ;
create  index
	mmm_StorageLog_Code on mmm_StorageLog (targetCode) ;
create  index
	mmm_StorageLog_Time on mmm_StorageLog (logTime) ;
create  index
	mmm_StorageLog_Warrant on mmm_StorageLog (warrantOid) ;

create table mmm_DailyCsvReq (
	reqOid integer not null auto_increment,
	code varchar(6),
	name varchar(20),
	stockType integer,
	pullReqState integer,
	csvUrl varchar(256),
	csvFilePath varchar(256),
	monthStr varchar(6),
	retryCount integer,
	errMsg varchar(512),
	createTime timestamp  null,
	updateTime timestamp  null,
        constraint mmm_DailyCsvReq_PK
		primary key (reqOid) 
)   ENGINE=InnoDB ;

create table mmm_StockGroup (
	code varchar(4) not null,
	name varchar(20),
	stockType integer,
	createTime timestamp  null,
        constraint mmm_StockGroup_PK
		primary key (code) 
)   ENGINE=InnoDB ;

create table mmm_BuyingStock (
	code varchar(4) not null,
        constraint mmm_BuyingStock_PK
		primary key (code) 
)   ENGINE=InnoDB ;

create table mmm_Deal (
	realOid integer not null auto_increment,
	code varchar(4),
	buyDateStr varchar(20),
	buyPrice double precision,
	recordHigh integer,
	sellDateStr varchar(20),
	sellPrice double precision,
	sellType integer,
	diffPrice double precision,
	keepDays integer,
	revenueRate double precision,
    	constraint mmm_Deal_BuyStock
		unique (code, buyDateStr) ,
    	constraint mmm_Deal_SellStock
		unique (code, sellDateStr) ,
        constraint mmm_Deal_PK
		primary key (realOid) 
)   ENGINE=InnoDB ;
create  index
	mmm_Deal_Code on mmm_Deal (code) ;

create table mmm_DealStats (
	statsOid integer not null auto_increment,
	code varchar(20),
	succCnt integer,
	failCnt integer,
	winChance integer,
	revenueRate double precision,
	avgKeepDays double precision,
    	constraint mmm_DealStats_Code
		unique (code) ,
        constraint mmm_DealStats_PK
		primary key (statsOid) 
)   ENGINE=InnoDB ;

create table mmm_QueryJob (
	jobOid integer not null auto_increment,
	action integer,
	code varchar(20),
	price double precision,
	amount integer,
	retryCnt integer,
	jobState integer,
	failMsg varchar(512),
	createTime timestamp  null,
	updateTime timestamp  null,
        constraint mmm_QueryJob_PK
		primary key (jobOid) 
)   ENGINE=InnoDB ;
create  index
	mmm_QueryJob_Code on mmm_QueryJob (code) ;

create table mmm_BidReq (
	bidOid integer not null auto_increment,
	warrantOid integer,
	jobOid integer,
	name varchar(20),
	price double precision,
	applyUnit integer,
	remainUnit integer,
	action integer,
	bidState integer,
	amount integer,
	failMsg varchar(512),
	warrantTickOid integer,
	createTime timestamp  null,
	updateTime timestamp  null,
        constraint mmm_BidReq_PK
		primary key (bidOid) 
)   ENGINE=InnoDB ;
create  index
	mmm_BidReq_State on mmm_BidReq (bidState) ;

create table mmm_Bias (
	biasOid integer not null auto_increment,
	code varchar(20),
	dateStr varchar(20),
	value double precision,
    	constraint mmm_Bias_CodeDate
		unique (code, dateStr) ,
        constraint mmm_Bias_PK
		primary key (biasOid) 
)   ENGINE=InnoDB ;
create  index
	mmm_Bias_Code on mmm_Bias (code) ;

create table mmm_WarrantTick (
	tickOid integer not null auto_increment,
	code varchar(20),
	price double precision,
	tickVolume integer,
	totalVolume integer,
	timestamp bigint,
	createTime timestamp  null,
	buyPrices varchar(100),
	buyVolumes varchar(100),
	seldPrices varchar(100),
	seldVolumes varchar(100),
        constraint mmm_WarrantTick_PK
		primary key (tickOid) 
)   ENGINE=InnoDB ;
create  index
	mmm_WarrantTick_Code on mmm_WarrantTick (code) ;
create  index
	mmm_WarrantTick_Time on mmm_WarrantTick (createTime) ;

create table mmm_Drive (
	driveOid integer not null auto_increment,
	license varchar(20),
	driveTime timestamp  null,
	poison varchar(50),
	weight double precision,
	unit varchar(20),
        constraint mmm_Drive_PK
		primary key (driveOid) 
)   ENGINE=InnoDB ;
create  index
	mmm_Drive_License on mmm_Drive (license) ;

create table mmm_License (
	licenseOid integer not null auto_increment,
	license varchar(20),
	startTime timestamp  null,
	endTime timestamp  null,
	poison varchar(256),
        constraint mmm_License_PK
		primary key (licenseOid) 
)   ENGINE=InnoDB ;
create  index
	mmm_License_License on mmm_License (license) ;

create table mmm_Training (
	trainingOid integer not null auto_increment,
	trainingNum varchar(20),
	pid varchar(20),
	category varchar(20),
	startTime timestamp  null,
	endTime timestamp  null,
        constraint mmm_Training_PK
		primary key (trainingOid) 
)   ENGINE=InnoDB ;
create  index
	mmm_Training_Num on mmm_Training (trainingNum) ;
                	
--  virtual tables
--  virtual tables 

        
alter table mmm_Storage add constraint
    mmm_Storage_Warrant foreign key (warrantOid)
    references mmm_Warrant(warrantOid);
    
create index
	mmm_BidReq_Warrant on mmm_BidReq (warrantOid) ;
	
create index
	mmm_BidReq_Tick on mmm_BidReq (warrantTickOid) ;
	
        
alter table mmm_BidReq add constraint
    mmm_BidReq_Job foreign key (jobOid)
    references mmm_QueryJob(jobOid);
    
