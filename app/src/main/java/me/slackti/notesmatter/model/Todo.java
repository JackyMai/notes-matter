package me.slackti.notesmatter.model;


import com.google.firebase.database.Exclude;

public class Todo {

    private String key;
    private String title;
    private int position;

    public Todo() {
        // Default constructor required for calls to DataSnapshot.getValue(Todo.class)
    }

    public Todo(String title) {
        this.title = title;
    }

    public Todo(String key, String title, int position) {
        this.key = key;
        this.title = title;
        this.position = position;
    }

    @Exclude
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

}
