package com.andrew749.flickrwallpaper;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by andrewcodispoti on 2015-05-09.
 */
public class FlickrSearcher {
    private static String REST_ENDPOINT = "https://api.flickr.com/services/rest/";
    ListDownloadingInterface downloadingInterface;

    public FlickrSearcher(ListDownloadingInterface downloadingInterface) {
        this.downloadingInterface = downloadingInterface;
    }


    //void because task should execute callback when done
    public void getImages() {
        GetTopImages task = new GetTopImages();
        task.execute();
    }
    public static String readIt(InputStream stream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        StringBuilder out = new StringBuilder();
        String newLine = System.getProperty("line.separator");
        String line;
        while ((line = reader.readLine()) != null) {
            out.append(line);
            out.append(newLine);
        }
        return out.toString();
    }
    //task to download image list
    private class GetTopImages extends AsyncTask<Void, Integer, ArrayList<FlickrResult>> {

        @Override
        protected ArrayList<FlickrResult> doInBackground(Void... voids) {
            ArrayList<FlickrResult> results = new ArrayList<FlickrResult>();
            String queryParameter = "?method=flickr.interestingness.getList&api_key=6c30fdb8388402770932f08d6e367939&format=json";
            try {
                URL url = new URL(REST_ENDPOINT + queryParameter);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.connect();
                InputStream is = httpURLConnection.getInputStream();
                Gson gson = new Gson();
                JsonElement jelem = gson.fromJson(readIt(is), JsonElement.class);
                JsonObject jobj = jelem.getAsJsonObject();

                //TODO create arraylist of flickr results with response
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return results;
        }

        @Override
        protected void onPostExecute(ArrayList<FlickrResult> flickrResults) {
            super.onPostExecute(flickrResults);
            if (!flickrResults.isEmpty())
                downloadingInterface.imageListIsDoneLoading(flickrResults);
        }
    }
}
