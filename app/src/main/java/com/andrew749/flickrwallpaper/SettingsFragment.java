package com.andrew749.flickrwallpaper;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.Spinner;

/**
 * Created by andrewcodispoti on 2015-05-30.
 */
public class SettingsFragment extends Fragment implements OnItemSelectedListener,View.OnClickListener{
    Spinner refresh,cache,quality;
    Button refreshImages,clearImages;
    SharedPreferences prefs;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs=getActivity().getSharedPreferences("FlickrWallpaperPrefs", Context.MODE_PRIVATE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.optionslayout,container,false);
        refresh=(Spinner)view.findViewById(R.id.spinnerRate);
        cache=(Spinner)view.findViewById(R.id.spinnerCache);
        quality=(Spinner)view.findViewById(R.id.imageSizeSelector);
        refreshImages=(Button)view.findViewById(R.id.refreshButton);
        clearImages=(Button)view.findViewById(R.id.clearButton);
        refresh.setOnItemSelectedListener(this);
        cache.setOnItemSelectedListener(this);
        quality.setOnItemSelectedListener(this);
        refreshImages.setOnClickListener(this);
        clearImages.setOnClickListener(this);
        return view;
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch(parent.getId()){
            case R.id.spinnerCache:
                prefs.edit().putInt("cache_size",getResources().getIntArray(R.array.cache_size_value)[position]).commit();
                break;
            case R.id.spinnerRate:
                prefs.edit().putInt("refresh_rate",getResources().getIntArray(R.array.intervals_value)[position]).commit();
                break;
            case R.id.imageSizeSelector:
                prefs.edit().putString("image_size",getResources().getStringArray(R.array.quality_list)[position]).commit();
                break;
        }
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.refreshButton:

                break;
            case R.id.clearButton:
                new LocalStorage(getActivity()).deleteImages();
                break;
        }
    }
}
