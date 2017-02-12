package me.slackti.notesmatter.listener;


import android.view.View;

import me.slackti.notesmatter.adapter.HistoryAdapter;
import me.slackti.notesmatter.model.Todo;
import me.slackti.notesmatter.touch.ClickListener;

public class UndoneButtonListener extends BaseListener {
    private HistoryAdapter adapter;

    public UndoneButtonListener(HistoryAdapter adapter, ClickListener clickListener) {
        super(clickListener);
        this.adapter = adapter;
    }

    @Override
    public void onClick(View v) {
        Todo todo = adapter.getSelectedItem();
        adapter.onItemUndone(todo.getPosition());

        super.onClick(v);
    }
}
