package com.example.android.baseballbythenumbers.UI.RosterActivity;

import android.support.v7.widget.RecyclerView;

public class PitchingRotationStartDragListener {

    public interface StartDragListener {
        void requestDrag(RecyclerView.ViewHolder viewHolder);
    }
}
