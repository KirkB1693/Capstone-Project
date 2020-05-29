package com.example.android.baseballbythenumbers.ui.roster;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.example.android.baseballbythenumbers.Adapters.PitchingRotationRecyclerViewAdapter;

import org.jetbrains.annotations.NotNull;

public class PitchingRotationItemMoveCallback extends ItemTouchHelper.Callback {

    private final RotationItemTouchHelperContract mAdapter;

    public PitchingRotationItemMoveCallback(RotationItemTouchHelperContract adapter) {
        mAdapter = adapter;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return false;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return false;
    }



    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

    }

    @Override
    public int getMovementFlags(@NotNull RecyclerView recyclerView, @NotNull RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        return makeMovementFlags(dragFlags, 0);
    }

    @Override
    public boolean onMove(@NotNull RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                          RecyclerView.ViewHolder target) {
        mAdapter.onRowMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder,
                                  int actionState) {


        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            if (viewHolder instanceof PitchingRotationRecyclerViewAdapter.ViewHolder) {
                PitchingRotationRecyclerViewAdapter.ViewHolder myViewHolder=
                        (PitchingRotationRecyclerViewAdapter.ViewHolder) viewHolder;
                mAdapter.onRowSelected(myViewHolder);
            }

        }

        super.onSelectedChanged(viewHolder, actionState);
    }
    @Override
    public void clearView(@NotNull RecyclerView recyclerView,
                          @NotNull RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);

        if (viewHolder instanceof PitchingRotationRecyclerViewAdapter.ViewHolder) {
            PitchingRotationRecyclerViewAdapter.ViewHolder myViewHolder=
                    (PitchingRotationRecyclerViewAdapter.ViewHolder) viewHolder;
            mAdapter.onRowClear(myViewHolder);
        }
    }

    public interface RotationItemTouchHelperContract {

        void onRowMoved(int fromPosition, int toPosition);
        void onRowSelected(PitchingRotationRecyclerViewAdapter.ViewHolder myViewHolder);
        void onRowClear(PitchingRotationRecyclerViewAdapter.ViewHolder myViewHolder);

    }



}