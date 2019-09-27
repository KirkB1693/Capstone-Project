package com.example.android.baseballbythenumbers.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.baseballbythenumbers.Data.Player;
import com.example.android.baseballbythenumbers.R;
import com.example.android.baseballbythenumbers.UI.RosterActivity.PitchingRotationItemMoveCallback;
import com.example.android.baseballbythenumbers.UI.RosterActivity.PitchingRotationStartDragListener;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

import static com.example.android.baseballbythenumbers.Constants.Constants.PitcherBaseStats.PITCHER_O_CONTACT_PCT_MIN;
import static com.example.android.baseballbythenumbers.Constants.Constants.PitcherBaseStats.PITCHER_O_CONTACT_RANGE;
import static com.example.android.baseballbythenumbers.Constants.Constants.PitcherBaseStats.PITCHER_O_SWING_PCT_MIN;
import static com.example.android.baseballbythenumbers.Constants.Constants.PitcherBaseStats.PITCHER_O_SWING_RANGE;
import static com.example.android.baseballbythenumbers.Constants.Constants.PitcherBaseStats.PITCHER_ZONE_PCT_MIN;
import static com.example.android.baseballbythenumbers.Constants.Constants.PitcherBaseStats.PITCHER_ZONE_RANGE;
import static com.example.android.baseballbythenumbers.Constants.Constants.PitcherBaseStats.PITCHER_Z_CONTACT_PCT_MIN;
import static com.example.android.baseballbythenumbers.Constants.Constants.PitcherBaseStats.PITCHER_Z_CONTACT_RANGE;
import static com.example.android.baseballbythenumbers.Constants.Constants.PitcherBaseStats.PITCHER_Z_SWING_PCT_MIN;
import static com.example.android.baseballbythenumbers.Constants.Constants.PitcherBaseStats.PITCHER_Z_SWING_RANGE;
import static com.example.android.baseballbythenumbers.Constants.Positions.LONG_RELIEVER;
import static com.example.android.baseballbythenumbers.Constants.Positions.SHORT_RELEIVER;
import static com.example.android.baseballbythenumbers.Constants.Positions.STARTING_PITCHER;
import static com.example.android.baseballbythenumbers.Constants.Positions.getPositionNameFromPrimaryPosition;

public class PitchingRotationRecyclerViewAdapter extends RecyclerView.Adapter<PitchingRotationRecyclerViewAdapter.ViewHolder> implements PitchingRotationItemMoveCallback.RotationItemTouchHelperContract {

    private final List<Player> mPlayers;
    private final PitchingRotationStartDragListener.StartDragListener mStartDragListener;
    private final Context mContext;

    public PitchingRotationRecyclerViewAdapter(Context context, List<Player> players, PitchingRotationStartDragListener.StartDragListener startDragListener) {
        mPlayers = players;
        mStartDragListener = startDragListener;
        mContext = context;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pitching_rotation_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NotNull final ViewHolder holder, int position) {
        holder.mPlayer = mPlayers.get(position);
        holder.mRotationPlayerName.setText(holder.mPlayer.getName());
        if (position < 5) {
            int rotationPosition = position + 1;
            String rotationPositionText = "SP"+rotationPosition + ")";
            holder.mRotationPlaceInOrderView.setText(rotationPositionText);
        } else if (position < 7){
            int longRelieverPosition = position - 4;
            String rotationPositionText = "LR"+longRelieverPosition + ")";
            holder.mRotationPlaceInOrderView.setText(rotationPositionText);
        } else if (position == mPlayers.size() - 1) {
            holder.mRotationPlaceInOrderView.setText("CL)");
        } else {
            int shortRelieverPosition = position - 6;
            String rotationPositionText = "SR"+shortRelieverPosition + ")";
            holder.mRotationPlaceInOrderView.setText(rotationPositionText);
        }
        if (holder.mPlayer.getPrimaryPosition() == STARTING_PITCHER) {
            holder.mRotationPlayerPosition.setText(getPositionNameFromPrimaryPosition(holder.mPlayer.getPrimaryPosition()));
        } else if (holder.mPlayer.getPrimaryPosition() == LONG_RELIEVER) {
            holder.mRotationPlayerPosition.setText("LR");
        } else if (holder.mPlayer.getPrimaryPosition() == SHORT_RELEIVER) {
            holder.mRotationPlayerPosition.setText("SR");
        }

        String currentStats = "ERA " + holder.mPlayer.getPitchingStats().get(0).getERA() + ", WHIP " + holder.mPlayer.getPitchingStats().get(0).getWHIP() +
                ", W " + holder.mPlayer.getPitchingStats().get(0).getWins() + ", L " + holder.mPlayer.getPitchingStats().get(0).getLosses() + ", S " + holder.mPlayer.getPitchingStats().get(0).getSaves();
        holder.mRotationPlayerStats.setText(currentStats);

        int movementRating = (int) (50.0 - ((double) (holder.mPlayer.getHittingPercentages().getOContactPct() - PITCHER_O_CONTACT_PCT_MIN) / PITCHER_O_CONTACT_RANGE) * 50) +
                (int) (50.00 - ((double) (holder.mPlayer.getHittingPercentages().getZContactPct()- PITCHER_Z_CONTACT_PCT_MIN) / PITCHER_Z_CONTACT_RANGE) * 50);
        setProgressBarDrawable(movementRating, holder.mRotationPlayerMovement);
        holder.mRotationPlayerMovement.setProgress(movementRating);

        int deceptionRating = (int) ((((double) (holder.mPlayer.getPitchingPercentages().getOSwingPct()- PITCHER_O_SWING_PCT_MIN) / PITCHER_O_SWING_RANGE) * 50)) +
                (int) (50.0 - ((double) (holder.mPlayer.getPitchingPercentages().getZSwingPct()- PITCHER_Z_SWING_PCT_MIN) / PITCHER_Z_SWING_RANGE) * 50);
        setProgressBarDrawable(deceptionRating, holder.mRotationPlayerDeception);
        holder.mRotationPlayerDeception.setProgress(deceptionRating);

        int accuracyRating = (int) (((double) (holder.mPlayer.getPitchingPercentages().getZonePct()- PITCHER_ZONE_PCT_MIN) / PITCHER_ZONE_RANGE) * 100);
        setProgressBarDrawable(accuracyRating, holder.mRotationPlayerAccuracy);
        holder.mRotationPlayerAccuracy.setProgress(accuracyRating);

        int stamina = holder.mPlayer.getPitchingPercentages().getPitchingStamina();
        setProgressBarDrawable(stamina, holder.mRotationPlayerStamina);
        holder.mRotationPlayerStamina.setProgress(stamina);

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

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final View mView;
        private final CardView mCardView;
        private final TextView mRotationPlaceInOrderView;
        private final TextView mRotationPlayerName;
        private final TextView mRotationPlayerPosition;
        private final TextView mRotationPlayerStats;
        private final ProgressBar mRotationPlayerStamina;
        private final ProgressBar mRotationPlayerAccuracy;
        private final ProgressBar mRotationPlayerDeception;
        private final ProgressBar mRotationPlayerMovement;
        private final ImageView mReorder;
        private Player mPlayer;

        private ViewHolder(View view) {
            super(view);
            mView = view;
            mCardView = (CardView) view.findViewById(R.id.pitching_rotation_cardview);
            mRotationPlaceInOrderView = (TextView) view.findViewById(R.id.pitching_rotation_place_in_order);
            mRotationPlayerName = (TextView) view.findViewById(R.id.pitching_rotation_name_of_player);
            mRotationPlayerPosition = (TextView) view.findViewById(R.id.pitching_rotation_position);
            mRotationPlayerStats = (TextView) view.findViewById(R.id.pitching_rotation_player_stats);
            mRotationPlayerStamina = (ProgressBar) view.findViewById(R.id.pitching_rotation_stamina_pb);
            mRotationPlayerAccuracy = (ProgressBar) view.findViewById(R.id.pitching_rotation_accuracy_pb);
            mRotationPlayerDeception = (ProgressBar) view.findViewById(R.id.pitching_rotation_deception_pb);
            mRotationPlayerMovement = (ProgressBar) view.findViewById(R.id.pitching_rotation_movement_pb);
            mReorder = (ImageView) view.findViewById(R.id.rotation_reorder_icon_iv);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mRotationPlayerName.getText() + "'";
        }
    }
}


