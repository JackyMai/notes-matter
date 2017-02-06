package me.slackti.notesmatter.model;


public class Todo {

    private String id;
    private String title;
    private int position;

    public Todo() {}

    public Todo(String title) {
        this.title = title;
    }

    public Todo(String id, String title, int position) {
        this.id = id;
        this.title = title;
        this.position = position;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
