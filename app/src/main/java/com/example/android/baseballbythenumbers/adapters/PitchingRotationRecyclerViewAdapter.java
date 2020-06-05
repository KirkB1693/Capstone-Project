package com.example.android.baseballbythenumbers.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.baseballbythenumbers.R;
import com.example.android.baseballbythenumbers.data.Player;
import com.example.android.baseballbythenumbers.ui.roster.PitchingRotationFragment;
import com.example.android.baseballbythenumbers.ui.roster.PitchingRotationItemMoveCallback;
import com.example.android.baseballbythenumbers.ui.roster.PitchingRotationStartDragListener;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

import static com.example.android.baseballbythenumbers.constants.Positions.LONG_RELIEVER;
import static com.example.android.baseballbythenumbers.constants.Positions.SHORT_RELIEVER;
import static com.example.android.baseballbythenumbers.constants.Positions.STARTING_PITCHER;
import static com.example.android.baseballbythenumbers.constants.Positions.getPositionNameFromPrimaryPosition;

public class PitchingRotationRecyclerViewAdapter extends RecyclerView.Adapter<PitchingRotationRecyclerViewAdapter.ViewHolder> implements PitchingRotationItemMoveCallback.RotationItemTouchHelperContract {

    private final List<Player> mPlayers;
    private final PitchingRotationStartDragListener.StartDragListener mStartDragListener;
    private final Context mContext;
    private final PitchingRotationFragment.OnPitchingRotationFragmentInteractionListener mListener;

    public PitchingRotationRecyclerViewAdapter(Context context, List<Player> players, PitchingRotationStartDragListener.StartDragListener startDragListener, PitchingRotationFragment.OnPitchingRotationFragmentInteractionListener clickListener) {
        mPlayers = players;
        mStartDragListener = startDragListener;
        mContext = context;
        mListener = clickListener;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pitching_rotation_item, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NotNull final ViewHolder holder, int position) {
        holder.mPlayer = mPlayers.get(position);
        String firstInitialAndLastName = holder.mPlayer.getFirstName().substring(0,1) + ". " + holder.mPlayer.getLastName();
        holder.mRotationPlayerName.setText(firstInitialAndLastName);
        if (position < 5) {
            int rotationPosition = position + 1;
            String rotationPositionText = "SP"+rotationPosition + ")";
            holder.mRotationPlaceInOrderView.setText(rotationPositionText);
        } else if (position < 7){
            int longRelieverPosition = position - 4;
            String rotationPositionText = "LR"+longRelieverPosition + ")";
            holder.mRotationPlaceInOrderView.setText(rotationPositionText);
        } else if (position == mPlayers.size() - 1) {
            holder.mRotationPlaceInOrderView.setText(R.string.rotation_label_closer);
        } else {
            int shortRelieverPosition = position - 6;
            String rotationPositionText = "SR"+shortRelieverPosition + ")";
            holder.mRotationPlaceInOrderView.setText(rotationPositionText);
        }
        if (holder.mPlayer.getPrimaryPosition() == STARTING_PITCHER) {
            holder.mRotationPlayerPosition.setText(getPositionNameFromPrimaryPosition(holder.mPlayer.getPrimaryPosition()));
        } else if (holder.mPlayer.getPrimaryPosition() == LONG_RELIEVER) {
            holder.mRotationPlayerPosition.setText(R.string.rotation_label_long_reliever);
        } else if (holder.mPlayer.getPrimaryPosition() == SHORT_RELIEVER) {
            holder.mRotationPlayerPosition.setText(R.string.rotation_label_short_reliever);
        }

        String currentStats = "ERA " + holder.mPlayer.getPitchingStats().get(0).getERA() + ", WHIP " + holder.mPlayer.getPitchingStats().get(0).getWHIP() +
                ", W " + holder.mPlayer.getPitchingStats().get(0).getWins() + ", L " + holder.mPlayer.getPitchingStats().get(0).getLosses() + ", S " + holder.mPlayer.getPitchingStats().get(0).getSaves();
        holder.mRotationPlayerStats.setText(currentStats);

        int movementRating = holder.mPlayer.getMovementRating();
        int deceptionRating = holder.mPlayer.getDeceptionRating();
        int accuracyRating = holder.mPlayer.getAccuracyRating();
        int stamina = holder.mPlayer.getPitchingStaminaRating();
        setProgressBarDrawable(stamina, holder.mRotationPlayerStamina);
        holder.mRotationPlayerStamina.setProgress(stamina);

        int overallPitchingRating = ((movementRating + deceptionRating) + (accuracyRating)) / 3;
        setProgressBarDrawable(overallPitchingRating, holder.mRotationPlayerOverall);
        holder.mRotationPlayerOverall.setProgress(overallPitchingRating);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onPitchingRotationFragmentInteraction(holder.mPlayer);
                }
            }
        });

        holder.mReorder.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() ==
                        MotionEvent.ACTION_DOWN) {
                    mStartDragListener.requestDrag(holder);
                }
                return false;
            }
        });
    }

    private void setProgressBarDrawable(int progress, ProgressBar progressBar) {
        if (progress < 40) {
            progressBar.setProgressDrawable(ContextCompat.getDrawable(mContext, R.drawable.player_rating_bar_red));
        } else if (progress < 60) {
            progressBar.setProgressDrawable(ContextCompat.getDrawable(mContext, R.drawable.player_rating_bar_yellow));
        } else {
            progressBar.setProgressDrawable(ContextCompat.getDrawable(mContext, R.drawable.player_rating_bar_green));
        }
    }

    @Override
    public int getItemCount() {
        return mPlayers.size();
    }

    @Override
    public void onRowMoved(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(mPlayers, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(mPlayers, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);

    }

    @Override
    public void onRowSelected(PitchingRotationRecyclerViewAdapter.ViewHolder myViewHolder) {
        myViewHolder.mCardView.setCardBackgroundColor(Color.GRAY);
    }

    @Override
    public void onRowClear(PitchingRotationRecyclerViewAdapter.ViewHolder myViewHolder) {
        myViewHolder.mCardView.setCardBackgroundColor(Color.WHITE);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final View mView;
        private final CardView mCardView;
        private final TextView mRotationPlaceInOrderView;
        private final TextView mRotationPlayerName;
        private final TextView mRotationPlayerPosition;
        private final TextView mRotationPlayerStats;
        private final ProgressBar mRotationPlayerStamina;
        private final ProgressBar mRotationPlayerOverall;
        private final ImageView mReorder;
        private Player mPlayer;

        private ViewHolder(View view) {
            super(view);
            mView = view;
            mCardView = view.findViewById(R.id.pitching_rotation_cardview);
            mRotationPlaceInOrderView = view.findViewById(R.id.pitching_rotation_place_in_order);
            mRotationPlayerName = view.findViewById(R.id.pitching_rotation_name_of_player);
            mRotationPlayerPosition = view.findViewById(R.id.pitching_rotation_position);
            mRotationPlayerStats = view.findViewById(R.id.pitching_rotation_player_stats);
            mRotationPlayerStamina = view.findViewById(R.id.pitching_rotation_stamina_rating_pb);
            mRotationPlayerOverall = view.findViewById(R.id.pitching_rotation_overall_rating_pb);
            mReorder = view.findViewById(R.id.rotation_reorder_icon_iv);
        }

        @NotNull
        @Override
        public String toString() {
            return super.toString() + " '" + mRotationPlayerName.getText() + "'";
        }
    }
}

