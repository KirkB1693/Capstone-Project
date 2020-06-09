package com.example.android.baseballbythenumbers.widget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.baseballbythenumbers.R;
import com.example.android.baseballbythenumbers.data.Division;
import com.example.android.baseballbythenumbers.data.Team;
import com.example.android.baseballbythenumbers.utilities.SyncServiceSupportImpl;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class BaseballByTheNumbersWidgetService extends RemoteViewsService {
    public static final String DIVISION_TO_USE = "division_to_use";
    public static final String DIVISION_TO_USE_ACTION = "division_to_use_action";
    public static final String USER_TEAM_ID = "user_team_id";

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new BaseballByTheNumbersAppRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}

class BaseballByTheNumbersAppRemoteViewsFactory extends BroadcastReceiver implements RemoteViewsService.RemoteViewsFactory {
    private static final String METHOD_SET_BACKGROUND_RESOURCE = "setBackgroundResource";
    private Context mContext;
    private static Division mDivision;
    private String mDivisionID;
    private String mUserTeamID;
    private static List<Team> mTeamList = new ArrayList<>();
    private static SyncServiceSupportImpl iSyncService;


    public BaseballByTheNumbersAppRemoteViewsFactory(Context applicationContext, Intent intent) {
        mContext = applicationContext;

    }



    private static void getFromDB(final String divisionId) {

        try {
            mTeamList = new AsyncTask<Void, Void, List<Team>>() {

                @Override
                protected List<Team> doInBackground(Void... voids) {
                    mDivision = iSyncService.getDivision(divisionId);
                    return iSyncService.getTeamsForDivision(divisionId);
                }

            }.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate() {
        //init service
        iSyncService = new SyncServiceSupportImpl(mContext);
        // initialize database

    }

    //called on start and when notifyAppWidgetViewDataChanged is called
    @Override
    public void onDataSetChanged() {
        SharedPreferences settings = mContext.getSharedPreferences(BaseballByTheNumbersWidgetService.DIVISION_TO_USE_ACTION,
                Context.MODE_PRIVATE);
        mDivisionID = settings.getString(BaseballByTheNumbersWidgetService.DIVISION_TO_USE, null);
        mUserTeamID = settings.getString(BaseballByTheNumbersWidgetService.USER_TEAM_ID, null);
        getFromDB(mDivisionID);
        if (!mTeamList.isEmpty()) {
            Collections.sort(mTeamList, Team.WonLossRecordComparator);
        }
    }

    @Override
    public void onDestroy() {
    }


    @Override
    public int getCount() {
        if (mTeamList != null) {
            return mTeamList.size();
        }
        else {
            return 0;
        }
    }

    /**
     * This method acts like the onBindViewHolder method in an Adapter
     *
     * @param position The current position of the item in the ListView to be displayed
     * @return The RemoteViews object to display for the provided postion
     */
    @Override
    public RemoteViews getViewAt(int position) {
        if (mDivision == null || mTeamList.size() == 0) {return null;}
        Team currentTeam = mTeamList.get(position);
        boolean userTeam = currentTeam.getTeamId().equals(mUserTeamID);
        boolean changeBackground = isEven(position);

        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);

        if (!changeBackground) {
            remoteViews.setInt(R.id.widget_background_wrapper_ll, METHOD_SET_BACKGROUND_RESOURCE, R.color.light_background);
        } else {
            remoteViews.setInt(R.id.widget_background_wrapper_ll, METHOD_SET_BACKGROUND_RESOURCE, R.color.white);
        }

        String team = String.format(Locale.getDefault(), "%s %s", currentTeam.getTeamCity(), currentTeam.getTeamName());
        SpannableString spannableTeam = new SpannableString(team);
        if (userTeam) {
            int end = spannableTeam.length();
            spannableTeam.setSpan(new StyleSpan(Typeface.BOLD), 0, end, 0);
        }
        remoteViews.setTextViewText(R.id.widget_item_team_name_tv, spannableTeam);
        String teamRecord = String.format(Locale.getDefault(), "%d - %d", currentTeam.getWins(), currentTeam.getLosses());
        SpannableString spannableRecord = new SpannableString(teamRecord);
        if (userTeam) {
            int end = spannableRecord.length();
            spannableRecord.setSpan(new StyleSpan(Typeface.BOLD), 0, end, 0);
        }
        remoteViews.setTextViewText(R.id.widget_team_record_tv, spannableRecord);
        DecimalFormat decimalFormat = new DecimalFormat(".000");
        double winPct;
        if (currentTeam.getWins() == 0) {
            winPct = 0.0;
        } else {
            winPct = (currentTeam.getWins() * 1.0)/(currentTeam.getWins() + currentTeam.getLosses());
        }
        String winPctString = decimalFormat.format(winPct);
        SpannableString spannableWinPct = new SpannableString(winPctString);
        if (userTeam) {
            int end = spannableWinPct.length();
            spannableWinPct.setSpan(new StyleSpan(Typeface.BOLD), 0, end, 0);
        }
        remoteViews.setTextViewText(R.id.widget_item_winning_percentage_tv, spannableWinPct);

        return remoteViews;
    }

    private boolean isEven(int position) {
        return ( ( position % 2 ) == 0 );
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.hasExtra(BaseballByTheNumbersWidgetService.DIVISION_TO_USE)) {
            mDivisionID = intent.getExtras().getString(BaseballByTheNumbersWidgetService.DIVISION_TO_USE);
        }
    }
}
