package com.thulium.beetobee.Formation;

/**
 * Created by Alex on 19/02/2017.
 */

public class MyFormationResponse {
    public int code;
    public String response;
    public Formation formation;

    public String getResponse() {
        return response;
    }

    public int getCode() { return code; }

    public Formation getFormation() {
        return formation;
    }
}
