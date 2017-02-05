package me.slackti.notesmatter.listener;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;

import me.slackti.notesmatter.adapter.TodoAdapter;
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
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Do you want to delete this todo?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Todo todo = adapter.getSelectedItem();
                adapter.onItemDismiss(todo.getPosition());
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Oops", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }
}
