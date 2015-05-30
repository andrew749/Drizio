package com.andrew749.flickrwallpaper;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements ListDownloadingInterface {
    MainActivityFragment fragment;
    LocalStorage storage;
    public static final String TAG = "FLICKRWALLPAPER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        FlickrSearcher searcher = new FlickrSearcher(this, this);
//        fragment = (MainActivityFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
//        searcher.getImages();
//        storage = new LocalStorage(this);
        getFragmentManager().beginTransaction().add(R.id.fragment, new SettingsFragment()).commit();
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
        fragment.setData(results);

        return false;
    }
}
