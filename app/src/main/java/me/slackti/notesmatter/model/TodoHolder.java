package me.slackti.notesmatter.model;

import android.content.Context;
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
    private TextView title;
    public TextView deadline;

    private TouchListener touchListener;

    public TodoHolder(View itemView, TouchListener touchListener) {
        super(itemView);
        this.touchListener = touchListener;

        context = itemView.getContext();
        title = (TextView) itemView.findViewById(R.id.todo_title);
        title.setTypeface(Typeface.createFromAsset(context.getAssets(),
                "fonts/Roboto_Slab/RobotoSlab-Regular.ttf"));
        deadline = (TextView) itemView.findViewById(R.id.todo_deadline);

        itemView.setOnClickListener(this);
    }

    public void setTitle(String title) {
        this.title.setText(title);
    }

    public void setDeadline(String deadline) {
        this.deadline.setText(deadline);
    }

    @Override
    public void onDraw() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            itemView.setElevation(8);
        } else {
            itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorItemDrag));
        }
    }

    @Override
    public void onItemSelected() { }

    @Override
    public void onItemClear() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            itemView.setElevation(0);
        } else {
            itemView.setBackgroundColor(0);
        }
    }

    @Override
    public void onClick(View v) {
        if(touchListener != null) {
            touchListener.onItemClicked(getAdapterPosition());
        }
    }
}
