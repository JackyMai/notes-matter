package me.slackti.notesmatter.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import me.slackti.notesmatter.R;
import me.slackti.notesmatter.adapter.HistoryAdapter;
import me.slackti.notesmatter.listener.UndoneButtonListener;

import static android.view.View.GONE;

public class HistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        setTitle("History");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final RelativeLayout actionBar = (RelativeLayout) findViewById(R.id.history_action_bar);
        actionBar.setVisibility(GONE);

        HistoryAdapter adapter = new HistoryAdapter(this, actionBar);

        ImageButton undoneButton = (ImageButton) findViewById(R.id.undone_button);
        undoneButton.setOnClickListener(new UndoneButtonListener(adapter));

        RecyclerView recView = (RecyclerView) findViewById(R.id.history_list);
        recView.setLayoutManager(new LinearLayoutManager(this));
        recView.setAdapter(adapter);
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
