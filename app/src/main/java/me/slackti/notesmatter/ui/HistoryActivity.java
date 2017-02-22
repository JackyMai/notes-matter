package me.slackti.notesmatter.ui;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import me.slackti.notesmatter.R;
import me.slackti.notesmatter.adapter.HistoryAdapter;
import me.slackti.notesmatter.callback.ActionModeCallback;
import me.slackti.notesmatter.listener.UndoneButtonListener;

import static android.view.View.GONE;

public class HistoryActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        setTitle("History");

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        final LinearLayout actionBar = (LinearLayout) findViewById(R.id.history_action_bar);
        actionBar.setVisibility(GONE);

        adapter = new HistoryAdapter(this, this, actionBar);
        actionModeCallback = new ActionModeCallback(this, adapter);

        ImageButton undoneButton = (ImageButton) findViewById(R.id.undone_button);
        undoneButton.setOnClickListener(new UndoneButtonListener((HistoryAdapter) adapter, this));

        // RecyclerView
        setupRecyclerView();
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

}
