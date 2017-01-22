package me.slackti.notesmatter.ui;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import me.slackti.notesmatter.R;
import me.slackti.notesmatter.adapter.SimpleItemTouchHelperCallback;
import me.slackti.notesmatter.adapter.TodoAdapter;
import me.slackti.notesmatter.listener.FabListener;
import me.slackti.notesmatter.model.Todo;

import static android.view.View.GONE;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ArrayList<Todo> todoList = new ArrayList<>();

        TodoAdapter adapter = new TodoAdapter(todoList, this);

        final RelativeLayout bar_container = (RelativeLayout) findViewById(R.id.bar_container);
        bar_container.setVisibility(GONE);

        RecyclerView recView = (RecyclerView) findViewById(R.id.todo_list);
        recView.setLayoutManager(new LinearLayoutManager(this));
        recView.setAdapter(adapter);

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recView);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new FabListener(this, adapter));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
