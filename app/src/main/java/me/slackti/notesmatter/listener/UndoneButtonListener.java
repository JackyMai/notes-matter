package me.slackti.notesmatter.listener;


import android.view.View;

import me.slackti.notesmatter.adapter.HistoryAdapter;
import me.slackti.notesmatter.model.Todo;
import me.slackti.notesmatter.touch.TouchListener;

public class UndoneButtonListener extends BaseListener {
    private HistoryAdapter adapter;

    public UndoneButtonListener(HistoryAdapter adapter, TouchListener clickListener) {
        super(clickListener);
        this.adapter = adapter;
    }

    @Override
    public void onClick(View v) {
        adapter.onItemUndone(adapter.getSelectedPos());
        super.onClick(v);
    }
}
