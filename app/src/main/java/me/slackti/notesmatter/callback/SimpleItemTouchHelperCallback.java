package me.slackti.notesmatter.callback;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import me.slackti.notesmatter.R;
import me.slackti.notesmatter.adapter.TodoAdapter;
import me.slackti.notesmatter.helper.AlertHelper;
import me.slackti.notesmatter.touch.ItemTouchHelperAdapter;
import me.slackti.notesmatter.touch.ItemTouchHelperViewHolder;


public class SimpleItemTouchHelperCallback extends ItemTouchHelper.Callback {

    private Context context;
    private final ItemTouchHelperAdapter adapter;

    private int fromPosition = -1;
    private int toPosition = -1;

    private boolean viewCleared;

    private Drawable green_background;
    private Drawable orange_background;
    private Drawable done_icon;
    private Drawable edit_icon;

    private final int icon_margin;

    public SimpleItemTouchHelperCallback(Context context, ItemTouchHelperAdapter adapter) {
        this.context = context;
        this.adapter = adapter;

        green_background = new ColorDrawable(Color.parseColor("#388E3C"));
        orange_background = new ColorDrawable(Color.parseColor("#FBC02D"));
        done_icon = ContextCompat.getDrawable(context, R.drawable.ic_done_white_24dp);
        edit_icon = ContextCompat.getDrawable(context, R.drawable.ic_edit_white_24dp);
        icon_margin = (int) context.getResources().getDimension(R.dimen.default_margin);
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        int swipeFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                          RecyclerView.ViewHolder target) {
        // Only modify fromPosition once, until it is reset by clearView()
        if(fromPosition == -1 && toPosition == -1) {
            fromPosition = viewHolder.getAdapterPosition();
        }

        toPosition = target.getAdapterPosition();

        adapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
        if(swipeDir == ItemTouchHelper.LEFT) {
            AlertHelper alertHelper = new AlertHelper();
            alertHelper.createEditDialog(context,  (TodoAdapter) adapter, viewHolder.getAdapterPosition());
        } else {
            adapter.onItemDone(viewHolder.getAdapterPosition());
        }
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            if (viewHolder instanceof ItemTouchHelperViewHolder) {
                ItemTouchHelperViewHolder itemViewHolder = (ItemTouchHelperViewHolder) viewHolder;
                itemViewHolder.onItemSelected();
            }
        }

        super.onSelectedChanged(viewHolder, actionState);
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);

        if(viewHolder instanceof ItemTouchHelperViewHolder) {
            ItemTouchHelperViewHolder itemViewHolder = (ItemTouchHelperViewHolder) viewHolder;
            itemViewHolder.onItemClear();
        }

        if(fromPosition != -1 && toPosition != -1) {
            adapter.updateItemPositions(fromPosition, toPosition);
        }

        viewCleared = true;

        resetDragPositions();
    }

    private void resetDragPositions() {
        this.fromPosition = -1;
        this.toPosition = -1;
    }

    // onChildDraw() is called both before and after clearView(), therefore viewCleared should
    // be set to false after it's been set to true by clearView()
    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView,
                            RecyclerView.ViewHolder viewHolder,
                            float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            View itemView = viewHolder.itemView;

            if(dX > 0) {
                green_background.setBounds(itemView.getLeft(), itemView.getTop(), (int) dX, itemView.getBottom());
                green_background.draw(c);

                int height = itemView.getBottom() - itemView.getTop();
                int intrinsicWidth = done_icon.getIntrinsicWidth();
                int intrinsicHeight = done_icon.getIntrinsicWidth();

                int left = itemView.getLeft() + icon_margin;
                int top = itemView.getTop() + (height - intrinsicHeight) / 2;
                int right = itemView.getLeft() + icon_margin + intrinsicWidth;
                int bottom = top + intrinsicHeight;
                done_icon.setBounds(left, top, right, bottom);
                done_icon.draw(c);
            } else {
                orange_background.setBounds(itemView.getRight() + (int) dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());
                orange_background.draw(c);

                int height = itemView.getBottom() - itemView.getTop();
                int intrinsicWidth = edit_icon.getIntrinsicWidth();
                int intrinsicHeight = edit_icon.getIntrinsicWidth();

                int left = itemView.getRight() - icon_margin - intrinsicWidth;
                int top = itemView.getTop() + (height - intrinsicHeight) / 2;
                int right = itemView.getRight() - icon_margin;
                int bottom = top + intrinsicHeight;
                edit_icon.setBounds(left, top, right, bottom);
                edit_icon.draw(c);
            }

//            final float alpha = 1.0f - Math.abs(dX) / (float) viewHolder.itemView.getWidth();
//            viewHolder.itemView.setAlpha(alpha);
//            viewHolder.itemView.setTranslationX(dX);
        }

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

        if(viewCleared) {
            viewCleared = false;
        } else {
            if(viewHolder instanceof ItemTouchHelperViewHolder) {
                ItemTouchHelperViewHolder itemViewHolder = (ItemTouchHelperViewHolder) viewHolder;
                itemViewHolder.onDraw();
            }
        }
    }

    @Override
    public int interpolateOutOfBoundsScroll(RecyclerView recyclerView, int viewSize, int viewSizeOutOfBounds, int totalSize, long msSinceStartScroll) {
        final int direction = (int) Math.signum(viewSizeOutOfBounds);
        return 20 * direction;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }
}
