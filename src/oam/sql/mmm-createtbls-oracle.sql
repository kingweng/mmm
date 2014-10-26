

create table mmm_SvcCfg (
	svcName varchar2(20) not null,
	simMode number(1),
	retryIntvl number(10),
	retryLimit number(10),
	dailyImportTime varchar2(20),
        constraint mmm_SvcCfg_PK
		primary key (svcName) using index tablespace tasind_svc
) tablespace  tas_svc  ;

create table mmm_Stock (
	stockOid number(10) not null,
	name varchar2(20 char),
	code varchar2(4),
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

create table mmm_Warrant (
	warrantOid number(10) not null,
	name varchar2(20 char),
	code varchar2(20),
	stockOid number(10),
	price float(126),
	priChange float(126),
	changePer float(126),
	volume number(20),
	createTime date,
	updateTime date,
        constraint mmm_Warrant_PK
		primary key (warrantOid) using index tablespace tasind_svc
) tablespace  tas_svc  ;
                	
create sequence mmm_Warrant_SEQ  increment by 1 cache 100 start with 1 maxvalue 2147483647 nocycle;

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

create table mmm_WatchStock (
	watchOid number(10) not null,
	name varchar2(20 char),
	code varchar2(4),
	createTime date,
    	constraint mmm_WatchStock_Code
		unique (code)  using index tablespace tasind_svc,
        constraint mmm_WatchStock_PK
		primary key (watchOid) using index tablespace tasind_svc
) tablespace  tas_svc  ;
                	
create sequence mmm_WatchStock_SEQ  increment by 1 cache 100 start with 1 maxvalue 2147483647 nocycle;

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
                	
--  virtual tables
--  virtual tables 

        
alter table mmm_Warrant add constraint
    mmm_Warrant_Stock foreign key (stockOid)
    references mmm_Stock;
    
create index
	mmm_Warrant_Stock on mmm_Warrant (stockOid) tablespace tasind_svc;
	
        
alter table mmm_Storage add constraint
    mmm_Storage_Warrant foreign key (warrantOid)
    references mmm_Warrant;
    
create index
	mmm_Storage_Warrant on mmm_Storage (warrantOid) tablespace tasind_svc;
	
        
alter table mmm_Bid add constraint
    mmm_Bid_Warrant foreign key (warrantOid)
    references mmm_Warrant;
    
create index
	mmm_Bid_Warrant on mmm_Bid (warrantOid) tablespace tasind_svc;
	
