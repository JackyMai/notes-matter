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

import java.util.ArrayList;

import me.slackti.notesmatter.R;
import me.slackti.notesmatter.adapter.SimpleItemTouchHelperCallback;
import me.slackti.notesmatter.adapter.TodoAdapter;
import me.slackti.notesmatter.listener.FabListener;
import me.slackti.notesmatter.model.Todo;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recView;
    private TodoAdapter adapter;

    private ArrayList<Todo> todoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recView = (RecyclerView) findViewById(R.id.todo_list);
        recView.setLayoutManager(new LinearLayoutManager(this));

        todoList = new ArrayList<>();

        adapter = new TodoAdapter(todoList, this);
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
