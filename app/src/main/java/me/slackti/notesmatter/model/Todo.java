package me.slackti.notesmatter.model;


public class Todo {
    private String title;
    private String note;
    private boolean complete;

    public Todo(String title) {
        this.title = title;
        this.complete = false;
    }

    public Todo(String title, String note) {
        this.title = title;
        this.note = note;
        this.complete = false;
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
