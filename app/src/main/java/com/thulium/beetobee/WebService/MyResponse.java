package com.thulium.beetobee.WebService;

/**
 * Created by Alex on 01/02/2017.
 */

public class MyResponse {
    public int code;
    public String response;
    public User user;

    public String getResponse() {
        return response;
    }

    public int getCode() { return code; }

    public User getUser() {
        return user;
    }
}
