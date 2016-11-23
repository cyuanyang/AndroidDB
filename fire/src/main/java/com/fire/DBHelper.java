package com.fire;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;


import com.fire.table.selector.Selector;
import com.fire.table.selector.WhereInfo;

import java.util.List;

/**
 * Created by cyy on 2016/10/31.
 * 说明：
 */

public interface DBHelper {

    /*********************************save***************************/
    void save(@NonNull Object obj);
    void save(@NonNull List<Object> objs);

    /*********************************select***************************/
    /**
     * 查找数据库 目前支持 where and or orderBy 操作
     * @param clazz 查找哪一张表
     * @param <T>  ""
     * @return 查到的数据集合
     * @throws Exception ""
     */
    <T> List<T> find(Class<T> clazz) throws Exception;

    <T> Selector<T> from(Class<T> clazz) throws Exception;

    /*********************************select***************************/
    /**
     * 修改 目前支持根据id修改
     * @param obj 根据id来修改这个值。传入是obj的主键不能为null
     */
    <T> void update(@NonNull T obj);
    <T> void update(@NonNull List<T> objs);

    /********************************* delete ************************/
    void delete(@NonNull Class<?> clazz, @Nullable WhereInfo whereInfo);
}
