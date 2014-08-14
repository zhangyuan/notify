package com.evcheung.libs.notify.app.models;

public class Message {
    private String id;
    private String content;
    private String title;

    public Message(String id, String title, String content) {
        this.title = title;
        this.content = content;
        this.id = id;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s\n%s", id, title, content);
    }
}
