package com.admiral.kernel.base.db;

import javax.sql.DataSource;
import java.io.Serializable;
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

    public void setName(String ad_name) {
        this.ad_name = ad_name;
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
}
