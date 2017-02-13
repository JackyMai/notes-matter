package me.slackti.notesmatter.adapter;


import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.Collections;
import java.util.Comparator;

import me.slackti.notesmatter.model.Todo;
import me.slackti.notesmatter.touch.TouchListener;

public class HistoryAdapter extends BaseAdapter {

    public HistoryAdapter(Context context, TouchListener clickListener, RelativeLayout actionBar) {
        super(context, clickListener, actionBar);

        getDatabaseItems();

        if(todoList.size() > 0) {
            updateItemPositions(0, todoList.size()-1);
        }
    }

    @Override
    public void toggleSelected(int clickedPos) {
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
    public void getDatabaseItems() {
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

    @Override
    protected void updateItemPositions(int fromPosition, int toPosition) {
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
}
