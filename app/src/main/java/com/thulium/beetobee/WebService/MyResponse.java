package com.thulium.beetobee.WebService;

import java.util.List;

/**
 * Created by Alex on 01/02/2017.
 */

public class MyResponse {
    public int code;
    public String response;
    public User user;

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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
