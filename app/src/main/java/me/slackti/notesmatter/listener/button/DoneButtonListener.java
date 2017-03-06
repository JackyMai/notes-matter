package me.slackti.notesmatter.listener.button;

import android.view.View;

import me.slackti.notesmatter.adapter.TodoAdapter;
import me.slackti.notesmatter.model.Todo;


public class DoneButtonListener extends BaseListener {
    private TodoAdapter adapter;

    public DoneButtonListener(TodoAdapter adapter, TouchListener touchListener) {
        super(touchListener);
        this.adapter = adapter;
    }

    @Override
    public void onClick(View v) {
        Todo todo = adapter.getSelectedItem();
        adapter.onItemDone(todo.getPosition());

        super.onClick(v);
    }
}
