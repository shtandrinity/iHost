package com.example.recviewfragment.Model;

public class ItemArtist {

    private int id;
    private String name;
    private String phone;
    private String deviceId;
    private int userId;

    public ItemArtist() {}

    public ItemArtist(String deviceId, String name, String phone, int id, int userId) {
        this.deviceId = deviceId;
        this.name = name;
        this.phone = phone;
        this.id = id;
        this.userId = userId;
    }

    public ItemArtist(String deviceId, String name, String phone, int userId) {
        this.deviceId = deviceId;
        this.name = name;
        this.phone = phone;
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
