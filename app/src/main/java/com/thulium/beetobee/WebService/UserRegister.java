package com.thulium.beetobee.WebService;

/**
 * Created by Alex on 01/02/2017.
 * This Class is used to receive the response from an registration
 */

public class UserRegister {

    public int code = 0;
    public String response;
    public String email;
    public String firstname;
    public String lastname;
    public String password;

    public UserRegister(String firstname, String lastname, String email,  String password){
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
    }

    public int getCode() {
        return code;
    }

    public String getResponse() {
        return response;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}
