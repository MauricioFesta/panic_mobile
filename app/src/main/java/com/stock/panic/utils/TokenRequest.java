package com.stock.panic.utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.stock.panic.data.model.CameraSql;
import com.stock.panic.ui.camera.SqLite;

import java.util.ArrayList;
import java.util.List;

public class TokenRequest {

    private SqLite sql = null;
    private CameraSql cameraSql;

    public String getToken(Context contex){

        sql = new SqLite(contex);
        cameraSql = new CameraSql();

        SQLiteDatabase db = sql.getReadableDatabase();

        String[] projection = {
                cameraSql.getColumnId(),
                cameraSql.getColumnHash()
        };

        String selection = cameraSql.getColumnId() + " = ?";
        String[] selectionArgs = {"1"};
        String sortOrder = cameraSql.getColumnId() + " DESC";

        Cursor cursor = db.query(
                cameraSql.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder

        );

        List itemIds = new ArrayList<>();
        while(cursor.moveToNext()) {
            String itemId = cursor.getString(1);
            itemIds.add(itemId);
        }

        cursor.close();

        if(itemIds.isEmpty()){
            return null;
        }
        return (String) itemIds.get(0);
    }

    public String getContaId(Context contex){

        sql = new SqLite(contex);
        cameraSql = new CameraSql();

        SQLiteDatabase db = sql.getReadableDatabase();

        String[] projection = {
                cameraSql.getColumnId(),
                cameraSql.getColumnHash(),
                cameraSql.getColumnContaId()
        };

        String selection = cameraSql.getColumnId() + " = ?";
        String[] selectionArgs = {"1"};
        String sortOrder = cameraSql.getColumnId() + " DESC";

        Cursor cursor = db.query(
                cameraSql.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder

        );

        List itemIds = new ArrayList<>();
        while(cursor.moveToNext()) {
            String itemId = cursor.getString(2);
            itemIds.add(itemId);
        }

        cursor.close();

        if(itemIds.isEmpty()){
            return null;
        }
        return (String) itemIds.get(0);
    }


    public String getUserId(Context contex){

        sql = new SqLite(contex);
        cameraSql = new CameraSql();

        SQLiteDatabase db = sql.getReadableDatabase();

        String[] projection = {
                cameraSql.getColumnId(),
                cameraSql.getColumnHash(),
                cameraSql.getColumnUserId()
        };

        String selection = cameraSql.getColumnId() + " = ?";
        String[] selectionArgs = {"1"};
        String sortOrder = cameraSql.getColumnId() + " DESC";

        Cursor cursor = db.query(
                cameraSql.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder

        );

        List itemIds = new ArrayList<>();
        while(cursor.moveToNext()) {
            String itemId = cursor.getString(2);
            itemIds.add(itemId);
        }

        cursor.close();

        if(itemIds.isEmpty()){
            return null;
        }
        return (String) itemIds.get(0);
    }


}
