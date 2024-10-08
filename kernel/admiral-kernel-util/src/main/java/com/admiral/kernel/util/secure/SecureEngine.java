package com.admiral.kernel.util.secure;

import java.util.Properties;
import java.util.logging.Logger;

public class SecureEngine {
    public static final Logger LOGGER = Logger.getLogger(SecureEngine.class.getName());

    public static SecureEngine s_engine = null;
    private SecureInterface implementation = null;

    private SecureEngine(String className){
        String realClass = className;
        if (realClass == null || realClass.length() == 0)
            realClass = SecureInterface.ADMIRAL_SECURE_DEFAULT;
        Exception cause = null;
        try
        {
            Class clazz = Class.forName(realClass);
            implementation = (SecureInterface)clazz.newInstance();
        }
        catch (Exception e)
        {
            cause = e;
        }
        if(implementation == null){
            String msg = "Could not initialize: " + realClass + " - " + cause.toString()
                    + "\nCheck start script parameter ADMIRAL_SECURE";
            LOGGER.severe(msg);
        }
        LOGGER.info(realClass + " initialized - " + implementation);
    }

    public static void init (Properties ctx){
        if (s_engine == null)
        {
            String className = ctx.getProperty(SecureInterface.ADMIRAL_SECURE);
            s_engine = new SecureEngine(className);
        }
    }

    public static String decrypt(String value){
        if (value == null)
            return null;
        if (s_engine == null)
            init(System.getProperties());
        boolean inQuotes = value.startsWith("'") && value.endsWith("'");
        if (inQuotes)
            value = value.substring(1, value.length()-1);
        String retValue = null;
        if (value.startsWith(SecureInterface.CLEARVALUE_START) && value.endsWith(SecureInterface.CLEARVALUE_END))
            retValue = value.substring(SecureInterface.CLEARVALUE_START.length(), value.length()-SecureInterface.CLEARVALUE_END.length());
        else
            retValue = s_engine.implementation.decrypt(value);
        if (inQuotes)
            return "'" + retValue + "'";
        return retValue;
    }

    public static String encrypt(String value){
        if(value == null || value.isEmpty()){
            return value;
        }
        if (s_engine == null)
            init(System.getProperties());
        boolean inQuotes = value.startsWith("'") && value.endsWith("'");
        if (inQuotes)
            value = value.substring(1, value.length()-1);
        //
        String retValue = s_engine.implementation.encrypt(value);
        if (inQuotes)
            return "'" + retValue + "'";
        return retValue;
    }
}
