package com.thulium.beetobee.Formation;

import java.io.Serializable;
import java.sql.Date;

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
    private Date date;
    private String hour;
    private String place;
    private int availableSeat;

    public Formation(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public Formation(int id, String title, String description, int duration, Date date, String hour, String place, int availableSeat) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.duration = duration;
        this.date = date;
        this.hour = hour;
        this.place = place;
        this.availableSeat = availableSeat;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
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
}
