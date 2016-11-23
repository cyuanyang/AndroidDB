package com.fire.table.selector;

import android.database.Cursor;
import android.util.Log;


import com.fire.sqlite.ColumnInfo;
import com.fire.sqlite.MySQLiteOpenHelper;
import com.fire.sqlite.SqlInfo;
import com.fire.sqlite.TableInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by cyy on 2016/11/1.
 * 说明：数据库查询类
 */

public class Selector<T> {

    private TableInfo<T> tableInfo;

    ///排序信息
    private List<OrderInfo> orderInfos;
    ///WhereInfo
    private WhereInfo whereInfo;

    private Selector(TableInfo<T> table) {
        this.tableInfo = table;
    }

    public static <T> Selector<T> from(TableInfo<T> tableInfo){
        return new Selector<>(tableInfo);
    }

    public Selector<T> where(String columnName , String op , String value){
        whereInfo = new WhereInfo(columnName , op , value);
        return this;
    }

    public Selector<T> and(String columnName , String op , String value){
        if (whereInfo == null){
            Log.e("" , "and 无效 || 先调用where语句");
        }else {
            whereInfo.and(columnName , op , value);
        }
        return this;
    }

    public Selector<T> or(String columnName , String op , String value){
        if (whereInfo == null){
            Log.e("" , "or 无效 || 先调用where语句");
        }else {
            whereInfo.or(columnName , op , value);
        }
        return this;
    }

    ///排序
    public Selector<T> orderBy(String columnName , boolean desc){
        if (orderInfos==null)orderInfos = new ArrayList<>(3);
        orderInfos.add(new OrderInfo(columnName , desc));
        return this;
    }

    /**
     * 查询所有的值
     * @return 返回查询的结果
     * @throws Exception
     */
    public List<T> find() throws Exception {
        List<T> result = new ArrayList<>();
        MySQLiteOpenHelper helper = MySQLiteOpenHelper.newInstance();
        try {
            SqlInfo sqlInfo = buildSelectSQL();
            Cursor cursor = helper.query( sqlInfo.getSql() , sqlInfo.getWhereArgs());
            while (cursor.moveToNext()){
                ///创建实例 根据 tableInfo
                T obj = tableInfo.newEntityInstance();
                setupObjFromCursor(obj , cursor , tableInfo);
                result.add(obj);
            }
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }finally {
            helper.close();
        }
        return result;
    }

//    ("select * from " + Contexts.TABLE_MESSAGE + " where " + Contexts.USER_NO + "=? order by " + Contexts._ID + " desc" , new String[]{userNo});

    /**
     * 构建查询SQL语句
     * @return SqlInfo
     */
    private SqlInfo buildSelectSQL(){
        String[] args = null; //防注入参数 就是 ？ 的值 与 ？ 的个数相同
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT * FROM ").append(tableInfo.getTableName()).append(" ");
        //Where
        if (whereInfo!=null){
            int count = whereInfo.getWhereLists().size();
            if (count > 0){
                builder.append("WHERE ");
                for (String string : whereInfo.getWhereLists()) {
                    builder.append(string);
                }
                args = new String[count];
                args = whereInfo.getArgs().toArray(args);
            }
        }
        //排序
        if (orderInfos!=null){
            builder.append("ORDER BY ");
            for (OrderInfo orderInfo:orderInfos) {
                builder.append(orderInfo.toString()).append(",");
            }
            builder.deleteCharAt(builder.length()-1);
        }

        SqlInfo sqlInfo = new SqlInfo(builder.toString());
        sqlInfo.setWhereArgs(args);
        return sqlInfo;
    }

    ///给对象设置值
    private void setupObjFromCursor(T obj , Cursor cursor , TableInfo<T> tableInfo) throws Exception {
        Map<String , ColumnInfo<T>> columnMap = tableInfo.getColumnInfoMap();
        ///一个一个的赋值到obj
        int cursorColumnCount = cursor.getColumnCount();
        for (int i = 0; i < cursorColumnCount; i++) {
            ColumnInfo<T> columnInfo = columnMap.get(cursor.getColumnName(i));
            columnInfo.setFieldValue(obj , cursor , i);
        }
    }
}
