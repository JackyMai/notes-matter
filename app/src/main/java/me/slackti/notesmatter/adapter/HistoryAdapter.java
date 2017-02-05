package me.slackti.notesmatter.adapter;


import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
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

public class HistoryAdapter extends RecyclerView.Adapter<TodoHolder> {

    private RelativeLayout actionBar;

    private Context context;

    private LayoutInflater inflater;
    private ArrayList<Todo> todoList;
    private DatabaseHelper databaseHelper;

    private Animation fadeInAnim;
    private Animation fadeOutAnim;

    private int selectedPos = -1;


    public HistoryAdapter(Context context, RelativeLayout actionBar) {
        this.context = context;
        this.actionBar = actionBar;

        inflater = LayoutInflater.from(context);
        todoList = new ArrayList<>();
        databaseHelper = new DatabaseHelper(context);

        setAnimation();
        getCompletedItems();

        if(todoList.size() > 0) {
            updateItemPositions(0, todoList.size()-1);
        }
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
                Toast.makeText(context, "selectedPos: " + selectedPos, Toast.LENGTH_SHORT).show();
            }
        });

        return todoHolder;
    }

    @Override
    public void onBindViewHolder(TodoHolder holder, int position) {
        Todo todo = todoList.get(position);
        holder.setTitle(todo.getTitle() + " " + todo.getId());
        holder.selectedOverlay.setVisibility(selectedPos == position ? View.VISIBLE : View.INVISIBLE);
    }

    private void toggleSelected(int clickedPos) {
        if(selectedPos == clickedPos) {     // Deselect item
            actionBar.startAnimation(fadeOutAnim);

            selectedPos = -1;
            notifyItemChanged(clickedPos);
        } else {                            // Select item
            if(selectedPos == -1) {
                actionBar.startAnimation(fadeInAnim);   // Only fade in if nothing is selected
            }

            notifyItemChanged(selectedPos);     // Update previous position
            selectedPos = clickedPos;
            notifyItemChanged(selectedPos);     // Update new position
        }

        actionBar.setVisibility(selectedPos == -1 ? View.GONE : View.VISIBLE);
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
                        listData.getInt(2));

                todoList.add(todo);
            }

            // So that newly completed items would be on top
            Collections.sort(todoList, new Comparator<Todo>() {
                @Override
                public int compare(Todo todo1, Todo todo2) {
                    return Integer.parseInt(todo2.getId())-Integer.parseInt(todo1.getId());
                }
            });
        }
    }

    public Todo getSelectedItem() {
        return todoList.get(selectedPos);
    }

    private void updateItemPositions(int fromPosition, int toPosition) {
        int start, end;

        if(fromPosition < toPosition) {
            start = fromPosition;
            end = toPosition;
        } else {
            start = toPosition;
            end = fromPosition;
        }

        if(!databaseHelper.updateInactiveItemPositions(todoList, start, end)) {
            Toast.makeText(context, "Failed to update positions", Toast.LENGTH_LONG).show();
        }
    }

    public void onItemUndone(int position) {
        Todo todo = todoList.get(position);

        if(databaseHelper.onItemUndone(todo)) {
            todoList.remove(position);
            this.notifyItemRemoved(position);

            clearSelection();

            // To prevent operation from going out of bound when removing last item
            if(position < todoList.size()) {
                updateItemPositions(position, todoList.size()-1);
            }
        } else {
            Toast.makeText(context, "Failed to undone item", Toast.LENGTH_LONG).show();
        }
    }

    private void clearSelection() {
        selectedPos = -1;
        toggleSelected(selectedPos);
    }

    private void setAnimation() {
        fadeInAnim = AnimationUtils.loadAnimation(context, android.R.anim.fade_in);
        fadeInAnim.setDuration(225);
        fadeOutAnim = AnimationUtils.loadAnimation(context, android.R.anim.fade_out);
        fadeOutAnim.setDuration(195);
    }

}
