package me.slackti.notesmatter.listener;


import android.view.View;

import me.slackti.notesmatter.adapter.HistoryAdapter;
import me.slackti.notesmatter.model.Todo;

public class UndoneButtonListener implements View.OnClickListener {
    private HistoryAdapter adapter;

    public UndoneButtonListener(HistoryAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public void onClick(View v) {
        Todo todo = adapter.getSelectedItem();
        adapter.onItemUndone(todo.getPosition());
    }
}
