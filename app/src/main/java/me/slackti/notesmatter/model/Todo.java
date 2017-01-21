package me.slackti.notesmatter.model;


public class Todo {

    private String id;
    private String title;
    private String note;
    private boolean complete;

    public Todo(String title) {
        this.title = title;
        this.complete = false;
    }

    public Todo(String id, String title) {
        this.id = id;
        this.title = title;
        this.complete = false;
    }

    public Todo(String id, String title, String note) {
        this.id = id;
        this.title = title;
        this.note = note;
        this.complete = false;
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

    public String getNote() {
        return this.note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }
}
