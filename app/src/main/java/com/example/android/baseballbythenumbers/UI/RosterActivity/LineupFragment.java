package com.example.android.baseballbythenumbers.UI.RosterActivity;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.baseballbythenumbers.Adapters.LineupRecyclerViewAdapter;
import com.example.android.baseballbythenumbers.Data.Player;
import com.example.android.baseballbythenumbers.Data.Team;
import com.example.android.baseballbythenumbers.R;
import com.example.android.baseballbythenumbers.databinding.FragmentLineupBinding;

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
    public void onAttach(Context context) {
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
    public void requestDrag(RecyclerView.ViewHolder viewHolder) {
        touchHelper.startDrag(viewHolder);
    }

    public interface OnLineupFragmentInteractionListener {
        void onLineupFragmentInteraction(Player player);
    }

}