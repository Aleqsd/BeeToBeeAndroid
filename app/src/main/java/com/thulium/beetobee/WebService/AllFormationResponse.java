package com.thulium.beetobee.WebService;

import com.thulium.beetobee.Formation.Formation;

import java.util.List;

/**
 * Created by Alex on 03/05/2017.
 */

public class AllFormationResponse {
    public Formation[] getFormations() {
        return formations;
    }

    public void setFormations(Formation[] formations) {
        this.formations = formations;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public int code;

    public AllFormationResponse(int code, String response, Formation[] formations) {
        this.code = code;
        this.response = response;
        this.formations = formations;
    }

    public String response;

    public AllFormationResponse() {
    }

    public Formation[] formations;
}
