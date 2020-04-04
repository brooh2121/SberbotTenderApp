package com.sberbot.app.dao;


import com.sberbot.app.model.AuctionModel;

import java.util.List;

public interface BotAppDao {

    AuctionModel addAuction (AuctionModel auctionModel);

    List<AuctionModel> getAllAutions();

}
