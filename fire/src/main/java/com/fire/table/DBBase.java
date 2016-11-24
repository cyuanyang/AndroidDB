package com.fire.table;

import android.app.Application;
import android.util.Log;


import com.fire.DBCallback;
import com.fire.DBHelper;
import com.fire.sqlite.SqlInfo;
import com.fire.sqlite.SqlInfoBuilder;
import com.fire.sqlite.TableInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by cyy on 2016/10/31.
 * 说明：单利类 在app中初始化
 */

public class DBBase {

    public static final String TAG = "DBBase";

    private static DBBase dbBase; //
    private Config mConfig; //数据库的配置数据
    private Application mApp; //这里会不会内存泄漏 待检测

    //******************************************* 缓存数据
    //这里存的是创建表的sql语句
    private List<SqlInfo> sqlInfos = new ArrayList<>(10);
    ///缓存的所有表的信息 不用每次反射去构建字段
    private HashMap<Class<?> , TableInfo> tableMap = new HashMap<>();

    /**
     * 单利创建
     * @return
     */
    public static DBBase getBase(){
        if (dbBase == null){
            synchronized (DBBase.class){
                if (dbBase == null){
                    dbBase = new DBBase();
                }
            }
        }
        return dbBase;
    }

    public DBBase init(Application app, Config config){
        mApp = app;
        mConfig = config;
        return this;
    }

    public void init(Application app , String name , int version){
        mApp = app;
        mConfig = new Config();
        mConfig.name = name;
        mConfig.version = version;
    }

    public <T> void creatTables(Class<T>[] clazzs){
        createTablesIfNotExist(clazzs);
    }

    public <T> TableInfo<T> getTable(Class<T> clazz) throws Exception {
        synchronized (tableMap){
            TableInfo<T> table = tableMap.get(clazz);
            if (table == null){
                ///反射加载table的信息
                table = new TableInfo<>(clazz);
                tableMap.put(clazz , table);
            }
            return table;
        }
    }

    /**
     * 根据model类 创建数据库
     * 执行一次就行了 一般在app中调用一次就够了
     * 不要再其他地方在调用 浪费资源
     */
    private <T> void createTablesIfNotExist(Class<T>[] clazzs) {
        for (Class<T> clazz :clazzs) {
            try {
                SqlInfo sqlInfo = new SqlInfoBuilder<T>().buildCreateTableSqlInfo(getTable(clazz));
                Log.e(TAG , "创建表的SQL语句为>>>"+sqlInfo.getSql());
                sqlInfos.add(sqlInfo);
            }catch (Exception e){
                if (mConfig.isDebug){
                    Log.d(TAG ,"建表"+clazz.getName()+"失败");
                    e.printStackTrace();
                }
            }
        }
    }

    public static DBHelper newDBHelper(){
        return new DBHelperImpl();
    }

    public Config getConfig(){
        return mConfig;
    }
    public Application getApp(){
        return mApp;
    }
    public List<SqlInfo> getSqlInfos() {
        return sqlInfos;
    }

    public static Config builcConfig(){
        return new Config();
    }

    public static class Config{
        public String name;
        public int version;
        public boolean openTransaction;//是否开始事务
        public boolean isDebug;
        public DBCallback callback; //数据库创建更新的回调

        public Config(){
            name = "default.db";
            version = 1;
        }

        public Config setName(String name) {
            this.name = name;
            return this;
        }

        public Config setVersion(int version) {
            this.version = version;
            return this;
        }

        public Config setOpenTransaction(boolean openTransaction) {
            this.openTransaction = openTransaction;
            return this;
        }

        public Config setDebug(boolean debug) {
            isDebug = debug;
            return this;
        }

        public Config setCallback(DBCallback callback) {
            this.callback = callback;
            return this;
        }
    }
}
