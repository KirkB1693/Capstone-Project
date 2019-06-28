package com.example.android.baseballbythenumbers;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.baseballbythenumbers.databinding.FragmentPickCountriesBinding;

import static com.example.android.baseballbythenumbers.Constants.Constants.Countries.ALL_COUNTRIES;
import static com.example.android.baseballbythenumbers.Constants.Constants.Countries.CANADA;
import static com.example.android.baseballbythenumbers.Constants.Constants.Countries.JAPAN;
import static com.example.android.baseballbythenumbers.Constants.Constants.Countries.MEXICO;
import static com.example.android.baseballbythenumbers.Constants.Constants.Countries.USA;


public class PickCountriesFragment extends Fragment implements View.OnClickListener {
    private FragmentPickCountriesBinding pickCountriesBinding;

    private OnPickCountriesFragmentInteractionListener mListener;

    public PickCountriesFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        pickCountriesBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_pick_countries,container, false);
        return pickCountriesBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pickCountriesBinding.doneWithCountriesButton.setOnClickListener(this);
    }

    public void onDoneWithCountriesButtonPressed(View view) {
        int countriesPicked = getWhichCountriesAreChecked();
        if (mListener != null && countriesPicked != -1) {
            mListener.onPickCountriesFragmentInteraction(countriesPicked);
        }
    }

    private int getWhichCountriesAreChecked() {
        if (!pickCountriesBinding.unitedStatesCB.isChecked()){
            Toast toast = Toast.makeText(getContext(), "Currently the United States must be chosen, all other countries are optional", Toast.LENGTH_SHORT);
            View view = toast.getView();
            view.getBackground().setColorFilter(getResources().getColor(R.color.secondaryColor), PorterDuff.Mode.SRC_IN);
            TextView text = view.findViewById(android.R.id.message);
            text.setTextColor(ContextCompat.getColor(getContext(), R.color.secondaryTextColor));
            text.setGravity(Gravity.CENTER);
            toast.show();
            pickCountriesBinding.unitedStatesCB.setChecked(true);
        } else {
            int countriesPicked = USA;
            if (pickCountriesBinding.canadaCB.isChecked()) {
                countriesPicked += CANADA;
            }
            if (pickCountriesBinding.japanCB.isChecked()) {
                countriesPicked += JAPAN;
            }
            if (pickCountriesBinding.mexicoCB.isChecked()) {
                countriesPicked += MEXICO;
            }
            if (countriesPicked == USA + CANADA + JAPAN + MEXICO) {
                countriesPicked = ALL_COUNTRIES;
            }
            return countriesPicked;
        }
        return -1;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnPickCountriesFragmentInteractionListener) {
            mListener = (OnPickCountriesFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.doneWithCountriesButton:
                onDoneWithCountriesButtonPressed(view);
                break;
        }

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnPickCountriesFragmentInteractionListener {
        void onPickCountriesFragmentInteraction(int countriesToInclude);
    }
}
