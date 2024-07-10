package com.example.choresandshop.Model;

public class Notification {
    private String name;
    private String createdByEmail;
    private Boolean isActive;

    public Notification() {
    }

    public String getName() {
        return name;
    }

    public Notification(String name, String createdByEmail, Boolean isActive) {
        this.name = name;
        this.createdByEmail = createdByEmail;
        this.isActive = isActive;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreatedByEmail() {
        return createdByEmail;
    }

    public void setCreatedByEmail(String createdByEmail) {
        this.createdByEmail = createdByEmail;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "name='" + name + '\'' +
                ", createdByEmail='" + createdByEmail + '\'' +
                ", isActive=" + isActive +
                '}';
    }
}
