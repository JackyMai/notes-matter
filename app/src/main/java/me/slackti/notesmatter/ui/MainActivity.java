package me.slackti.notesmatter.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import me.slackti.notesmatter.R;
import me.slackti.notesmatter.adapter.TodoAdapter;
import me.slackti.notesmatter.callback.ActionModeCallback;
import me.slackti.notesmatter.callback.SimpleItemTouchHelperCallback;
import me.slackti.notesmatter.listener.DeleteButtonListener;
import me.slackti.notesmatter.listener.DoneButtonListener;
import me.slackti.notesmatter.listener.EditButtonListener;
import me.slackti.notesmatter.listener.FabListener;
import me.slackti.notesmatter.touch.ClickListener;

import static android.view.View.GONE;

public class MainActivity extends AppCompatActivity implements ClickListener {

    private TodoAdapter adapter;
    private ActionMode actionMode;
    private ActionModeCallback actionModeCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final RelativeLayout actionBar = (RelativeLayout) findViewById(R.id.main_action_bar);
        actionBar.setVisibility(GONE);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        adapter = new TodoAdapter(this, this, actionBar, fab);

        actionModeCallback = new ActionModeCallback(adapter);

        fab.setOnClickListener(new FabListener(this, adapter));

        // ImageButton
        ImageButton doneButton = (ImageButton) findViewById(R.id.done_button);
        doneButton.setOnClickListener(new DoneButtonListener(adapter));

        ImageButton editButton = (ImageButton) findViewById(R.id.edit_button);
        editButton.setOnClickListener(new EditButtonListener(this, adapter));

        ImageButton deleteButton = (ImageButton) findViewById(R.id.delete_button);
        deleteButton.setOnClickListener(new DeleteButtonListener(this, adapter));

        // RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        RecyclerView recView = (RecyclerView) findViewById(R.id.todo_list);
        recView.setLayoutManager(layoutManager);
        recView.setAdapter(adapter);
        recView.addItemDecoration(new DividerItemDecoration(this, layoutManager.getOrientation()));

        // ItemTouchHelper
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(this, adapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recView);
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
        super.onRestart();
        adapter.getDatabaseItems();
        adapter.notifyItemInserted(adapter.getItemCount());
    }

}
