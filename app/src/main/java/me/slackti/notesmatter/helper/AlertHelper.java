package me.slackti.notesmatter.helper;


import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import me.slackti.notesmatter.R;
import me.slackti.notesmatter.adapter.TodoAdapter;
import me.slackti.notesmatter.model.Todo;
import me.slackti.notesmatter.touch.ItemTouchHelperAdapter;

public class AlertHelper {

    private final int WORD_LIMIT = 140;
    private final int ADD = 0;
    private final int EDIT = 1;

    private TodoAdapter adapter;
    private AlertDialog dialog;
    private EditText editText;
    private Todo todo;

    private TextView word_count;
    private ImageButton add_button;

    private void createInputDialog(final Context context, final TodoAdapter adapter, final int intention) {
        this.adapter = adapter;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialog_view = inflater.inflate(R.layout.dialog_add, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(dialog_view);

        dialog = builder.create();

        word_count = (TextView) dialog_view.findViewById(R.id.word_count);
        final int defaultColor = word_count.getCurrentTextColor();

        add_button = (ImageButton) dialog_view.findViewById(R.id.dialog_add_button);
        editText = (EditText) dialog_view.findViewById(R.id.dialog_todo_text);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!checkForNewLine(s, start, count, intention)) {
                    int remainingChar = WORD_LIMIT - s.length();
                    word_count.setText(String.valueOf(remainingChar));

                    if(remainingChar < 0) {
                        word_count.setTextColor(Color.RED);
                        add_button.setEnabled(false);
                        add_button.setColorFilter(Color.GRAY);
                    } else {
                        word_count.setTextColor(defaultColor);
                        add_button.setEnabled(true);
                        add_button.setColorFilter(ResourcesCompat.getColor(context.getResources(), R.color.colorAccent, null));
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void afterTextChanged(Editable s) {}
        });

        if(dialog.getWindow() != null) {
            // getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        }
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    public void createAddDialog(final Context context, final TodoAdapter adapter) {
        createInputDialog(context, adapter, ADD);

        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = editText.getText().toString().trim();
                if(!input.isEmpty()) {
                    adapter.onItemAdd(new Todo(input));
                }
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void createEditDialog(final Context context, final TodoAdapter adapter, final int position) {
        createInputDialog(context, adapter, EDIT);

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

        todo = adapter.getTodoItem(position);
        word_count.setText(String.valueOf(WORD_LIMIT - todo.getTitle().length()));
        editText.setText(todo.getTitle());

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

    private boolean checkForNewLine(CharSequence s, int start, int count, int intention) {
        int end = start + count;

        for(int i = start; i < end; i++) {
            if (s.charAt(i) == '\n') {      // Enter key detected
                String input = editText.getText().toString().trim();
                if (!input.isEmpty()) {      // If string is empty, close dialog
                    if(intention == ADD) {
                        adapter.onItemAdd(new Todo(input));
                    } else {
                        todo.setTitle(input);
                        adapter.onItemUpdate(todo);
                    }
                }

                dialog.dismiss();
                return true;
            }
        }

        return false;
    }
}
