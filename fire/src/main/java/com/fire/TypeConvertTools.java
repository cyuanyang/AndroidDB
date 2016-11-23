package com.fire;

import java.lang.reflect.Field;
import java.util.Date;

/**
 * Created by cyy on 2016/11/11.
 * 说明：类型转换类
 */

public class TypeConvertTools {

   public static String typeConvert(Field field) throws Exception {
        Class<?> typeClass = field.getType();
        if (typeClass == Integer.TYPE || typeClass == int.class || typeClass == Long.TYPE || typeClass == long.class){
            return "INTEGER";
        }
        else if (typeClass == String.class){
            return "TEXT";
        }
        else if (typeClass == Boolean.TYPE || typeClass == boolean.class){
            return "INTEGER";
        }
        else  if (typeClass == Date.class){
            return "INTEGER";
        }
        else if (typeClass == float.class || typeClass == Float.TYPE || typeClass == Double.TYPE || typeClass == double.class){
            return "REAL";
        }
        else if (typeClass == byte[].class){
            return "BLOB";
        }
        else {
            throw new Exception(field.getName()+"的类型不是数据库支持的类型"+"他的类型为"+typeClass);
        }
    }
}
