package com.sberbot.app.dao.impl;

import com.sberbot.app.dao.BotAppOracleDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class BotAppOracleDaoImpl implements BotAppOracleDao {

    @Autowired
    @Qualifier("jdbcTemplateOracleTend")
    JdbcTemplate jdbcTemplateOracleTend;

    @Override
    public String getHelthCheck() {
        String query = "select 1 from dual";
        return jdbcTemplateOracleTend.queryForObject(query,String.class);
    }
}
