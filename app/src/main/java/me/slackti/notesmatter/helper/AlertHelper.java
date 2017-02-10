package me.slackti.notesmatter.helper;


import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import me.slackti.notesmatter.adapter.TodoAdapter;
import me.slackti.notesmatter.touch.ItemTouchHelperAdapter;

public class AlertHelper {

    public static void createDeleteDialog(Context context, final ItemTouchHelperAdapter adapter, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Do you want to delete this todo?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                adapter.onItemDismiss(position);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Oops", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ((TodoAdapter) adapter).notifyItemChanged(position);
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

}
