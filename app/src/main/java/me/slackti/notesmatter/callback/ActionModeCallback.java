package me.slackti.notesmatter.callback;


import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;

import me.slackti.notesmatter.R;
import me.slackti.notesmatter.adapter.BaseAdapter;
import me.slackti.notesmatter.ui.BaseActivity;

public class ActionModeCallback implements ActionMode.Callback {
    private BaseActivity activity;
    private BaseAdapter adapter;

    public ActionModeCallback(BaseActivity activity, BaseAdapter adapter) {
        this.activity = activity;
        this.adapter = adapter;
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        mode.getMenuInflater().inflate(R.menu.menu_action_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        if(Build.VERSION.SDK_INT >= 21) {
            activity.getWindow().setStatusBarColor(ContextCompat.getColor(activity.getApplicationContext(), R.color.colorActionModePrimaryDark));
            return true;
        }

        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        if(Build.VERSION.SDK_INT >= 21) {
            activity.getWindow().setStatusBarColor(ContextCompat.getColor(activity.getApplicationContext(), R.color.colorSecondaryDark));
        }

        activity.setActionMode(null);
        adapter.clearSelection();
    }
}
