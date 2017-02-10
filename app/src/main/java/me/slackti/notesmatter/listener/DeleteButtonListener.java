package me.slackti.notesmatter.listener;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;

import me.slackti.notesmatter.adapter.TodoAdapter;
import me.slackti.notesmatter.helper.AlertHelper;
import me.slackti.notesmatter.model.Todo;


public class DeleteButtonListener implements View.OnClickListener {
    private Context context;
    private TodoAdapter adapter;

    public DeleteButtonListener(Context context, TodoAdapter adapter) {
        this.context = context;
        this.adapter = adapter;
    }

    @Override
    public void onClick(View v) {
        int position = adapter.getSelectedItem().getPosition();
        AlertHelper.createDeleteDialog(context, adapter, position);
    }
}
