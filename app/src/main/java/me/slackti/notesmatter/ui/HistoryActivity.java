package me.slackti.notesmatter.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import me.slackti.notesmatter.R;
import me.slackti.notesmatter.adapter.HistoryAdapter;
import me.slackti.notesmatter.callback.ActionModeCallback;
import me.slackti.notesmatter.listener.UndoneButtonListener;
import me.slackti.notesmatter.touch.ClickListener;

import static android.view.View.GONE;

public class HistoryActivity extends AppCompatActivity implements ClickListener {

    private HistoryAdapter adapter;
    private ActionMode actionMode;
    private ActionModeCallback actionModeCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        setTitle("History");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final RelativeLayout actionBar = (RelativeLayout) findViewById(R.id.history_action_bar);
        actionBar.setVisibility(GONE);

        adapter = new HistoryAdapter(this, this, actionBar);
        actionModeCallback = new ActionModeCallback(adapter);

        ImageButton undoneButton = (ImageButton) findViewById(R.id.undone_button);
        undoneButton.setOnClickListener(new UndoneButtonListener(adapter));

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        RecyclerView recView = (RecyclerView) findViewById(R.id.todo_list);
        recView.setLayoutManager(new LinearLayoutManager(this));
        recView.setAdapter(adapter);
        recView.addItemDecoration(new DividerItemDecoration(this, layoutManager.getOrientation()));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClicked(int position) {
        if(actionMode == null) {
            actionMode = startSupportActionMode(actionModeCallback);
        }

        toggleSelected(position);
    }

    private void toggleSelected(int position) {
        adapter.toggleSelected(position);

        if(adapter.getSelectedPos() == -1) {
            actionMode.finish();
            actionMode = null;
        }
    }
}
