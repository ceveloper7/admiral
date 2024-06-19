package com.admiral.kernel.base.db;

public interface AdmiralDatabase {
    public static final int LOCK_TIMEOUT = 60;
    public static final int CMD_CREATE_USER = 0;
    public static final int CMD_CREATE_DATABASE = 1;
    public static final int CMD_DELETE_DATABASE = 2;

    public static final String DEFAULT_CONNECTION_TEST_QUERY = "SELECT version FROM ad_system";
}
