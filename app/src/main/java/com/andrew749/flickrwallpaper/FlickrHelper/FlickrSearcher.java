package com.andrew749.flickrwallpaper.FlickrHelper;

import android.content.Context;
import android.os.AsyncTask;

import com.andrew749.flickrwallpaper.Interfaces.ListDownloadingInterface;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by andrewcodispoti on 2015-05-09.
 */
public class FlickrSearcher {
    private static String REST_ENDPOINT = "https://api.flickr.com/services/rest/";
    ListDownloadingInterface downloadingInterface;
    Context context;

    public FlickrSearcher(ListDownloadingInterface downloadingInterface, Context context) {
        this.downloadingInterface = downloadingInterface;
        this.context = context;
    }

    public static String readUrl(URL url) throws Exception {
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

    //void because task should execute callback when done
    public void getImages(int number) {
        GetTopImages task = new GetTopImages();
        task.execute(number);
    }
    //synchronous method to get the images.
    public ArrayList<FlickrResult> getImagesSync(int number){
        GetTopImages task=new GetTopImages();
        try {
            task.execute(number);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            return task.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }


    //task to download image list
    private class GetTopImages extends AsyncTask<Integer, Integer, ArrayList<FlickrResult>> {
        @Override
        protected ArrayList<FlickrResult> doInBackground(Integer... params) {
            ArrayList<FlickrResult> results = new ArrayList<FlickrResult>();
            int numImages=params[0];
            String queryParameter = "?method=flickr.interestingness.getList&api_key=6c30fdb8388402770932f08d6e367939&format=json&nojsoncallback=1&per_page="+numImages;
            try {
                URL url = new URL(REST_ENDPOINT + queryParameter);
                Gson gson = new GsonBuilder().create();
                String json = readUrl(url);
                FlickrParent flickrParent = gson.fromJson(json, FlickrParent.class);
                FlickrObject flickrObject = flickrParent.photos;
                List<FlickrPhoto> photos = flickrObject.photo;
                for (FlickrPhoto photo : photos) {
                    results.add(new FlickrResult(photo.title, photo.id, context));
                }
                //TODO create arraylist of flickr results with response
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return results;        }

        @Override
        protected void onPostExecute(ArrayList<FlickrResult> flickrResults) {
            super.onPostExecute(flickrResults);
            if (!flickrResults.isEmpty())
                downloadingInterface.imageListIsDoneLoading(flickrResults);
        }

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
    }
}
