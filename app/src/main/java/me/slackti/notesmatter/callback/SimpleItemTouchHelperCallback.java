package me.slackti.notesmatter.callback;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
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

    public SimpleItemTouchHelperCallback(Context context, ItemTouchHelperAdapter adapter) {
        this.context = context;
        this.adapter = adapter;
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
        Bitmap icon;
        if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            View itemView = viewHolder.itemView;
            float height = (float) itemView.getBottom() - (float) itemView.getTop();
            float width = height / 3;

            if(dX > 0) {
                Paint p = new Paint();
                p.setColor(Color.parseColor("#388E3C"));
                RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX, (float) itemView.getBottom());
                c.drawRect(background,p);
                icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_done_white_24dp);
                RectF icon_dest = new RectF((float) itemView.getLeft() + width , (float) itemView.getTop() + width, (float) itemView.getLeft()+ 2*width, (float)itemView.getBottom() - width);
                c.drawBitmap(icon, null, icon_dest, p);
            } else {
                Paint p = new Paint();
                p.setColor(Color.parseColor("#FBC02D"));
                RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom());
                c.drawRect(background,p);
                icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_edit_white_24dp);
                RectF icon_dest = new RectF((float) itemView.getRight() - 2*width, (float) itemView.getTop() + width, (float) itemView.getRight() - width, (float)itemView.getBottom() - width);
                c.drawBitmap(icon, null, icon_dest, p);
            }
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
