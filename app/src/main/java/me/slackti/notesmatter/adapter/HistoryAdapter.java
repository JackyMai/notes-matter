package me.slackti.notesmatter.adapter;


import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;

import me.slackti.notesmatter.R;
import me.slackti.notesmatter.database.DatabaseHelper;
import me.slackti.notesmatter.model.Todo;
import me.slackti.notesmatter.model.TodoHolder;

public class HistoryAdapter extends RecyclerView.Adapter<TodoHolder> {

    private LayoutInflater inflater;
    private ArrayList<Todo> todoList;
    private DatabaseHelper databaseHelper;

    private int selectedPos = -1;

    public HistoryAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        todoList = new ArrayList<>();
        databaseHelper = new DatabaseHelper(context);

        getCompletedItems();
    }

    @Override
    public TodoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = inflater.inflate(R.layout.todo_item, parent, false);
        final TodoHolder todoHolder = new TodoHolder(view);

        return todoHolder;
    }

    @Override
    public void onBindViewHolder(TodoHolder holder, int position) {
        Todo todo = todoList.get(position);
        holder.setTitle(todo.getTitle());
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    private void getCompletedItems() {
        Cursor listData = databaseHelper.getCompletedItems();

        if(listData.getCount() > 0) {
            while(listData.moveToNext()) {
                Todo todo = new Todo(listData.getString(0),
                        listData.getString(1),
                        listData.getInt(2),
                        listData.getInt(3) != 0);   // Convert int to boolean

                todoList.add(todo);
            }
        }
    }
}
