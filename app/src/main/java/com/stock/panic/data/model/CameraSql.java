package com.stock.panic.data.model;

public class CameraSql {
    public String SQL_CREATE_ENTRIES;
    public String SQL_DELETE_ENTRIES;
    public String DATABASE_NAME;
    public String COLUMN_HASH;
    public String COLUMN_ID;
    public String TABLE_NAME;
    public String SQL_CLEAR_ALL_DATA;
    public String COLUMN_CONTA_ID;
    public String COLUMN_USER_ID;


    public CameraSql(){

        this.COLUMN_CONTA_ID = "conta_id";
        this.COLUMN_USER_ID = "user_id";
        this.COLUMN_HASH = "tk";
        this.COLUMN_ID = "_id";
        this.TABLE_NAME = "token";
        this.SQL_CREATE_ENTRIES = "CREATE TABLE IF NOT EXISTS " + getTableName() + " (" +
                getColumnId()  + " INTEGER PRIMARY KEY," +
                getColumnHash() + " TEXT, " +
                getColumnContaId() + " TEXT, " +
                getColumnUserId() + " TEXT)";

        this.SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + getTableName();
        this.DATABASE_NAME= "panic.db";
        this.SQL_CLEAR_ALL_DATA = "DELETE FROM " + getTableName() + ";";

    }


    public String getDatabaseName(){

        return DATABASE_NAME;
    }

    public String getSqlCreateTables(){
        return SQL_CREATE_ENTRIES;
    }

    public String getSqlDeleteTable(){
        return SQL_DELETE_ENTRIES;
    }

    public String getColumnHash(){
        return COLUMN_HASH;
    }

    public String getColumnId(){
        return COLUMN_ID;
    }

    public String getTableName(){

        return TABLE_NAME;
    }

    public String getSqlTruncateTable(){

        return SQL_CLEAR_ALL_DATA;
    }
    public String getColumnContaId(){

        return COLUMN_CONTA_ID;

    }

    public String getColumnUserId(){

        return COLUMN_USER_ID;
    }
}
