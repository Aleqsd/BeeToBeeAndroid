package com.thulium.beetobee.WebService;

import java.io.Serializable;
import java.sql.Date;

/**
 * Created by Alex on 01/02/2017.
 */

public class User implements Serializable{
    public int id;
    public String email;
    public String firstname;
    public String lastname;
    public String password;
    public Date birthDate;
    public String profilePicture;
    public String skypeId;
    public String city;
    public String university;
    public String education;
    public int level;
    public String fbLink;
    public String twitterLink;
    public String access_token;
    public String createdAt;
    public String updatedAt;
    public int roleId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstName(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getSkypeId() {
        return skypeId;
    }

    public void setSkypeId(String skypeId) {
        this.skypeId = skypeId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getFbLink() {
        return fbLink;
    }

    public void setFbLink(String fbLink) {
        this.fbLink = fbLink;
    }

    public String getTwitterLink() {
        return twitterLink;
    }

    public void setTwitterLink(String twitterLink) {
        this.twitterLink = twitterLink;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", password='" + password + '\'' +
                ", birthDate=" + birthDate +
                ", profilePicture='" + profilePicture + '\'' +
                ", skypeId='" + skypeId + '\'' +
                ", city='" + city + '\'' +
                ", university='" + university + '\'' +
                ", education='" + education + '\'' +
                ", level=" + level +
                ", fbLink='" + fbLink + '\'' +
                ", twitterLink='" + twitterLink + '\'' +
                ", access_token='" + access_token + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                ", roleId=" + roleId +
                '}';
    }
}
