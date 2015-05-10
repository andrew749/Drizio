package com.andrew749.flickrwallpaper;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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
import java.util.List;

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
    private static String readUrl(URL url) throws Exception {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuffer buffer = new StringBuffer();
            int read;
            char[] chars = new char[1024];
            while ((read = reader.read(chars)) != -1)
                buffer.append(chars, 0, read);

            return buffer.toString();
        } finally {
            if (reader != null)
                reader.close();
        }
    }
    //task to download image list
    private class GetTopImages extends AsyncTask<Void, Integer, ArrayList<FlickrResult>> {
        public class FlickrPhoto {
            public long id;
            public String owner;
            public String secret;
            public String server;
            public int farm;
            public String title;
            public int ispublic;
            public int isfriend;
            public int isfamily;
        }
        public class FlickrObject{
            public List<FlickrPhoto> photo;
        }
        public class FlickrParent{
            public FlickrObject photos;
        }
        @Override
        protected ArrayList<FlickrResult> doInBackground(Void... voids) {
            ArrayList<FlickrResult> results = new ArrayList<FlickrResult>();
            String queryParameter = "?method=flickr.interestingness.getList&api_key=6c30fdb8388402770932f08d6e367939&format=json&nojsoncallback=1";
            try {
                URL url = new URL(REST_ENDPOINT + queryParameter);

                Gson gson = new GsonBuilder().create();
                String json=readUrl(url);
                FlickrParent flickrParent=gson.fromJson(json, FlickrParent.class);
                FlickrObject flickrObject=flickrParent.photos;
                List<FlickrPhoto> photos= flickrObject.photo;
                for (FlickrPhoto photo: photos){
                    results.add(new FlickrResult(photo.title,photo.id));
                }
                //TODO create arraylist of flickr results with response
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return results;
        }

        @Override
        protected void onPostExecute(ArrayList<FlickrResult> flickrResults) {
            super.onPostExecute(flickrResults);
//            if (!flickrResults.isEmpty())
                downloadingInterface.imageListIsDoneLoading(flickrResults);
        }
    }
}
