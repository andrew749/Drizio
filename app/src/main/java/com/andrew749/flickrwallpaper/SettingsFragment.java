package com.andrew749.flickrwallpaper;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

/**
 * Created by andrewcodispoti on 2015-05-30.
 */
public class SettingsFragment extends Fragment implements SeekBar.OnSeekBarChangeListener{
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.optionslayout,container,false);
        SeekBar seekBar=(SeekBar)view.findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(this);
        return view;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
