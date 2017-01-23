package me.slackti.notesmatter.model;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import me.slackti.notesmatter.R;
import me.slackti.notesmatter.adapter.ItemTouchHelperViewHolder;


public class TodoHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {
//    private View container;
    private TextView title;
    public View selectedOverlay;
    private Context context;

    public TodoHolder(View itemView) {
        super(itemView);
//        container = itemView.findViewById(R.id.todo_container);
        title = (TextView) itemView.findViewById(R.id.todo_title);
        selectedOverlay = itemView.findViewById(R.id.selected_overlay);
        context = itemView.getContext();
    }

    public void setTitle(String title) {
        this.title.setText(title);
    }

    @Override
    public void onItemSelected() {
//        itemView.setElevation(10);
//        itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorItemSelected));
    }

    @Override
    public void onItemClear() {
//        itemView.setBackgroundColor(Color.WHITE);
//        itemView.setElevation(0);
    }
}
