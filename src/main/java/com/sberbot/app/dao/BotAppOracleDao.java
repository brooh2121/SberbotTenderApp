package com.sberbot.app.dao;

import com.sberbot.app.model.AuctionModel;

public interface BotAppOracleDao {
    String getHelthCheck();
    Long getOraTenderSequence();
    void addAuctionModelToOracle(long seq_id,AuctionModel auctionModel);
}
