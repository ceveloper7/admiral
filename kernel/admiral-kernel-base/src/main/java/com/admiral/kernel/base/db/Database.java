package com.admiral.kernel.base.db;

import java.util.logging.Logger;

public class Database {
    public static final Logger log = Logger.getLogger(Database.class.getName());

    public static int CONNECTION_TIMEOUT = 10;
    public static String DATABASE_POSTGRESQL = "PostgreSQL";
    public static String DATABASE_MARIADB = "MariaDB";
    public static String DATABASE_SQLSERVER = "SQLServer";

    public static String[] DATABASE_NAMES = new String[]{
            DATABASE_POSTGRESQL,
    };

    public static Class<?>[] DATABASE_CLASSES = new Class<?>[]{
            Database_PostgreSQL.class,
    };

    /**
     *
     * Get Database by id
     * @return Database
     * @throws Exception
     */
    public static AdmiralDatabase getDatabase (String type)
            throws Exception
    {
        AdmiralDatabase db = null;
        for (int i = 0; i < Database.DATABASE_NAMES.length; i++)
        {
            if (Database.DATABASE_NAMES[i].equals (type))
            {
                db = (AdmiralDatabase) Database.DATABASE_CLASSES[i].
                        newInstance ();
                break;
            }
        }
        return db;
    }

    public static AdmiralDatabase getDatabaseFromUrl(String url){
        if(url == null){
            log.severe("No Database URL provided");
            return null;
        }
        if(url.indexOf("postgresql") != -1){
            return new Database_PostgreSQL();
        }
        log.severe("No Database URL provided for " + url);
        return null;
    }
}
