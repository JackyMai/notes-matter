package me.slackti.notesmatter.listener.button;


import android.view.View;

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
