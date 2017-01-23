package me.slackti.notesmatter.adapter;


import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import me.slackti.notesmatter.R;
import me.slackti.notesmatter.database.DatabaseHelper;
import me.slackti.notesmatter.model.Todo;
import me.slackti.notesmatter.model.TodoHolder;

import static android.view.View.GONE;


public class TodoAdapter extends RecyclerView.Adapter<TodoHolder> implements ItemTouchHelperAdapter {

    private RelativeLayout bar_container;

    private DatabaseHelper databaseHelper;

    private ArrayList<Todo> todoList;
    private LayoutInflater inflater;
    private Context context;

    private Animation fadeInAnim;
    private Animation fadeOutAnim;

    private int selectedPos = -1;

    public TodoAdapter(ArrayList<Todo> todoList, Context context, RelativeLayout bar_container) {
        this.todoList = todoList;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.bar_container = bar_container;

        fadeInAnim = AnimationUtils.loadAnimation(context, android.R.anim.fade_in);
        fadeInAnim.setDuration(225);
        fadeOutAnim = AnimationUtils.loadAnimation(context, android.R.anim.fade_out);
        fadeOutAnim.setDuration(195);

        databaseHelper = new DatabaseHelper(context);

        getItems();
    }

    @Override
    public TodoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = inflater.inflate(R.layout.todo_item, parent, false);
        final TodoHolder todoHolder = new TodoHolder(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = todoHolder.getAdapterPosition();
                toggleSelected(position);
            }
        });

        return todoHolder;
    }

    @Override
    public void onBindViewHolder(TodoHolder holder, int position) {
        Todo todo = todoList.get(position);
        holder.setTitle(todo.getTitle());
        holder.selectedOverlay.setVisibility(selectedPos == position ? View.VISIBLE : View.INVISIBLE);
    }

    private void toggleSelected(int clickedPos) {
        if(selectedPos == clickedPos) {     // Deselect item
            bar_container.startAnimation(fadeOutAnim);

            selectedPos = -1;
            notifyItemChanged(clickedPos);
        } else {                            // Select item
            if(selectedPos == -1) {
                bar_container.startAnimation(fadeInAnim);   // Only fade in if nothing is selected
            }

            notifyItemChanged(selectedPos);     // Update previous position
            selectedPos = clickedPos;
            notifyItemChanged(selectedPos);     // Update new position
        }

        bar_container.setVisibility(selectedPos == -1 ? View.GONE : View.VISIBLE);
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
