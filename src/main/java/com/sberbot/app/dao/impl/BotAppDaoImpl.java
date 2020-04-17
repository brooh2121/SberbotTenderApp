package com.sberbot.app.dao.impl;


import com.sberbot.app.dao.BotAppDao;
import com.sberbot.app.dao.mapper.AuctionsMapper;
import com.sberbot.app.model.AuctionModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BotAppDaoImpl implements BotAppDao {

    @Autowired
    @Qualifier("jdbcTemplatePostgreTend")
    JdbcTemplate jdbcTemplatePostgreTend;


    @Override
    public AuctionModel addAuction(AuctionModel auctionModel) {
        String query="insert into public.auctions values(\n" +
                "'"+auctionModel.getAuctionNumber()+"',\n" +
                "'"+auctionModel.getOrgName()+"',\n" +
                "'"+auctionModel.getTenderName()+"',\n" +
                "'"+auctionModel.getPublicDate()+"',\n" +
                "'"+auctionModel.getSum()+"'\n" +
                 ")";
        jdbcTemplatePostgreTend.update(query);
        return auctionModel;
    }

    @Override
    public List<AuctionModel> getAllAutions() {
        String query = "select * from public.auctions a order by to_timestamp(publication_date,'dd.mm.yyyy hh24:mi:ss') desc limit 20";
        return jdbcTemplatePostgreTend.query(query,new AuctionsMapper());
    }

    @Override
    public String getMaxPublicDate() {
        String query = "select max(to_timestamp(publication_date,'DD.MM.YYYY HH24:MI')) from auctions";
        String strResult = jdbcTemplatePostgreTend.queryForObject(query,String.class);
        return strResult;
    }

}
