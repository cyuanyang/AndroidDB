package com.fire.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by cyy on 2016/10/31.
 * 说明：
 */

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Column {
    String name(); //column name

    /// INTEGER
//    String type() default "TEXT"; // column property  "INTEGER" ,"DATE" ,"TEXT", "BOOLEAN"

    boolean isId() default false; //是否是ID
    boolean isAuto() default false;//id是否是自增长模式 只有isId为true时才有效 且id类型为 INTEGER
}
