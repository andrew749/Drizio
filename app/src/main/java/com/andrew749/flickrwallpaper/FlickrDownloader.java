package com.andrew749.flickrwallpaper;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import java.net.URL;

/**
 * Created by andrewcodispoti on 2015-05-09.
 * A class to download an image from flickr.
 */
public class FlickrDownloader {

    private  class ImageDownloader extends AsyncTask<URL,Integer,Bitmap>{

        @Override
        protected Bitmap doInBackground(URL... urls) {
            return null;
        }

    }
}
