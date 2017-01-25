package me.slackti.notesmatter.adapter;


import android.content.Context;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
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


public class TodoAdapter extends RecyclerView.Adapter<TodoHolder> implements ItemTouchHelperAdapter {

    private RelativeLayout bar_container;
    private FloatingActionButton fab;

    private DatabaseHelper databaseHelper;

    private ArrayList<Todo> todoList;
    private LayoutInflater inflater;
    private Context context;

    private Animation fadeInAnim;
    private Animation fadeOutAnim;

    private int selectedPos = -1;

    public TodoAdapter(ArrayList<Todo> todoList, Context context, RelativeLayout bar_container,
                       FloatingActionButton fab) {
        this.todoList = todoList;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.bar_container = bar_container;
        this.fab = fab;

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
            fab.show();

            selectedPos = -1;
            notifyItemChanged(clickedPos);
        } else {                            // Select item
            if(selectedPos == -1) {
                fab.hide();
                bar_container.startAnimation(fadeInAnim);   // Only fade in if nothing is selected
            }

            notifyItemChanged(selectedPos);     // Update previous position
            selectedPos = clickedPos;
            notifyItemChanged(selectedPos);     // Update new position
        }

        bar_container.setVisibility(selectedPos == -1 ? View.GONE : View.VISIBLE);
    }

    public Todo getSelectedItem() {
        return todoList.get(selectedPos);
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    private void getItems() {
        Cursor listData = databaseHelper.getListContents();

        if(listData.getCount() > 0) {
            while(listData.moveToNext()) {
                Todo newTodo = new Todo(listData.getString(0),
                        listData.getString(1),
                        listData.getInt(2),
                        listData.getInt(3) != 0); // Convert int to boolean

                // Only add todos that are incomplete
                if(!newTodo.getDone()) {
                    todoList.add(newTodo);
                }
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
        } else {
            Toast.makeText(context, "404 something went wrong", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onItemUpdate(Todo todo) {
        if(databaseHelper.updateData(todo)) {
            this.notifyItemChanged(todo.getPosition());
        } else {
            Toast.makeText(context, "Failed to edit todo!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemMove(int oldPos, int newPos) {
        if(oldPos < newPos) {   // Moved down the list
            Collections.swap(todoList, oldPos, oldPos+1);
            if(selectedPos == oldPos) {
                selectedPos++;
            } else if(selectedPos == newPos) {
                selectedPos--;
            }
        } else {                // Moved up the list
            Collections.swap(todoList, oldPos, oldPos-1);
            if(selectedPos == oldPos) {
                selectedPos--;
            } else if(selectedPos == newPos) {
                selectedPos++;
            }
        }

        this.notifyItemMoved(oldPos, newPos);
    }

    @Override
    public void updateItemPositions(int fromPosition, int toPosition) {
        int start, end;

        if(fromPosition < toPosition) {
            start = fromPosition;
            end = toPosition;
        } else {
            start = toPosition;
            end = fromPosition;
        }

        if(!databaseHelper.updateListPosition(todoList, start, end)) {
            Toast.makeText(context, "Failed to update positions", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onItemDone(int position) {
        Todo todo = todoList.get(position);
        todo.setDone(true);

        if(databaseHelper.updateData(todo)) {
            todoList.remove(position);
            this.notifyItemRemoved(position);

            selectedPos = -1;
            toggleSelected(selectedPos);

            // To prevent operation from going out of bound when removing last item
            if(position < todoList.size()) {
                updateItemPositions(position, todoList.size()-1);
            }
        } else {
            Toast.makeText(context, "Failed to archive item", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onItemDismiss(int position) {
        Todo todo = todoList.get(position);

        if(databaseHelper.deleteData(todo.getId())) {
            todoList.remove(position);
            this.notifyItemRemoved(position);

            selectedPos = -1;
            toggleSelected(selectedPos);

            // To prevent operation from going out of bound when removing last item
            if(position < todoList.size()) {
                updateItemPositions(position, todoList.size()-1);
            }
        } else {
            Toast.makeText(context, "Failed to remove item", Toast.LENGTH_LONG).show();
        }
    }
}
