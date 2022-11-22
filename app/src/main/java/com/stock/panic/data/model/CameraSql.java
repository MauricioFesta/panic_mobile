package com.stock.panic.data.model;

public class CameraSql {
    public String SQL_CREATE_ENTRIES;
    public String SQL_DELETE_ENTRIES;
    public String DATABASE_NAME;
    public String COLUMN_HASH;
    public String COLUMN_ID;
    public String TABLE_NAME;


    public CameraSql(){
        this.COLUMN_HASH = "tk";
        this.COLUMN_ID = "_id";
        this.TABLE_NAME = "token";
        this.SQL_CREATE_ENTRIES = "CREATE TABLE " + getTableName() + " (" +
                getColumnId()  + " INTEGER PRIMARY KEY," +
                getColumnHash() + " TEXT)";

        this.SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + getTableName();
        this.DATABASE_NAME= "panic.db";

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
}
