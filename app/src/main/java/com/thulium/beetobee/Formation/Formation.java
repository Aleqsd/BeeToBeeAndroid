package com.thulium.beetobee.Formation;

import com.thulium.beetobee.WebService.Creator;
import com.thulium.beetobee.WebService.User;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;

/**
 * Created by Alex on 01/02/2017.
 * Classe Formation de l'objet servant à remplir la liste des formations
 * id: {type: DataTypes.INTEGER, primaryKey: true, autoIncrement: true},
 * title: {type: DataTypes.STRING},
 * description: {type: DataTypes.STRING(1000)},
 * duration: {type: DataTypes.INTEGER},
 * date: {type: DataTypes.DATE},
 * hour: {type: DataTypes.STRING},
 * place: {type: DataTypes.STRING},
 * availableSeat: {type: DataTypes.INTEGER}
 */

public class Formation implements Serializable {

    private int id;
    private String title;
    private String description;
    private int duration;
    private String date;
    private String hour;
    private String place;
    private int availableSeat;
    private int creatorId;
    private Creator creator;
    private List<User> users;
    private List<Theme> themes;
    private int themeId;

    public int getThemeId() {
        return themeId;
    }

    public void setThemeId(int themeId) {
        this.themeId = themeId;
    }

    public Formation(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public Formation(int id, String title, String description, int duration, String date, String hour, String place, int availableSeat, int creatorId, int themeId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.duration = duration;
        this.date = date;
        this.hour = hour;
        this.place = place;
        this.availableSeat = availableSeat;
        this.creatorId = creatorId;
        this.themeId = themeId;
    }

    public Formation(String title, String description, int duration) {
        this.title = title;
        this.description = description;
        this.duration = duration;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }


    public int getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(int creatorId) {
        this.creatorId = creatorId;
    }


    public Creator getCreator() {
        return creator;
    }

    public void setCreator(Creator creator) {
        this.creator = creator;
    }

    public int getAvailableSeat() {
        return availableSeat;
    }

    public void setAvailableSeat(int availableSeat) {
        this.availableSeat = availableSeat;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public List<Theme> getThemes() {
        return themes;
    }

    public void setThemes(List<Theme> themes) {
        this.themes = themes;
    }

}
