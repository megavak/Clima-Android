package com.londonappbrewery.climapm.model;

/**
 * Created by user on 2017/06/22.
 */

public class Weather {
    private String temperature;
    private  int condition;
    private String city;
    private String iconName;

    public Weather(){}

    public Weather(String temperature, int condition, String city, String iconName) {
        this.temperature = temperature;
        this.condition = condition;
        this.city = city;
        this.iconName = iconName;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public int getCondition() {
        return condition;
    }

    public void setCondition(int condition) {
        this.condition = condition;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getIconName() {
        return iconName;
    }

    public void setIconName(String iconName) {
        this.iconName = iconName;
    }
}
