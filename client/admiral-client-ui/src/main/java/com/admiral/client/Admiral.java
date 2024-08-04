package com.admiral.client;


import com.admiral.kernel.util.Ini;
import com.admiral.kernel.util.secure.SecureEngine;
import com.admiral.kernel.base.db.ADConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class Admiral
{
    private static final Logger log = LoggerFactory.getLogger(Admiral.class);
    private static final String SYSTEM_NAME = "Admiral";
    private static String ADMIRAL_VERSION = "Release 1.0";
    private static String DATE_VERSION = "2025-01-01";
    private static String DATABASE_VERSION = "2025-01-01";
    private static String s_ImplementationVersion = null;
    private static String s_ImplementationVendor = null;

    static{
        ClassLoader loader = Admiral.class.getClassLoader();
        try(InputStream inputStream  = loader.getResourceAsStream(
                "com" + File.separator + "admiral" + File.separator +
                        "client" + File.separator + "ui" + File.separator +
                        "admiral-version.properties")){
            if(inputStream != null){
                Properties properties = new Properties();
                try{
                    properties.load(inputStream);
                    if (properties.containsKey("MAIN_VERSION"))
                        ADMIRAL_VERSION = properties.getProperty("MAIN_VERSION");
                    if (properties.containsKey("DATE_VERSION"))
                        DATE_VERSION = properties.getProperty("DATE_VERSION");
                    if (properties.containsKey("DB_VERSION"))
                        DATABASE_VERSION = properties.getProperty("DB_VERSION");
                    if (properties.containsKey("IMPLEMENTATION_VERSION"))
                        s_ImplementationVersion = properties.getProperty("IMPLEMENTATION_VERSION");
                    if (properties.containsKey("IMPLEMENTATION_VENDOR"))
                        s_ImplementationVendor = properties.getProperty("IMPLEMENTATION_VENDOR");

                }
                catch (IOException e){}
            }
        }
        catch(Exception e){}
    }

    public static String getName()
    {
        return SYSTEM_NAME;
    }

    private static void validateConnectionDialog(){
        String attributes = Ini.getProperty(Ini.P_CONNECTION);
        if(attributes == null || attributes.isEmpty()){
            attributes = SecureEngine.decrypt(System.getProperty(Ini.P_CONNECTION));
        }
        if(attributes == null || attributes.isEmpty()){
            ADConnection cc = new ADConnection("");
        }
    }

    private static synchronized void startup(boolean isClient) {
        Ini.setClient(isClient);
        Ini.loadProperties(false);
    }

    public static void main( String[] args )
    {
        startup(true);
    }


}
