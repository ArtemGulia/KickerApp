package com.g_art.kickerapp.model;

/**
 * Kicker App
 * Created by G_Art on 16/2/2016.
 */
public class Achievement {
    private String icon;
    private String name;
    private String desc;

    public Achievement() {
    }

    public Achievement(String icon, String name, String desc) {
        this.icon = icon;
        this.name = name;
        this.desc = desc;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
