

create table mmm_SvcCfg (
	svcName varchar(20) not null,
	simMode boolean,
	retryIntvl integer,
	retryLimit integer,
	dailyImportTime varchar(20),
	tickInterval integer,
	tickTimeRange varchar(20),
        constraint mmm_SvcCfg_PK
		primary key (svcName) 
)   ENGINE=InnoDB ;

create table mmm_ReportParams (
	svcName varchar(20) not null,
	dayCount integer,
	overTimes integer,
        constraint mmm_ReportParams_PK
		primary key (svcName) 
)   ENGINE=InnoDB ;

create table mmm_Stock (
	stockOid integer not null auto_increment,
	name varchar(20),
	code varchar(4),
	price double precision,
	startPrice double precision,
	lowestPrice double precision,
	highestPrice double precision,
	changePrice double precision,
	dateStr varchar(8),
	monthStr varchar(8),
	volume bigint,
	createTime timestamp  null,
    	constraint mmm_Stock_CodeDate
		unique (code, dateStr) ,
        constraint mmm_Stock_PK
		primary key (stockOid) 
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
	buyPrices varchar(20),
	buyVolumes varchar(20),
	seldPrices varchar(20),
	seldVolumes varchar(20),
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
	stockOid integer,
	price double precision,
	priChange double precision,
	changePer double precision,
	volume bigint,
	createTime timestamp  null,
	updateTime timestamp  null,
        constraint mmm_Warrant_PK
		primary key (warrantOid) 
)   ENGINE=InnoDB ;

create table mmm_Storage (
	storageOid integer not null auto_increment,
	warrantOid integer,
	name varchar(20),
	price double precision,
	unit varchar(20),
	totalPrice double precision,
	createTime timestamp  null,
        constraint mmm_Storage_PK
		primary key (storageOid) 
)   ENGINE=InnoDB ;

create table mmm_DailyCsvReq (
	reqOid integer not null auto_increment,
	code varchar(6),
	name varchar(20),
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

create table mmm_WatchStock (
	watchOid integer not null auto_increment,
	name varchar(20),
	code varchar(4),
	createTime timestamp  null,
    	constraint mmm_WatchStock_Code
		unique (code) ,
        constraint mmm_WatchStock_PK
		primary key (watchOid) 
)   ENGINE=InnoDB ;

create table mmm_Bid (
	bidOid integer not null auto_increment,
	warrantOid integer,
	name varchar(20),
	price double precision,
	unit varchar(20),
	bidState integer,
	totalPrice double precision,
	createTime timestamp  null,
	updateTime timestamp  null,
        constraint mmm_Bid_PK
		primary key (bidOid) 
)   ENGINE=InnoDB ;

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
                	
--  virtual tables
--  virtual tables 

        
alter table mmm_Warrant add constraint
    mmm_Warrant_Stock foreign key (stockOid)
    references mmm_Stock(stockOid);
    
        
alter table mmm_Storage add constraint
    mmm_Storage_Warrant foreign key (warrantOid)
    references mmm_Warrant(warrantOid);
    
        
alter table mmm_Bid add constraint
    mmm_Bid_Warrant foreign key (warrantOid)
    references mmm_Warrant(warrantOid);
    
