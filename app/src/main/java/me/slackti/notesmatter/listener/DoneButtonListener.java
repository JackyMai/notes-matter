package me.slackti.notesmatter.listener;

import android.view.View;

import me.slackti.notesmatter.adapter.TodoAdapter;
import me.slackti.notesmatter.model.Todo;
import me.slackti.notesmatter.touch.ClickListener;


public class DoneButtonListener extends BaseListener {
    private TodoAdapter adapter;

    public DoneButtonListener(TodoAdapter adapter, ClickListener clickListener) {
        super(clickListener);
        this.adapter = adapter;
    }

    @Override
    public void onClick(View v) {
        Todo todo = adapter.getSelectedItem();
        adapter.onItemDone(todo.getPosition());

        super.onClick(v);
    }
}
