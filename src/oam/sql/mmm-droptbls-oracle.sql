
alter table mmm_Storage drop constraint mmm_Storage_Warrant;

alter table mmm_Deal drop constraint mmm_Deal_BuyStock;

alter table mmm_Deal drop constraint mmm_Deal_SellStock;

alter table mmm_Bid drop constraint mmm_Bid_Warrant;



drop table mmm_SvcCfg;


drop table mmm_ReportParams;


drop sequence mmm_Stock_SEQ;
drop table mmm_Stock;


drop sequence mmm_Tick_SEQ;
drop table mmm_Tick;


drop sequence mmm_Warrant_SEQ;
drop table mmm_Warrant;


drop sequence mmm_Storage_SEQ;
drop table mmm_Storage;


drop sequence mmm_DailyCsvReq_SEQ;
drop table mmm_DailyCsvReq;


drop table mmm_StockGroup;


drop sequence mmm_Deal_SEQ;
drop table mmm_Deal;


drop sequence mmm_DealStats_SEQ;
drop table mmm_DealStats;


drop sequence mmm_Bid_SEQ;
drop table mmm_Bid;


drop sequence mmm_Drive_SEQ;
drop table mmm_Drive;


drop sequence mmm_License_SEQ;
drop table mmm_License;


drop sequence mmm_Training_SEQ;
drop table mmm_Training;


/*  virtual tables */
/*  virtual tables */
