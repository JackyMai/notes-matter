package me.slackti.notesmatter.listener.button;


import android.content.Context;
import android.view.View;

import me.slackti.notesmatter.adapter.TodoAdapter;
import me.slackti.notesmatter.helper.AlertHelper;


public class EditButtonListener extends BaseListener {
    private Context context;
    private TodoAdapter adapter;

    public EditButtonListener(Context context, TodoAdapter adapter, TouchListener clickListener) {
        super(clickListener);
        this.context = context;
        this.adapter = adapter;
    }

    @Override
    public void onClick(View v) {
        AlertHelper alertHelper = new AlertHelper();
        alertHelper.createEditDialog(context, adapter, adapter.getSelectedPos());

        super.onClick(v);
    }

}
