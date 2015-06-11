package com.andrew749.drizio.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.andrew749.drizio.FlickrHelper.FlickrResult;
import com.andrew749.drizio.Adapters.ImageAdapter;
import com.andrew749.drizio.R;

import java.util.ArrayList;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    public MainActivityFragment() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        adapter = new ImageAdapter(results, getActivity().getApplicationContext());
    }

    ArrayList<FlickrResult> results = new ArrayList<FlickrResult>();
    ListView lv;
    ImageAdapter adapter;


    public void setData(ArrayList<FlickrResult> results) {
        this.results.addAll( results);
        adapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        lv = (ListView) view.findViewById(R.id.mainImageList);
        lv.setAdapter(adapter);
        return view;
    }

}
