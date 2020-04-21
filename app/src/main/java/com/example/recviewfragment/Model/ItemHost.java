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

    public ItemHost() {}

    public ItemHost(String email, String eventName, String password, String login, double latitude, double longitude) {
        this.login = login;
        this.password = password;
        this.email = email;
        this.eventName = eventName;
        this.latitude = latitude;
        this.longitude = longitude;
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
}
