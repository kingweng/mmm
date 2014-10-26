
alter table mmm_Warrant drop foreign key mmm_Warrant_Stock;

alter table mmm_Storage drop foreign key mmm_Storage_Warrant;

alter table mmm_Bid drop foreign key mmm_Bid_Warrant;



drop table mmm_SvcCfg;


drop table mmm_Stock;


drop table mmm_Warrant;


drop table mmm_Storage;


drop table mmm_DailyCsvReq;


drop table mmm_WatchStock;


drop table mmm_Bid;


/*  virtual tables */
/*  virtual tables */
