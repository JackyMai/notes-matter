package me.slackti.notesmatter.adapter;


import android.content.Context;
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

public abstract class BaseAdapter extends RecyclerView.Adapter<TodoHolder> {

    Context context;

    ArrayList<Todo> todoList;
    DatabaseHelper databaseHelper;

    Animation fadeInAnim;
    Animation fadeOutAnim;

    RelativeLayout actionBar;
    private LayoutInflater inflater;

    int selectedPos = -1;

    BaseAdapter(Context context, RelativeLayout actionBar) {
        this.context = context;
        this.actionBar = actionBar;

        inflater = LayoutInflater.from(context);
        todoList = new ArrayList<>();
        databaseHelper = new DatabaseHelper(context);

        setAnimation();
    }

    @Override
    public TodoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = inflater.inflate(R.layout.todo_item, parent, false);
        final TodoHolder todoHolder = new TodoHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = todoHolder.getAdapterPosition();
                toggleSelected(position);
            }
        });

        return todoHolder;
    }

    @Override
    public void onBindViewHolder(TodoHolder holder, int position) {
        Todo todo = todoList.get(position);
        holder.setTitle(todo.getTitle());
        holder.selectedOverlay.setVisibility(selectedPos == position ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    protected abstract void toggleSelected(int clickedPos);

    protected abstract void getDatabaseItems();

    protected abstract void updateItemPositions(int fromPosition, int toPosition);

    public Todo getSelectedItem() {
        return todoList.get(selectedPos);
    }

    void clearSelection() {
        selectedPos = -1;
        toggleSelected(selectedPos);
    }

    private void setAnimation() {
        fadeInAnim = AnimationUtils.loadAnimation(context, android.R.anim.fade_in);
        fadeInAnim.setDuration(225);
        fadeOutAnim = AnimationUtils.loadAnimation(context, android.R.anim.fade_out);
        fadeOutAnim.setDuration(195);
    }
}
