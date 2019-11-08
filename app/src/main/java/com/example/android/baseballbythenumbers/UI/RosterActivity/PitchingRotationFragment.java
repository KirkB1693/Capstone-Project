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

import com.example.android.baseballbythenumbers.Adapters.PitchingRotationRecyclerViewAdapter;
import com.example.android.baseballbythenumbers.Data.Player;
import com.example.android.baseballbythenumbers.Data.Team;
import com.example.android.baseballbythenumbers.R;
import com.example.android.baseballbythenumbers.databinding.FragmentPitchingRotationBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.example.android.baseballbythenumbers.Constants.Positions.LONG_RELIEVER;
import static com.example.android.baseballbythenumbers.Constants.Positions.SHORT_RELEIVER;
import static com.example.android.baseballbythenumbers.Constants.Positions.STARTING_PITCHER;

/**
 * A placeholder fragment containing a simple view.
 */
public class PitchingRotationFragment extends Fragment implements PitchingRotationStartDragListener.StartDragListener{

    private static final String ARG_TEAM = "team";
    private Team mTeam;
    private RecyclerView mRecyclerView;
    private OnPitchingRotationFragmentInteractionListener mListener;
    private ItemTouchHelper touchHelper;

    private FragmentPitchingRotationBinding pitchingRotationBinding;

    public PitchingRotationFragment(){
    }

    public static PitchingRotationFragment newInstance(Team team) {
        PitchingRotationFragment fragment = new PitchingRotationFragment();
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
        pitchingRotationBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_pitching_rotation, container, false);

        List<Player> pitchers = new ArrayList<>();
        for (Player player : mTeam.getPlayers()) {
            if (player.getPrimaryPosition() == STARTING_PITCHER || player.getPrimaryPosition() == LONG_RELIEVER || player.getPrimaryPosition() == SHORT_RELEIVER) {
                pitchers.add(player);
            }
        }

        Collections.sort(pitchers, Player.BestPitcherComparator);

        mRecyclerView = pitchingRotationBinding.rotationRecyclerView;
        PitchingRotationRecyclerViewAdapter mAdapter = new PitchingRotationRecyclerViewAdapter(getContext(), pitchers, this, mListener);
        ItemTouchHelper.Callback callback = new PitchingRotationItemMoveCallback(mAdapter);
        touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);

        return pitchingRotationBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnPitchingRotationFragmentInteractionListener) {
            mListener = (OnPitchingRotationFragmentInteractionListener) context;
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

    public interface OnPitchingRotationFragmentInteractionListener {
        void onPitchingRotationFragmentInteraction(Player player);
    }

}