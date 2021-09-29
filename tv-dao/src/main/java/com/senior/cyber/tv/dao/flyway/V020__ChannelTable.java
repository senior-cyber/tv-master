package com.senior.cyber.tv.dao.flyway;

import com.senior.cyber.tv.dao.LiquibaseMigration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.Arrays;
import java.util.List;

public class V020__ChannelTable extends LiquibaseMigration {

    public V020__ChannelTable() {
    }

    @Override
    protected List<String> getXmlChecksum() {
        return Arrays.asList("V020__ChannelTable.xml");
    }

    @Override
    protected void doMigrate(NamedParameterJdbcTemplate named) throws Exception {
        updateLiquibase("V020__ChannelTable.xml");
    }

}