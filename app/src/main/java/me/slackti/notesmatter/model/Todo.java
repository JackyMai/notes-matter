package me.slackti.notesmatter.model;


public class Todo {

    private String id;
    private String title;
    private int position;
    private boolean done;

    public Todo(String title) {
        this.title = title;
        this.done = false;
    }

    public Todo(String id, String title, int position, boolean done) {
        this.id = id;
        this.title = title;
        this.position = position;
        this.done = done;
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

    public boolean getDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}
