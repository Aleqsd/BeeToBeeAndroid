package com.thulium.beetobee.Formation;

/**
 * Created by Alex on 01/02/2017.
 */

public class Formation {

    private int icon;
    private String title;
    private String counter;

    public Formation(int icon, String title, String counter) {
        super();
        this.icon = icon;
        this.title = title;
        this.counter = counter;
    }

    public int getIcon() {
        return icon;
    }

    public String getTitle() {
        return title;
    }

    public String getCounter() {
        return counter;
    }

}
