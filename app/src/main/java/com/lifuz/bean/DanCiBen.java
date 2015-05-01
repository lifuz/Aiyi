package com.lifuz.bean;

/**
 * Created by 半夏微凉 on 2015/5/1.
 */
public class DanCiBen {

    private int id;
    private String name;


    public DanCiBen() {

    }

    public DanCiBen( int id,String name) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
