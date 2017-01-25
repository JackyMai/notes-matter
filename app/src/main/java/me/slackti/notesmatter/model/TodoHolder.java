package me.slackti.notesmatter.model;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import me.slackti.notesmatter.R;
import me.slackti.notesmatter.adapter.ItemTouchHelperViewHolder;


public class TodoHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {

    private TextView title;
    public View selectedOverlay;
    private Context context;

    public TodoHolder(View itemView) {
        super(itemView);
        title = (TextView) itemView.findViewById(R.id.todo_title);
        selectedOverlay = itemView.findViewById(R.id.selected_overlay);
        context = itemView.getContext();
    }

    public void setTitle(String title) {
        this.title.setText(title);
    }

    @Override
    public void onDraw() {
        if(Build.VERSION.SDK_INT >= 21) {
            itemView.setElevation(8);
        } else {
            itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorItemDrag));
        }
    }

    @Override
    public void onItemSelected() { }

    @Override
    public void onItemClear() {
        if(Build.VERSION.SDK_INT >= 21) {
            itemView.setElevation(0);
        } else {
            itemView.setBackgroundColor(Color.WHITE);
        }
    }
}
