package com.andrew749.flickrwallpaper;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by andrewcodispoti on 2015-05-09.
 * A class to download an image from flickr.
 */
public class FlickrDownloader {
    private static String REST_ENDPOINT = "https://api.flickr.com/services/rest/";
    //TODO need url builder and photo downloader.
    private  class ImageDownloader extends AsyncTask<URL,Integer,Bitmap>{

        @Override
        protected Bitmap doInBackground(URL... urls) {
            String queryParameter="?method=flickr.photos.getSizes&api_key=6c30fdb8388402770932f08d6e367939&photo_id=";
            try {
                URL url = new URL(REST_ENDPOINT + queryParameter);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.connect();
                InputStream is = httpURLConnection.getInputStream();
                Gson gson = new Gson();
                JsonElement jelem = gson.fromJson(is.toString(), JsonElement.class);
                JsonObject jobj = jelem.getAsJsonObject();

                //TODO create arraylist of flickr results with response
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);

        }
    }
}
