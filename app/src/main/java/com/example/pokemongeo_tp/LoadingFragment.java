package com.example.pokemongeo_tp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

public class LoadingFragment extends Fragment {

    public LoadingFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // create spinner loading
        ProgressBar spinner = new ProgressBar(getContext());
        spinner.setIndeterminate(true);
        spinner.setVisibility(View.VISIBLE);
        FrameLayout.LayoutParams frameLayoutParams = new FrameLayout.LayoutParams(500,500, Gravity.CENTER);
        spinner.setLayoutParams(frameLayoutParams);

        View view = inflater.inflate(R.layout.fragment_loading, container, false);
        // add spinner to view
        ((ViewGroup) view).addView(spinner);
        return view;
    }
}