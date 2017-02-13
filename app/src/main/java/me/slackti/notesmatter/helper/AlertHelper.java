package me.slackti.notesmatter.helper;


import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;

import me.slackti.notesmatter.R;
import me.slackti.notesmatter.adapter.TodoAdapter;
import me.slackti.notesmatter.model.Todo;
import me.slackti.notesmatter.touch.ItemTouchHelperAdapter;

public class AlertHelper {

    private TodoAdapter adapter;
    private AlertDialog dialog;
    private EditText editText;
    private Todo todo;

    public void createEditDialog(Context context, final TodoAdapter adapter, final int position) {
        this.adapter = adapter;

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialog_view = inflater.inflate(R.layout.dialog_add, null);

        builder.setView(dialog_view);

        final ImageButton add_button = (ImageButton) dialog_view.findViewById(R.id.dialog_add_button);

        dialog = builder.create();
        todo = adapter.getTodoItem(position);

        editText = (EditText) dialog_view.findViewById(R.id.dialog_todo_text);
        editText.setText(todo.getTitle());
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkForNewLine(s, start, count);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void afterTextChanged(Editable s) {}
        });

        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = editText.getText().toString().trim();
                if(!input.isEmpty()) {
                    todo.setTitle(input);
                    adapter.onItemUpdate(todo);
                }

                dialog.dismiss();
            }
        });

        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                adapter.notifyItemChanged(position);
            }
        });

        if(dialog.getWindow() != null) {
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        }
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    public void createDeleteDialog(Context context, final ItemTouchHelperAdapter adapter, final int position) {
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

    private void checkForNewLine(CharSequence s, int start, int count) {
        int end = start + count;

        for(int i = start; i < end; i++) {
            if (s.charAt(i) == '\n') {      // Enter key detected
                String input = editText.getText().toString().trim();
                if (!input.isEmpty()) {      // If string is empty, close dialog
                    todo.setTitle(input);
                    adapter.onItemUpdate(todo);
                }

                dialog.dismiss();
            }
        }
    }
}
