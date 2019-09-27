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
import com.example.android.baseballbythenumbers.UI.RosterActivity.LineupItemMoveCallback;
import com.example.android.baseballbythenumbers.UI.RosterActivity.LineupStartDragListener;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.List;

import static com.example.android.baseballbythenumbers.Constants.Constants.BatterBaseStats.BATTING_HOME_RUN_PCT_MIN;
import static com.example.android.baseballbythenumbers.Constants.Constants.BatterBaseStats.BATTING_HOME_RUN_RANGE;
import static com.example.android.baseballbythenumbers.Constants.Constants.BatterBaseStats.BATTING_O_CONTACT_PCT_MIN;
import static com.example.android.baseballbythenumbers.Constants.Constants.BatterBaseStats.BATTING_O_CONTACT_RANGE;
import static com.example.android.baseballbythenumbers.Constants.Constants.BatterBaseStats.BATTING_O_SWING_PCT_MIN;
import static com.example.android.baseballbythenumbers.Constants.Constants.BatterBaseStats.BATTING_O_SWING_RANGE;
import static com.example.android.baseballbythenumbers.Constants.Constants.BatterBaseStats.BATTING_SPEED_MIN;
import static com.example.android.baseballbythenumbers.Constants.Constants.BatterBaseStats.BATTING_SPEED_RANGE;
import static com.example.android.baseballbythenumbers.Constants.Constants.BatterBaseStats.BATTING_Z_CONTACT_PCT_MIN;
import static com.example.android.baseballbythenumbers.Constants.Constants.BatterBaseStats.BATTING_Z_CONTACT_RANGE;
import static com.example.android.baseballbythenumbers.Constants.Constants.BatterBaseStats.BATTING_Z_SWING_PCT_MIN;
import static com.example.android.baseballbythenumbers.Constants.Constants.BatterBaseStats.BATTING_Z_SWING_RANGE;
import static com.example.android.baseballbythenumbers.Constants.Positions.getPositionNameFromPrimaryPosition;

public class LineupRecyclerViewAdapter extends RecyclerView.Adapter<LineupRecyclerViewAdapter.ViewHolder> implements LineupItemMoveCallback.ItemTouchHelperContract {

    private final List<Player> mPlayers;
    private final LineupStartDragListener.StartDragListener mStartDragListener;
    private final Context mContext;

    public LineupRecyclerViewAdapter(Context context, List<Player> players, LineupStartDragListener.StartDragListener startDragListener) {
        mPlayers = players;
        mStartDragListener = startDragListener;
        mContext = context;

        Collections.sort(mPlayers, Player.BestOnBaseComparator);
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lineup_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NotNull final ViewHolder holder, int position) {
        holder.mPlayer = mPlayers.get(position);
        holder.mLineupPlayerName.setText(holder.mPlayer.getName());
        if (position < 9) {
            int lineupPosition = position + 1;
            String lineupPositionText = lineupPosition + ")";
            holder.mLineupPlaceInOrderView.setText(lineupPositionText);
        } else {
            holder.mLineupPlaceInOrderView.setText("B)");
        }

        DecimalFormat decimalFormat = new DecimalFormat(".000");
        String currentStats = "AVG " + decimalFormat.format(holder.mPlayer.getBattingStats().get(0).getAverage()) + ", OBP " + decimalFormat.format(holder.mPlayer.getBattingStats().get(0).getOnBasePct()) +
                ", HR " + holder.mPlayer.getBattingStats().get(0).getHomeRuns() + ", SB " + holder.mPlayer.getBattingStats().get(0).getStolenBases();
        holder.mLineupPlayerStats.setText(currentStats);

        holder.mLineupPlayerPosition.setText(getPositionNameFromPrimaryPosition(holder.mPlayer.getPrimaryPosition()));

        int contactRating = (int) (((double) (holder.mPlayer.getHittingPercentages().getOContactPct()- BATTING_O_CONTACT_PCT_MIN) / BATTING_O_CONTACT_RANGE) * 50) +
                (int) (((double) (holder.mPlayer.getHittingPercentages().getZContactPct()- BATTING_Z_CONTACT_PCT_MIN) / BATTING_Z_CONTACT_RANGE) * 50);
        setProgressBarDrawable(contactRating, holder.mLineupPlayerContact);
        holder.mLineupPlayerContact.setProgress(contactRating);

        int eyeRating = (int) ((50.0 - ((double) (holder.mPlayer.getHittingPercentages().getOSwingPct()- BATTING_O_SWING_PCT_MIN) / BATTING_O_SWING_RANGE) * 50)) +
                (int) (((double) (holder.mPlayer.getHittingPercentages().getZSwingPct()- BATTING_Z_SWING_PCT_MIN) / BATTING_Z_SWING_RANGE) * 50);
        setProgressBarDrawable(eyeRating, holder.mLineupPlayerEye);
        holder.mLineupPlayerEye.setProgress(eyeRating);

        int powerRating = (int) (((double) (holder.mPlayer.getHittingPercentages().getHomeRunPct()- BATTING_HOME_RUN_PCT_MIN) / BATTING_HOME_RUN_RANGE) * 100);
        setProgressBarDrawable(powerRating, holder.mLineupPlayerPower);
        holder.mLineupPlayerPower.setProgress(powerRating);

        int speedRating = (int) (((double) (holder.mPlayer.getHittingPercentages().getSpeed()- BATTING_SPEED_MIN) / BATTING_SPEED_RANGE) * 100);
        setProgressBarDrawable(speedRating, holder.mLineupSpeed);
        holder.mLineupSpeed.setProgress(speedRating);

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
    public void onRowSelected(ViewHolder myViewHolder) {
        myViewHolder.mCardView.setCardBackgroundColor(Color.GRAY);
    }

    @Override
    public void onRowClear(ViewHolder myViewHolder) {
        myViewHolder.mCardView.setCardBackgroundColor(Color.WHITE);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final View mView;
        private final CardView mCardView;
        private final TextView mLineupPlaceInOrderView;
        private final TextView mLineupPlayerName;
        private final TextView mLineupPlayerPosition;
        private final TextView mLineupPlayerStats;
        private final ProgressBar mLineupPlayerContact;
        private final ProgressBar mLineupPlayerEye;
        private final ProgressBar mLineupPlayerPower;
        private final ProgressBar mLineupSpeed;
        private final ImageView mReorder;
        private Player mPlayer;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mCardView = (CardView) view.findViewById(R.id.lineup_cardview);
            mLineupPlaceInOrderView = (TextView) view.findViewById(R.id.lineup_place_in_order);
            mLineupPlayerName = (TextView) view.findViewById(R.id.lineup_name_of_player);
            mLineupPlayerPosition = (TextView) view.findViewById(R.id.lineup_player_position);
            mLineupPlayerStats = (TextView) view.findViewById(R.id.lineup_player_stats);
            mLineupPlayerContact = (ProgressBar) view.findViewById(R.id.lineup_contact_pb);
            mLineupPlayerEye = (ProgressBar) view.findViewById(R.id.lineup_eye_pb);
            mLineupPlayerPower = (ProgressBar) view.findViewById(R.id.lineup_power_pb);
            mLineupSpeed = (ProgressBar) view.findViewById(R.id.lineup_speed_pb);
            mReorder = (ImageView) view.findViewById(R.id.lineup_reorder);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mLineupPlayerName.getText() + "'";
        }
    }
}


