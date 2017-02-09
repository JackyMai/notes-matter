package me.slackti.notesmatter.adapter;


import android.content.Context;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.Collections;
import java.util.Comparator;

import me.slackti.notesmatter.model.Todo;
import me.slackti.notesmatter.touch.ItemTouchHelperAdapter;


public class TodoAdapter extends BaseAdapter implements ItemTouchHelperAdapter {

    private FloatingActionButton fab;

    public TodoAdapter(Context context, RelativeLayout actionBar, FloatingActionButton fab) {
        super(context, actionBar);

        this.fab = fab;

        getDatabaseItems();
    }

    @Override
    protected void toggleSelected(int clickedPos) {
        if(selectedPos == clickedPos) {     // Deselect item
            actionBar.startAnimation(fadeOutAnim);
            fab.show();

            selectedPos = -1;
            notifyItemChanged(clickedPos);
        } else {                            // Select item
            if(selectedPos == -1) {
                fab.hide();
                actionBar.startAnimation(fadeInAnim);   // Only fade in if nothing is selected
            }

            notifyItemChanged(selectedPos);     // Update previous position
            selectedPos = clickedPos;
            notifyItemChanged(selectedPos);     // Update new position
        }

        actionBar.setVisibility(selectedPos == -1 ? View.GONE : View.VISIBLE);
    }

    @Override
    public void getDatabaseItems() {
        todoList.clear();

        Cursor listData = databaseHelper.getIncompleteItems();

        if(listData.getCount() > 0) {
            while(listData.moveToNext()) {
                Todo todo = new Todo(listData.getString(0),
                        listData.getString(1),
                        listData.getInt(2));

                if(todo.getPosition() == -1) {
                    todo.setPosition(todoList.size());
                }

                todoList.add(todo);
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
    public void onItemDone(int position) {
        Todo todo = todoList.get(position);

        if(databaseHelper.onItemDone(todo)) {
            todoList.remove(position);
            this.notifyItemRemoved(position);

            clearSelection();

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

        if(databaseHelper.deleteData(todo)) {
            todoList.remove(position);
            this.notifyItemRemoved(position);

            clearSelection();

            // To prevent operation from going out of bound when removing last item
            if(position < todoList.size()) {
                updateItemPositions(position, todoList.size()-1);
            }
        } else {
            Toast.makeText(context, "Failed to remove item", Toast.LENGTH_LONG).show();
        }
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

        if(!databaseHelper.updateActiveItemPositions(todoList, start, end)) {
            Toast.makeText(context, "Failed to update positions", Toast.LENGTH_LONG).show();
        }
    }
}
