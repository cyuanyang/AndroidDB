package com.example.cyy.firedemo.model;

import com.fire.annotation.Column;
import com.fire.annotation.Table;

import java.util.Date;

/**
 * Created by cyy on 2016/11/11.
 * 说明：
 */

@Table(name = "person")
public class Person {

    @Column(name = "_id",isAuto = true , isId = true)
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "birth")
    private Date birth;

    @Column(name = "money")
    private long money;

    @Column(name = "hot")
    private boolean hot;

    @Column(name = "hour")
    private float hour;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    public long getMoney() {
        return money;
    }

    public void setMoney(long money) {
        this.money = money;
    }

    public boolean isHot() {
        return hot;
    }

    public void setHot(boolean hot) {
        this.hot = hot;
    }

    public float getHour() {
        return hour;
    }

    public void setHour(float hour) {
        this.hour = hour;
    }
}
