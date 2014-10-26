

create table mmm_SvcCfg (
	svcName varchar(20) not null,
	simMode boolean,
	retryIntvl integer,
	retryLimit integer,
	dailyImportTime varchar(20),
        constraint mmm_SvcCfg_PK
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
    
