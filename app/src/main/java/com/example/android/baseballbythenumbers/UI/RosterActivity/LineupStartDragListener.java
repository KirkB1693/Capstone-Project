package com.example.android.baseballbythenumbers.UI.RosterActivity;

import android.support.v7.widget.RecyclerView;

public class LineupStartDragListener {

    public interface StartDragListener {
        void requestDrag(RecyclerView.ViewHolder viewHolder);
    }
}
