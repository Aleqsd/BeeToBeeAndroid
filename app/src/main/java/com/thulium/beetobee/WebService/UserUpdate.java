package com.thulium.beetobee.WebService;

import java.io.Serializable;

/**
 * Created by Alex on 01/02/2017.
 */

public class UserUpdate implements Serializable{
    public int id;
    public String email;
    public String firstname;

    public UserUpdate(User user)
    {
        this.id = user.getId();
        this.email = user.getEmail();
        this.firstname = user.getFirstname();
        this.lastname = user.getLastname();
        this.birthDate = user.getBirthDate();
        this.profilePicture = user.getProfilePicture();
        this.skypeId = user.getBirthDate();
        this.city = user.getCity();
        this.university = user.getUniversity();
        this.education = user.getEducation();
        this.level = user.getLevel();
        this.fbLink = user.getFbLink();
        this.twitterLink = user.getTwitterLink();
        this.createdAt = user.getCreatedAt();
        this.updatedAt = user.getUpdatedAt();
        this.roleId = user.getRoleId();
    }

    public UserUpdate(int id, String email, String firstname, String lastname, String birthDate, String profilePicture, String skypeId, String city, String university, String education, String level, String fbLink, String twitterLink, String createdAt, String updatedAt, int roleId) {
        this.id = id;
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.birthDate = birthDate;
        this.profilePicture = profilePicture;
        this.skypeId = skypeId;
        this.city = city;
        this.university = university;
        this.education = education;
        this.level = level;
        this.fbLink = fbLink;
        this.twitterLink = twitterLink;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.roleId = roleId;
    }

    public String lastname;
    public String birthDate;
    public String profilePicture;
    public String skypeId;
    public String city;
    public String university;
    public String education;
    public String level;
    public String fbLink;
    public String twitterLink;
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

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
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

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", birthDate=" + birthDate +
                ", profilePicture='" + profilePicture + '\'' +
                ", skypeId='" + skypeId + '\'' +
                ", city='" + city + '\'' +
                ", university='" + university + '\'' +
                ", education='" + education + '\'' +
                ", level=" + level +
                ", fbLink='" + fbLink + '\'' +
                ", twitterLink='" + twitterLink + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                ", roleId=" + roleId +
                '}';
    }
}
