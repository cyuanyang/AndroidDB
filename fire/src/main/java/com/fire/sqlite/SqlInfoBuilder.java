package com.fire.sqlite;


import com.fire.table.selector.WhereInfo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Created by cyy on 2016/10/31.
 * 说明： 根据model类构建SQL语句
 */
public class SqlInfoBuilder<T> {

    /**
     * 构建创建table的sql语句
     * @param tableInfo 标的信息
     * @return SqlInfo
     */
    public SqlInfo buildCreateTableSqlInfo(TableInfo<T> tableInfo){
        StringBuilder builder = new StringBuilder();
        builder.append("CREATE TABLE IF NOT EXISTS ");
        builder.append(tableInfo.getTableName());
        builder.append(" ( ");
        //id
        builder.append(tableInfo.getTableId().getColumnName())
                .append(" ").append(tableInfo.getTableId().getType());
        if (tableInfo.getTableId().getIsId() && tableInfo.getTableId().isAuto()){
            builder.append(" ").append("PRIMARY KEY AUTOINCREMENT,");
        }else {
            builder.append(" ").append("PRIMARY KEY,");
        }

        for (ColumnInfo column: tableInfo.getColumnInfos()) {
            if (!column.getIsId()){
                builder.append(column.getColumnName())
                        .append(" ").append(column.getType()).append(",");
            }
        }
        builder.deleteCharAt(builder.length()-1);
        builder.append(");");
        return new SqlInfo(builder.toString());
    }

    /**
     * 构建插入数据库的sql语句
     * @param tableInfo 哪一张表
     * @param object 要插入的对象
     * @return sql语句
     */
    public SqlInfo buildInsertSqlInfo(TableInfo<T> tableInfo , Object object){
        StringBuilder builder = new StringBuilder();
        builder.append("INSERT INTO ").append(tableInfo.getTableName());

        List<KeyValue> keyValues = entity2KeyValueList(tableInfo , object);
        builder.append(" ( ");
        for (KeyValue kv: keyValues) {
            builder.append(kv.key).append(",");
        }
        builder.deleteCharAt(builder.length()-1);
        builder.append(" ) ");
        builder.append("VALUES(");
        for (int i = 0; i < keyValues.size(); i++) {
            builder.append("?,");
        }
        builder.deleteCharAt(builder.length()-1);
        builder.append(" ) ");

        SqlInfo sqlInfo = new SqlInfo(builder.toString());
        sqlInfo.setBindArgs(keyValues);
        return sqlInfo;
    }

    private List<KeyValue> entity2KeyValueList(TableInfo<T> table, Object entity) {
        Collection<ColumnInfo<T>> columns = table.getColumnInfos();
        List<KeyValue> keyValueList = new ArrayList<>(columns.size());
        for (ColumnInfo column : columns) {
            KeyValue kv = column2KeyValue(entity, column);
            if (kv != null) {
                keyValueList.add(kv);
            }
        }
        return keyValueList;
    }

    private KeyValue column2KeyValue(Object entity, ColumnInfo column) {
        if (column.getIsId() && column.isAuto()){
            return  null;
        }
        String key = column.getColumnName();
        Object value = column.getFieldValue(entity);
        ///若为日期将其转换成时间戳
        if (value instanceof Date){
            value = ((Date) value).getTime();
        }
        return new KeyValue(key, value);
    }

//    ("update " + Contexts.TABLE_MESSAGE + " set " + Contexts.READ + "=?" +" where " + Contexts._ID + "=?", new Object[]{msgId, readStatus});
    /**
     * 修改会将根据这个model类的值给这一条数据重新赋值
     * @param tableInfo ""
     * @param object ""
     * @return
     */
    public SqlInfo buildUpdateSqlInfo(TableInfo<T> tableInfo , T object){
        StringBuilder builder = new StringBuilder();
        builder.append("UPDATE ").append(tableInfo.getTableName()).append(" set ");
        //得到id的值
        Object idObj = tableInfo.getTableId().getFieldValue(object);
        if (idObj == null){
            throw new IllegalArgumentException("传入修改的实例的id值为null");
        }
        //set的值
        List<KeyValue> keyValues = entity2KeyValueList(tableInfo , object);
        for (KeyValue kv : keyValues) {
            builder.append(kv.key).append("=?,");
        }
        builder.deleteCharAt(builder.length()-1);

        //where
        builder.append(" WHERE ");
        builder.append(tableInfo.getTableId().getColumnName()).append("=?");

        SqlInfo sqlInfo = new SqlInfo(builder.toString());
        //加入最后一个 ？ 的参数
        keyValues.add(new KeyValue(tableInfo.getTableId().getColumnName() ,idObj ));
        sqlInfo.setBindArgs(keyValues);
        return sqlInfo;
    }
    //    "delete from "+TABLE_SEARCH + " where " + KEY_SEARCH + "=?";

    /**
     *  删除
     * @param tableInfo ""
     * @param whereInfo ""
     * @return
     */
    public SqlInfo buildDeleteSqlInfo(TableInfo<?> tableInfo , WhereInfo whereInfo){
        StringBuilder builder = new StringBuilder();
        builder.append("DELETE FROM ").append(tableInfo.getTableName());
        ///Where
        String[] args = null; //防注入参数 就是 ？ 的值 与 ？ 的个数相同
        if (whereInfo != null){
            int count = whereInfo.getWhereLists().size();
            if (count > 0){
                builder.append(" WHERE ");
                for (String condition: whereInfo.getWhereLists()) {
                    builder.append(condition);
                }
                args = new String[count];
                args = whereInfo.getArgs().toArray(args);
            }
        }

        SqlInfo sqlInfo = new SqlInfo(builder.toString());
        sqlInfo.setWhereArgs(args);
        return sqlInfo;
    }
}
