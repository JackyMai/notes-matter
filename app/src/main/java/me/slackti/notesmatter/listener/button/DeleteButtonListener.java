package me.slackti.notesmatter.listener.button;

import android.content.Context;
import android.view.View;

import me.slackti.notesmatter.adapter.TodoAdapter;
import me.slackti.notesmatter.helper.AlertHelper;


public class DeleteButtonListener extends BaseListener {
    private Context context;
    private TodoAdapter adapter;

    public DeleteButtonListener(Context context, TodoAdapter adapter, TouchListener touchListener) {
        super(touchListener);
        this.context = context;
        this.adapter = adapter;
    }

    @Override
    public void onClick(View v) {
        int position = adapter.getSelectedItem().getPosition();

        AlertHelper alertHelper = new AlertHelper();
        alertHelper.createDeleteDialog(context, adapter, position);

        super.onClick(v);
    }
}
