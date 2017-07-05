package com.example.maks.maxwatchapp.models;

/**
 * Created by Maks on 05/07/17.
 */

public class Message {
    private String id = "";
    private String text = "";

    public String getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public Message(String id, String text) {

        this.id = id;
        this.text = text;
    }
}
