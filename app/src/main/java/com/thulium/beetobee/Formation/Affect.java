package com.thulium.beetobee.Formation;

import java.io.Serializable;

/**
 * Created by Alex on 03/05/2017.
 */

public class Affect implements Serializable{
    private int formationId;

    public int getFormationId() {
        return formationId;
    }

    public void setFormationId(int formationId) {
        this.formationId = formationId;
    }

    public int getThemeId() {
        return themeId;
    }

    public void setThemeId(int themeId) {
        this.themeId = themeId;
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

    private int themeId;
    private String createdAt;
    private String updateAt;
}
