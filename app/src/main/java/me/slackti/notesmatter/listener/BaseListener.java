package me.slackti.notesmatter.listener;


import android.view.View;

import me.slackti.notesmatter.touch.TouchListener;

public class BaseListener implements View.OnClickListener {
    private TouchListener touchListener;

    BaseListener(TouchListener touchListener) {
        this.touchListener = touchListener;
    }

    @Override
    public void onClick(View v) {
        if(touchListener != null) {
            touchListener.onSelectionCleared();
        }
    }

}
