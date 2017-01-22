package me.slackti.notesmatter.model;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import me.slackti.notesmatter.R;
import me.slackti.notesmatter.adapter.ItemTouchHelperViewHolder;


public class TodoHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {
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

    @Override
    public void onItemSelected() {
        itemView.setBackgroundColor(Color.LTGRAY);
    }

    @Override
    public void onItemClear() {
        itemView.setBackgroundColor(Color.WHITE);
    }
}
