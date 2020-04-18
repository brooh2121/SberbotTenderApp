package com.sberbot.app.dao.impl;

import com.sberbot.app.dao.BotAppOracleDao;
import com.sberbot.app.model.AuctionModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;


@Repository
public class BotAppOracleDaoImpl implements BotAppOracleDao {

    String constTenderPlace = "SBERBANK-AST";
    String dummyUrl1 = "DUMMY_URL";
    String dummyUrl2 = "DUMMY_URL";
    String botName = "SberBot - Munin";


    @Autowired
    @Qualifier("jdbcTemplateOracleTend")
    JdbcTemplate jdbcTemplateOracleTend;

    @Override
    public String getHelthCheck() {
        String query = "select 1 from dual";
        return jdbcTemplateOracleTend.queryForObject(query,String.class);
    }

    @Override
    public Long getOraTenderSequence() {
        String query = "select tend.sq_tender_pk.nextval from dual";
        return jdbcTemplateOracleTend.queryForObject(query,Long.class);
    }

    @Override
    public void addAuctionModelToOracle(long seq_id, AuctionModel auctionModel) {
        Double sumDouble = Double.parseDouble(auctionModel.getSum().replace(" ",""));
        //DateTimeFormatter df = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        //LocalDateTime publicDate = LocalDateTime.parse(auctionModel.getPublicDate(),df);
        //LocalDateTime tenderBegDate = LocalDateTime.parse(auctionModel.getTenderBegDate(),df);
        //LocalDateTime tenderEndDate = LocalDateTime.parse(auctionModel.getTenderEndDate(),df);
        //String str = "'\"to_date('"+auctionModel.getPublicDate()+"',\"dd.mm.yyyy hh24:mi\"),\n";
        String query = "insert into tend.tendera values ("+seq_id+",'"+auctionModel.getAuctionNumber()+"',"+sumDouble+",'"+auctionModel.getOrgName()+"', null,'"+auctionModel.getTenderName()+"','"+auctionModel.getTenderType()+"','"+constTenderPlace+"','"+dummyUrl1+"','"+dummyUrl2+"',to_date('"+auctionModel.getPublicDate()+"',\'dd.mm.yyyy hh24:mi\'),to_date('"+auctionModel.getTenderBegDate()+"',\'dd.mm.yyyy hh24:mi\'),to_date('"+auctionModel.getTenderEndDate()+"',\'dd.mm.yyyy hh24:mi\'),null,'"+botName+"',sysdate,sysdate)";
        jdbcTemplateOracleTend.update(query);
    }

}
