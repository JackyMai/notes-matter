package me.slackti.notesmatter.listener;


import android.content.Context;
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
import me.slackti.notesmatter.helper.AlertHelper;
import me.slackti.notesmatter.model.Todo;
import me.slackti.notesmatter.touch.ClickListener;


public class EditButtonListener extends BaseListener {
    private Context context;
    private TodoAdapter adapter;

    public EditButtonListener(Context context, TodoAdapter adapter, ClickListener clickListener) {
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
