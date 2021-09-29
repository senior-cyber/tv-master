package com.senior.cyber.tv.dao.flyway;

import com.senior.cyber.tv.dao.LiquibaseMigration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.Arrays;
import java.util.List;

public class V021__ShowTable extends LiquibaseMigration {

    public V021__ShowTable() {
    }

    @Override
    protected List<String> getXmlChecksum() {
        return Arrays.asList("V021__ShowTable.xml");
    }

    @Override
    protected void doMigrate(NamedParameterJdbcTemplate named) throws Exception {
        updateLiquibase("V021__ShowTable.xml");
    }

}