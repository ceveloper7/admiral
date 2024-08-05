package com.admiral.kernel.base.db;

import java.util.logging.Logger;

public class Database_PostgreSQL implements AdmiralDatabase{
    private static final Logger log = Logger.getLogger(Database_PostgreSQL.class.getName());

    private org.postgresql.Driver ad_driver = null;
    private static final String DRIVER = "org.postgresql.Driver";
    private static final int DEFAULT_PORT = 5432;

    private String ad_connection;
    private String ad_dbName = null;
    private String ad_userName = null;
    private String ad_connectionUrl;

    public Database_PostgreSQL() {}

    @Override
    public void close() {

    }
}
