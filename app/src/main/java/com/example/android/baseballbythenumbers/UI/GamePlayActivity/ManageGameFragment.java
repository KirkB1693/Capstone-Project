package com.example.android.baseballbythenumbers.UI.GamePlayActivity;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.baseballbythenumbers.R;
import com.example.android.baseballbythenumbers.databinding.FragmentManageGameBinding;

public class ManageGameFragment extends Fragment implements View.OnClickListener {

    private FragmentManageGameBinding manageGameBinding;

    private ManageGameClickListener manageGameClickListener;


    public ManageGameFragment(){
    }


    public static ManageGameFragment newInstance() {

        return new ManageGameFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        manageGameBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_manage_game, container, false);
        return manageGameBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        manageGameBinding.pinchHitButton.setOnClickListener(this);
        manageGameBinding.subPitcherButton.setOnClickListener(this);
        manageGameBinding.pauseButton.setOnClickListener(this);
        manageGameBinding.simThisAtBatButton.setOnClickListener(this);
        manageGameBinding.simRestOfGameButton.setOnClickListener(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ManageGameClickListener) {
            manageGameClickListener = (ManageGameClickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ManageGameClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        manageGameClickListener = null;
    }

    @Override
    public void onClick(View view) {
        manageGameClickListener.manageGameOnClickResponse(view.getId());
    }

    public interface ManageGameClickListener{
        void manageGameOnClickResponse(int buttonId);
    }
}
