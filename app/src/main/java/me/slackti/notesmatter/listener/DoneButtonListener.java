package me.slackti.notesmatter.listener;

import android.view.View;

import me.slackti.notesmatter.adapter.TodoAdapter;
import me.slackti.notesmatter.model.Todo;


public class DoneButtonListener implements View.OnClickListener{
    private TodoAdapter adapter;

    public DoneButtonListener(TodoAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public void onClick(View v) {
        Todo todo = adapter.getSelectedItem();
        adapter.onItemDone(todo.getPosition());
    }
}
