package me.slackti.notesmatter.model;


import com.google.firebase.database.Exclude;

public class Todo {

    private String key;
    private String title;
    private String deadline;
    private int position;

    public Todo() {
        // Default constructor required for calls to DataSnapshot.getValue(Todo.class)
    }

    public Todo(String title, String deadline) {
        this.title = title;
        this.deadline = deadline;
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

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

}
