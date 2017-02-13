package me.slackti.notesmatter.listener;

import android.content.Context;
import android.view.View;

import me.slackti.notesmatter.adapter.TodoAdapter;
import me.slackti.notesmatter.helper.AlertHelper;


public class FabListener implements View.OnClickListener {
    private Context context;
    private TodoAdapter adapter;

    public FabListener(Context context, TodoAdapter adapter) {
        this.context = context;
        this.adapter = adapter;
    }

    @Override
    public void onClick(View v) {
        AlertHelper alertHelper = new AlertHelper();
        alertHelper.createAddDialog(context, adapter);
    }
}
