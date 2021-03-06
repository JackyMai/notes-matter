package me.slackti.notesmatter.adapter;


import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import me.slackti.notesmatter.R;
import me.slackti.notesmatter.helper.database.FirebaseHelper;
import me.slackti.notesmatter.model.Todo;
import me.slackti.notesmatter.model.TodoHolder;
import me.slackti.notesmatter.listener.button.TouchListener;

public abstract class BaseAdapter extends RecyclerView.Adapter<TodoHolder> {

    private Context context;
    private TouchListener touchListener;

    ArrayList<Todo> todoList;
    FirebaseHelper firebaseHelper;

    Animation fadeInAnim;
    Animation fadeOutAnim;

    LinearLayout actionBar;
    private LayoutInflater inflater;

    int selectedPos = -1;

    BaseAdapter(Context context, TouchListener touchListener, LinearLayout actionBar) {
        this.context = context;
        this.touchListener = touchListener;
        this.actionBar = actionBar;

        inflater = LayoutInflater.from(context);
        todoList = new ArrayList<>();

        firebaseHelper = FirebaseHelper.getInstance();
        firebaseHelper.updateUserReferences();

        setAnimation();
    }

    @Override
    public TodoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = inflater.inflate(R.layout.todo_item, parent, false);
        return new TodoHolder(view, touchListener);
    }

    @Override
    public void onBindViewHolder(TodoHolder holder, int position) {
        Todo todo = todoList.get(position);
        holder.setTitle(todo.getTitle());

        if(todo.getDeadline() != null) {
            holder.setDeadline(formatDeadline(todo.getDeadline()));
            holder.deadline.setVisibility(View.VISIBLE);
        } else {
            holder.deadline.setVisibility(View.GONE);
        }

        holder.itemView.setBackground(selectedPos == position ?
                ContextCompat.getDrawable(context, R.drawable.bg_select_state) :
                ContextCompat.getDrawable(context, R.drawable.bg_normal_state));
    }

    private String formatDeadline(String deadline) {
        // Split deadline into parts and adding 1 to the month
        // because Calendar.MONTH starts from index 0 (i.e. 0 = January)
        String[] parts = deadline.split("/");
        parts[1] = String.valueOf(Integer.parseInt(parts[1]) + 1);

        // Set up the pattern for input and output for parsing and formatting
        SimpleDateFormat input = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        SimpleDateFormat output = new SimpleDateFormat("d MMM", Locale.ENGLISH);

        // Only format year as well if the year for deadline is not this year
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        if(Integer.parseInt(parts[2]) != currentYear) {
            output.applyPattern("d MMM yy");
        }

        try {
            return output.format(input.parse(parts[0] + "/" + parts[1] + "/" + parts[2]));
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
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
        touchListener.onSelectionCleared();
    }

    private void setAnimation() {
        fadeInAnim = AnimationUtils.loadAnimation(context, android.R.anim.fade_in);
        fadeInAnim.setDuration(225);
        fadeOutAnim = AnimationUtils.loadAnimation(context, android.R.anim.fade_out);
        fadeOutAnim.setDuration(195);
    }
}
