package com.example.android.baseballbythenumbers.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.example.android.baseballbythenumbers.R;
import com.example.android.baseballbythenumbers.data.Division;
import com.example.android.baseballbythenumbers.ui.main.MainActivity;

/**
 * Implementation of App Widget functionality.
 */
public class BaseballByTheNumbersAppWidgetProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Division division, String usersTeamId) {

        // Construct the RemoteViews object
        RemoteViews remoteViews = getDivisionStandingsRemoteView(context, division, usersTeamId);
        remoteViews.setEmptyView(R.id.widget_teams_lv, R.id.empty_widget);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widget_teams_lv);
    }

    public static void updateAppWidgets(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds, Division division, String usersTeamId) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, division, usersTeamId);
        }
    }

    private static RemoteViews getDivisionStandingsRemoteView(Context context, Division division, String usersTeamId) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.app_widget);
        views.setTextViewText(R.id.widgetDivisionTV, division.getDivisionName());
        // Set the BaseballByTheNumbersAppWidgetService intent to act as the adapter for the ListView
        Intent intent = new Intent(context, BaseballByTheNumbersWidgetService.class);
        intent.putExtra(BaseballByTheNumbersWidgetService.DIVISION_TO_USE, division.getDivisionId());
        intent.putExtra(BaseballByTheNumbersWidgetService.USER_TEAM_ID, usersTeamId);
        views.setRemoteAdapter(R.id.widget_teams_lv, intent);
        // Set the MainActivity intent to launch when clicked
        Intent appIntent = new Intent(context, MainActivity.class);
        PendingIntent appPendingIntent = PendingIntent.getActivity(context, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.widget_wrapper_ll, appPendingIntent);
        return views;
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_teams_lv);
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }


    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

