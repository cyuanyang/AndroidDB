package com.fire.table;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;


import com.fire.DBHelper;
import com.fire.sqlite.MySQLiteOpenHelper;
import com.fire.sqlite.SqlInfo;
import com.fire.sqlite.SqlInfoBuilder;
import com.fire.sqlite.TableInfo;
import com.fire.table.selector.Selector;
import com.fire.table.selector.WhereInfo;

import java.util.Collections;
import java.util.List;

/**
 * Created by cyy on 2016/10/31.
 * 说明：DBHelper的实现类
 */

public class DBHelperImpl implements DBHelper {

    /**
     * 往数据库中插入一条数据
     * @param obj 数据的model类
     */
    @Override
    public void save(@NonNull Object obj) {
        MySQLiteOpenHelper helper = MySQLiteOpenHelper.newInstance();
        //先判断这是哪一个类
        try {
            TableInfo tableInfo = DBBase.getBase().getTable(obj.getClass());
            SqlInfo sqlInfo = new SqlInfoBuilder<>().buildInsertSqlInfo(tableInfo , obj);
            helper.exeSQL(sqlInfo.getSql() , sqlInfo.getArgs());
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            helper.close();
        }
    }

    @Override
    public void save(List<Object> objs) {

    }

    @Override
    public <T> List<T> find(Class<T> clazz) {
        try {
            Selector<T> selector = from(clazz);
            return selector.find();
        }catch (Exception e){
            if (DBBase.getBase().getConfig().isDebug){
                Log.e(DBBase.TAG , "查询出现错误了");
                e.printStackTrace();
            }
        }
        return Collections.emptyList();
    }

    @Override
    public <T> Selector<T> from(Class<T> clazz) {
        try {
            return Selector.from(DBBase.getBase().getTable(clazz));
        }catch (Exception e){
            if (DBBase.getBase().getConfig().isDebug){
                Log.e(DBBase.TAG , "查询出现错误了");
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public <T> void update(@NonNull T obj) {
        MySQLiteOpenHelper helper = MySQLiteOpenHelper.newInstance();
        try {
            TableInfo<T> tableInfo = (TableInfo<T>) DBBase.getBase().getTable(obj.getClass());
            SqlInfo sqlInfo = new SqlInfoBuilder<T>().buildUpdateSqlInfo(tableInfo , obj);
            helper.exeSQL(sqlInfo.getSql() , sqlInfo.getArgs());
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            helper.close();
        }
    }

    @Override
    public <T> void update(@NonNull List<T> objs) {
        MySQLiteOpenHelper helper = MySQLiteOpenHelper.newInstance();
        try {
            for(T obj  : objs){
                TableInfo<T> tableInfo = (TableInfo<T>) DBBase.getBase().getTable(obj.getClass());
                SqlInfo sqlInfo = new SqlInfoBuilder<T>().buildUpdateSqlInfo(tableInfo , obj);
                helper.exeSQL(sqlInfo.getSql() , sqlInfo.getArgs());
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            helper.close();
        }
    }

    @Override
    public void delete(@NonNull Class<?> clazz , @Nullable WhereInfo whereInfo) {
        MySQLiteOpenHelper helper = MySQLiteOpenHelper.newInstance();
        try {
            TableInfo<?> tableInfo = DBBase.getBase().getTable(clazz);
            SqlInfo sqlInfo = new SqlInfoBuilder<>().buildDeleteSqlInfo(tableInfo ,whereInfo);
            helper.exeSQL(sqlInfo.getSql() ,sqlInfo.getWhereArgs());
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            helper.close();
        }
    }

    @Override
    public void deleteById(@NonNull Class<?> clazz ,@NonNull String id) {
        MySQLiteOpenHelper helper = MySQLiteOpenHelper.newInstance();
        try {
            TableInfo<?> tableInfo = DBBase.getBase().getTable(clazz);
            WhereInfo whereInfo = WhereInfo.Builder.buildWhere(tableInfo.getTableId().getColumnName() , "=",id).build();
            SqlInfo sqlInfo = new SqlInfoBuilder<>().buildDeleteSqlInfo(tableInfo ,whereInfo);
            helper.exeSQL(sqlInfo.getSql() ,sqlInfo.getWhereArgs());
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            helper.close();
        }
    }

    @Override
    public void deleteById(List<?> objects) {
        MySQLiteOpenHelper helper = MySQLiteOpenHelper.newInstance();
        try {
            for (Object obj : objects) {
                TableInfo<?> tableInfo = DBBase.getBase().getTable(obj.getClass());
                String id = tableInfo.getTableId().getFieldValue(obj)+"";
                if (!TextUtils.isEmpty(id)){
                    WhereInfo whereInfo = WhereInfo.Builder.buildWhere(tableInfo.getTableId().getColumnName() , "=",id).build();
                    SqlInfo sqlInfo = new SqlInfoBuilder<>().buildDeleteSqlInfo(tableInfo ,whereInfo);
                    helper.exeSQL(sqlInfo.getSql() ,sqlInfo.getWhereArgs());
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            helper.close();
        }
    }
}
