package me.slackti.notesmatter.model;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import me.slackti.notesmatter.R;
import me.slackti.notesmatter.touch.ItemTouchHelperViewHolder;
import me.slackti.notesmatter.touch.TouchListener;


public class TodoHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder,
        View.OnClickListener {

    private Context context;
    public TextView textView;

    private TouchListener touchListener;

    public TodoHolder(View itemView, TouchListener touchListener) {
        super(itemView);
        this.touchListener = touchListener;

        context = itemView.getContext();
        textView = (TextView) itemView.findViewById(R.id.todo_title);
        textView.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/Roboto_Slab/RobotoSlab-Regular.ttf"));

        itemView.setOnClickListener(this);
    }

    public void setTitle(String title) {
        this.textView.setText(title);
    }

    @Override
    public void onDraw() {
        if(Build.VERSION.SDK_INT >= 21) {
            itemView.setElevation(8);
            itemView.setBackgroundColor(ContextCompat.getColor(context, android.R.color.white));
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
            itemView.setBackgroundColor(0);
        } else {
            itemView.setBackgroundColor(Color.WHITE);
        }
    }

    @Override
    public void onClick(View v) {
        if(touchListener != null) {
            touchListener.onItemClicked(getAdapterPosition());
        }
    }
}
