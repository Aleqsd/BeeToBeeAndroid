package com.thulium.beetobee.WebService;

/**
 * Created by Alex on 07/05/2017.
 */

public class SimpleResponse {
    public int code;

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

    public String response;
}
