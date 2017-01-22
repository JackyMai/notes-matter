package me.slackti.notesmatter.adapter;


import me.slackti.notesmatter.model.Todo;

public interface ItemTouchHelperAdapter {

    void onItemAdd(Todo todo);

    void onItemMove(int fromPosition, int toPosition);

    void onItemDismiss(int position);

    void updateItemPositions(int fromPosition, int toPosition);
}
