package com.senior.cyber.tv.dao;

import com.senior.cyber.frmk.metamodel.LiquibaseJavaMigration;

public abstract class LiquibaseMigration extends LiquibaseJavaMigration {

    public LiquibaseMigration() {
        super("/liquibase_output/");
    }
}
