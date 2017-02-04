package me.slackti.notesmatter.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;

import me.slackti.notesmatter.R;
import me.slackti.notesmatter.database.DatabaseHelper;
import me.slackti.notesmatter.model.Todo;
import me.slackti.notesmatter.model.TodoHolder;

public class HistoryAdapter extends RecyclerView.Adapter<TodoHolder> {

    private Context context;

    private LayoutInflater inflater;
    private ArrayList<Todo> todoList;
    private DatabaseHelper databaseHelper;

    private int selectedPos = -1;

    public HistoryAdapter(Context context) {
        this.context = context;

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

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    private void getCompletedItems() {

    }
}
