package me.slackti.notesmatter.listener.button;


import android.view.View;

import me.slackti.notesmatter.adapter.HistoryAdapter;

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
