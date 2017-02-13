package me.slackti.notesmatter.touch;


public interface TouchListener {
    void onItemClicked(int position);
//    void onItemLongClicked(int position);
    void onSelectionCleared();
}
