package me.slackti.notesmatter.listener;

import android.util.Log;
import android.view.View;

import me.slackti.notesmatter.adapter.TodoAdapter;
import me.slackti.notesmatter.model.Todo;


public class DeleteButtonListener implements View.OnClickListener {
    private TodoAdapter adapter;

    public DeleteButtonListener(TodoAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public void onClick(View v) {
        Todo todo = adapter.getSelectedItem();
        Log.d("TEST RESPONSE", "Todo position retrieved: " + todo.getPosition());
        adapter.onItemDismiss(todo.getPosition());
    }
}
