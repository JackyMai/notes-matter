package me.slackti.notesmatter.adapter;


import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import me.slackti.notesmatter.R;
import me.slackti.notesmatter.database.DatabaseHelper;
import me.slackti.notesmatter.model.Todo;
import me.slackti.notesmatter.model.TodoHolder;

public class TodoAdapter extends RecyclerView.Adapter<TodoHolder> implements ItemTouchHelperAdapter {

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
                Todo newTodo = new Todo(listData.getString(0), listData.getString(1), listData.getInt(2));
                todoList.add(newTodo);
            }

            // Sort arraylist according to item's position
            Collections.sort(todoList, new Comparator<Todo>() {
                @Override
                public int compare(Todo todo1, Todo todo2) {
                    return todo1.getPosition()-todo2.getPosition();
                }
            });
        }
    }

    @Override
    public void onItemAdd(Todo todo) {
        todo.setPosition(todoList.size());

        long id = databaseHelper.addData(todo);

        if(id != -1) {
            todo.setId(Long.toString(id));
            todoList.add(todo);

            this.notifyItemInserted(todoList.indexOf(todo));

            Toast.makeText(context, "ID: " + todo.getId() + ", Position: " + todo.getPosition(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, "404 something went wrong", Toast.LENGTH_LONG).show();
        }
    }

//    @Override
//    public void onItemMove(int oldPos, int newPos) {
//        if(databaseHelper.updateData(todoList, oldPos, newPos)) {
//            todoList.get(oldPos).setPosition(newPos);
//            todoList.get(newPos).setPosition(oldPos);
//
//            if(oldPos < newPos) {   // Moved down the list
//                Collections.swap(todoList, oldPos, oldPos+1);
//            } else {                // Moved up the list
//                Collections.swap(todoList, oldPos, oldPos-1);
//            }
//
//            this.notifyItemMoved(oldPos, newPos);
//
//            Toast.makeText(context, "Got it bro", Toast.LENGTH_LONG).show();
//        } else {
//            Toast.makeText(context, "Failed to update position", Toast.LENGTH_LONG).show();
//        }
//    }

    @Override
    public void onItemMove(int oldPos, int newPos) {
        if(oldPos < newPos) {   // Moved down the list
            Collections.swap(todoList, oldPos, oldPos+1);
        } else {                // Moved up the list
            Collections.swap(todoList, oldPos, oldPos-1);
        }

        this.notifyItemMoved(oldPos, newPos);
    }

    @Override
    public void updateItemPositions(int fromPosition, int toPosition) {
        if(databaseHelper.updateListPosition(todoList, fromPosition, toPosition)) {
            Toast.makeText(context, "Got it bro", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, "Failed to update positions", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onItemDismiss(int position) {
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
