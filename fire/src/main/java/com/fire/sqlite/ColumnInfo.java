package com.fire.sqlite;

import android.database.Cursor;

import com.fire.TypeConvertTools;
import com.fire.annotation.Column;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;

/**
 * Created by cyy on 2016/10/31.
 * 说明：
 */

public class ColumnInfo<T> {

    private boolean isId;
    private boolean isAuto;//id 是否为自增长 要求id为Integer
    private String type;
    private String columnName;

    private Field mField; //用于得到该列的值和设置该列的值
    private Method getMethod;
    private Method setMethod;

    public ColumnInfo(Class<T> clazz , Column column , Field field) throws Exception {
        isId = column.isId();
        type = getColumnType(column , field);
        columnName = column.name();
        isAuto = column.isAuto();
        mField = field;

        //拿到get方法
        getMethod = getGetMethod(clazz , field);
        if (getMethod!=null && !getMethod.isAccessible()){
            getMethod.setAccessible(true);
        }

        setMethod = getSetMethod(clazz , field);
        if (setMethod!=null && !setMethod.isAccessible()){
            setMethod.setAccessible(true);
        }
        Class<?>[] typeClass = setMethod.getParameterTypes();
        if (typeClass.length != 1 && typeClass[0] != field.getType()){
            throw new Exception(field.getName()+"变量的set方法的参数应该只有一个且参数的类型应该相同");
        }
    }

    private Method getGetMethod(Class<T> clazz , Field field) throws NoSuchMethodException {
        Class<?> fieldType = field.getType();
        String methodNamePrefix = "get" ;
        if (fieldType == Boolean.TYPE || fieldType == boolean.class){
            methodNamePrefix = "is";
        }
        return clazz.getDeclaredMethod(methodNamePrefix + field.getName().substring(0 , 1).toUpperCase() + field.getName().substring(1));
    }

    private Method getSetMethod(Class<T> clazz , Field field) throws NoSuchMethodException {
        return clazz.getDeclaredMethod(
                    "set" + field.getName().substring(0 , 1).toUpperCase() + field.getName().substring(1) , field.getType());
    }

    /**
     * 从一个实例中得到这个列的值
     * @param object 数据库实例
     * @return 该列的值
     */
    public Object getFieldValue(Object object){
        try {
            return getMethod.invoke(object);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将cursor中的数据存到传入的model类中
     * @param entity 传入的数据库实体类
     * @param cursor 数据库查询结果 不要持有cursor;
     * @param index 取cursor的那一列
     * @param <A> ""
     * @throws Exception
     */
    public <A> void setFieldValue(A entity , Cursor cursor , int index) throws Exception {
        if (setMethod!=null) {
            Class<?>[] paramsType = setMethod.getParameterTypes();
            if (paramsType.length != 1 ){
                throw new Exception(columnName+"的set方法参数个数应该只有一个且类型应该和"+columnName+"一致");
            }
            Object arg = null;
            Class<?> classType = setMethod.getParameterTypes()[0];
            if (Boolean.TYPE == classType || boolean.class == classType ){
                arg = cursor.getInt(index) == 1;
            }
            else if (int.class == classType || Integer.TYPE == classType){
                arg = cursor.getInt(index);
            }
            else if (String.class == classType){
                arg = cursor.getString(index);
            }
            else if (Long.TYPE == classType || long.class == classType){
                arg = cursor.getLong(index);
            }else if (Date.class == classType){
                //日期格式
                long time = cursor.getLong(index);
                arg = time == 0 ? null : new Date(time);
            }else if (Double.TYPE == classType || double.class == classType){
                arg = cursor.getDouble(index);
            }else if (Float.TYPE == classType || float.class == classType){
                arg = cursor.getFloat(index);
            }else if (byte[].class == classType || Byte[].class == classType){
                arg = cursor.getBlob(index);
            }
            setMethod.invoke(entity , arg);
        }
    }

    /**
     * 根据model类的 type 注释 返回数据库具体的字段类型
     * @param column ""
     * @return String;
     */
    private String getColumnType(Column column , Field field) throws Exception {
        return TypeConvertTools.typeConvert(field);
//        String type = column.type();
//        if ("DATE".equals(type)){
//            //若为日期格式则设置为整形 保存时间戳
//            type = "INTEGER";
//        }
//        return type;
    }

    /**
     * 该列是否是ID
     * @return
     */
    public boolean getIsId(){
        return isId;
    }

    public boolean isAuto() {
        return isAuto;
    }

    public String getColumnName(){
        return columnName;
    }

    public String getType() {
        return type;
    }
}
