package me.slackti.notesmatter.adapter;


import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.LinearLayout;

import java.util.Collections;

import me.slackti.notesmatter.model.Todo;
import me.slackti.notesmatter.touch.ItemTouchHelperAdapter;
import me.slackti.notesmatter.touch.TouchListener;


public class TodoAdapter extends BaseAdapter implements ItemTouchHelperAdapter {

    private FloatingActionButton fab;

    public TodoAdapter(Context context, TouchListener touchListener, LinearLayout actionBar, FloatingActionButton fab) {
        super(context, touchListener, actionBar);

        this.fab = fab;

        getDatabaseItems();
    }

    @Override
    public void toggleSelected(int clickedPos) {
        // Highlighted item is the same as the click item, deselect it
        if(selectedPos == clickedPos) {
            if(clickedPos != -1) {  // -1 is reserved for programmatic operations
                actionBar.startAnimation(fadeOutAnim);
                notifyItemChanged(clickedPos);
            }

            fab.show();
            selectedPos = -1;
        } else {    // User is selecting an item
            if(selectedPos == -1) {     // Nothing is highlighted at the moment
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
        firebaseHelper.setActiveDataListener(todoList, this);
    }

    @Override
    public void onItemAdd(Todo todo) {
        todo.setPosition(todoList.size());
        firebaseHelper.addActiveData(todo);
    }

    @Override
    public void onItemUpdate(Todo todo) {
        firebaseHelper.updateData(todo);
        this.notifyItemChanged(todo.getPosition());
        clearSelection();
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
        firebaseHelper.moveActiveData(todo);

        todoList.remove(position);
        this.notifyItemRemoved(position);

        clearSelection();

        // To prevent operation from going out of bound when removing last item
        if(position < todoList.size()) {
            updateItemPositions(position, todoList.size()-1);
        }
    }

    @Override
    public void onItemDismiss(int position) {
        Todo todo = todoList.get(position);
        firebaseHelper.removeData(todo);

        todoList.remove(position);
        this.notifyItemRemoved(position);

        clearSelection();

        // To prevent operation from going out of bound when removing last item
        if(position < todoList.size()) {
            updateItemPositions(position, todoList.size()-1);
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

        firebaseHelper.updateActiveItemPositions(todoList, start, end);
    }
}
