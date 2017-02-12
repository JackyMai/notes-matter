package me.slackti.notesmatter.listener;


import android.view.View;

import me.slackti.notesmatter.touch.ClickListener;

public class BaseListener implements View.OnClickListener {
    private ClickListener clickListener;

    BaseListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public void onClick(View v) {
        if(clickListener != null) {
            clickListener.onButtonClicked();
        }
    }

}
