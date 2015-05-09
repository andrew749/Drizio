package com.andrew749.flickrwallpaper;

import android.os.AsyncTask;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by andrewcodispoti on 2015-05-09.
 */
public class FlickrSearcher {
    private static String REST_ENDPOINT="https://api.flickr.com/services/rest/";

    private class GetTopImages extends AsyncTask<Void,Integer,ArrayList<FlickrResult>> {

        @Override
        protected ArrayList<FlickrResult> doInBackground(Void... voids) {
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<FlickrResult> flickrResults) {
            super.onPostExecute(flickrResults);
        }
    }
}
