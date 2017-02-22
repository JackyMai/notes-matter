package me.slackti.notesmatter.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import me.slackti.notesmatter.R;
import me.slackti.notesmatter.adapter.TodoAdapter;
import me.slackti.notesmatter.callback.ActionModeCallback;
import me.slackti.notesmatter.callback.SimpleItemTouchHelperCallback;
import me.slackti.notesmatter.listener.DeleteButtonListener;
import me.slackti.notesmatter.listener.DoneButtonListener;
import me.slackti.notesmatter.listener.EditButtonListener;
import me.slackti.notesmatter.listener.FabListener;
import me.slackti.notesmatter.touch.ItemTouchHelperAdapter;

import static android.view.View.GONE;

public class MainActivity extends BaseActivity {
    RelativeLayout emptyState;

    Animation fadeInAnim;
    Animation fadeOutAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final LinearLayout actionBar = (LinearLayout) findViewById(R.id.main_action_bar);
        actionBar.setVisibility(GONE);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        adapter = new TodoAdapter(this, this, actionBar, fab);
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                checkEmptyState();
            }

            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                checkEmptyState();
            }
        });

        actionModeCallback = new ActionModeCallback(this, adapter);
        fab.setOnClickListener(new FabListener(this, (TodoAdapter) adapter));

        emptyState = (RelativeLayout) findViewById(R.id.empty_state_main);

        // ImageButton
        setupActionBarButtons();

        // RecyclerView
        setupRecyclerView();

        // ItemTouchHelper
        setupItemTouchHelper();

        // Animations
        setupAnimation();

        // Empty State
        checkEmptyState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.history_button) {
            Intent intent = new Intent(this, HistoryActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRestart() {
        int start = adapter.getItemCount();
        adapter.getDatabaseItems();
        adapter.notifyItemRangeInserted(start, adapter.getItemCount()-start);
        super.onRestart();
    }

    private void setupActionBarButtons() {
        ImageButton doneButton = (ImageButton) findViewById(R.id.done_button);
        doneButton.setOnClickListener(new DoneButtonListener((TodoAdapter) adapter, this));

        ImageButton editButton = (ImageButton) findViewById(R.id.edit_button);
        editButton.setOnClickListener(new EditButtonListener(this, (TodoAdapter) adapter, this));

        ImageButton deleteButton = (ImageButton) findViewById(R.id.delete_button);
        deleteButton.setOnClickListener(new DeleteButtonListener(this, (TodoAdapter) adapter, this));
    }

    private void setupItemTouchHelper() {
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(this, (ItemTouchHelperAdapter) adapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recView);
    }

    private void setupAnimation() {
        fadeInAnim = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        fadeInAnim.setDuration(225);
        fadeOutAnim = AnimationUtils.loadAnimation(this, android.R.anim.fade_out);
        fadeOutAnim.setDuration(195);
    }

    private void checkEmptyState() {
        // Animations are only played when it needs to to avoid peculiar visual bugs and delays
        if(adapter.getItemCount() == 0) {
            if(recView.getVisibility() != View.INVISIBLE) {
                recView.startAnimation(fadeOutAnim);
            }
            recView.setVisibility(View.INVISIBLE);

            if(emptyState.getVisibility() != View.VISIBLE) {
                emptyState.startAnimation(fadeInAnim);
            }
            emptyState.setVisibility(View.VISIBLE);
        } else {
            if(recView.getVisibility() != View.VISIBLE) {
                recView.startAnimation(fadeInAnim);
            }
            recView.setVisibility(View.VISIBLE);

            // Starting fade-in animation would cause emptyState to appear for a brief moment
            emptyState.setVisibility(View.INVISIBLE);
        }
    }
}
