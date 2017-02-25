package me.slackti.notesmatter.ui;


import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import com.google.firebase.database.FirebaseDatabase;

import me.slackti.notesmatter.R;
import me.slackti.notesmatter.adapter.BaseAdapter;
import me.slackti.notesmatter.callback.ActionModeCallback;
import me.slackti.notesmatter.touch.TouchListener;

public class BaseActivity extends AppCompatActivity implements TouchListener {
    RelativeLayout emptyState;
    RecyclerView recView;
    BaseAdapter adapter;
    ActionMode actionMode;
    ActionModeCallback actionModeCallback;

    Animation fadeInAnim;
    Animation fadeOutAnim;

    @Override
    public void onItemClicked(int position) {
        if(actionMode == null) {
            actionMode = startSupportActionMode(actionModeCallback);
        }

        toggleSelected(position);
    }

    @Override
    public void onSelectionCleared() {
        if(actionMode != null) {
            actionMode.finish();
        }
    }

    private void toggleSelected(int position) {
        adapter.toggleSelected(position);

        if(adapter.getSelectedPos() == -1) {
            actionMode.finish();
        }
    }

    public void setActionMode(ActionMode actionMode) {
        this.actionMode = actionMode;
    }

    void setupRecyclerView() {
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recView = (RecyclerView) findViewById(R.id.todo_list);
        recView.setLayoutManager(linearLayoutManager);
        recView.setAdapter(adapter);
        recView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if(adapter.getSelectedPos() == linearLayoutManager.findLastVisibleItemPosition()
                        && adapter.getSelectedPos() != RecyclerView.NO_POSITION) {
                    recView.smoothScrollToPosition(adapter.getSelectedPos());
                }
            }
        });
    }

    void setupAnimation() {
        fadeInAnim = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        fadeInAnim.setDuration(225);
        fadeOutAnim = AnimationUtils.loadAnimation(this, android.R.anim.fade_out);
        fadeOutAnim.setDuration(195);
    }

    void checkEmptyState() {
        // Animations are only played when it needs to to avoid peculiar visual bugs and delays
        if(adapter.getItemCount() == 0) {
            if(recView.getVisibility() != View.INVISIBLE) {
                recView.startAnimation(fadeOutAnim);
            }
            recView.setVisibility(View.INVISIBLE);

            if(emptyState.getVisibility() != View.VISIBLE) {
                emptyState.startAnimation(fadeInAnim);
            }
            emptyState.setVisibility(View.VISIBLE);
        } else {
            if(recView.getVisibility() != View.VISIBLE) {
                recView.startAnimation(fadeInAnim);
            }
            recView.setVisibility(View.VISIBLE);

            // Starting fade-in animation would cause emptyState to appear for a brief moment
            emptyState.setVisibility(View.INVISIBLE);
        }
    }
}
