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
    <T> List<T> find(Class<T> clazz);

    <T> Selector<T> from(Class<T> clazz);

    /*********************************select***************************/
    /**
     * 修改 目前支持根据id修改
     * @param obj 根据id来修改这个值。传入是obj的主键不能为null
     */
    <T> void update(@NonNull T obj);
    <T> void update(@NonNull List<T> objs);

    /********************************* delete ************************/

    /**
     * 删除
     * @param clazz 删除的表
     * @param whereInfo 删除条件 为空则删除所有
     */
    void delete(@NonNull Class<?> clazz, @Nullable WhereInfo whereInfo);

    /**
     * 删除某一条数据
     * @param clazz 要删除的数据的model
     * @param id 删除数据的id
     */
    void deleteById(@NonNull Class<?> clazz , @NonNull String id);

    /**
     * 根据id删除一组数据
     * @param objects 传入的对象的id不为null 为null会忽略掉
     */
    void deleteById(List<?> objects);
}
