package me.slackti.notesmatter.ui;


import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import me.slackti.notesmatter.R;
import me.slackti.notesmatter.adapter.BaseAdapter;
import me.slackti.notesmatter.callback.ActionModeCallback;
import me.slackti.notesmatter.touch.TouchListener;

public class BaseActivity extends AppCompatActivity implements TouchListener {
    RecyclerView recView;
    BaseAdapter adapter;
    ActionMode actionMode;
    ActionModeCallback actionModeCallback;

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

    void setupRecyclerView() {
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recView = (RecyclerView) findViewById(R.id.todo_list);
        recView.setLayoutManager(linearLayoutManager);
        recView.setAdapter(adapter);
        recView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if(adapter.getSelectedPos() == linearLayoutManager.findLastVisibleItemPosition()
                        && adapter.getSelectedPos() != RecyclerView.NO_POSITION) {
                    recView.smoothScrollToPosition(adapter.getSelectedPos());
                }
            }
        });
    }
}
