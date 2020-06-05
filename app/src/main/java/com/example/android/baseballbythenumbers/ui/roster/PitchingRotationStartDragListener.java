package com.example.android.baseballbythenumbers.ui.roster;


import androidx.recyclerview.widget.RecyclerView;

public class PitchingRotationStartDragListener {

    public interface StartDragListener {
        void requestDrag(RecyclerView.ViewHolder viewHolder);
    }
}
