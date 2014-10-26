
alter table mmm_Warrant drop constraint mmm_Warrant_Stock;

alter table mmm_Storage drop constraint mmm_Storage_Warrant;

alter table mmm_Bid drop constraint mmm_Bid_Warrant;



drop table mmm_SvcCfg;


drop sequence mmm_Stock_SEQ;
drop table mmm_Stock;


drop sequence mmm_Warrant_SEQ;
drop table mmm_Warrant;


drop sequence mmm_Storage_SEQ;
drop table mmm_Storage;


drop sequence mmm_DailyCsvReq_SEQ;
drop table mmm_DailyCsvReq;


drop sequence mmm_WatchStock_SEQ;
drop table mmm_WatchStock;


drop sequence mmm_Bid_SEQ;
drop table mmm_Bid;


/*  virtual tables */
/*  virtual tables */
