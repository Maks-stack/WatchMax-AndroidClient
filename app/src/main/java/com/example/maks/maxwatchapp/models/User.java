package com.example.maks.maxwatchapp.models;

import java.util.ArrayList;

/**
 * Created by Maks on 22/06/17.
 */

public class User {
    private String id = "";
    private String name = "";
    private String status = "";
    private Double energyLevel = 0.0;
    private Double latitude = 0.0;
    private Double longitude = 0.0;
    private ArrayList<Message> messages;

    public String getId() { return id; }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public Double getEnergyLevel() {
        return energyLevel;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public ArrayList<Message> getMessages() { return messages; }

    public User(String id, String name, String status, Double energyLevel, Double latitude, Double longitude, ArrayList<Message> messages) {

        this.id = id;
        this.name = name;
        this.status = status;
        this.energyLevel = energyLevel;
        this.latitude = latitude;
        this.longitude = longitude;
        this.messages = messages;
    }
}
