package com.fire.sqlite;

import com.fire.annotation.Column;
import com.fire.annotation.Table;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by cyy on 2016/10/31.
 * 说明：
 */

public class TableInfo<T> {

    private String tableName;
    private ColumnInfo tableId;
    private Constructor<T> constructor;

    ///这个table列的信息
    private LinkedHashMap<String , ColumnInfo<T>> columnInfoMap;

    public TableInfo(Class<T> clazz) throws Exception {
        Table table = clazz.getAnnotation(Table.class);
        this.tableName = table.name();
        this.columnInfoMap = setupColumns(clazz);
        for (ColumnInfo column: columnInfoMap.values()) {
            if (column.getIsId()){
                tableId = column;
                break;
            }
        }
        if (tableId == null){
            throw new Exception("每一张表应该有一个Id");
        }

        //构造函数
        this.constructor = clazz.getConstructor();
        this.constructor.setAccessible(true);
    }

    public ColumnInfo getTableId(){
        return tableId;
    }

    public Collection<ColumnInfo<T>> getColumnInfos(){
        return columnInfoMap.values();
    }

    public HashMap<String, ColumnInfo<T>> getColumnInfoMap() {
        return columnInfoMap;
    }

    public String getTableName(){
        return tableName;
    }

    ///加载表表中列的数据
    private LinkedHashMap<String , ColumnInfo<T>> setupColumns(Class<T> clazz ) throws Exception {
        LinkedHashMap<String , ColumnInfo<T>> map = new LinkedHashMap<>(10);
        try {
            Field[] fields = clazz.getDeclaredFields();
            for (Field field: fields ) {
                //先判定访问权限
                int modify = field.getModifiers();
                if (Modifier.isStatic(modify) || Modifier.isTransient(modify)) {
                    continue;
                }

                ///遍历列
                Column columnAnn = field.getAnnotation(Column.class);
                if (columnAnn != null) {
                    ColumnInfo<T> columnInfo = new ColumnInfo<>(clazz , columnAnn , field);
                    map.put(columnInfo.getColumnName() , columnInfo);
                }
            }
        }catch (Exception e){
            throw e;
        }
        return map;
    }

    /**
     * 根据表创建一个实例 在查询的时候用到
     * @return 这个实例对象
     * @throws Exception
     */
    public T newEntityInstance() throws Exception {
        return this.constructor.newInstance();
    }
}
