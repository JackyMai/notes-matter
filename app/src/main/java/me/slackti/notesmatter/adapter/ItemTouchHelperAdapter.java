package me.slackti.notesmatter.adapter;


import me.slackti.notesmatter.model.Todo;

public interface ItemTouchHelperAdapter {

    void onItemAdd(Todo todo);

    void onItemUpdate(Todo todo);

    void onItemMove(int fromPosition, int toPosition);

    void updateItemPositions(int fromPosition, int toPosition);

    void onItemDone(int position);

    void onItemDismiss(int position);
}
