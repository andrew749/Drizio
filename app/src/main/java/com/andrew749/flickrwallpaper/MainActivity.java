package com.andrew749.flickrwallpaper;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.andrew749.flickrwallpaper.DataHelper.LocalStorage;
import com.andrew749.flickrwallpaper.FlickrHelper.FlickrResult;
import com.andrew749.flickrwallpaper.FlickrHelper.FlickrSearcher;
import com.andrew749.flickrwallpaper.Fragments.MainActivityFragment;
import com.andrew749.flickrwallpaper.Fragments.SettingsFragment;
import com.andrew749.flickrwallpaper.Interfaces.ListDownloadingInterface;
import com.andrew749.flickrwallpaper.Interfaces.RefreshController;

import java.util.ArrayList;
import java.util.Set;


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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
