package me.slackti.notesmatter.listener;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.inputmethodservice.KeyboardView;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import me.slackti.notesmatter.R;
import me.slackti.notesmatter.adapter.TodoAdapter;
import me.slackti.notesmatter.model.Todo;


public class FabListener implements View.OnClickListener {
    private Context context;
    private TodoAdapter adapter;
    private AlertDialog dialog;
    private EditText editText;

    public FabListener(Context context, TodoAdapter adapter) {
        this.context = context;
        this.adapter = adapter;
    }

    @Override
    public void onClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialog_view = inflater.inflate(R.layout.dialog_add, null);

        builder.setView(dialog_view);

        dialog = builder.create();

        final Button add_button = (Button) dialog_view.findViewById(R.id.dialog_add_button);
        add_button.setEnabled(false);

        Button cancel_button = (Button) dialog_view.findViewById(R.id.dialog_cancel_button);

        editText = (EditText) dialog_view.findViewById(R.id.dialog_todo_text);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().trim().isEmpty()) {
                    add_button.setEnabled(false);
                } else {
                    add_button.setEnabled(true);
                }

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
                String input = editText.getText().toString();
                if(!input.isEmpty()) {
                    adapter.addItem(new Todo(input));
                }
                dialog.dismiss();
            }
        });
        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        // getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void checkForNewLine(CharSequence s, int start, int count) {
        int end = start + count;

        for(int i = start; i < end; i++) {
            if (s.charAt(i) == '\n') {      // Enter key detected
                String input = editText.getText().toString().trim();
                if (input.isEmpty()) {      // If string is empty, close dialog
                    dialog.cancel();
                } else {
                    adapter.addItem(new Todo(input));
                    dialog.dismiss();
                }
            }
        }
    }
}
