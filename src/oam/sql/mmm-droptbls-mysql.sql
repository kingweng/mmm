
alter table mmm_Storage drop foreign key mmm_Storage_Warrant;

alter table mmm_Deal drop foreign key mmm_Deal_BuyStock;

alter table mmm_Deal drop foreign key mmm_Deal_SellStock;

alter table mmm_Bid drop foreign key mmm_Bid_Warrant;



drop table mmm_SvcCfg;


drop table mmm_ReportParams;


drop table mmm_Stock;


drop table mmm_Tick;


drop table mmm_Warrant;


drop table mmm_Storage;


drop table mmm_DailyCsvReq;


drop table mmm_StockGroup;


drop table mmm_Deal;


drop table mmm_DealStats;


drop table mmm_Bid;


drop table mmm_Drive;


drop table mmm_License;


drop table mmm_Training;


/*  virtual tables */
/*  virtual tables */
