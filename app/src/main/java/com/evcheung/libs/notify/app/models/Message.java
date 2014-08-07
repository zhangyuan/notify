package com.evcheung.libs.notify.app.models;

public class Message {
    private String id;
    private String content;

    public Message(String id, String content) {
        this.content = content;
        this.id = id;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s", id, content);
    }
}
