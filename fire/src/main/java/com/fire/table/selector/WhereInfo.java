package com.fire.table.selector;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cyy on 2016/11/1.
 * 说明：查询 Where info
 */

public class WhereInfo {
    private List<String> whereLists = new ArrayList<>(5);
    private List<String> args = new ArrayList<>(5);

    public WhereInfo(String columnName , String op , String value) {
        whereAppend( null , columnName , op , value);
    }

    private void whereAppend(String condition, String columnName , String op , String value){
        StringBuilder builder = new StringBuilder();
        if (!TextUtils.isEmpty(condition)){
            builder.append(condition);
        }
        builder.append(columnName).append(op).append("? ");
        args.add(value);
        whereLists.add(builder.toString());
    }

    public void and(String columnName , String op , String value){
        whereAppend("AND ",columnName , op , value);
    }

    public void or(String columnName , String op , String value){
        whereAppend("OR ",columnName , op , value);
    }

    public List<String> getWhereLists() {
        return whereLists;
    }

    public List<String> getArgs() {
        return args;
    }

    public static class Builder{

        private WhereInfo whereInfo;

        Builder(String columnName , String op , String value){
            whereInfo = new WhereInfo(columnName,op ,value);
        }

        public Builder and(String columnName , String op , String value){
            whereInfo.whereAppend("AND ",columnName , op , value);
            return this;
        }

        public Builder or(String columnName , String op , String value){
            whereInfo.whereAppend("OR ",columnName , op , value);
            return this;
        }

        public WhereInfo build(){
            return whereInfo;
        }

        public static Builder buildWhere(String columnName , String op , String value){
            return new Builder(columnName,op , value);
        }
    }
}
