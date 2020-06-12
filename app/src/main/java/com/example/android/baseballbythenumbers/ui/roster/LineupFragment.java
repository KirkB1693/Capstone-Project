package com.example.android.baseballbythenumbers.ui.roster;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.baseballbythenumbers.R;
import com.example.android.baseballbythenumbers.adapters.LineupRecyclerViewAdapter;
import com.example.android.baseballbythenumbers.data.Player;
import com.example.android.baseballbythenumbers.data.Team;
import com.example.android.baseballbythenumbers.databinding.FragmentLineupBinding;

import org.jetbrains.annotations.NotNull;

/**
 * A placeholder fragment containing a simple view.
 */
public class LineupFragment extends Fragment implements LineupStartDragListener.StartDragListener {

    private static final String ARG_TEAM = "team";
    private Team mTeam;

    private FragmentLineupBinding lineupBinding;
    private OnLineupFragmentInteractionListener mListener;
    private RecyclerView mRecyclerView;
    private ItemTouchHelper touchHelper;

    public LineupFragment(){
    }

    public static LineupFragment newInstance(Team team) {
        LineupFragment fragment = new LineupFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_TEAM, team);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTeam = getArguments().getParcelable(ARG_TEAM);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        lineupBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_lineup, container, false);

        mRecyclerView = lineupBinding.lineupRecyclerView;
        LineupRecyclerViewAdapter mAdapter = new LineupRecyclerViewAdapter(getContext(), mTeam.getPlayers(), this, mListener);
        ItemTouchHelper.Callback callback = new LineupItemMoveCallback(mAdapter);
        touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);

        return lineupBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        if (context instanceof OnLineupFragmentInteractionListener) {
            mListener = (OnLineupFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnLineupFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void requestDrag(LineupRecyclerViewAdapter.ViewHolder viewHolder) {
        touchHelper.startDrag(viewHolder);
    }

    public interface OnLineupFragmentInteractionListener {
        void onLineupFragmentInteraction(Player player);
    }

}