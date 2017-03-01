package me.slackti.notesmatter.adapter;


import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import me.slackti.notesmatter.model.Todo;
import me.slackti.notesmatter.touch.TouchListener;

public class HistoryAdapter extends BaseAdapter {

    public HistoryAdapter(Context context, TouchListener touchListener, LinearLayout actionBar) {
        super(context, touchListener, actionBar);

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
        firebaseHelper.retrieveInactiveData(todoList, this);
    }

    public void onItemUndone(int position) {
        Todo todo = todoList.get(position);
        firebaseHelper.moveInactiveData(todo);

        todoList.remove(position);
        this.notifyItemRemoved(position);

        clearSelection();

        // To prevent operation from going out of bound when removing last item
        if(position < todoList.size()) {
            updateItemPositions(position, todoList.size()-1);
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

        firebaseHelper.updateInactiveItemPositions(todoList, start, end);
    }
}
