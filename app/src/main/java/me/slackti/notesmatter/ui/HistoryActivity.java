package me.slackti.notesmatter.ui;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import me.slackti.notesmatter.R;
import me.slackti.notesmatter.adapter.HistoryAdapter;
import me.slackti.notesmatter.callback.ActionModeCallback;
import me.slackti.notesmatter.listener.UndoneButtonListener;

import static android.view.View.GONE;

public class HistoryActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        setTitle("History");

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        final LinearLayout actionBar = (LinearLayout) findViewById(R.id.history_action_bar);
        actionBar.setVisibility(GONE);

        adapter = new HistoryAdapter(this, this, actionBar);
//        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
//            @Override
//            public void onItemRangeInserted(int positionStart, int itemCount) {
//                checkEmptyState();
//            }
//
//            @Override
//            public void onItemRangeRemoved(int positionStart, int itemCount) {
//                checkEmptyState();
//            }
//        });

        actionModeCallback = new ActionModeCallback(this, adapter);

        // emptyState is set to visible to prevent the fade-in animation from starting
        emptyState = (RelativeLayout) findViewById(R.id.empty_state_history);

        ImageButton undoneButton = (ImageButton) findViewById(R.id.undone_button);
        undoneButton.setOnClickListener(new UndoneButtonListener((HistoryAdapter) adapter, this));

        // RecyclerView
        setupRecyclerView();

        // Animations
        setupAnimation();

        // Empty State
//        checkEmptyState();
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
