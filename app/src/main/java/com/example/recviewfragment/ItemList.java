package com.example.recviewfragment;

public class ItemList {

    private int id;
    private String name;
    private String phone;
    private boolean isLocated;

    public ItemList() {}

    public ItemList(int id, String name, String phone, boolean isLocated) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.isLocated = isLocated;
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

    public boolean getIsLocated() {
        return isLocated;
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

    public void setLocated(boolean located) {
        isLocated = located;
    }
}
