package com.example.android.baseballbythenumbers.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.baseballbythenumbers.R;
import com.example.android.baseballbythenumbers.ui.newleaguesetup.PickCityFragment.OnListFragmentInteractionListener;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MyPickCityRecyclerViewAdapter extends RecyclerView.Adapter<MyPickCityRecyclerViewAdapter.ViewHolder> {

    private final List<String> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyPickCityRecyclerViewAdapter(List<String> cities, OnListFragmentInteractionListener listener) {
        mValues = cities;
        mListener = listener;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pick_city_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NotNull final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mContentView.setText(holder.mItem);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final View mView;
        private final CardView mCardView;
        private final TextView mContentView;
        private String mItem;

        private ViewHolder(View view) {
            super(view);
            mView = view;
            mCardView = view.findViewById(R.id.city_list_item_cv);
            mContentView = view.findViewById(R.id.city_List_item_tv);
        }

        @NotNull
        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}

