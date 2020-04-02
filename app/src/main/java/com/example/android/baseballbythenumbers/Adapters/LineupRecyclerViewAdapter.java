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
import com.example.android.baseballbythenumbers.UI.RosterActivity.LineupFragment;
import com.example.android.baseballbythenumbers.UI.RosterActivity.LineupItemMoveCallback;
import com.example.android.baseballbythenumbers.UI.RosterActivity.LineupStartDragListener;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.List;

import static com.example.android.baseballbythenumbers.Constants.Positions.getPositionNameFromPrimaryPosition;

public class LineupRecyclerViewAdapter extends RecyclerView.Adapter<LineupRecyclerViewAdapter.ViewHolder> implements LineupItemMoveCallback.ItemTouchHelperContract {

    private final List<Player> mPlayers;
    private final LineupStartDragListener.StartDragListener mStartDragListener;
    private final Context mContext;
    private final LineupFragment.OnLineupFragmentInteractionListener mListener;

    public LineupRecyclerViewAdapter(Context context, List<Player> players, LineupStartDragListener.StartDragListener startDragListener, LineupFragment.OnLineupFragmentInteractionListener clickListener) {
        mPlayers = players;
        mStartDragListener = startDragListener;
        mContext = context;
        mListener = clickListener;

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
        String firstInitialAndLastName = holder.mPlayer.getFirstName().substring(0,1) + ". " + holder.mPlayer.getLastName();
        holder.mLineupPlayerName.setText(firstInitialAndLastName);
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

        int contactRating = holder.mPlayer.getBattingContactRating(holder.mPlayer);
        int eyeRating = holder.mPlayer.getBattingEyeRating(holder.mPlayer);
        int powerRating = holder.mPlayer.getBattingPowerRating(holder.mPlayer);
        int speedRating = holder.mPlayer.getBattingSpeedRating(holder.mPlayer);
        int overallRating = (2 * (contactRating + eyeRating) + (powerRating + speedRating)) / 6;
        setProgressBarDrawable(overallRating, holder.mLineupPlayerOverallBattingRating);
        holder.mLineupPlayerOverallBattingRating.setProgress(overallRating);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onLineupFragmentInteraction(holder.mPlayer);
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
        private final ProgressBar mLineupPlayerOverallBattingRating;
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
            mLineupPlayerOverallBattingRating = (ProgressBar) view.findViewById(R.id.lineup_overall_rating_pb);
            mReorder = (ImageView) view.findViewById(R.id.lineup_reorder);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mLineupPlayerName.getText() + "'";
        }
    }
}
