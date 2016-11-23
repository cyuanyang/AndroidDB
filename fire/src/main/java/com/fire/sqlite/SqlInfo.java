package com.fire.sqlite;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cyy on 2016/10/31.
 * 说明：
 */

public class SqlInfo {

    private String sql; //数据库SQL语句

    private List<KeyValue> bindArgs;
    private Object[] args; ///插入/修改 时防注入时数据库的数据
    private String[] whereArgs; //查询/删除 时的参数

    public SqlInfo(String sql){
        this.sql = sql;
    }

    public void setBindArgs(List<KeyValue> bindArgs) {
        this.bindArgs = new ArrayList<>(bindArgs.size());
        this.bindArgs.addAll(bindArgs);

        args = new Object[bindArgs.size()];
        for (int i = 0 ; i<args.length ; i++) {
            args[i] = bindArgs.get(i).value;
        }
    }

    public void setWhereArgs(String[] whereArgs) {
        this.whereArgs = whereArgs;
    }

    public Object[] getArgs() {
        return args;
    }

    public String[] getWhereArgs() {
        return whereArgs;
    }

    public String getSql() {
        return sql;
    }

}
