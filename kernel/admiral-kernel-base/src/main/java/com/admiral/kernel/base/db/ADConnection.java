package com.admiral.kernel.base.db;

import com.admiral.kernel.base.Admiral;
import com.admiral.kernel.util.Ini;
import com.admiral.kernel.util.secure.SecureEngine;

import javax.sql.DataSource;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ADConnection implements Serializable, Cloneable {
    private static final long serialVersionUID = -3793699874332585444L;
    private static final Logger log = Logger.getLogger(ADConnection.class.getName());

    private static ADConnection ad_cc = null;
    private String ad_apps_host = "MyAppsServer";
    private String ad_name = "Standard";
    private String ad_type = "";
    private String ad_db_host = "MyDBServer";
    private String ad_db_port = "";
    private String ad_db_name = "";
    private String ad_db_uid = "";
    private String ad_db_pwd = "";

    /** Info                */
    private String[] 	m_info = new String[2];

    private AdmiralDatabase ad_db = null;
    private Exception ad_dbException = null;

    private boolean ad_okDB = false;
    private DataSource ad_ds = null;

    public ADConnection(String host){
        if(host != null){
            ad_apps_host = host;
            ad_db_host = host;
        }
    }

    public static ADConnection get(){
        return get(null);
    }

    public static ADConnection get(String ad_apps_host){
        if(ad_cc == null){
            String attributes = Ini.getProperty(Ini.P_CONNECTION);
            if(attributes == null || attributes.isEmpty()){
                attributes = SecureEngine.decrypt(System.getProperty(Ini.P_CONNECTION));
            }

            if(attributes != null && !attributes.isEmpty()){
                ad_cc = new ADConnection(null);
                ad_cc.setAttributes(attributes);
                log.fine(ad_cc.toString());
                Ini.setProperty(Ini.P_CONNECTION, ad_cc.toString());
                // TODO: Ini.saveProperties(Ini.isClient());
            }
        }
        return ad_cc;
    }

    public boolean isDatabaseOK ()
    {
        return ad_okDB;
    } 	//  isDatabaseOK

    /**
     * @param value
     * @return un-escape value
     */
    private String unescape(String value) {
        value = value.replace("&eq;", "=");
        value = value.replace("&comma;", ",");
        return value;
    }

    private void setAttributes(String attributes){
        try{
            attributes = attributes.substring(attributes.indexOf("[") + 1, attributes.length() - 1);
            String[] pairs = attributes.split("[,]");
            for(String pair : pairs){
                String[] pairComponents = pair.split("[=]");
                String key = pairComponents[0];
                String value = pairComponents.length == 2 ? unescape(pairComponents[1]) : "";
                if("name".equalsIgnoreCase(key)){
                    setName(value);
                }
                else if("AppHost".equalsIgnoreCase(key)){
                    setAppsHost(value);
                }
                else if("type".equalsIgnoreCase(key)){
                    setType(value);
                }
                else if("DBhost".equalsIgnoreCase(key)){
                    setDbHost(value);
                }
                else if("DBPort".equalsIgnoreCase(key)){
                    setDbPort(value);
                }
                else if("DBName".equalsIgnoreCase(key)){
                    setDbName(value);
                }
                else if("UID".equalsIgnoreCase(key)){
                    setDbUid(value);
                }
                else if("PWD".equalsIgnoreCase(key)){
                    setDbPwd(value);
                }
            }
        }
        catch(Exception e){
            log.severe(attributes + " - " + e.toString());
        }
    }

    public String toString(){
        StringBuffer sb = new StringBuffer(ad_apps_host);
        sb.append("{").append(ad_db_host)
                .append("-").append(ad_db_name)
                .append("-").append(ad_db_uid)
                .append("}");
        return sb.toString();
    }

    public String toStringDetail ()
    {
        StringBuffer sb = new StringBuffer (ad_apps_host);
        sb.append ("{").append (ad_db_host)
                .append ("-").append (ad_db_name)
                .append ("-").append (ad_db_uid)
                .append ("}");
        //
        Connection conn = getConnection (true,
                Connection.TRANSACTION_READ_COMMITTED);
        if (conn != null)
        {
            try
            {
                DatabaseMetaData dbmd = conn.getMetaData ();
                sb.append("\nDatabase=" + dbmd.getDatabaseProductName ()
                        + " - " + dbmd.getDatabaseProductVersion());
                sb.append("\nDriver  =" + dbmd.getDriverName ()
                        + " - " + dbmd.getDriverVersion ());
                if (isDataSource())
                    sb.append(" - via DS");
                conn.close ();
            }
            catch (Exception e)
            {
            }
        }
        conn = null;
        return sb.toString ();
    } 	//  toStringDetail

    public String getName() {
        return ad_name;
    }

    public void setName (String name)
    {
        this.ad_name = name;
    }	//  setName

    public void setName() {
        this.ad_name = toString();
    }

    public String getAppsHost() {
        return ad_apps_host;
    }

    public void setAppsHost(String ad_apps_host) {
        this.ad_apps_host = ad_apps_host;
    }

    public String getDbHost() {
        return ad_db_host;
    }

    public void setDbHost(String ad_db_host) {
        this.ad_db_host = ad_db_host;
        this.ad_name = toString();
        this.ad_okDB = false;
    }

    public String getDbPort() {
        return ad_db_port;
    }

    public void setDbPort(String ad_db_port) {
        this.ad_db_port = ad_db_port;
        this.ad_okDB = false;
    }

    public String getDbName() {
        return ad_db_name;
    }

    public void setDbName(String ad_db_name) {
        this.ad_db_name = ad_db_name;
        this.ad_name = toString();
        this.ad_okDB = false;
    }

    public String getDbUid() {
        return ad_db_uid;
    }

    public void setDbUid(String ad_db_uid) {
        this.ad_db_uid = ad_db_uid;
        this.ad_name = toString();
        this.ad_okDB = false;
    }

    public String getDbPwd() {
        return ad_db_pwd;
    }

    public void setDbPwd(String ad_db_pwd) {
        this.ad_db_pwd = ad_db_pwd;
        this.ad_okDB = false;
    }

    public String getType() {
        return ad_type;
    }

    public void setType(String type){
        for(int i = 0; i < Databases.DATABASE_NAMES.length; i++){
            if(type.contains(Databases.DATABASE_NAMES[i])){
                ad_type = type;
                ad_okDB = false;
                break;
            }
        }
    }

    public boolean isDataSource ()
    {
        return ad_ds != null;
    } 	//	isDataSource

    public boolean setDataSource(){
        if((ad_ds == null) && Ini.isClient()){
            if(getDatabase() != null){
                ad_ds = getDatabase().getDataSource(this);
            }
        }
        return ad_ds != null;
    }

    public boolean setDataSource(DataSource ds){
        if(ds == null && ad_ds != null){
            getDatabase().close();
        }
        ad_ds = ds;
        return ad_ds != null;
    }

    public AdmiralDatabase getDatabase(){
        if((ad_db != null) && !ad_db.getName().equals(ad_type)){
            ad_db = null;
        }

        if(ad_db == null){
            try{
                for(int i = 0; i < Databases.DATABASE_NAMES.length; i++){
                    if(Databases.DATABASE_NAMES[i].equals(ad_type)){
                        ad_db = (AdmiralDatabase) Databases.DATABASE_CLASSES[i].newInstance();
                        break;
                    }
                }
//                if(ad_db != null){
//                    ad_db.getDataSource(this);
//                }
            }
            catch(Exception e){
                log.severe(e.toString());
            }
        }
        return ad_db;
    }

    /**
     *      Get Transaction Isolation Info
     *      @param transactionIsolation trx iso
     *      @return clear test
     */
    public static String getTransactionIsolationInfo(int transactionIsolation) {

        if (transactionIsolation == Connection.TRANSACTION_NONE) {
            return "NONE";
        }

        if (transactionIsolation == Connection.TRANSACTION_READ_COMMITTED) {
            return "READ_COMMITTED";
        }

        if (transactionIsolation == Connection.TRANSACTION_READ_UNCOMMITTED) {
            return "READ_UNCOMMITTED";
        }

        if (transactionIsolation == Connection.TRANSACTION_REPEATABLE_READ) {
            return "REPEATABLE_READ";
        }

        if (transactionIsolation == Connection.TRANSACTION_READ_COMMITTED) {
            return "SERIALIZABLE";
        }

        return "<?" + transactionIsolation + "?>";

    }		// getTransactionIsolationInfo

    public String getConnectionURL(){
        getDatabase();
        if(ad_db != null){
            return ad_db.getConnectionURL(this);
        }else{
            return "";
        }
    }

    public Connection getConnection(boolean autoCommit, int transactionIsolation){
        Connection connection = null;
        ad_dbException = null;
        ad_okDB = false;

        getDatabase();
        if(ad_db == null){
            ad_dbException = new IllegalStateException("No database connector");
            return null;
        }

        try{
            Exception ee = null;
            try{
                connection = ad_db.getCachedConnection(this, autoCommit, transactionIsolation);
            }
            catch(Exception exception){
                log.severe(exception.getMessage());
                ee = exception;
            }
            //	Verify Connection
            if (connection != null)
            {
                if (connection.getTransactionIsolation() != transactionIsolation)
                    connection.setTransactionIsolation (transactionIsolation);
                if (connection.getAutoCommit() != autoCommit)
                    connection.setAutoCommit (autoCommit);
                ad_okDB = true;
            }
        }
        catch (UnsatisfiedLinkError ule){
            String msg = ule.getLocalizedMessage()
                    + " -> Did you set the LD_LIBRARY_PATH ? - " + getConnectionURL();
            ad_dbException = new Exception(msg);
            log.severe(msg);
        }
        catch (SQLException ex){
            if(connection == null){
                log.log(Level.SEVERE, getConnectionURL() + ", (1) AutoCommit=" + autoCommit + ",TrxIso=" + getTransactionIsolationInfo(transactionIsolation)

                        // + " (" + getDbUid() + "/" + getDbPwd() + ")"
                        + " - " + ex.getMessage());
            }else{
                try{
                    log.severe(getConnectionURL() + ", (2) AutoCommit=" + connection.getAutoCommit() + "->" + autoCommit + ", TrxIso=" + getTransactionIsolationInfo(connection.getTransactionIsolation()) + "->" + getTransactionIsolationInfo(transactionIsolation)

                            // + " (" + getDbUid() + "/" + getDbPwd() + ")"
                            + " - " + ex.getMessage());
                }
                catch (Exception ee){
                    log.severe(getConnectionURL() + ", (3) AutoCommit=" + autoCommit + ", TrxIso=" + getTransactionIsolationInfo(transactionIsolation)

                            // + " (" + getDbUid() + "/" + getDbPwd() + ")"
                            + " - " + ex.getMessage());
                }
            }
        }
        catch(Exception ex){
            ad_dbException = ex;
            log.log(Level.SEVERE, getConnectionURL(), ex);
        }
        return connection;
    }

    public void readInfo(Connection conn) throws SQLException {
        DatabaseMetaData dbmd = conn.getMetaData();
        m_info[0] = "Databases=" + dbmd.getDatabaseProductName() + " - " + dbmd.getDatabaseProductVersion();
        m_info[0] = m_info[0].replace('\n', ' ');
        m_info[1] = "Drive =" + dbmd.getDriverName() + " - " + dbmd.getDriverVersion();
        if(isDataSource()){
            m_info[1] += " - via Datasouurce";
        }
        m_info[1] = m_info[1].replace('\n', ' ');
        log.config(m_info[0] + " - " + m_info[1]);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        ADConnection c = (ADConnection)super.clone();
        String[] info = new String[2];
        info[0] = m_info[0];
        info[1] = m_info[1];
        c.m_info = info;
        return c;
    }

    public Exception testDatabase(){
        if(ad_ds != null){
            getDatabase().close();
        }
        ad_ds = null;
        setDataSource();

        Connection conn = getConnection(true, Connection.TRANSACTION_READ_COMMITTED);
        if(conn != null){
            try{
                readInfo(conn);
                conn.close();
            }
            catch(Exception ex){
                log.log(Level.SEVERE, ex.toString());
                return ex;
            }
        }
        return ad_dbException;
    }



    public static void main(String[] args) {
        Admiral.startup(true);
        System.out.println("Connection = ");

        System.out.println(Ini.getProperty(Ini.P_CONNECTION));
        ADConnection cc = ADConnection.get();
        System.out.println(">> " + cc.toStringDetail());

        Connection con = cc.getConnection(false, Connection.TRANSACTION_READ_COMMITTED);
        new ADConnectionDialog(cc);
    }
}
