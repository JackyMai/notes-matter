package me.slackti.notesmatter.listener;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.renderscript.ScriptGroup;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import me.slackti.notesmatter.R;
import me.slackti.notesmatter.adapter.TodoAdapter;
import me.slackti.notesmatter.model.Todo;


public class FabListener implements View.OnClickListener {
    private Context context;
    private TodoAdapter adapter;

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

        final Button add_button = (Button) dialog_view.findViewById(R.id.dialog_add_button);
        add_button.setEnabled(false);

        Button cancel_button = (Button) dialog_view.findViewById(R.id.dialog_cancel_button);

        final EditText editText = (EditText) dialog_view.findViewById(R.id.dialog_todo_text);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().trim().isEmpty()) {
                    add_button.setEnabled(false);
                } else {
                    add_button.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        final AlertDialog dialog = builder.create();

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

        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }
}
