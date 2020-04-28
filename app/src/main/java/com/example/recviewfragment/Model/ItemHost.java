package com.example.recviewfragment.Model;

import java.io.Serializable;

public class ItemHost implements Serializable {

    private int id;
    private String login;
    private String password;
    private String email;
    private String eventName;
    private double latitude;
    private double longitude;
    private boolean isActive;


    public ItemHost() {}

    public ItemHost(String email, String eventName, String password, String login, double latitude, double longitude, boolean isActive) {
        this.login = login;
        this.password = password;
        this.email = email;
        this.eventName = eventName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.isActive = isActive;
    }

    public ItemHost(String email, String eventName, String password, String login, double latitude, double longitude, int id, boolean isActive) {
        this.login = login;
        this.password = password;
        this.email = email;
        this.eventName = eventName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.id = id;
        this.isActive = isActive;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean active) {
        isActive = active;
    }
}
