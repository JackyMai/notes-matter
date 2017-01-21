package me.slackti.notesmatter.ui;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import me.slackti.notesmatter.R;
import me.slackti.notesmatter.adapter.TodoAdapter;
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
        todoList.add(new Todo("Get Apples"));
        todoList.add(new Todo("Buy Bananas"));
        todoList.add(new Todo("Sell Sheep"));

        adapter = new TodoAdapter(todoList, this);
        recView.setAdapter(adapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(createHelperCallBack());
        itemTouchHelper.attachToRecyclerView(recView);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                View dialog_view = getLayoutInflater().inflate(R.layout.dialog_add, null);

                final EditText input = (EditText) dialog_view.findViewById(R.id.dialog_todo_text);
                Button add_button = (Button) dialog_view.findViewById(R.id.dialog_add_button);
                Button cancel_button = (Button) dialog_view.findViewById(R.id.dialog_cancel_button);

                builder.setView(dialog_view);
                final AlertDialog dialog = builder.create();

                add_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!input.getText().toString().isEmpty()) {
                            Toast.makeText(MainActivity.this, "Successfully added new todo", Toast.LENGTH_LONG).show();
                        }
                        dialog.dismiss();
                    }
                });
                cancel_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });

                dialog.show();
            }
        });
    }

    private ItemTouchHelper.Callback createHelperCallBack() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback =
                new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN,
                        ItemTouchHelper.LEFT | ItemTouchHelper. RIGHT) {
                    @Override
                    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                                          RecyclerView.ViewHolder target) {
                        moveItem(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                        return true;
                    }

                    @Override
                    public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                        deleteItem(viewHolder.getAdapterPosition());
                    }
                };
        return simpleItemTouchCallback;
    }

    private void addItem(Todo todo) {
        todoList.add(todo);
        adapter.notifyItemInserted(todoList.indexOf(todo));
    }

    private void moveItem(int oldPos, int newPos) {
        Todo todo = (Todo) todoList.get(oldPos);
        todoList.remove(oldPos);
        todoList.add(newPos, todo);
        adapter.notifyItemMoved(oldPos, newPos);
    }

    private void deleteItem(int position) {
        todoList.remove(position);
        adapter.notifyItemRemoved(position);
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
