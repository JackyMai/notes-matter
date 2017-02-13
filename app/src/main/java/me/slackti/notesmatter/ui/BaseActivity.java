package me.slackti.notesmatter.ui;


import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;

import me.slackti.notesmatter.adapter.BaseAdapter;
import me.slackti.notesmatter.callback.ActionModeCallback;
import me.slackti.notesmatter.touch.TouchListener;

public class BaseActivity extends AppCompatActivity implements TouchListener {
    protected BaseAdapter adapter;
    protected ActionMode actionMode;
    protected ActionModeCallback actionModeCallback;

    @Override
    public void onItemClicked(int position) {
        if(actionMode == null) {
            actionMode = startSupportActionMode(actionModeCallback);
        }

        toggleSelected(position);
    }

    @Override
    public void onSelectionCleared() {
        if(actionMode != null) {
            actionMode.finish();
        }
    }

    private void toggleSelected(int position) {
        adapter.toggleSelected(position);

        if(adapter.getSelectedPos() == -1) {
            actionMode.finish();
        }
    }

    public void setActionMode(ActionMode actionMode) {
        this.actionMode = actionMode;
    }
}
