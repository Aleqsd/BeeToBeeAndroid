package com.thulium.beetobee.Formation;

/**
 * Created by Alex on 03/05/2017.
 */

public class Theme {
    private int id;

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

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(String updateAt) {
        this.updateAt = updateAt;
    }

    public Affect getAffect() {
        return affect;
    }

    public void setAffect(Affect affect) {
        this.affect = affect;
    }

    private String name;
    private String createdAt;
    private String updateAt;
    private Affect affect;
}
