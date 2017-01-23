package me.slackti.notesmatter.listener;

import android.view.View;

import me.slackti.notesmatter.adapter.TodoAdapter;
import me.slackti.notesmatter.model.Todo;

/**
 * Created by Jacky on 24/01/2017.
 */

public class DeleteButtonListener implements View.OnClickListener {
    private TodoAdapter adapter;

    public DeleteButtonListener(TodoAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public void onClick(View v) {
        Todo todo = adapter.getSelectedItem();
        adapter.onItemDismiss(todo.getPosition());
    }
}
