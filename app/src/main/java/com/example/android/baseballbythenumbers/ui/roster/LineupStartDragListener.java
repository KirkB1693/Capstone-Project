package com.example.android.baseballbythenumbers.ui.roster;


import com.example.android.baseballbythenumbers.adapters.LineupRecyclerViewAdapter;

public class LineupStartDragListener {

    public interface StartDragListener {
        void requestDrag(LineupRecyclerViewAdapter.ViewHolder viewHolder);
    }
}
