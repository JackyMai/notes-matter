package me.slackti.notesmatter.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import me.slackti.notesmatter.R;


public class TodoHolder extends RecyclerView.ViewHolder {
    private TextView title;
    private View container;

    public TodoHolder(View itemView) {
        super(itemView);

        title = (TextView) itemView.findViewById(R.id.todo_title);
        container = itemView.findViewById(R.id.todo_container);
    }

    public void setTitle(String title) {
        this.title.setText(title);
    }
}
