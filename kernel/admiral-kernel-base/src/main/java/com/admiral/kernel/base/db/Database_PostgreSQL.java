package com.admiral.kernel.base.db;

import com.admiral.kernel.util.Ini;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.util.logging.Level;
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

    /** Data Source	Long Running 	*/
    private DataSource datasourceLongRunning = null;

    public Database_PostgreSQL() {}

    @Override
    public void close() {

    }

    public String getConnectionURL (ADConnection connection)
    {
        //  jdbc:postgresql://hostname:portnumber/databasename?encoding=UNICODE
        StringBuffer sb = new StringBuffer("jdbc:postgresql:");
        sb.append("//").append(connection.getDbHost())
                .append(":").append(connection.getDbPort())
                .append("/").append(connection.getDbName())
                .append("?encoding=UNICODE");
        ad_connection = sb.toString();
        return ad_connection;
    }   //  getConnectionString

    public DataSource getDataSource(ADConnection connection) {
        if(datasourceLongRunning != null) {
            return datasourceLongRunning;
        }
        try{
            if(Ini.isClient()){
                log.warning("Config Hikari Connection Pool Datasource");
                HikariConfig config = new HikariConfig();
                config.setDriverClassName(DRIVER);
                config.setJdbcUrl(getConnectionURL(connection));
                config.setUsername(connection.getDbUid());
                config.setPassword(connection.getDbPwd());
                config.setConnectionTestQuery(DEFAULT_CONNECTION_TEST_QUERY);
                config.setIdleTimeout(0);
                config.setMinimumIdle(15);
                config.setMaximumPoolSize(150);
                config.setPoolName("AdempiereDS");
                config.addDataSourceProperty( "cachePrepStmts" , "true" );
                config.addDataSourceProperty( "prepStmtCacheSize" , "250" );
                config.addDataSourceProperty( "prepStmtCacheSqlLimit" , "2048" );
                datasourceLongRunning = new HikariDataSource(config);;
                log.warning("Starting Client Hikari Connection Pool");
            }
        }
        catch(Exception exception){
            datasourceLongRunning = null;
            log.log(Level.SEVERE, "Application Server does not exist, no is possible to initialize the initialise Hikari Connection Pool", exception);
            exception.printStackTrace();
        }
        return datasourceLongRunning;
    }
}
