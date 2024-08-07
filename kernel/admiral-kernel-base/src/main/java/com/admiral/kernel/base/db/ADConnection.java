package com.admiral.kernel.base.db;

import com.admiral.kernel.util.Ini;
import com.admiral.kernel.util.secure.SecureEngine;

import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
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
        for(int i = 0; i < Database.DATABASE_NAMES.length; i++){
            if(type.contains(Database.DATABASE_NAMES[i])){
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

    public void readInfo(Connection conn) throws SQLException {
        DatabaseMetaData dbmd = conn.getMetaData();
        m_info[0] = "Database=" + dbmd.getDatabaseProductName() + " - " + dbmd.getDatabaseProductVersion();
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
}
