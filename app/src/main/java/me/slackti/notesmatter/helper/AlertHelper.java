package me.slackti.notesmatter.helper;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Calendar;

import me.slackti.notesmatter.R;
import me.slackti.notesmatter.adapter.TodoAdapter;
import me.slackti.notesmatter.model.Todo;
import me.slackti.notesmatter.touch.ItemTouchHelperAdapter;

public class AlertHelper {

    private final int WORD_LIMIT = 140;

    private AlertDialog dialog;
    private EditText editText;
    private String deadline;

    private TextView word_count;
    private ImageButton date_button;
    private ImageButton add_button;

    private Calendar calendar;

    private void createInputDialog(final Context context) {
        @SuppressLint("InflateParams")
        View dialog_view = LayoutInflater.from(context).inflate(R.layout.dialog_add, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(dialog_view);

        dialog = builder.create();

        word_count = (TextView) dialog_view.findViewById(R.id.word_count);
        final int defaultColor = word_count.getCurrentTextColor();

        calendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                deadline = dayOfMonth + "/" + month + "/" + year;  // Set deadline if date is picked
                date_button.setColorFilter(ResourcesCompat.getColor(context.getResources(), R.color.colorAccent, null));
            }
        };

        date_button = (ImageButton) dialog_view.findViewById(R.id.dialog_date_button);
        date_button.setColorFilter(Color.GRAY);
        date_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                        dateListener,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Remove", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deadline = null;
                        date_button.setColorFilter(Color.GRAY);
                        datePickerDialog.dismiss();
                    }
                });
                datePickerDialog.show();
            }
        });

        add_button = (ImageButton) dialog_view.findViewById(R.id.dialog_add_button);
        editText = (EditText) dialog_view.findViewById(R.id.dialog_todo_text);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int remainingChar = WORD_LIMIT - s.toString().trim().length();

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

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().trim().isEmpty()) {   // Input is not whitespace
                    if(s.charAt(s.length()-1) == '\n') {    // Last character is new line
                        if(add_button.isEnabled()) {
                            add_button.callOnClick();
                        } else {
                            // Last character is new line but button is disabled, enter key will do nothing
                            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                            s.delete(s.length()-1, s.length());
                        }
                    }
                } else {
                    dialog.dismiss();   // Input is whitespace, close dialog
                }
            }
        });

        if(dialog.getWindow() != null) {
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    public void createAddDialog(final Context context, final TodoAdapter adapter) {
        createInputDialog(context);

        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = editText.getText().toString().trim();
                if(!input.isEmpty()) {
                    adapter.onItemAdd(new Todo(input, deadline));
                }
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void createEditDialog(final Context context, final TodoAdapter adapter, final int position) {
        createInputDialog(context);

        final Todo todo = adapter.getTodoItem(position);

        // Set deadline for calendar if the item has one
        if(todo.getDeadline() != null) {
            // Deadline format in String = "dd/MM/yyyy"
            String[] deadline = todo.getDeadline().split("/");
            calendar.set(Calendar.YEAR, Integer.parseInt(deadline[2]));
            calendar.set(Calendar.MONTH, Integer.parseInt(deadline[1]));
            calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(deadline[0]));
            date_button.setColorFilter(ResourcesCompat.getColor(context.getResources(), R.color.colorAccent, null));
        }

        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = editText.getText().toString().trim();
                if(!input.isEmpty()) {
                    todo.setTitle(input);
                    todo.setDeadline(deadline);
                    adapter.onItemUpdate(todo);
                }
                dialog.dismiss();
            }
        });

        word_count.setText(String.valueOf(WORD_LIMIT - todo.getTitle().length()));
        editText.setText(todo.getTitle());
        editText.setSelection(editText.getText().length());

        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // Used to trigger the swipe recover animation
                adapter.notifyItemChanged(position);
            }
        });
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

}
