package com.admiral.kernel.base;

import com.admiral.kernel.base.db.ADConnection;
import com.admiral.kernel.base.db.ADConnectionDialog;
import com.admiral.kernel.base.db.DB;
import com.admiral.kernel.util.Ini;
import com.admiral.kernel.util.secure.SecureEngine;

import java.util.logging.Logger;

public class Admiral {
    private static final Logger log = Logger.getLogger(Admiral.class.getName());

    private static void validateConnectionDialog(){
        String attributes = Ini.getProperty(Ini.P_CONNECTION);
        if(attributes == null || attributes.isEmpty()){
            attributes = SecureEngine.decrypt(System.getProperty(Ini.P_CONNECTION));
        }
        if(attributes == null || attributes.isEmpty()){
            ADConnection cc = new ADConnection("");
            ADConnectionDialog ccd = new ADConnectionDialog(cc);
            cc = ccd.getConnection();
            if(!cc.isDatabaseOK() && !ccd.isCancel()){}
        }
    }

    public static boolean startupEnvironment(boolean isClient){
        startup(isClient);
        if(!DB.isConnected()){
            log.severe("No Database");
            return false;
        }

        return true;
    }

    public static synchronized boolean startup(boolean isClient){
        Ini.setClient(isClient);
        Ini.loadProperties(false);

        ADConnection cc = ADConnection.get();
        DB.setDBTarget(cc);

        if(isClient){
            return false;
        }

        return startupEnvironment(isClient);
    }

    public static void main(String[] args) {
        startup(true);
    }
}
