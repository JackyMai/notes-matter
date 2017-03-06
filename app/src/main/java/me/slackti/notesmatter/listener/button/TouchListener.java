package me.slackti.notesmatter.listener.button;


public interface TouchListener {
    void onItemClicked(int position);
//    void onItemLongClicked(int position);
    void onSelectionCleared();
}
