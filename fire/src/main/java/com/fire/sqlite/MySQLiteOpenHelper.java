package com.fire.sqlite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import com.fire.DBCallback;
import com.fire.table.DBBase;

import java.util.List;

/**
 * Created by cyy on 2016/10/31.
 * 说明：
 */

public class MySQLiteOpenHelper extends SQLiteOpenHelper {

    private List<SqlInfo> sqlInfos;
    private Boolean openTransaction;

    private DBCallback callback;

    public MySQLiteOpenHelper(Context context , DBBase.Config config , List<SqlInfo> sqlInfos) {
        super(context, config.name, null, config.version);
        this.openTransaction = config.openTransaction;
        this.sqlInfos = sqlInfos;
        this.callback = config.callback;
    }

    //构建一个新的数据库帮助类
    public static MySQLiteOpenHelper newInstance(){
        DBBase dbBase = DBBase.getBase();
        return new MySQLiteOpenHelper(dbBase.getApp() , dbBase.getConfig() , dbBase.getSqlInfos());
    }

    /**
     * 若是全新安装的用户 只会执行onCreate方法
     * 若是覆盖安装的用户 则不会执行onCreate方法  只会执行onUpgrade方法
     * switch 语句是没有 break 的，会一直执行到语句结束
     *比如用户手上的版本是 1，新版 App 的版本是 5，那么就会有 4 个版本的数据库升级，
     * switch() 自然不能中途 break，必须执行这 4 个版本的数据库升级语句。
     * 以后若有数据库版本升级，则继续再其后添加case3  case4 ...
     */

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.e("" , "MySQLiteOpenHelper onCreate");
        try {
            if (sqlInfos!=null){
                for (SqlInfo sql: sqlInfos) {
                    db.execSQL(sql.getSql());
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        if (callback!=null){
            callback.dbCreate();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            if (sqlInfos!=null){
                for (SqlInfo sql: sqlInfos) {
                    db.execSQL(sql.getSql());
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        if (callback!=null){
            callback.dbUpdate(db, oldVersion, newVersion);
        }
    }

    private void beginTransaction(SQLiteDatabase db){
        if (openTransaction){
            db.beginTransaction();
        }
    }

    private void setTransactionSuccessful(SQLiteDatabase db){
        if (openTransaction){
            db.setTransactionSuccessful();
        }
    }

    private void endTransaction(SQLiteDatabase db){
        if (openTransaction){
            db.endTransaction();
        }
    }

    public void setCallback(DBCallback callback){
        this.callback = callback;
    }

    /**
     * 执行读取性的数库操作
     * @param sql 读取数据库的sql
     */
    public Cursor query(String sql, String[] args){
        Log.e("query>>>SQL=" , sql);
        Log.e("query>>>args=" , args+"");
        Cursor cursor = null;
        SQLiteDatabase db = this.getReadableDatabase();
        beginTransaction(db);
        try {
            cursor = db.rawQuery(sql, args);
            setTransactionSuccessful(db);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            endTransaction(db);
        }
        return cursor;
    }

    /**
     * 执行可写性的数据库操作
     * @param sql 写入数据库的SQL
     * @param args 防注入时的参数
     */
    public void exeSQL(String sql , Object[] args){
        Log.e("query>>>SQL=" , sql);
        Log.e("query>>>args=" , args+"");
        SQLiteDatabase db = this.getWritableDatabase();
        beginTransaction(db);
        try {
            if (args == null){
                db.execSQL(sql);
            }else {
                db.execSQL(sql , args);
            }
            setTransactionSuccessful(db);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            endTransaction(db);
        }
    }

    @Override
    public synchronized void close() {
        super.close();
    }
}
