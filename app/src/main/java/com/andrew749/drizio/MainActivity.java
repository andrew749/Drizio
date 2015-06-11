package com.andrew749.drizio;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.andrew749.drizio.DataHelper.LocalStorage;
import com.andrew749.drizio.FlickrHelper.FlickrResult;
import com.andrew749.drizio.FlickrHelper.FlickrSearcher;
import com.andrew749.drizio.Fragments.MainActivityFragment;
import com.andrew749.drizio.Fragments.SettingsFragment;
import com.andrew749.drizio.Interfaces.ListDownloadingInterface;
import com.andrew749.drizio.Interfaces.RefreshController;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements ListDownloadingInterface,RefreshController {
    MainActivityFragment mainActivityFragment;
    SettingsFragment settingsFragment;
    LocalStorage storage;
    public static final String TAG = "FLICKRWALLPAPER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_main);
        storage = LocalStorage.getInstance(getApplicationContext());
        mainActivityFragment = (MainActivityFragment) getSupportFragmentManager().findFragmentById(R.id.listfragment);
        settingsFragment=(SettingsFragment)getFragmentManager().findFragmentById(R.id.fragment);
        settingsFragment.setCallback(this);
    }

    @Override
    public boolean imageListIsDoneLoading(ArrayList<FlickrResult> results) {
        mainActivityFragment.setData(results);
        return false;
    }

    @Override
    public void refresh() {
        FlickrSearcher searcher = new FlickrSearcher(this);
        searcher.getImages(getSharedPreferences(SettingsFragment.prefsName, Context.MODE_PRIVATE).getInt(SettingsFragment.cacheName,100));
    }
}
