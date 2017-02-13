package me.slackti.notesmatter.adapter;


import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import me.slackti.notesmatter.R;
import me.slackti.notesmatter.database.DatabaseHelper;
import me.slackti.notesmatter.model.Todo;
import me.slackti.notesmatter.model.TodoHolder;
import me.slackti.notesmatter.touch.ClickListener;

public abstract class BaseAdapter extends RecyclerView.Adapter<TodoHolder> {

    Context context;
    private ClickListener clickListener;

    ArrayList<Todo> todoList;
    DatabaseHelper databaseHelper;

    Animation fadeInAnim;
    Animation fadeOutAnim;

    RelativeLayout actionBar;
    private LayoutInflater inflater;

    int selectedPos = -1;

    BaseAdapter(Context context, ClickListener clickListener, RelativeLayout actionBar) {
        this.context = context;
        this.clickListener = clickListener;
        this.actionBar = actionBar;

        inflater = LayoutInflater.from(context);
        todoList = new ArrayList<>();
        databaseHelper = new DatabaseHelper(context);

        setAnimation();
    }

    @Override
    public TodoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = inflater.inflate(R.layout.todo_item, parent, false);
        return new TodoHolder(view, clickListener);
    }

    @Override
    public void onBindViewHolder(TodoHolder holder, int position) {
        Todo todo = todoList.get(position);
        holder.setTitle(todo.getTitle());
        holder.textView.setBackgroundColor(selectedPos == position ? ContextCompat.getColor(context, R.color.colorItemSelected) : 0);
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    public Todo getTodoItem(int index) {
        return todoList.get(index);
    }

    public abstract void toggleSelected(int clickedPos);

    public abstract void getDatabaseItems();

    protected abstract void updateItemPositions(int fromPosition, int toPosition);

    public Todo getSelectedItem() {
        return todoList.get(selectedPos);
    }

    public int getSelectedPos() {
        return selectedPos;
    }

    public void clearSelection() {
        int position = selectedPos;
        selectedPos = -1;
        toggleSelected(selectedPos);
        notifyItemChanged(position);
    }

    private void setAnimation() {
        fadeInAnim = AnimationUtils.loadAnimation(context, android.R.anim.fade_in);
        fadeInAnim.setDuration(225);
        fadeOutAnim = AnimationUtils.loadAnimation(context, android.R.anim.fade_out);
        fadeOutAnim.setDuration(195);
    }
}
