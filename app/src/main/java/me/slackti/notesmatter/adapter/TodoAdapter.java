package me.slackti.notesmatter.adapter;


import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import me.slackti.notesmatter.R;
import me.slackti.notesmatter.database.DatabaseHelper;
import me.slackti.notesmatter.model.Todo;

public class TodoAdapter extends RecyclerView.Adapter<TodoHolder> {

    private DatabaseHelper databaseHelper;

    private ArrayList<Todo> todoList;
    private LayoutInflater inflater;
    private Context context;

    public TodoAdapter(ArrayList<Todo> todoList, Context context) {
        this.todoList = todoList;
        this.inflater = LayoutInflater.from(context);
        this.context = context;

        databaseHelper = new DatabaseHelper(context);

        getItems();
    }

    @Override
    public TodoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.todo_item, parent, false);

        return new TodoHolder(view);
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

    private void getItems() {
        Cursor listData = databaseHelper.getListContents();
        if(listData.getCount() > 0) {
            while(listData.moveToNext()) {
                Todo newTodo = new Todo(listData.getString(0), listData.getString(1));
                todoList.add(newTodo);
            }
        }
    }

    public void addItem(Todo todo) {
        long id = databaseHelper.addData(todo);
        
        if(id != -1) {
            Toast.makeText(context, "Successfully added todo!", Toast.LENGTH_LONG).show();

            todo.setId(Long.toString(id));
            todoList.add(todo);
            this.notifyItemInserted(todoList.indexOf(todo));
        } else {
            Toast.makeText(context, "404 something went wrong", Toast.LENGTH_LONG).show();
        }
    }

    public void moveItem(int oldPos, int newPos) {
        Todo todo = todoList.get(oldPos);
        todoList.remove(oldPos);
        todoList.add(newPos, todo);

        this.notifyItemMoved(oldPos, newPos);
    }

    public void deleteItem(int position) {
        Todo todo = todoList.get(position);

        if(databaseHelper.deleteData(todo.getId())) {
            Toast.makeText(context, "Successfully deleted todo!", Toast.LENGTH_LONG).show();

            todoList.remove(position);
            this.notifyItemRemoved(position);
        } else {
            Toast.makeText(context, "Failed to remove item", Toast.LENGTH_LONG).show();
        }
    }
}
