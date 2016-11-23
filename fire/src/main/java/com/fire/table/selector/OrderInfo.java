package com.fire.table.selector;

/**
 * Created by cyy on 2016/11/1.
 * 说明：排序信息类
 */

public class OrderInfo {

    private String orderName;//依据哪一个排序
    private boolean desc;//默认升序

    OrderInfo(String orderName , boolean desc) {
        this.desc = desc;
        this.orderName = orderName;
    }

    @Override
    public String toString(){
        return orderName + (desc ? " DESC" : " ASC") ;
    }
}
