package me.slackti.notesmatter.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import me.slackti.notesmatter.R;
import me.slackti.notesmatter.model.Todo;

public class TodoAdapter extends RecyclerView.Adapter<TodoHolder> {

    private ArrayList<Todo> todoList;
    private LayoutInflater inflater;

    public TodoAdapter(ArrayList<Todo> todoList, Context context) {
        this.todoList = todoList;
        this.inflater = LayoutInflater.from(context);
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
}
