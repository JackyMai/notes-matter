package me.slackti.notesmatter.listener;


import android.view.View;

import me.slackti.notesmatter.touch.TouchListener;

public class BaseListener implements View.OnClickListener {
    private TouchListener clickListener;

    BaseListener(TouchListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public void onClick(View v) {
        if(clickListener != null) {
            clickListener.onSelectionCleared();
        }
    }

}
