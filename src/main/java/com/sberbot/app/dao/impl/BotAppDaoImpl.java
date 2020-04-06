package com.sberbot.app.dao.impl;


import com.sberbot.app.dao.BotAppDao;
import com.sberbot.app.dao.mapper.AuctionsMapper;
import com.sberbot.app.model.AuctionModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BotAppDaoImpl implements BotAppDao {

    @Autowired
    JdbcTemplate jdbcTemplate;


    @Override
    public AuctionModel addAuction(AuctionModel auctionModel) {
        String query="insert into public.auctions values(\n" +
                "'"+auctionModel.getAuctionNumber()+"',\n" +
                "'"+auctionModel.getOrgName()+"',\n" +
                "'"+auctionModel.getTenderName()+"',\n" +
                "'"+auctionModel.getPublicDate()+"',\n" +
                "'"+auctionModel.getSum()+"'\n" +
                 ")";
        jdbcTemplate.update(query);
        return auctionModel;
    }

    @Override
    public List<AuctionModel> getAllAutions() {
        String query = "select * from public.auctions a order by to_timestamp(publication_date,'dd.mm.yyyy hh24:mi:ss') desc";
        return jdbcTemplate.query(query,new AuctionsMapper());
    }
}
